package com.pengu.hammercore.common.blocks.multipart;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.pengu.hammercore.api.handlers.iHandlerProvider;
import com.pengu.hammercore.api.handlers.iTileHandler;
import com.pengu.hammercore.api.multipart.MultipartSignature;
import com.pengu.hammercore.api.multipart.iRandomDisplayTick;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.vec.Cuboid6;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class TileMultipart extends TileSyncableTickable implements iHandlerProvider
{
	private Set<MultipartSignature> signatures = new HashSet<>();
	private Cuboid6[] lastBaked = null;
	
	private boolean hasSyncedOnce = false;
	
	private List<MultipartSignature> renderSignatures = new ArrayList<>();
	private Set<iRandomDisplayTick> displayTickable = new HashSet<>();
	
	private int lastPlayerCount = 0;
	private int ticksEmpty = 0;
	
	@Override
	public void tick()
	{
		// if(world.isRemote) System.out.println(hasSyncedOnce);
		
		for(MultipartSignature signature : signatures())
		{
			if(signature.getOwner() != this)
			{
				removeMultipart(signature, false);
				return;
			}
			
			signature.setWorld(world);
			signature.setPos(pos);
			
			if(signature instanceof ITickable)
				((ITickable) signature).update();
		}
		
		// Attempted to wait 4 seconds before actually removing multipart.
		if(!world.isRemote && signatures().isEmpty())
			world.setBlockToAir(pos);
		
		// Attempted sync
		if(!world.isRemote && ticksExisted > 40 && hasSyncedOnce)
		{
			List<EntityPlayerMP> players = world.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(pos).expand(4, 4, 4));
			if(lastPlayerCount == -1 || lastPlayerCount != players.size())
				sync();
			lastPlayerCount = players.size();
		}
		
		if(!hasSyncedOnce && !world.isRemote)
		{
			ticksExisted = 0;
			hasSyncedOnce = true;
			lastPlayerCount = -1;
		}
		
		if(ticksExisted % 40 == 0)
			lastBaked = null;
	}
	
	@Override
	public void sync()
	{
		super.sync();
		lastBaked = bakeCuboids();
	}
	
	public int getWeakPower(EnumFacing side)
	{
		int power = 0;
		for(MultipartSignature s : signatures())
			power = Math.max(s.getWeakPower(side), power);
		return power;
	}
	
	public int getStrongPower(EnumFacing side)
	{
		int power = 0;
		for(MultipartSignature s : signatures())
			power = Math.max(s.getStrongPower(side), power);
		return power;
	}
	
	public List<MultipartSignature> signatures()
	{
		return renderSignatures;
	}
	
	public boolean onBoxActivated(int boxID, Cuboid6 box, World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		for(MultipartSignature s : signatures())
			if(s != null && box != null && s.getBoundingBox() != null && s.getBoundingBox().intersects(box.aabb()))
				return s.onSignatureActivated(worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
		return false;
	}
	
	public int getLightLevel()
	{
		int max = 0;
		for(MultipartSignature s : signatures())
			max = Math.max(max, s.getLightLevel());
		return max;
	}
	
	public void randomDisplayTick(Random rand)
	{
		for(iRandomDisplayTick rdt : displayTickable)
			rdt.randomDisplayTick(rand);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		if(world != null && world.isRemote)
			hasSyncedOnce = true;
		lastBaked = null;
		
		signatures = new HashSet<>();
		renderSignatures = new ArrayList<>();
		displayTickable = new HashSet<>();
		
		NBTTagList list = nbt.getTagList("signature", NBT.TAG_COMPOUND);
		for(int i = 0; i < list.tagCount(); ++i)
			internal_addMultipart(MultipartSignature.createAndLoadSignature(list.getCompoundTagAt(i), this));
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		NBTTagList list = new NBTTagList();
		for(MultipartSignature s : signatures())
		{
			NBTTagCompound tag = new NBTTagCompound();
			s.writeSignature(tag);
			list.appendTag(tag);
		}
		nbt.setTag("signature", list);
	}
	
	public int getNextSignatureIndex()
	{
		return signatures().size();
	}
	
	public boolean canPlace_def(MultipartSignature signature)
	{
		AxisAlignedBB aabb = signature.getBoundingBox();
		for(MultipartSignature s : signatures())
			if(s.getBoundingBox() != null && s.getBoundingBox().intersects(aabb) && !s.isReplaceable() && s.doesMindCollision(signature.getBoundingBox(), aabb.union(signature.getBoundingBox())))
				return false;
		return true;
	}
	
	public boolean canPlace(MultipartSignature signature)
	{
		return signature.canPlaceInto(this);
	}
	
	public boolean addMultipart(MultipartSignature signature)
	{
		if(!canPlace(signature))
			return false;
		internal_addMultipart(signature);
		return true;
	}
	
	private void internal_addMultipart(MultipartSignature signature)
	{
		signature.setPos(pos);
		signature.setWorld(world);
		signature.setOwner(this);
		
		Set<MultipartSignature> signs_new = new HashSet<>(signatures);
		signs_new.add(signature);
		signatures = signs_new;
		
		if(signature instanceof iRandomDisplayTick)
		{
			Set<iRandomDisplayTick> ticks = new HashSet<>(displayTickable);
			ticks.add((iRandomDisplayTick) signature);
			displayTickable = ticks;
		}
		renderSignatures = new ArrayList<>(signs_new);
		lastBaked = null;
		if(world != null && !world.isRemote)
			sync();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void removeMultipart(MultipartSignature signature, boolean spawnDrop)
	{
		if(!signatures.contains(signature))
			return;
		signature.onRemoved(spawnDrop);
		
		Set<MultipartSignature> signs_new = new HashSet<>(signatures);
		signs_new.remove(signature);
		signatures = signs_new;
		
		renderSignatures = new ArrayList<>(signatures);
		signature.setOwner(null);
		if(signature instanceof iRandomDisplayTick)
		{
			Set<iRandomDisplayTick> ticks = new HashSet<>(displayTickable);
			ticks.remove(signature);
			displayTickable = ticks;
		}
		lastBaked = null;
		if(world != null && !world.isRemote)
			sync();
	}
	
	public MultipartSignature getSignature(Vec3d pos)
	{
		if(signatures().size() > 100)
			world.destroyBlock(getPos(), false);
		for(MultipartSignature s : signatures())
			if(s.getBoundingBox() != null && s.getBoundingBox().intersects(pos.addVector(-.0001, -.0001, -.0001), pos.addVector(.0001, .0001, .0001)))
				return s;
		return null;
	}
	
	public Cuboid6[] getCuboids()
	{
		if(lastBaked == null)
			lastBaked = bakeCuboids();
		return lastBaked;
	}
	
	public Cuboid6[] bakeCuboids()
	{
		List<Cuboid6> cubs = new ArrayList<>();
		for(MultipartSignature signature : signatures())
			cubs.add(new Cuboid6(signature.getBoundingBox()));
		return cubs.toArray(new Cuboid6[0]);
	}
	
	@Override
	public <T extends iTileHandler> T getHandler(EnumFacing facing, Class<T> handler, Object... params)
	{
		for(MultipartSignature signature : signatures())
		{
			iHandlerProvider provider = WorldUtil.cast(signature, iHandlerProvider.class);
			if(provider != null)
			{
				T h = provider.getHandler(facing, handler, params);
				if(handler != null)
					return h;
			}
		}
		
		return null;
	}
	
	@Override
	public <T extends iTileHandler> boolean hasHandler(EnumFacing facing, Class<T> handler, Object... params)
	{
		for(MultipartSignature signature : signatures())
		{
			iHandlerProvider provider = WorldUtil.cast(signature, iHandlerProvider.class);
			if(provider != null && provider.hasHandler(facing, handler, params))
				return true;
		}
		
		return false;
	}
}