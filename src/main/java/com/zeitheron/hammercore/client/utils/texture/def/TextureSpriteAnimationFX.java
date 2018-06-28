package com.zeitheron.hammercore.client.utils.texture.def;

import com.zeitheron.hammercore.client.utils.texture.TextureSpriteFX;
import com.zeitheron.hammercore.utils.math.MathHelper;

public class TextureSpriteAnimationFX extends TextureSpriteFX
{
	private float[] red;
	private float[] green;
	private float[] blue;
	private float[] alpha;
	
	public TextureSpriteAnimationFX(int resolution)
	{
		super(resolution, "hammercore:animation_fx");
		setup();
	}
	
	@Override
	public void setup()
	{
		super.setup();
		this.red = new float[this.tileSizeSquare];
		this.green = new float[this.tileSizeSquare];
		this.blue = new float[this.tileSizeSquare];
		this.alpha = new float[this.tileSizeSquare];
	}
	
	@Override
	public void onTick()
	{
		for(int i = 0; i < this.tileSizeBase; i++)
		{
			for(int j = 0; j < this.tileSizeBase; j++)
			{
				float var3 = -0.05F;
				int rotation1 = (int) (MathHelper.sin(j * (float) Math.PI * 3.0F / 2.0F) * 1.0F);
				int rotation2 = (int) (MathHelper.sin(i * (float) Math.PI * 3.0F / 2.0F) * 1.0F);
				for(int k = i - 1; k <= i + 1; k++)
					for(int l = j - 1; l <= j + 1; l++)
					{
						int mod1 = k + rotation1 & this.tileSizeMask;
						int mod2 = l + rotation2 & this.tileSizeMask;
						var3 += this.red[(mod1 + mod2 * this.tileSizeBase)];
					}
				this.green[(i + j * this.tileSizeBase)] = (var3 / 10.0F + (this.blue[((i & this.tileSizeMask) + (j & this.tileSizeMask) * this.tileSizeBase)] + this.blue[((i + 1 & this.tileSizeMask) + (j & this.tileSizeMask) * this.tileSizeBase)] + this.blue[((i + 1 & this.tileSizeMask) + (j + 1 & this.tileSizeMask) * this.tileSizeBase)] + this.blue[((i & this.tileSizeMask) + (j + 1 & this.tileSizeMask) * this.tileSizeBase)]) / 4.0F * .8F);
				this.blue[(i + j * this.tileSizeBase)] += this.alpha[(i + j * this.tileSizeBase)] * 0.01F;
				if(this.blue[(i + j * this.tileSizeBase)] < 0.0F)
					this.blue[(i + j * this.tileSizeBase)] = 0.0F;
				this.alpha[(i + j * this.tileSizeBase)] -= 0.052F;
				if(Math.random() < 0.0055D)
					this.alpha[(i + j * this.tileSizeBase)] = 1.33F;
			}
		}
		
		float[] var11 = this.green;
		this.green = this.red;
		this.red = var11;
		for(int i = 0; i < tileSizeSquare; i++)
		{
			float var3 = this.red[i] * 2.0f;
			
			var3 = (float) MathHelper.clip(var3, 0, 1);
			
			int r = (int) (var3 * 255F);
			int g = (int) (var3 * 255F);
			int b = (int) (var3 * 255F);
			int a = (int) (MathHelper.clip(this.alpha[i] * 2, 0, 1) * 255F);
			
			if(this.anaglyphEnabled)
			{
				int var8 = (r * 30 + g * 59 + b * 11) / 100;
				int var9 = (r * 30 + g * 70) / 100;
				int var10 = (r * 30 + b * 70) / 100;
				r = var8;
				g = var9;
				b = var10;
			}
			
			this.imageData[i] = (255) << 24 | (r & 255) << 16 | (g & 255) << 8 | b & 255;
		}
	}
}