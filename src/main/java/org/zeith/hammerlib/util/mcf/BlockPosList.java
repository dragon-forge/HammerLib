package org.zeith.hammerlib.util.mcf;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.AbstractList;
import java.util.function.LongConsumer;

public class BlockPosList
		extends AbstractList<BlockPos>
		implements INBTSerializable<ListNBT>
{
	final LongList backing;

	public BlockPosList()
	{
		this.backing = new LongArrayList();
	}

	public BlockPosList(int capacity)
	{
		this.backing = new LongArrayList(capacity);
	}

	public BlockPosList(ListNBT lst)
	{
		this.backing = new LongArrayList(lst.size());
		deserializeNBT(lst);
	}

	@Override
	public void clear()
	{
		backing.clear();
	}

	@Override
	public void add(int index, BlockPos element)
	{
		backing.add(index, element.asLong());
	}

	@Override
	public BlockPos remove(int index)
	{
		return BlockPos.of(backing.removeLong(index));
	}

	@Override
	public int indexOf(Object o)
	{
		return o instanceof BlockPos ? backing.indexOf(((BlockPos) o).asLong()) : -1;
	}

	@Override
	public int lastIndexOf(Object o)
	{
		return o instanceof BlockPos ? backing.lastIndexOf(((BlockPos) o).asLong()) : -1;
	}

	@Override
	public BlockPos get(int index)
	{
		return BlockPos.of(backing.getLong(index));
	}

	@Override
	public BlockPos set(int index, BlockPos element)
	{
		return BlockPos.of(backing.set(index, element.asLong()));
	}

	@Override
	public int size()
	{
		return backing.size();
	}

	@Override
	public boolean isEmpty()
	{
		return backing.isEmpty();
	}

	@Override
	public boolean contains(Object o)
	{
		return o instanceof BlockPos && backing.contains(((BlockPos) o).asLong());
	}

	@Override
	public boolean add(BlockPos blockPos)
	{
		return backing.add(blockPos.asLong());
	}

	@Override
	public boolean remove(Object o)
	{
		return o instanceof BlockPos && backing.remove(((BlockPos) o).asLong());
	}

	@Override
	public ListNBT serializeNBT()
	{
		ListNBT nbt = new ListNBT();
		backing.forEach((LongConsumer) l -> nbt.add(LongNBT.valueOf(l)));
		return nbt;
	}

	@Override
	public void deserializeNBT(ListNBT nbt)
	{
		backing.clear();
		nbt.forEach(i -> backing.add(((LongNBT) i).getAsLong()));
	}
}