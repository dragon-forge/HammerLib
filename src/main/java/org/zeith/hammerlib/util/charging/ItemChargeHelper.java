package org.zeith.hammerlib.util.charging;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.objectweb.asm.Type;
import org.zeith.hammerlib.HammerLib;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ItemChargeHelper
{
	private static final Map<Class<? extends AbstractCharge>, IChargeHandler> CHARGE_HANDLERS = new HashMap<>();
	private static final Map<String, IChargeHandler> CHARGE_HANDLERS_BY_ID = new HashMap<>();

	public static final List<IPlayerInventoryLister> playerInvListers = new ArrayList<>();

	static
	{
//		handle(FECharge.class, new FEChargeHandler());
//		handle(FluidCharge.class, new FluidChargeHandler());
	}

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
							Constructor<? extends IChargeHandler> ctor = registerer.asSubclass(IChargeHandler.class).getDeclaredConstructor();
							ctor.setAccessible(true);

							Type typeRaw = Cast.cast(data.getProperty("value").orElse(null));
							Class<? extends AbstractCharge> type = ReflectionUtil.fetchClass(typeRaw);
							IChargeHandler handler = ctor.newInstance();

							HammerLib.LOG.info("Registered charge handler for type " + type.getName() + " - " + handler);

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
							Constructor<? extends IPlayerInventoryLister> ctor = registerer.asSubclass(IPlayerInventoryLister.class).getDeclaredConstructor();
							ctor.setAccessible(true);
							playerInvListers.add(ctor.newInstance());
							HammerLib.LOG.info("Registered inventory lister " + registerer.getName());
						} catch(ReflectiveOperationException e)
						{
							throw new RuntimeException(e);
						}
					}
				});
	}

	/**
	 * Maps a handler to a charge type.
	 *
	 * @param <T>        The charge type
	 * @param chargeType the class of the charge
	 * @param handler    the handler to be assigned
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

	public static <T extends AbstractCharge> T charge(ItemStack stack, T charge, IChargeHandler.ChargeAction simulate)
	{
		IChargeHandler<T> h = getHandler(charge);
		if(h == null)
			return charge;
		return h.charge(stack, charge, simulate);
	}

	private static final Predicate<Integer> I_TRUE = i -> true;

	public static <T extends AbstractCharge> T chargeInventory(Inventory inv, T charge, IChargeHandler.ChargeAction simulate)
	{
		return chargeInventory(inv, I_TRUE, charge, simulate);
	}

	/**
	 * Charges player's inventory. Supports modules, HammerCore supports
	 * player's vanilla inventory and baubles inventory.
	 *
	 * @param <T>      The charge type
	 * @param player   The player
	 * @param charge   The charge
	 * @param simulate true to simulate, false to perform
	 */
	public static <T extends AbstractCharge> T chargePlayer(Player player, T charge, IChargeHandler.ChargeAction simulate)
	{
		IChargeHandler<T> h = getHandler(charge);
		if(h == null)
			return charge;

		List<IItemHandlerModifiable> handlers = new ArrayList<>();
		playerInvListers.forEach(pil -> pil.listItemHandlers(player, handlers));

		for(int j = 0; j < handlers.size() && charge.containsCharge(); ++j)
		{
			IItemHandlerModifiable handler = handlers.get(j);
			for(int i = 0; i < handler.getSlots() && charge.containsCharge(); ++i)
				if(h.canCharge(handler.getStackInSlot(i), charge))
				{
					ItemStack stack = handler.getStackInSlot(i).copy();
					charge = h.charge(stack, charge, simulate);
					handler.setStackInSlot(i, stack);
				}
		}

		return charge;
	}

	public static <T extends AbstractCharge> T chargeInventory(Inventory inv, Predicate<Integer> chargeSlot, T charge, IChargeHandler.ChargeAction simulate)
	{
		IChargeHandler<T> h = getHandler(charge);
		if(h == null)
			return charge;
		for(int i = 0; i < inv.getContainerSize() && charge.containsCharge(); ++i)
		{
			ItemStack item = inv.getItem(i);
			if(chargeSlot.test(i) && h.canCharge(item, charge))
				charge = h.charge(item, charge, simulate);
		}
		return charge;
	}
}