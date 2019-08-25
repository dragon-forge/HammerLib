package com.zeitheron.hammercore.api.inconnect;

import com.zeitheron.hammercore.client.model.mc.BakedConnectModel;
import com.zeitheron.hammercore.client.model.mc.BakedConnectV2Model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum EnumConnTexVersion
{
	V1
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IBakedModel create(IBlockState state)
		{
			return new BakedConnectModel(state);
		}
	}, //
	V2
	{
		@Override
		@SideOnly(Side.CLIENT)
		public IBakedModel create(IBlockState state)
		{
			return new BakedConnectV2Model(state);
		}
	};
	
	@SideOnly(Side.CLIENT)
	public IBakedModel create(IBlockState state)
	{
		return null;
	}
}