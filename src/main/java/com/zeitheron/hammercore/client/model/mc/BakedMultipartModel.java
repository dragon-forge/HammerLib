package com.zeitheron.hammercore.client.model.mc;

import com.google.common.base.Predicates;
import com.zeitheron.hammercore.api.multipart.IMultipartBaked;
import com.zeitheron.hammercore.internal.blocks.multipart.TileMultipart;
import com.zeitheron.hammercore.utils.PositionedStateImplementation;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BakedMultipartModel
		implements IBakedModel
{
	@Override
	public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
	{
		List<BakedQuad> quads = new ArrayList<>();
		if(state instanceof PositionedStateImplementation)
		{
			PositionedStateImplementation pstate = (PositionedStateImplementation) state;
			IBlockAccess world = pstate.getWorld();
			BlockPos pos = pstate.getPos();
			TileEntity tile = world.getTileEntity(pos);
			if(tile instanceof TileMultipart)
			{
				TileMultipart tmp = (TileMultipart) tile;
				Consumer<BakedQuad> qc = quads::add;
				Function<String, TextureAtlasSprite> sf = Minecraft.getMinecraft().getTextureMapBlocks()::getAtlasSprite;
				tmp.signatures().stream().filter(Predicates.instanceOf(IMultipartBaked.class)).map(s -> (IMultipartBaked) s).filter(b -> b.acceptsFacing(side)).forEach(b -> b.generateBakedQuads(qc, sf, BakedConnectModel.FACE_BAKERY, side, rand));
			}
		}
		return quads;
	}

	@Override
	public boolean isAmbientOcclusion()
	{
		return false;
	}

	@Override
	public boolean isGui3d()
	{
		return false;
	}

	@Override
	public boolean isBuiltInRenderer()
	{
		return true;
	}

	@Override
	public TextureAtlasSprite getParticleTexture()
	{
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}

	@Override
	public ItemOverrideList getOverrides()
	{
		return ItemOverrideList.NONE;
	}
}