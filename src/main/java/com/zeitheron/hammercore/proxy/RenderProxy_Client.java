package com.zeitheron.hammercore.proxy;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.properties.PropertyMap;
import com.zeitheron.hammercore.HammerCore;
import com.zeitheron.hammercore.ServerHCClientPlayerData;
import com.zeitheron.hammercore.TooltipAPI;
import com.zeitheron.hammercore.annotations.AtTESR;
import com.zeitheron.hammercore.cfg.HammerCoreConfigs;
import com.zeitheron.hammercore.client.HCClientOptions;
import com.zeitheron.hammercore.client.HammerCoreClient;
import com.zeitheron.hammercore.client.PerUserModule;
import com.zeitheron.hammercore.client.gui.impl.GuiPersonalisation;
import com.zeitheron.hammercore.client.model.HasNoModel;
import com.zeitheron.hammercore.client.particle.RenderHelperImpl;
import com.zeitheron.hammercore.client.render.Render3D;
import com.zeitheron.hammercore.client.render.item.TileEntityItemStackRendererHC;
import com.zeitheron.hammercore.client.render.tesr.TESR;
import com.zeitheron.hammercore.client.utils.IEnchantmentColorManager;
import com.zeitheron.hammercore.client.utils.IRenderHelper;
import com.zeitheron.hammercore.client.utils.ItemColorHelper;
import com.zeitheron.hammercore.client.utils.RenderGui;
import com.zeitheron.hammercore.client.utils.TexturePixelGetter;
import com.zeitheron.hammercore.client.utils.texture.BufferedTexture;
import com.zeitheron.hammercore.client.utils.texture.ClientSkinManager;
import com.zeitheron.hammercore.client.utils.texture.TextureFXManager;
import com.zeitheron.hammercore.client.utils.texture.TextureUtils;
import com.zeitheron.hammercore.client.utils.texture.gui.theme.GuiTheme;
import com.zeitheron.hammercore.client.witty.SplashTextHelper;
import com.zeitheron.hammercore.internal.init.ItemsHC;
import com.zeitheron.hammercore.lib.zlib.error.JSONException;
import com.zeitheron.hammercore.lib.zlib.io.IOUtils;
import com.zeitheron.hammercore.lib.zlib.json.JSONObject;
import com.zeitheron.hammercore.lib.zlib.utils.Threading;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.tile.tooltip.own.EntityTooltipRenderEngine;
import com.zeitheron.hammercore.utils.AnnotatedInstanceUtil;
import com.zeitheron.hammercore.utils.WorldUtil;
import com.zeitheron.hammercore.utils.color.ColorHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderProxy_Client extends RenderProxy_Common implements IEnchantmentColorManager
{
	public final EntityTooltipRenderEngine tooltipEngine = new EntityTooltipRenderEngine();
	
	private List<TESR> tesrs;
	private boolean cticked, reloaded;
	
	public static PerUserModule module;
	
	@Override
	public void construct()
	{
		MinecraftForge.EVENT_BUS.register(new RenderGui());
		MinecraftForge.EVENT_BUS.register(new Render3D());
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new TooltipAPI());
		MinecraftForge.EVENT_BUS.register(new TextureUtils());
		MinecraftForge.EVENT_BUS.register(new TexturePixelGetter());
		MinecraftForge.EVENT_BUS.register(new SplashTextHelper());
		MinecraftForge.EVENT_BUS.register(new HammerCoreClient());
		
		TextureFXManager.INSTANCE.preInit();
	}
	
	@Override
	public void preInit(ASMDataTable table)
	{
		tesrs = AnnotatedInstanceUtil.getInstances(table, AtTESR.class, TESR.class);
		module = AnnotatedInstanceUtil.getUserModule(table);
		
		HammerCore.LOG.info("Using per-user module " + module.getClass().getSimpleName());
		
		if(module != null)
			module.preInit();
		
		ClientCommandHandler.instance.registerCommand(new CommandBase()
		{
			@Override
			public int getRequiredPermissionLevel()
			{
				return 0;
			}
			
			@Override
			public boolean checkPermission(MinecraftServer server, ICommandSender sender)
			{
				return sender instanceof EntityPlayer;
			}
			
			@Override
			public String getUsage(ICommandSender sender)
			{
				return "/hc_themes";
			}
			
			@Override
			public String getName()
			{
				return "hc_themes";
			}
			
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
			{
				new Thread(() ->
				{
					Scanner sc = new Scanner(HammerCore.class.getResourceAsStream("/assets/hammercore/themes/themes.txt"));
					while(sc.hasNext())
						GuiTheme.makeTheme(sc.next());
					sc.close();
					
					try
					{
						Thread.sleep(100L);
					} catch(Throwable err)
					{
					}
					
					Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().displayGuiScreen(new GuiPersonalisation()));
				}).start();
			}
		});
	}
	
	@Override
	public void init()
	{
		if(module != null)
			module.init();
			
		// This is an example of how to make custom textures!
		// TextureSpriteCustom.createSprite(new ResourceLocation("hammercore",
		// "builtin/animation_fx")).addTextureFX(new
		// TextureSpriteAnimationFX(16));
		
		ItemColorHelper.addManager(this, Items.EXPERIENCE_BOTTLE, Items.GOLDEN_APPLE, Items.NETHER_STAR);
		
		new TileEntityItemStackRendererHC();
		
		registerRenders(ItemsHC.items);
		
		for(Item i : ItemsHC.rendered_items)
			Minecraft.getMinecraft().getRenderItem().registerItem(i, 0, "chest");
		
		HammerCore.LOG.info("Registering TESRs...");
		for(TESR t : tesrs)
		{
			AtTESR at = t.getClass().getAnnotation(AtTESR.class);
			if(at != null)
			{
				HammerCore.LOG.info(" -" + t.getClass().getName() + " for " + at.value().getName());
				ClientRegistry.bindTileEntitySpecialRenderer(at.value(), t);
			}
		}
		HammerCore.LOG.info("Registered " + tesrs.size() + " TESRs.");
		
		try
		{
			HCClientOptions.options.load((JSONObject) IOUtils.jsonparse(new File("hc_options.txt")));
		} catch(JSONException e)
		{
			e.printStackTrace();
		}
		
		loadCAPS();
	}
	
	@Override
	public void postInit()
	{
		if(module != null)
			module.postInit();
	}
	
	@Override
	public IRenderHelper getRenderHelper()
	{
		return RenderHelperImpl.INSTANCE;
	}
	
	@Override
	public EntityPlayer getClientPlayer()
	{
		return Minecraft.getMinecraft().player;
	}
	
	private static final int DELETION_ID = 0x16F7F6;
	private static int lastAdded;
	
	@Override
	public void sendNoSpamMessages(ITextComponent[] messages)
	{
		GuiNewChat chat = Minecraft.getMinecraft().ingameGUI.getChatGUI();
		for(int i = DELETION_ID + messages.length - 1; i <= lastAdded; i++)
			chat.deleteChatLine(i);
		for(int i = 0; i < messages.length; i++)
			chat.printChatMessageWithOptionalDeletion(messages[i], DELETION_ID + i);
		lastAdded = DELETION_ID + messages.length - 1;
	}
	
	public static void registerRenders(Iterable<Item> items)
	{
		Iterator<Item> iter = items.iterator();
		while(iter.hasNext())
			registerRender(iter.next());
	}
	
	public static void registerRender(Item item)
	{
		if(item.getClass().getAnnotation(HasNoModel.class) != null || (item instanceof ItemBlock && ((ItemBlock) item).getBlock().getClass().getAnnotation(HasNoModel.class) != null))
			return;
		HammerCore.LOG.info("Model definition for location " + item.getTranslationKey().substring(5));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(new ResourceLocation(item.getTranslationKey().substring(5)), "inventory"));
	}
	
	public static void registerRender(Item item, int meta, String modelName)
	{
		HammerCore.LOG.info("Model definition for location " + modelName);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, meta, new ModelResourceLocation(new ResourceLocation(modelName), "inventory"));
	}
	
	@Override
	public void cl_loadOpts(HCClientOptions opts, NBTTagCompound nbt)
	{
		if(module != null && opts == HCClientOptions.getOptions())
			module.loadClientOpts(nbt);
	}
	
	@Override
	public void cl_saveOpts(HCClientOptions opts, NBTTagCompound nbt)
	{
		if(module != null && opts == HCClientOptions.getOptions())
			module.saveClientOpts(nbt);
	}
	
	@Override
	public World getWorld(PacketContext context)
	{
		if(context == null)
			return Minecraft.getMinecraft().world;
		if(context.side == Side.CLIENT)
			return Minecraft.getMinecraft().world;
		return super.getWorld(context);
	}
	
	@Override
	public World getWorld(PacketContext context, int dim)
	{
		if(context == null)
			return Minecraft.getMinecraft().world;
		if(context.side == Side.CLIENT)
		{
			World w = getWorld(context);
			if(w == null)
				return null;
			if(w.provider.getDimension() == dim)
				return w;
		}
		return super.getWorld(context, dim);
	}
	
	@Override
	public double getBlockReachDistance_client()
	{
		return Minecraft.getMinecraft().playerController.getBlockReachDistance();
	}
	
	@Override
	public void bindTexture(ResourceLocation texture)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
	}
	
	public static boolean needsClConfigSync;
	
	@SubscribeEvent
	public void ctick(ClientTickEvent e)
	{
		cticked = true;
	}
	
	@SubscribeEvent
	public void crel(TextureStitchEvent e)
	{
		reloaded = cticked;
	}
	
	@SubscribeEvent
	public void clientJoined(FMLNetworkEvent.ClientConnectedToServerEvent cctse)
	{
		// Reload cape list
		loadCAPS();
		
		// Reload plugins on join
		HammerCore.instance.reloadPlugins();
	}
	
	private final Map<String, String> customCapes = new HashMap<>();
	
	@SubscribeEvent
	public void renderPlayer(RenderPlayerEvent.Pre rpe)
	{
		AbstractClientPlayer acp = WorldUtil.cast(rpe.getEntityPlayer(), AbstractClientPlayer.class);
		if(acp == null)
			return;
		GameProfile gp = rpe.getEntityPlayer().getGameProfile();
		String name = gp.getName();
		PropertyMap pm = gp.getProperties();
		
		Map<Type, ResourceLocation> mp = ClientSkinManager.getPlayerMap(acp);
		
		if(mp != null)
		{
			ResourceLocation cape = mp.get(Type.CAPE);
			
			if(cape == null || (!cape.getNamespace().equals("hammercore") && ServerHCClientPlayerData.getOptionsFor(acp).overrideCape))
			{
				if(!customCapes.containsKey(name))
					return;
				
				ResourceLocation loc = new ResourceLocation("hammercore", "capes/" + name);
				mp.put(Type.CAPE, loc);
				
				Threading.createAndStart(() ->
				{
					BufferedImage bi = IOUtils.downloadPicture(customCapes.get(name));
					Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().getTextureManager().loadTexture(loc, new BufferedTexture(bi)));
				});
			}
		}
	}
	
	@Override
	public int apply(ItemStack stack, int prev)
	{
		/** Skip this color manager */
		if(!HammerCoreConfigs.client_customDefEnchCols)
			return prev;
		
		Item i = stack.getItem();
		
		if(i == Items.EXPERIENCE_BOTTLE)
		{
			float f9 = (float) (System.currentTimeMillis() % 5000L / 1000D) * 5F;
			float l = (float) ((Math.sin(f9 + 0.0F) + 1.0F) * 0.5F);
			float j1 = (float) ((Math.sin(f9 + 4.1887903F) + 1.0F) * 0.1F);
			return 255 << 24 | ColorHelper.packRGB(l * .75F, .75F, j1 * .75F);
		}
		
		if(i == Items.GOLDEN_APPLE && stack.getItemDamage() == 1)
			return 0xFFAF9600;
		
		if(i == Items.NETHER_STAR)
			return 0x66770066;
		
		return prev;
	}
	
	@Override
	public boolean applies(ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean shouldTruncateColorBrightness(ItemStack stack)
	{
		return true;
	}
	
	private Thread curCAPT;
	
	private Map<String, String> loadCAPS()
	{
		if(curCAPT == null || !curCAPT.isAlive())
		{
			curCAPT = new Thread(() ->
			{
				final Map<String, String> customCapes = new HashMap<>();
				{
					try
					{
						JSONObject arr = (JSONObject) IOUtils.downloadjsonOrLoadFromInternal("https://pastebin.com/raw/zjtZm2np", "/assets/hammercore/io/capes.json");
						for(String key : arr.keySet())
							customCapes.put(key, arr.getString(key));
					} catch(JSONException e)
					{
						// Shouldn't happen
					}
				}
				this.customCapes.clear();
				this.customCapes.putAll(customCapes);
				curCAPT = null;
			});
			curCAPT.setName("Cape list downloader");
			curCAPT.start();
		}
		return customCapes;
	}
}