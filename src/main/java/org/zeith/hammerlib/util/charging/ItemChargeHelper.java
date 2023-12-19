package org.zeith.hammerlib.util.charging;

import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.util.java.*;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ItemChargeHelper
{
	private static final Map<Class<? extends AbstractCharge>, IChargeHandler> CHARGE_HANDLERS = new HashMap<>();
	private static final Map<String, IChargeHandler> CHARGE_HANDLERS_BY_ID = new HashMap<>();
	
	public static final List<IPlayerInventoryLister> playerInvListers = new ArrayList<>();
	
	public static void setup()
	{
		ScanDataHelper.lookupAnnotatedObjects(IChargeHandler.ChargeHandler.class)
				.forEach(data ->
				{
					Class<?> registerer = data.getOwnerClass();
					
					if(IChargeHandler.class.isAssignableFrom(registerer))
					{
						try
						{
							Constructor<? extends IChargeHandler> ctor = registerer.asSubclass(IChargeHandler.class)
									.getDeclaredConstructor();
							ctor.setAccessible(true);
							
							Type typeRaw = Cast.cast(data.getProperty("value").orElse(null));
							Class<? extends AbstractCharge> type = ReflectionUtil.fetchClass(typeRaw);
							IChargeHandler handler = ctor.newInstance();
							
							HammerLib.LOG.info(
									"Registered charge handler for type " + type.getName() + " - " + handler);
							
							CHARGE_HANDLERS.put(type, handler);
							CHARGE_HANDLERS_BY_ID.put(handler.getId(), handler);
						} catch(ReflectiveOperationException e)
						{
							throw new RuntimeException(e);
						}
					}
				});
		
		ScanDataHelper.lookupAnnotatedObjects(IPlayerInventoryLister.InventoryLister.class)
				.forEach(data ->
				{
					Class<?> registerer = data.getOwnerClass();
					
					if(IPlayerInventoryLister.class.isAssignableFrom(registerer))
					{
						try
						{
							Constructor<? extends IPlayerInventoryLister> ctor = registerer.asSubclass(IPlayerInventoryLister.class)
									.getDeclaredConstructor();
							ctor.setAccessible(true);
							registerInventoryFactory(ctor.newInstance());
							HammerLib.LOG.info("Registered inventory lister " + registerer.getName());
						} catch(ReflectiveOperationException e)
						{
							throw new RuntimeException(e);
						}
					}
				});
	}
	
	public static void registerInventoryFactory(IPlayerInventoryLister lister)
	{
		playerInvListers.add(lister);
	}
	
	/**
	 * Maps a handler to a charge type.
	 *
	 * @param <T>
	 * 		The charge type
	 * @param chargeType
	 * 		the class of the charge
	 * @param handler
	 * 		the handler to be assigned
	 */
	public static <T extends AbstractCharge> void handle(Class<T> chargeType, IChargeHandler<T> handler)
	{
		CHARGE_HANDLERS.put(chargeType, handler);
		CHARGE_HANDLERS_BY_ID.put(handler.getId(), handler);
	}
	
	public static <T extends AbstractCharge> IChargeHandler<T> getHandler(String id)
	{
		return CHARGE_HANDLERS_BY_ID.get(id);
	}
	
	public static <T extends AbstractCharge> IChargeHandler<T> getHandler(T charge)
	{
		return CHARGE_HANDLERS.get(charge.getClass());
	}
	
	public static <T extends AbstractCharge> T charge(ItemStack stack, T charge, IChargeHandler.ChargeAction action)
	{
		return charge(new AtomicReference<>(stack), charge, action);
	}
	
	public static <T extends AbstractCharge> T charge(AtomicReference<ItemStack> stack, T charge, IChargeHandler.ChargeAction action)
	{
		IChargeHandler<T> h = getHandler(charge);
		if(h == null)
			return charge;
		return h.charge(stack, charge, action);
	}
	
	private static final Predicate<Integer> I_TRUE = i -> true;
	
	public static <T extends AbstractCharge> T chargeInventory(Inventory inv, T charge, IChargeHandler.ChargeAction action)
	{
		return chargeInventory(inv, I_TRUE, charge, action);
	}
	
	public static Stream<IItemHandlerModifiable> listPlayerInventories(Player player)
	{
		return playerInvListers.stream()
				.mapMulti((pil, c) -> pil.listItemHandlers(player, c));
	}
	
	/**
	 * Charges player's inventory. Supports modules, HammerCore supports
	 * player's vanilla inventory and baubles inventory.
	 *
	 * @param <T>
	 * 		The charge type
	 * @param player
	 * 		The player
	 * @param charge
	 * 		The charge
	 * @param action
	 * 		The action to do (either simulate, or execute)
	 */
	public static <T extends AbstractCharge> T chargePlayer(Player player, T charge, IChargeHandler.ChargeAction action)
	{
		IChargeHandler<T> h = getHandler(charge);
		if(h == null)
			return charge;
		
		var itr = listPlayerInventories(player).iterator();
		
		while(itr.hasNext() && charge.containsCharge())
		{
			IItemHandlerModifiable handler = itr.next();
			for(int i = 0; i < handler.getSlots() && charge.containsCharge(); ++i)
				if(h.canCharge(handler.getStackInSlot(i), charge))
				{
					ItemStack stack = handler.getStackInSlot(i);
					AtomicReference<ItemStack> ref = new AtomicReference<>(stack);
					charge = h.charge(ref, charge, action);
					if(ref.get() != stack && action.execute())
						handler.setStackInSlot(i, ref.get());
				}
		}
		
		return charge;
	}
	
	public static <T extends AbstractCharge> T chargeInventory(Inventory inv, Predicate<Integer> chargeSlot, T charge, IChargeHandler.ChargeAction action)
	{
		IChargeHandler<T> h = getHandler(charge);
		if(h == null)
			return charge;
		for(int i = 0; i < inv.getContainerSize() && charge.containsCharge(); ++i)
		{
			ItemStack item = inv.getItem(i);
			if(chargeSlot.test(i) && h.canCharge(item, charge))
			{
				AtomicReference<ItemStack> ref = new AtomicReference<>(item);
				charge = h.charge(ref, charge, action);
				if(ref.get() != item && action.execute())
					inv.setItem(i, ref.get());
			}
		}
		return charge;
	}
	
	public static <T extends AbstractCharge> T chargeInventory(IItemHandlerModifiable inv, Predicate<Integer> chargeSlot, T charge, IChargeHandler.ChargeAction action)
	{
		IChargeHandler<T> h = getHandler(charge);
		if(h == null)
			return charge;
		for(int i = 0; i < inv.getSlots() && charge.containsCharge(); ++i)
		{
			ItemStack item = inv.getStackInSlot(i);
			if(chargeSlot.test(i) && h.canCharge(item, charge))
			{
				AtomicReference<ItemStack> ref = new AtomicReference<>(item);
				charge = h.charge(ref, charge, action);
				if(ref.get() != item && action.execute())
					inv.setStackInSlot(i, ref.get());
			}
		}
		return charge;
	}
}