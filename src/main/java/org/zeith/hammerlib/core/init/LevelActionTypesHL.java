package org.zeith.hammerlib.core.init;

import org.zeith.hammerlib.abstractions.actions.ILevelActionType;
import org.zeith.hammerlib.abstractions.actions.impl.DelayedLevelAction;
import org.zeith.hammerlib.abstractions.actions.impl.MethodHandleLevelAction;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface LevelActionTypesHL
{
	@RegistryName("delayed")
	ILevelActionType DELAYED_TYPE = ILevelActionType.simple(DelayedLevelAction::new);
	
	@RegistryName("method_handle")
	ILevelActionType METHOD_HANDLE_TYPE = ILevelActionType.simple(MethodHandleLevelAction::new);
}