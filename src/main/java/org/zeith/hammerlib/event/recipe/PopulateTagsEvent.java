package org.zeith.hammerlib.event.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This event is fired on {@link org.zeith.hammerlib.HammerLib#EVENT_BUS}
 */
@Deprecated(forRemoval = true)
public class PopulateTagsEvent<T extends IForgeRegistryEntry<T>>
		extends GenericEvent<T>
{
	public static final List<Class<?>> TAG_BASE_TYPES = new ArrayList<>()
	{
		{
			add(MobEffect.class);
			add(EntityType.class);
			add(Item.class);
			add(Potion.class);
			add(Enchantment.class);
			add(Block.class);
			add(BlockEntityType.class);
			add(GameEvent.class);
			add(StructureFeature.class);
			add(Fluid.class);
		}
	};

	public final ResourceLocation id;
	public final Tag<T> tag;

	private final Consumer<T> add, remove;
	private boolean hasChanged;

	public PopulateTagsEvent(Consumer<T> add, Consumer<T> remove, ResourceLocation id, Tag<T> tag, Class<T> type)
	{
		super(type);
		this.add = add;
		this.remove = remove;
		this.id = id;
		this.tag = tag;
	}

	public void add(T thing)
	{
		add.accept(thing);
		hasChanged = true;
	}

	public void remove(T thing)
	{
		remove.accept(thing);
		hasChanged = true;
	}

	public boolean hasChanged()
	{
		return hasChanged;
	}

	public List<T> getValues()
	{
		return tag.getValues();
	}

	public ResourceLocation getId()
	{
		return id;
	}

	public Tag<T> getTag()
	{
		return tag;
	}

	public boolean is(TagKey<T> tag)
	{
		return id.equals(tag.location());
	}

	@Override
	public String toString()
	{
		return "PopulateTagsEvent<" + ((Class<?>) getGenericType()).getSimpleName() + ">{" +
				"tag=" + id +
				'}';
	}
}