package com.zeitheron.hammercore.utils.base;

import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

import java.util.Objects;
import java.util.function.*;

public class SideLocal<T>
{
	private T client, server;

	public static <T> SideLocal<T> createEmpty()
	{
		return new SideLocal<>();
	}

	public static <T> SideLocal<T> initializeSideBased(Function<Side, ? extends T> trade)
	{
		SideLocal<T> s = createEmpty();
		s.server = trade.apply(Side.SERVER);
		s.client = trade.apply(Side.CLIENT);
		return s;
	}

	public static <T> SideLocal<T> initializeSeparately(Supplier<? extends T> server, Supplier<? extends T> client)
	{
		SideLocal<T> s = createEmpty();
		s.server = server.get();
		s.client = client.get();
		return s;
	}

	public static <T> SideLocal<T> initializeForBoth(Supplier<? extends T> both)
	{
		SideLocal<T> s = createEmpty();
		s.server = both.get();
		s.client = both.get();
		return s;
	}

	public static <T> SideLocal<T> initializeForBothF(Function<Side, ? extends T> both)
	{
		SideLocal<T> s = createEmpty();
		s.server = both.apply(Side.SERVER);
		s.client = both.apply(Side.CLIENT);
		return s;
	}

	public static <T> SideLocal<T> withInitial(T server, T client)
	{
		SideLocal<T> s = createEmpty();
		s.server = server;
		s.client = client;
		return s;
	}

	// Without side

	public T get()
	{
		return get(getCurrentSide());
	}

	public void set(T data)
	{
		set(getCurrentSide(), data);
	}

	public T getAndSet(T data)
	{
		return getAndSet(getCurrentSide(), data);
	}
	
	public void apply(UnaryOperator<T> op)
	{
		apply(side(), op);
	}
	
	public boolean equalsTo(T value)
	{
		return Objects.equals(get(), value);
	}
	
	// With level
	
	public T get(World level)
	{
		return get(LogicalSidePredictor.getCurrentLogicalSide(level));
	}
	
	public void set(World level, T data)
	{
		set(LogicalSidePredictor.getCurrentLogicalSide(level), data);
	}
	
	public T getAndSet(World level, T data)
	{
		return getAndSet(LogicalSidePredictor.getCurrentLogicalSide(level), data);
	}
	
	public void apply(World level, UnaryOperator<T> op)
	{
		apply(LogicalSidePredictor.getCurrentLogicalSide(level), op);
	}
	
	public boolean equalsTo(World level, T value)
	{
		return Objects.equals(get(level), value);
	}
	
	// With side
	
	public T get(Side side)
	{
		if(side.isClient()) return client;
		return server;
	}
	
	public void set(Side side, T data)
	{
		if(side.isClient()) client = data;
		else server = data;
	}
	
	public T getAndSet(Side side, T data)
	{
		T prev = get(side);
		set(side, data);
		return prev;
	}
	
	public void apply(Side side, UnaryOperator<T> op)
	{
		set(side, op.apply(get(side)));
	}
	
	public boolean equalsTo(Side side, T value)
	{
		return Objects.equals(get(side), value);
	}
	
	// Misc

	public Side getCurrentSide()
	{
		return FMLCommonHandler.instance().getEffectiveSide();
	}

	public static Side side()
	{
		return FMLCommonHandler.instance().getEffectiveSide();
	}

	@Override
	public String toString()
	{
		return "SideLocal{" +
				"client=" + client +
				", server=" + server +
				'}';
	}
}