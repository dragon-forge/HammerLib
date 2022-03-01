package org.zeith.hammerlib.mixins;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.resources.*;
import net.minecraft.util.text.StringTextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.client.adapter.ResourcePackAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(ResourcePackList.class)
public class ResourcePackListMixin
{
	@Shadow
	private Map<String, ResourcePackInfo> available;

	@Shadow
	private List<ResourcePackInfo> selected;

	@Inject(
			method = "reload",
			at = @At("TAIL")
	)
	public void reloadHook(CallbackInfo ci)
	{
		if(selected instanceof ImmutableList)
			selected = new ArrayList<>(selected);
		if(available instanceof ImmutableMap)
			available = new HashMap<>(available);

		for(IResourcePack pack : ResourcePackAdapter.BUILTIN_PACKS)
		{
			if(pack instanceof IRegisterListener)
				((IRegisterListener) pack).onPreRegistered();
			ResourcePackInfo rpi = new ResourcePackInfo(pack.getName(), true, () -> pack, new StringTextComponent(pack.getName()), new StringTextComponent("Builtin."), PackCompatibility.COMPATIBLE, ResourcePackInfo.Priority.TOP, true, IPackNameDecorator.DEFAULT, true);
			available.put(pack.getName(), rpi);
			selected.add(rpi);
			if(pack instanceof IRegisterListener)
				((IRegisterListener) pack).onPostRegistered();
		}
	}
}