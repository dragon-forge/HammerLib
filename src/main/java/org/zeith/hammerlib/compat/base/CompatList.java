package org.zeith.hammerlib.compat.base;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModList;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.core.adapter.OnlyIfAdapter;
import org.zeith.hammerlib.util.java.*;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * A utility class to manage a list of {@link BaseCompat} instances and their associated abilities.
 *
 * <p>This class allows you to gather a list of {@link BaseCompat} instances that are annotated with the
 * {@link BaseCompat.LoadCompat} annotation and are associated with a specific mod, and then retrieve the
 * abilities provided by these instances using the {@link #getAbilities(Ability)} method.
 *
 * <p>To use this class, create an instance using the {@link #gather(Class, CompatContext)} method and pass in the base
 * class for the type of compatibility that you are interested in. For example, to gather block compatibility
 * classes, you could do the following:
 *
 * <pre>
 * {@code
 *
 * // Gather block compatibility classes
 * CompatList<BaseHLCompat> hlCompatList = CompatList.gather(BaseHLCompat.class);
 *
 * // Get the list of active HL compatibility classes
 * List<BaseHLCompat> activeBlockCompats = hlCompatList.getActive();
 *
 * // Get a list of all Bloom abilities from all compatibilities
 * List<Bloom> bloomAbilities = hlCompatList.getAbilities(HLAbilities.BLOOM);
 * }
 * </pre>
 *
 * @param <T>
 * 		the type of compatibility that this list manages
 */
public class CompatList<T extends BaseCompat<T>>
{
	private final List<? extends T> lst;
	private final Map<Ability<?>, List<?>> cachedAbilities = new ConcurrentHashMap<>();
	
	protected CompatList(List<? extends T> lst)
	{
		this.lst = lst;
	}
	
	/**
	 * Gathers a list of {@link BaseCompat} instances that are annotated with the {@link BaseCompat.LoadCompat}
	 * annotation and are associated with a mod that is currently loaded in the game.
	 *
	 * @param base
	 * 		the base class for the type of compatibility that you want to gather
	 * @param <T>
	 * 		the type of compatibility that you want to gather
	 *
	 * @return a {@link CompatList} instance containing the gathered compatibility instances
	 */
	public static <T extends BaseCompat<T>> CompatList<T> gather(Class<T> base, CompatContext context)
	{
		return gather(base, context, CompatList::new);
	}
	
	/**
	 * Gathers a custom list of {@link BaseCompat} instances that are annotated with the {@link BaseCompat.LoadCompat}
	 * annotation and are associated with a mod that is currently loaded in the game.
	 *
	 * <p>This method looks for classes annotated with the {@link BaseCompat.LoadCompat} annotation and
	 * checks the specified mod dependencies and loading conditions to determine which classes should be
	 * included in the list. The resulting list of compatibility classes is then passed to the provided
	 * `listFun` function to be transformed into a `CompatList` instance.
	 *
	 * @param base
	 * 		the base type of the compatibility classes to gather
	 * @param listFun
	 * 		a function to transform the list of gathered compatibility classes into a `CompatList`
	 * 		instance
	 * @param <T>
	 * 		the base type of the compatibility classes
	 * @param <R>
	 * 		the type of the `CompatList` instance to be returned
	 *
	 * @return a `CompatList` instance containing the gathered compatibility classes
	 */
	public static <T extends BaseCompat<T>, R extends CompatList<T>> R gather(Class<T> base, CompatContext context, Function<List<T>, R> listFun)
	{
		return listFun.apply(
				ScanDataHelper.lookupAnnotatedObjects(BaseCompat.LoadCompat.class)
						.stream()
						.filter(data -> ModList.get().isLoaded(Objects.toString(data.getProperty("modid").orElse(""))))
						.filter(data -> base.isAssignableFrom(ReflectionUtil.fetchClass((Type) data.getProperty("compatType").orElseThrow())))
						.filter(data -> data.getProperty("shouldLoad")
								.map(onlyIf ->
										OnlyIfAdapter.checkCondition(
												OnlyIfAdapter.decode(Cast.cast(onlyIf)),
												"CompatList.gather",
												base.getSimpleName(),
												data,
												new ResourceLocation(Objects.toString(data.getProperty("modid").orElse("")), "root")
										)
								)
								.orElse(true)
						)
						.map(data ->
						{
							var cls = data.getOwnerClass();
							for(Constructor<?> ct : cls.getDeclaredConstructors())
							{
								if(ct.getParameterCount() != 1) continue;
								
								var t0 = ct.getParameterTypes()[0];
								if(!t0.isInstance(context)) continue;
								
								ct.setAccessible(true);
								
								try
								{
									return (T) ct.newInstance(context);
								} catch(ReflectiveOperationException e)
								{
									throw new RuntimeException(e);
								}
							}
							throw new RuntimeException("Unable to find suitable contructor for " + data.clazz());
						})
						.toList()
		);
	}
	
	@Override
	public String toString()
	{
		return "CompatList{" +
			   "lst=" + lst +
			   ", cachedAbilities=" + cachedAbilities +
			   '}';
	}
	
	/**
	 * Returns a list of all active compatibility classes in this list.
	 *
	 * <p>The active compatibility classes are those that are enabled for the current environment, based on
	 * the mod dependencies and any other specified conditions.
	 *
	 * @return a list of all active compatibility classes in this list
	 */
	public List<? extends T> getActive()
	{
		return lst;
	}
	
	/**
	 * Returns a list of all abilities of the specified type in this list.
	 *
	 * <p>Abilities are special features or behaviors that compatibility classes can provide. This method
	 * allows you to retrieve a list of all instances of a given ability.
	 *
	 * @param ability
	 * 		the type of ability to retrieve
	 * @param <R>
	 * 		the type of the ability
	 *
	 * @return a list of all abilities of the specified type in this list
	 */
	public <R> List<R> getAbilities(Ability<R> ability)
	{
		return Cast.cast(cachedAbilities.computeIfAbsent(ability, a -> lst.stream().flatMap(c -> c.getAbility(a).stream()).toList()));
	}
	
	/**
	 * Returns the first ability of the specified type in this list, if present.
	 *
	 * <p>Abilities are special features or behaviors that compatibility classes can provide. This method
	 * allows you to retrieve the first compatibility class in this list that provides a specific ability, if
	 * one exists.
	 *
	 * @param ability
	 * 		the type of ability to retrieve
	 * @param <R>
	 * 		the type of the ability
	 *
	 * @return an {@link Optional} containing the first ability of the specified type in this list, if present;
	 * otherwise, an empty {@link Optional}
	 */
	public <R> Optional<R> firstAbility(Ability<R> ability)
	{
		return getAbilities(ability).stream().findFirst();
	}
}