package org.zeith.hammerlib.util;

import net.minecraft.world.level.LevelReader;
import net.neoforged.fml.LogicalSide;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;

import java.util.Objects;
import java.util.function.*;
import java.util.stream.Stream;

public final class SidedLocal<T>
{
	private T client, server;
	
	public SidedLocal()
	{
	}
	
	public SidedLocal(Function<LogicalSide, T> defaultValue)
	{
		this.client = defaultValue.apply(LogicalSide.CLIENT);
		this.server = defaultValue.apply(LogicalSide.SERVER);
	}
	
	public static <T> SidedLocal<T> createEmpty()
	{
		return new SidedLocal<>();
	}
	
	public static <T> SidedLocal<T> initializeSideBased(Function<LogicalSide, ? extends T> trade)
	{
		SidedLocal<T> s = createEmpty();
		s.server = trade.apply(LogicalSide.SERVER);
		s.client = trade.apply(LogicalSide.CLIENT);
		return s;
	}
	
	public static <T> SidedLocal<T> initializeSeparately(Supplier<? extends T> server, Supplier<? extends T> client)
	{
		SidedLocal<T> s = createEmpty();
		s.server = server.get();
		s.client = client.get();
		return s;
	}
	
	public static <T> SidedLocal<T> initializeForBoth(Supplier<? extends T> both)
	{
		SidedLocal<T> s = createEmpty();
		s.server = both.get();
		s.client = both.get();
		return s;
	}
	
	public static <T> SidedLocal<T> initializeForBothF(Function<LogicalSide, ? extends T> both)
	{
		SidedLocal<T> s = createEmpty();
		s.server = both.apply(LogicalSide.SERVER);
		s.client = both.apply(LogicalSide.CLIENT);
		return s;
	}
	
	public static <T> SidedLocal<T> withInitial(T server, T client)
	{
		SidedLocal<T> s = createEmpty();
		s.server = server;
		s.client = client;
		return s;
	}
	
	// Without side
	
	public T get()
	{
		return get(LogicalSidePredictor.getCurrentLogicalSide());
	}
	
	public void set(T data)
	{
		set(LogicalSidePredictor.getCurrentLogicalSide(), data);
	}
	
	public T getAndSet(T data)
	{
		return getAndSet(LogicalSidePredictor.getCurrentLogicalSide(), data);
	}
	
	public void apply(UnaryOperator<T> op)
	{
		apply(LogicalSidePredictor.getCurrentLogicalSide(), op);
	}
	
	public boolean equalsTo(T value)
	{
		return Objects.equals(get(), value);
	}
	
	// With level
	
	public T get(LevelReader level)
	{
		return get(LogicalSidePredictor.getCurrentLogicalSide(level));
	}
	
	public void set(LevelReader level, T data)
	{
		set(LogicalSidePredictor.getCurrentLogicalSide(level), data);
	}
	
	public T getAndSet(LevelReader level, T data)
	{
		return getAndSet(LogicalSidePredictor.getCurrentLogicalSide(level), data);
	}
	
	public void apply(LevelReader level, UnaryOperator<T> op)
	{
		apply(LogicalSidePredictor.getCurrentLogicalSide(level), op);
	}
	
	public boolean equalsTo(LevelReader level, T value)
	{
		return Objects.equals(get(level), value);
	}
	
	// With side
	
	public T get(LogicalSide side)
	{
		if(side.isClient()) return client;
		return server;
	}
	
	public void set(LogicalSide side, T data)
	{
		if(side.isClient()) client = data;
		else server = data;
	}
	
	public T getAndSet(LogicalSide side, T data)
	{
		T prev = get(side);
		set(side, data);
		return prev;
	}
	
	public void apply(LogicalSide side, UnaryOperator<T> op)
	{
		set(side, op.apply(get(side)));
	}
	
	public boolean equalsTo(LogicalSide side, T value)
	{
		return Objects.equals(get(side), value);
	}
	
	// Misc
	
	public void applyForAllSides(UnaryOperator<T> op)
	{
		set(LogicalSide.SERVER, op.apply(get(LogicalSide.SERVER)));
		set(LogicalSide.CLIENT, op.apply(get(LogicalSide.CLIENT)));
	}
	
	public Stream<T> bothSides()
	{
		return Stream.of(client, server);
	}
	
	public void acceptBoth(Consumer<T> handler)
	{
		handler.accept(client);
		handler.accept(server);
	}
	
	public void acceptBoth(BiConsumer<T, T> handler)
	{
		handler.accept(client, server);
	}
	
	@Override
	public String toString()
	{
		return "SidedLocal{" +
			   "client=" + client +
			   ", server=" + server +
			   '}';
	}
}