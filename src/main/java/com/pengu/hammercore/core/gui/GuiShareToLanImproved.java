package com.pengu.hammercore.core.gui;

import java.io.IOException;
import java.lang.reflect.Field;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.cfg.HammerCoreConfigs;
import com.pengu.hammercore.net.LanUtil;
import com.zeitheron.hammercore.lib.weupnp.EnumProtocol;
import com.zeitheron.hammercore.lib.weupnp.WeUPnP;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiShareToLanImproved extends GuiShareToLan
{
	private GuiTextField txtPort, txtMaxPlayers;
	
	public GuiShareToLanImproved(GuiScreen lastScreenIn)
	{
		super(lastScreenIn);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		txtPort = new GuiTextField(0, fontRenderer, width / 2 - 155, 145, 150, 20);
		txtPort.setVisible(true);
		
		txtMaxPlayers = new GuiTextField(0, fontRenderer, width / 2 - 155, 180, 150, 20);
		txtMaxPlayers.setVisible(true);
		
		LanUtil.load();
		txtPort.setText(LanUtil.port + "");
		txtMaxPlayers.setText(LanUtil.maxPlayers + "");
		buttonList.add(new GuiButton(-800, width / 2 + 26, 150, 128, 20, "PvP Mode: " + (LanUtil.pvp ? "ON" : " OFF")));
		buttonList.add(new GuiButton(-801, width / 2 + 26, 174, 128, 20, "Online Mode: " + (LanUtil.online ? "ON" : "OFF")));
	}
	
	@Override
	protected void actionPerformed(GuiButton b) throws IOException
	{
		super.actionPerformed(b);
		
		if(b.id == 101)
		{
			LanUtil.load();
			
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			server.setAllowPvp(LanUtil.pvp);
			server.setOnlineMode(LanUtil.online);
			server.getPlayerList().maxPlayers = LanUtil.maxPlayers;
			
			LanUtil.port = Integer.parseInt(txtPort.getText());
			
			mc.player.sendMessage(new TextComponentString("Server properties:"));
			mc.player.sendMessage(new TextComponentString("   PvP: " + (server.isPVPEnabled() ? "ON" : "OFF")));
			mc.player.sendMessage(new TextComponentString("   Online: " + (server.isServerInOnlineMode() ? "ON" : "OFF")));
			mc.player.sendMessage(new TextComponentString("   Max Players: " + server.getPlayerList().getMaxPlayers()));
			
			if(HammerCoreConfigs.CustomLANPortInstalled)
				new Thread(() ->
				{
					mc.player.sendMessage(new TextComponentString("Attempting to open port " + LanUtil.port + " via UPnP..."));
					
					try
					{
						WeUPnP p = new WeUPnP();
						p.setup();
						p.discover();
						p.logFound(HammerCore.LOG);
						HammerCore.closeAfterLogoff.add(p.attune(EnumProtocol.TCP, LanUtil.port, LanUtil.port, "MC LAN from " + mc.player.getGameProfile().getName() + " | TCP"));
						HammerCore.closeAfterLogoff.add(p.attune(EnumProtocol.UDP, LanUtil.port, LanUtil.port, "MC LAN from " + mc.player.getGameProfile().getName() + " | UDP"));
						mc.player.sendMessage(new TextComponentString("Port " + LanUtil.port + " opened via UPnP! This means that your friends can join you using your external IP!"));
					} catch(Throwable err)
					{
						mc.player.sendMessage(new TextComponentString("Port " + LanUtil.port + " failed to open via UPnP!"));
					}
				}).start();
			
			LanUtil.save();
		}
		
		if(b.id == -800)
		{
			LanUtil.pvp = !LanUtil.pvp;
			LanUtil.save();
			b.displayString = "PvP Mode: " + (LanUtil.pvp ? "ON" : "OFF");
		}
		
		if(b.id == -801)
		{
			LanUtil.online = !LanUtil.online;
			LanUtil.save();
			b.displayString = "Online Mode: " + (LanUtil.online ? "ON" : "OFF");
		}
	}
	
	@Override
	protected void mouseClicked(int p_mouseClicked_1_, int p_mouseClicked_2_, int p_mouseClicked_3_) throws IOException
	{
		super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
		txtPort.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
		txtMaxPlayers.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
	}
	
	@Override
	public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_)
	{
		super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
		txtPort.drawTextBox();
		txtMaxPlayers.drawTextBox();
		fontRenderer.drawString("Open on port: (1000 - 65535 or 0 for auto)", width / 2 - 155, 135, 16777215, false);
		fontRenderer.drawString("Max. Players:", width / 2 - 155, 170, 16777215, false);
	}
	
	@Override
	protected void keyTyped(char par1, int par2) throws IOException
	{
		if(txtPort.isFocused())
		{
			txtPort.textboxKeyTyped(par1, par2);
			int port = 0;
			try
			{
				port = Integer.valueOf(txtPort.getText()).intValue();
			} catch(NumberFormatException e)
			{
			}
			LanUtil.port = port;
		}
		
		if(txtMaxPlayers.isFocused())
		{
			txtMaxPlayers.textboxKeyTyped(par1, par2);
			int maxPlayers = 0;
			try
			{
				maxPlayers = Integer.valueOf(txtMaxPlayers.getText()).intValue();
			} catch(NumberFormatException e)
			{
			}
			LanUtil.maxPlayers = maxPlayers;
		}
		
		GuiButton b = (buttonList.get(0));
		b.enabled = (LanUtil.port >= 1000 && LanUtil.port <= 65535 || LanUtil.port == 0) && LanUtil.maxPlayers > 0;
	}
}