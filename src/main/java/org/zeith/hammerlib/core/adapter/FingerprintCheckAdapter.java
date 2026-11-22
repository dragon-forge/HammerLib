package org.zeith.hammerlib.core.adapter;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import org.jetbrains.annotations.Nullable;
import org.zeith.hammerlib.event.fml.FMLFingerprintCheckEvent;
import org.zeith.hammerlib.proxy.HLConstants;

import java.util.*;
import java.util.concurrent.*;

@Slf4j
public class FingerprintCheckAdapter
{
	public static final ResourceLocation SECURE_JAR_CHECK_ICONS = HLConstants.id("textures/gui/secure_jar.png");
	public static final int[] SECURE_JAR_CHECK_ICONS_SIZE = {8, 16};
	
	private static final List<Runnable> TASKS = Collections.synchronizedList(new ArrayList<>());
	private static final Map<FMLModContainer, CompletableFuture<FingerprintCheckResult>> CHECKS = new HashMap<>();
	
	private static final Map<String, FMLModContainer> FML_MODS = new ConcurrentHashMap<>();
	
	public static void service(FMLModContainer ctr)
	{
		FML_MODS.putIfAbsent(ctr.getModId(), ctr);
		
		var bus = ctr.getEventBus();
		var evt = new FMLFingerprintCheckEvent(ctr);
		
		assert bus != null;
		bus.addListener((FMLCommonSetupEvent e) -> bus.post(evt));
		
		enqueue(evt);
	}
	
	@Nullable
	public static FMLModContainer getFMLMod(String modid)
	{
		return FML_MODS.get(modid);
	}
	
	public static void loadComplete()
	{
		TASKS.forEach(Util.backgroundExecutor()::submit);
	}
	
	public static FingerprintCheckResult getCheckResult(ModContainer mc)
	{
		if(!(mc instanceof FMLModContainer fml)) return FingerprintCheckResult.NOT_PRESENT;
		var res = CHECKS.get(fml);
		if(res == null) return FingerprintCheckResult.NOT_PRESENT;
		if(res.isDone()) return res.join();
		return FingerprintCheckResult.NOT_PRESENT;
	}
	
	public static void enqueue(FMLFingerprintCheckEvent mod)
	{
		CHECKS.computeIfAbsent(mod.getModContainer(), m ->
				CompletableFuture.supplyAsync(() -> check(mod), TASKS::add)
		);
	}
	
	private static FingerprintCheckResult check(FMLFingerprintCheckEvent evt)
	{
		boolean expectingSignature = evt.getExpectedSignature() != null;
		
		if(!evt.isJarSigned())
			return expectingSignature ? FingerprintCheckResult.FAILED : FingerprintCheckResult.NOT_PRESENT;
		
		if(expectingSignature && evt.isViolated(evt.getExpectedSignature()))
			return FingerprintCheckResult.FAILED;
		
		if(evt.anyInvalidFiles())
			return FingerprintCheckResult.FAILED;
		
		return FingerprintCheckResult.PASSED;
	}
	
	@AllArgsConstructor
	@Getter
	public enum FingerprintCheckResult
	{
		PASSED(true, 0),
		FAILED(true, 8),
		NOT_PRESENT(false, 0);
		
		private final boolean iconVisible;
		private final int y;
	}
}