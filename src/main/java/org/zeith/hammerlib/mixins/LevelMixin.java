package org.zeith.hammerlib.mixins;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.api.level.IBlockEntityLevel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin(Level.class)
@Implements(
		@Interface(iface = IBlockEntityLevel.class, prefix = "BEL$")
)
public abstract class LevelMixin
		implements IBlockEntityLevel
{
	private final List<BlockEntity> loadedBlockEntities_HL = new ArrayList<>(), loadBlockEntitityQueue_HL = new ArrayList<>(), unloadBlockEntitityQueue_HL = new ArrayList<>();

	public List<BlockEntity> BEL$getLoadedBlockEntities_HammerLib()
	{
		return loadedBlockEntities_HL;
	}

	public void BEL$loadBlockEntity_HammerLib(BlockEntity be)
	{
		loadBlockEntitityQueue_HL.add(be);
	}

	public void BEL$unloadBlockEntity_HammerLib(BlockEntity be)
	{
		unloadBlockEntitityQueue_HL.add(be);
	}

	@Inject(
			method = "addFreshBlockEntities",
			at = @At("HEAD"),
			remap = false
	)
	public void addFreshBlockEntities_HammerLib(Collection<BlockEntity> beList, CallbackInfo ci)
	{
		loadBlockEntitityQueue_HL.addAll(beList);
	}

	@Inject(
			method = "tickBlockEntities",
			at = @At("HEAD")
	)
	public void tickBlockEntities_HammerLib(CallbackInfo ci)
	{
		while(!loadBlockEntitityQueue_HL.isEmpty())
			loadedBlockEntities_HL.add(loadBlockEntitityQueue_HL.remove(0));
		while(!unloadBlockEntitityQueue_HL.isEmpty())
			loadedBlockEntities_HL.remove(unloadBlockEntitityQueue_HL.remove(0));
	}
}