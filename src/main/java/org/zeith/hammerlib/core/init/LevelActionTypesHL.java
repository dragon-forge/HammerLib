package org.zeith.hammerlib.core.init;

import org.zeith.hammerlib.abstractions.actions.impl.DelayedLevelAction;
import org.zeith.hammerlib.abstractions.actions.impl.MethodHandleLevelAction;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;

@SimplyRegister
public interface LevelActionTypesHL
{
	@RegistryName("delayed")
	DelayedLevelAction.DelayedType DELAYED_TYPE = new DelayedLevelAction.DelayedType();
	
	@RegistryName("method_handle")
	MethodHandleLevelAction.MethodHandleActionType METHOD_HANDLE_TYPE = new MethodHandleLevelAction.MethodHandleActionType();
}