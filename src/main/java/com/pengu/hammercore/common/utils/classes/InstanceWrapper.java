package com.pengu.hammercore.common.utils.classes;

public class InstanceWrapper extends ClassWrapper
{
	private Object inst;
	
	public InstanceWrapper(Object inst)
	{
		super(inst.getClass());
		this.inst = inst;
	}
	
	@Override
	public Object getInstance()
	{
		return inst;
	}
}