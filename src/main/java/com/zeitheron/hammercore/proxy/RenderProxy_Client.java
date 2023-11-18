package com.zeitheron.hammercore.proxy;

import com.google.common.io.Files;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.properties.PropertyMap;
import com.zeitheron.hammercore.*;
import com.zeitheron.hammercore.annotations.AtTESR;
import com.zeitheron.hammercore.api.events.ResourceManagerReloadEvent;
import com.zeitheron.hammercore.api.inconnect.*;
import com.zeitheron.hammercore.api.multipart.*;
import com.zeitheron.hammercore.asm.HammerCoreTransformer;
import com.zeitheron.hammercore.cfg.HammerCoreConfigs;
import com.zeitheron.hammercore.client.*;
import com.zeitheron.hammercore.client.gui.impl.GuiPersonalisation;
import com.zeitheron.hammercore.client.model.HasNoModel;
import com.zeitheron.hammercore.client.model.mc.BakedMultipartModel;
import com.zeitheron.hammercore.client.particle.RenderHelperImpl;
import com.zeitheron.hammercore.client.render.Render3D;
import com.zeitheron.hammercore.client.render.item.TileEntityItemStackRendererHC;
import com.zeitheron.hammercore.client.render.item.img.Stack2ImageRenderer;
import com.zeitheron.hammercore.client.utils.*;
import com.zeitheron.hammercore.client.utils.texture.*;
import com.zeitheron.hammercore.client.utils.texture.gui.theme.GuiTheme;
import com.zeitheron.hammercore.client.witty.SplashTextHelper;
import com.zeitheron.hammercore.command.CommandBasic;
import com.zeitheron.hammercore.compat.jei.IJeiHelper;
import com.zeitheron.hammercore.internal.blocks.multipart.TileMultipart;
import com.zeitheron.hammercore.internal.init.*;
import com.zeitheron.hammercore.lib.zlib.error.JSONException;
import com.zeitheron.hammercore.lib.zlib.io.IOUtils;
import com.zeitheron.hammercore.lib.zlib.json.JSONObject;
import com.zeitheron.hammercore.lib.zlib.utils.Threading;
import com.zeitheron.hammercore.net.PacketContext;
import com.zeitheron.hammercore.tile.tooltip.own.EntityTooltipRenderEngine;
import com.zeitheron.hammercore.utils.*;
import com.zeitheron.hammercore.utils.base.Cast;
import com.zeitheron.hammercore.utils.color.ColorHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.resource.*;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.*;
import org.lwjgl.input.Keyboard;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SideOnly(Side.CLIENT)
public class RenderProxy_Client
		extends RenderProxy_Common
		implements IEnchantmentColorManager, ISelectiveResourceReloadListener
{
	public static final EmptyModelPack EMP = new EmptyModelPack();
	public final EntityTooltipRenderEngine tooltipEngine = new EntityTooltipRenderEngine();
	
	public static final KeyBinding BIND_RENDER = new KeyBinding("keybind.hammercorerenderstack", KeyConflictContext.GUI, Keyboard.KEY_NUMPAD6, "key.categories.inventory");
	
	private List<TileEntitySpecialRenderer> tesrs;
	private boolean cticked, reloaded;
	
	public static PerUserModule module;
	
	public static final IdentityHashMapWC<IBlockState, IBakedModel> bakedModelStore = new IdentityHashMapWC<>();
	
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
		MinecraftForge.EVENT_BUS.register(Stack2ImageRenderer.INSTANCE);
		((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
		
		TextureFXManager.INSTANCE.preInit();
		
		HammerCoreClient.injectResourcePackLast(EMP);
		
		hasConstructed = true;
	}
	
	@Override
	public void preInit(ASMDataTable table)
	{
		tesrs = AnnotatedInstanceUtil.getInstances(table, AtTESR.class, TileEntitySpecialRenderer.class);
		module = AnnotatedInstanceUtil.getUserModule(table);
		
		ClientRegistry.registerKeyBinding(BIND_RENDER);
		
		HammerCore.LOG.info("Using per-user module " + module.getClass().getSimpleName());
		
		if(module != null)
			module.preInit();
		
		ClientCommandHandler.instance.registerCommand(new CommandBasic("hc_themes", 0, (server, sender, args) ->
				Threading.createAndStart(() ->
				{
					try(InputStream in = HammerCore.class.getResourceAsStream("/assets/hammercore/themes/themes.txt"))
					{
						if(in == null) return;
						Scanner sc = new Scanner(in);
						while(sc.hasNext())
							GuiTheme.makeTheme(sc.next());
						sc.close();
					} catch(IOException e)
					{
						return;
					}
					
					try
					{
						Thread.sleep(100L);
					} catch(Throwable err)
					{
					}
					
					Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft()
							.displayGuiScreen(new GuiPersonalisation()));
				})));
		
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
				return "/hc_render_mod";
			}
			
			@Override
			public String getName()
			{
				return "hc_render_mod";
			}
			
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args)
					throws CommandException
			{
				if(args.length > 0)
				{
					sender.sendMessage(new TextComponentString(
							"Rendering in " + Runtime.getRuntime().availableProcessors() + " cores."));
					if(args[0].equals("ALL"))
						renderAll(args.length > 1 ? parseInt(args[1], 1, 1024) : 1024);
					else
						renderMod(args[0], args.length > 1 ? parseInt(args[1], 1, 1024) : 1024);
				} else
					throw new CommandException("Modid not specified!");
			}
			
			@Override
			public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
			{
				if(args.length == 1)
				{
					List<String> l = new ArrayList<>(Loader.instance().getActiveModList().stream()
							.map(mc -> mc.getModId()).collect(Collectors.toList()));
					l.add("ALL");
					return getListOfStringsMatchingLastWord(args, l);
				}
				return super.getTabCompletions(server, sender, args, targetPos);
			}
		});
		
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
				return "/hc_export_mapping";
			}
			
			@Override
			public String getName()
			{
				return "hc_export_mapping";
			}
			
			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args)
					throws CommandException
			{
				sender.sendMessage(new TextComponentString(
						"Located " + HammerCoreTransformer.MC_CLASS_MAP.size() + " classes, exporting."));
				
				List<String> deobf = new ArrayList<>(HammerCoreTransformer.MC_CLASS_MAP.keySet());
				deobf.sort(Comparator.comparing(String::toString));
				
				int entry = 0;
				
				try(FileOutputStream fos = new FileOutputStream(new File("hc_asm.properties")))
				{
					for(entry = 0; entry < deobf.size(); ++entry)
					{
						String s = deobf.get(entry);
						String o = HammerCoreTransformer.MC_CLASS_MAP.get(s);
						fos.write((s + "=" + o + System.lineSeparator()).getBytes());
					}
				} catch(Throwable err)
				{
					sender.sendMessage(new TextComponentString("Entry " + (entry + 1) + " failed to export: " + err));
					sender.sendMessage(new TextComponentString("See console for details."));
					err.printStackTrace();
				}
			}
			
			@Override
			public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos)
			{
				if(args.length == 1)
				{
					List<String> l = new ArrayList<>(Loader.instance().getActiveModList().stream()
							.map(mc -> mc.getModId()).collect(Collectors.toList()));
					l.add("ALL");
					return getListOfStringsMatchingLastWord(args, l);
				}
				return super.getTabCompletions(server, sender, args, targetPos);
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
		
		Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, world, pos, tintIndex) ->
		{
			TileMultipart tmp = MultipartAPI.getMultipart(world, pos);
			if(tmp != null)
			{
				List<MultipartSignature> signatures = tmp.signatures();
				for(int i = 0; i < signatures.size(); ++i)
				{
					MultipartSignature sign = signatures.get(i);
					if(sign instanceof IMultipartBaked)
					{
						IMultipartBaked baked = (IMultipartBaked) sign;
						int c = baked.getBakedModelTintCount();
						if(tintIndex >= c)
						{
							tintIndex -= c;
							continue;
						} else
							return baked.getColorByTint(tintIndex);
					}
				}
			}
			return 0xFFFFFF;
		}, BlocksHC.MULTIPART);
		
		HammerCore.LOG.info("Registering TESRs...");
		for(TileEntitySpecialRenderer t : tesrs)
		{
			AtTESR at = t.getClass().getAnnotation(AtTESR.class);
			if(at != null)
			{
				HammerCore.LOG.debug(" Register TESR {} for tile {}.", t.getClass().getName(), at.value().getName());
				ClientRegistry.bindTileEntitySpecialRenderer(at.value(), t);
			}
		}
		HammerCore.LOG.info("Registered " + tesrs.size() + " TESRs.");
		
		try
		{
			HCClientOptions.options.load((JSONObject) IOUtils.jsonparse(new File("hc_options.txt")));
		} catch(JSONException e)
		{
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
		for(Item item : items) registerRender(item);
	}
	
	public static void registerRender(Item item)
	{
		if(item.getClass().isAnnotationPresent(HasNoModel.class) || (item instanceof ItemBlock &&
				((ItemBlock) item).getBlock().getClass().isAnnotationPresent(HasNoModel.class)))
			return;
		HammerCore.LOG.debug("Model definition for location " + item.getTranslationKey().substring(5));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
	public static void registerRender(Item item, int meta, String modelName)
	{
		HammerCore.LOG.debug("Model definition for location " + modelName);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.register(item, meta, new ModelResourceLocation(new ResourceLocation(modelName), "inventory"));
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
	
	private boolean renderPress = false;
	
	public static boolean isKeyDownSFW(int keyCode)
	{
		try
		{
			return Keyboard.isKeyDown(keyCode);
		} catch(IndexOutOfBoundsException ioobe)
		{
		}
		return false;
	}
	
	@SubscribeEvent
	public void ctick(ClientTickEvent e)
	{
		cticked = true;
		
		boolean rp = BIND_RENDER.isKeyDown();
		if(rp != renderPress)
		{
			renderPress = rp;
			if(rp)
			{
				GuiScreen gs = Minecraft.getMinecraft().currentScreen;
				GuiContainer gc = Cast.cast(gs, GuiContainer.class);
				
				ITextComponent a = new TextComponentString("Renderer"), b = null;
				
				if(gc != null)
				{
					Slot mouse = gc.getSlotUnderMouse();
					
					ItemStack jei = IJeiHelper.instance().getSlotUnderMouseInJEI();
					
					if(mouse != null || !jei.isEmpty())
					{
						ItemStack stack = mouse != null ? mouse.getStack() : jei;
						if(!stack.isEmpty())
						{
							Stack2ImageRenderer.queueRenderer(stack, 1024, image ->
							{
								SystemToast.addOrUpdate(Minecraft.getMinecraft()
										.getToastGui(), SystemToast.Type.NARRATOR_TOGGLE, a.appendSibling(new TextComponentString(": Rendered!")), new TextComponentString(stack.getDisplayName()));
								
								SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");
								
								try
								{
									ResourceLocation rl = stack.getItem().getRegistryName();
									File fail = new File("hammercore",
											"renderers" + File.separator + rl.getNamespace() + File.separator +
													rl.getPath() +
													(stack.getItemDamage() == 0 ? "" : "." + stack.getItemDamage()) +
													"-" + sdf.format(Date.from(Instant.now())) + ".png"
									);
									Files.createParentDirs(fail);
									
									Threading.createAndStart("SaveRenderer" + image.hashCode(), () ->
									{
										try
										{
											ImageIO.write(image, "png", fail);
										} catch(IOException e1)
										{
											e1.printStackTrace();
										}
									});
								} catch(Throwable e1)
								{
									e1.printStackTrace();
								}
							});
						} else
							b = new TextComponentString("Slot under mouse is empty!");
					} else
						b = new TextComponentString("Mouse doesn't hover any slot!");
				}
				
				if(b != null)
					SystemToast.addOrUpdate(Minecraft.getMinecraft()
							.getToastGui(), SystemToast.Type.NARRATOR_TOGGLE, a, b);
			}
		}
	}
	
	private static void renderAll(int size)
	{
		NonNullList<ItemStack> sub = NonNullList.create();
		
		for(Item item : ForgeRegistries.ITEMS.getValuesCollection())
		{
			NonNullList<ItemStack> sb = NonNullList.create();
			try
			{
				item.getSubItems(CreativeTabs.SEARCH, sb);
			} catch(Throwable err)
			{
			}
			for(ItemStack sbs : sb)
				sub.add(sbs);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");
		
		File faild = new File("hammercore",
				"renderers" + File.separator + "all-" + sdf.format(Date.from(Instant.now()))
		);
		
		int i = 0;
		AtomicInteger ati = new AtomicInteger(0);
		ExecutorService saver = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for(ItemStack sttr : sub)
		{
			final int index = i;
			Stack2ImageRenderer.queueRenderer(sttr, size, image ->
			{
				ResourceLocation rl = sttr.getItem().getRegistryName();
				saver.execute(() ->
				{
					try
					{
						File fail = new File(faild, rl.getNamespace() + File.separator +
								(rl.getPath() + (sttr.getItemDamage() == 0 ? "" : "." + sttr.getItemDamage()) +
										(sttr.hasTagCompound() ? "_" + sttr.getTagCompound() : "") +
										".png").replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
						Files.createParentDirs(fail);
						
						ImageIO.write(image, "png", fail);
						
						Minecraft.getMinecraft().addScheduledTask(() -> SystemToast.addOrUpdate(Minecraft.getMinecraft()
								.getToastGui(), SystemToast.Type.NARRATOR_TOGGLE, new TextComponentString(
								"Rendered " + (index + 1) + "/" + sub.size() +
										":"), new TextComponentString(sttr.getDisplayName())));
					} catch(IOException e1)
					{
						e1.printStackTrace();
					}
					
					if(ati.addAndGet(1) == sub.size())
						saver.shutdown();
				});
			});
			++i;
		}
	}
	
	private static void renderMod(String modid, int size)
	{
		NonNullList<ItemStack> sub = NonNullList.create();
		
		for(Item item : ForgeRegistries.ITEMS.getValuesCollection())
		{
			NonNullList<ItemStack> sb = NonNullList.create();
			try
			{
				item.getSubItems(CreativeTabs.SEARCH, sb);
			} catch(Throwable err)
			{
			}
			for(ItemStack sbs : sb)
				if(sbs.getItem().getRegistryName().getNamespace().equals(modid))
					sub.add(sbs);
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-hh.mm.ss");
		
		File faild = new File("hammercore",
				"renderers" + File.separator + modid + "-" + sdf.format(Date.from(Instant.now()))
		);
		
		int i = 0;
		AtomicInteger ati = new AtomicInteger(0);
		ExecutorService saver = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		for(ItemStack sttr : sub)
		{
			final int index = i;
			Stack2ImageRenderer.queueRenderer(sttr, size, image ->
			{
				ResourceLocation rl = sttr.getItem().getRegistryName();
				saver.execute(() ->
				{
					try
					{
						File fail = new File(faild, (rl.getPath() +
								(sttr.getItemDamage() == 0 ? "" : "." + sttr.getItemDamage()) +
								(sttr.hasTagCompound() ? "_" + sttr.getTagCompound() : "") +
								".png").replaceAll("[^a-zA-Z0-9\\.\\-]", "_"));
						Files.createParentDirs(fail);
						
						ImageIO.write(image, "png", fail);
						
						Minecraft.getMinecraft().addScheduledTask(() -> SystemToast.addOrUpdate(Minecraft.getMinecraft()
								.getToastGui(), SystemToast.Type.NARRATOR_TOGGLE, new TextComponentString(
								"Rendered " + (index + 1) + "/" + sub.size() +
										":"), new TextComponentString(sttr.getDisplayName())));
					} catch(IOException e1)
					{
						e1.printStackTrace();
					}
					
					if(ati.addAndGet(1) == sub.size())
						saver.shutdown();
				});
			});
			++i;
		}
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
		AbstractClientPlayer acp = Cast.cast(rpe.getEntityPlayer(), AbstractClientPlayer.class);
		if(acp == null)
			return;
		GameProfile gp = rpe.getEntityPlayer().getGameProfile();
		String name = gp.getName();
		PropertyMap pm = gp.getProperties();
		
		Map<Type, ResourceLocation> mp = ClientSkinManager.getPlayerMap(acp);
		
		if(mp != null)
		{
			ResourceLocation cape = mp.get(Type.CAPE);
			
			if(cape == null || (!cape.getNamespace().equals("hammercore") &&
					ServerHCClientPlayerData.getOptionsFor(acp).overrideCape))
			{
				if(!customCapes.containsKey(name))
					return;
				
				ResourceLocation loc = new ResourceLocation("hammercore", "capes/" + name);
				mp.put(Type.CAPE, loc);
				
				Threading.createAndStart(() ->
				{
					BufferedImage bi = IOUtils.downloadPicture(customCapes.get(name));
					Minecraft.getMinecraft().addScheduledTask(() -> Minecraft.getMinecraft().getTextureManager()
							.loadTexture(loc, new BufferedTexture(bi)));
				});
			}
		}
	}
	
	@Override
	public int apply(ItemStack stack, int prev)
	{
		/* Skip this color manager */
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
						JSONObject arr = (JSONObject) IOUtils.downloadjson("https://raw.githubusercontent.com/Zeitheron/HCCapes/master/index.json");
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
	
	@Override
	public void loadComplete()
	{
		BlockModelShapes shapes = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes();
		
		ForgeRegistries.BLOCKS.getValuesCollection().stream() //
				.filter(b -> b instanceof IBlockConnectable) //
				.flatMap(c -> c.getBlockState().getValidStates().stream()) //
				.forEach(state -> bakedModelStore.putConstant(state, ((IBlockConnectable) state.getBlock()).getConnectTextureVersion()
						.create(state)));
		
		bakedModelStore.putConstant(BlocksHC.MULTIPART.getDefaultState(), new BakedMultipartModel());
		
		bakedModelStore.clear();
		try
		{
			bakedModelStore.putAll(shapes.bakedModelStore);
			Field modelMap = ReflectionUtil.getFieldByValue(BlockModelShapes.class, shapes, shapes.bakedModelStore);
			if(modelMap != null)
				ReflectionUtil.setFinalField(modelMap, shapes, bakedModelStore);
			else
				HammerCore.LOG.info("UNABLE TO FIND bakedModelStore IN " + BlockModelShapes.class +
						"!! CUSTOM MODEL LOADING IS CORRUPTED!!");
		} catch(Throwable e)
		{
			HammerCore.LOG.info("EXCEPTION HAPPENED WHILE HOOKING INTO " + BlockModelShapes.class +
					"!! CUSTOM MODEL LOADING IS CORRUPTED!!");
			e.printStackTrace();
		}
	}
	
	@SubscribeEvent
	public void textureStitch(TextureStitchEvent.Pre e)
	{
		TextureMap txMap = e.getMap();
		ForgeRegistries.BLOCKS.getValuesCollection().stream() //
				.filter(b -> b instanceof IBlockConnectable) //
				.map(IBlockConnectable.class::cast) //
				.flatMap(IBlockConnectable::getSprites) //
				.forEach(txMap::registerSprite);
	}
	
	@Override
	public void noModel(Block blk)
	{
		HammerCoreClient.emptyBlockState(blk);
	}
	
	private static boolean hasConstructed;
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager, Predicate<IResourceType> resourcePredicate)
	{
		if(hasConstructed)
			MinecraftForge.EVENT_BUS.post(new ResourceManagerReloadEvent(resourceManager, resourcePredicate));
	}
}