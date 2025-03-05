package org.zeith.hammerlib.core.adapter;

import lombok.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import org.zeith.hammerlib.event.fml.FMLFingerprintCheckEvent;
import org.zeith.hammerlib.proxy.HLConstants;

import java.util.*;
import java.util.concurrent.*;

public class FingerprintCheckAdapter
{
	public static final ResourceLocation SECURE_JAR_CHECK_ICONS = HLConstants.id("textures/gui/secure_jar.png");
	public static final int[] SECURE_JAR_CHECK_ICONS_SIZE = {8, 16};
	
	private static final List<Runnable> TASKS = Collections.synchronizedList(new ArrayList<>());
	private static final Map<FMLModContainer, CompletableFuture<FingerprintCheckResult>> CHECKS = new HashMap<>();
	
	public static void service(FMLModContainer ctr)
	{
		var bus = ctr.getEventBus();
		var evt = new FMLFingerprintCheckEvent(ctr);
		
		assert bus != null;
		bus.addListener((FMLCommonSetupEvent e) -> ctr.getEventBus().post(evt));
		
		enqueue(evt);
	}
	
	public static void loadComplete()
	{
		TASKS.forEach(Thread.ofVirtual().name("HammerLibSignatureChecker", 0L)::start);
	}
	
	public static FingerprintCheckResult getCheckResult(ModContainer mc)
	{
		var res = CHECKS.get(mc);
		if(res == null) return FingerprintCheckResult.NOT_PRESENT;
		if(res.isDone()) return res.join();
		return FingerprintCheckResult.NOT_PRESENT;
	}
	
	public static void enqueue(FMLFingerprintCheckEvent mod)
	{
		CHECKS.computeIfAbsent(mod.getModContainer(), m -> CompletableFuture.supplyAsync(() ->
								check(mod),
						TASKS::add
				)
		);
	}
	
	private static FingerprintCheckResult check(FMLFingerprintCheckEvent evt)
	{
		if(!evt.isJarSigned())
			return FingerprintCheckResult.NOT_PRESENT;
		
		if(evt.getExpectedSignature() != null && evt.isViolated(evt.getExpectedSignature()))
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