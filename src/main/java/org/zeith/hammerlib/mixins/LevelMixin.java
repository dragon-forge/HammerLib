package org.zeith.hammerlib.mixins;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.api.level.IBlockEntityLevel;

import java.util.*;

@Mixin(Level.class)
@Implements(
		@Interface(iface = IBlockEntityLevel.class, prefix = "BEL$")
)
public abstract class LevelMixin
		implements IBlockEntityLevel
{
	@Unique
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

	public void BEL$unloadBlockEntities_HammerLib(Collection<BlockEntity> bes)
	{
		unloadBlockEntitityQueue_HL.addAll(bes);
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
		if (!loadBlockEntitityQueue_HL.isEmpty())
		{
			loadedBlockEntities_HL.addAll(loadBlockEntitityQueue_HL);
			loadBlockEntitityQueue_HL.clear();
		}

		if (!unloadBlockEntitityQueue_HL.isEmpty())
		{
			Map<BlockEntity, Integer> removals = new HashMap<>();

			for (BlockEntity be : unloadBlockEntitityQueue_HL)
				removals.merge(be, 1, Integer::sum);

			loadedBlockEntities_HL.removeIf(be -> {
				Integer remaining = removals.get(be);
				if (remaining == null)
					return false;

				if (remaining == 1)
					removals.remove(be);
				else
					removals.put(be, remaining - 1);

				return true;
			});

			unloadBlockEntitityQueue_HL.clear();
		}
	}
}