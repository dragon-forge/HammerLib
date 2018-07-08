package com.zeitheron.hammercore.client.userlocal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleSupplier;

import org.lwjgl.input.Keyboard;

import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.client.PerUserModule;
import com.zeitheron.hammercore.client.UserModule;
import com.zeitheron.hammercore.client.color.PlayerInterpolator;
import com.zeitheron.hammercore.client.gui.GuiCentered;
import com.zeitheron.hammercore.client.gui.impl.GuiCustomizeSkinHC;
import com.zeitheron.hammercore.client.utils.RenderUtil;
import com.zeitheron.hammercore.utils.color.ColorHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

@UserModule(username = "Zeitheron")
public class Zeitheron extends PerUserModule
{
	public static final KeyBinding openEyeColor = new KeyBinding("Eye changing GUI", Keyboard.KEY_APOSTROPHE, "key.categories.gameplay");
	
	@Override
	public void preInit()
	{
		GuiCustomizeSkinHC.customization = () -> new Customization();
		HammerCore.instance.MCFBusObjects.add(this);
	}
	
	@Override
	public void init()
	{
		ClientRegistry.registerKeyBinding(openEyeColor);
		openEyeColor.setKeyConflictContext(KeyConflictContext.IN_GAME);
	}
	
	@SubscribeEvent
	public void clientTick(ClientTickEvent cte)
	{
		if(cte.phase != Phase.START)
			return;
			
		if(openEyeColor.isKeyDown())
			Minecraft.getMinecraft().displayGuiScreen(new Customization());
	}
	
	private static class Customization extends GuiCentered
	{
		public int currentColor;
		public int targetColor;
		public int timeInt;
		public DoubleSupplier progress;
		
		public GuiTextField time, targetColorTF;
		
		public Customization()
		{
			refresh();
		}
		
		public void refresh()
		{
			NBTTagCompound nbt = HCClientOptions.getOptions().getCustomData();
			NBTTagCompound eyecol = nbt.getCompoundTag("EyeColor");
			
			timeInt = eyecol.getInteger("Time");
			
			progress = () ->
			{
				long ctime = Minecraft.getMinecraft().player.world.getTotalWorldTime();
				long stime = eyecol.getLong("TimeStart");
				int time = eyecol.getInteger("Time");
				long ttime = stime + time;
				return time == 0 ? 1F : ctime > ttime ? 0 : ctime < stime ? 1 : (ttime - ctime) / (double) time;
			};
			
			targetColor = eyecol.getInteger("New");
			currentColor = eyecol.getInteger("Old");
		}
		
		@Override
		public void initGui()
		{
			xSize = 180;
			ySize = 200;
			
			super.initGui();
			time = new GuiTextField(0, fontRenderer, (int) guiLeft, (int) guiTop + 90, (int) xSize, 20);
			NBTTagCompound nbt = HCClientOptions.getOptions().getCustomData();
			NBTTagCompound eyecol = nbt.getCompoundTag("EyeColor");
			time.setText(eyecol.getInteger("Time") + "");
			
			targetColorTF = new GuiTextField(1, fontRenderer, (int) guiLeft, (int) guiTop + 120, (int) xSize, 20);
			
			addButton(new GuiButton(0, (int) guiLeft, (int) guiTop + 150, (int) xSize, 20, "Start interpolation!"));
		}
		
		List<String> tooltip = new ArrayList<>();
		
		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
		{
			drawDefaultBackground();
			
			RenderUtil.drawGradientRect(guiLeft, guiTop, 40, 40, 255 << 24 | currentColor, 255 << 24 | currentColor);
			RenderUtil.drawGradientRect(guiLeft + xSize - 40, guiTop, 40, 40, 255 << 24 | targetColor, 255 << 24 | targetColor);
			double progress = 1 - this.progress.getAsDouble();
			RenderUtil.drawGradientRect(guiLeft + 48, guiTop, xSize - 96, 40, 0xFF444444, 0xFF444444);
			int interpRGB = ColorHelper.interpolate(255 << 24 | currentColor, 255 << 24 | targetColor, (float) progress);
			RenderUtil.drawGradientRect(guiLeft + 50, guiTop + 2, (xSize - 100) * progress, 36, interpRGB, interpRGB);
			
			time.drawTextBox();
			targetColorTF.drawTextBox();
			
			tooltip.clear();
			
			if(mouseX >= time.x && mouseX < time.x + time.width && mouseY >= time.y && mouseY < time.y + time.height)
			{
				tooltip.add("Time (Int)");
			}
			
			if(mouseX >= targetColorTF.x && mouseX < targetColorTF.x + targetColorTF.width && mouseY >= targetColorTF.y && mouseY < targetColorTF.y + targetColorTF.height)
			{
				tooltip.add("New Target Color (Hex Int or Color)");
			}
			
			drawHoveringText(tooltip, mouseX, mouseY);
			
			GlStateManager.disableLighting();
		}
		
		@Override
		protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
		{
			if(!time.mouseClicked(mouseX, mouseY, mouseButton))
				if(!targetColorTF.mouseClicked(mouseX, mouseY, mouseButton))
					super.mouseClicked(mouseX, mouseY, mouseButton);
		}
		
		@Override
		protected void keyTyped(char typedChar, int keyCode) throws IOException
		{
			String ot = time.getText();
			boolean timeTyped = time.textboxKeyTyped(typedChar, keyCode);
			
			if(timeTyped && !time.getText().isEmpty())
				try
				{
					Integer.parseInt(time.getText());
				} catch(Throwable err)
				{
					time.setText(ot);
				}
			
			if(!timeTyped)
				if(!targetColorTF.textboxKeyTyped(typedChar, keyCode))
					super.keyTyped(typedChar, keyCode);
		}
		
		@Override
		protected void actionPerformed(GuiButton button) throws IOException
		{
			if(button.id == 0)
			{
				NBTTagCompound nbt = HCClientOptions.getOptions().getCustomData();
				nbt.setTag("EyeColor", PlayerInterpolator.targetTo(nbt.getCompoundTag("EyeColor"), mc.player, ColorHelper.getColorByName(targetColorTF.getText()), Integer.parseInt(time.getText())));
				HCClientOptions.getOptions().saveAndSendToServer();
				refresh();
			}
		}
	}
}