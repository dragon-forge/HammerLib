package org.zeith.hammerlib.core.scans;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.api.items.CreativeTab;
import org.zeith.hammerlib.core.adapter.RegistryAdapter;
import org.zeith.hammerlib.core.scans.base.IAnnotationScanListener;
import org.zeith.hammerlib.core.scans.base.IScanListener;
import org.zeith.hammerlib.util.java.ReflectionUtil;
import org.zeith.hammerlib.util.mcf.ScanDataHelper;

import java.lang.annotation.ElementType;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ScanTabs
{
	public static IScanListener create()
	{
		return IAnnotationScanListener.forAnnotation(CreativeTab.RegisterTab.class, ElementType.FIELD, ScanTabs::handle);
	}
	
	private static void handle(ScanDataHelper.ModAwareAnnotationData data)
	{
		var mc = data.getOwnerMod().orElse(null);
		if(mc == null) return;
		
		Objects.requireNonNull(mc.getEventBus(), "Mod's event bus (" + mc.getModId() + ")").addListener((Consumer<RegisterEvent>) e ->
		{
			var registrar = RegistryAdapter.createRegisterer(e, Registries.CREATIVE_MODE_TAB, null);
			
			registrar.ifPresent(register ->
			{
				Optional<CreativeTab> tab = ReflectionUtil.getStaticFinalField(data.getOwnerClass(), data.getMemberName());
				tab.ifPresent(t0 -> t0.register(t ->
				{
					var tabBuilder = CreativeModeTab.builder();
					t.factory().accept(tabBuilder);
					var ct = tabBuilder.build();
					register.accept(t.id(), ct);
					return ct;
				}));
			});
		});
	}
}