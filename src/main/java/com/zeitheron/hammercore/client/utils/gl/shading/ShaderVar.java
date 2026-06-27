package com.zeitheron.hammercore.client.utils.gl.shading;


import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.fml.relauncher.*;

import java.util.Objects;

@SideOnly(Side.CLIENT)
public abstract class ShaderVar<STATE>
{
	public boolean hasChanged;
	protected final String key;
	STATE state;
	String value;
	VariableShaderProgram program;
	
	public ShaderVar(String key)
	{
		this.key = key;
	}
	
	public void prepareReload(IResourceManager resources)
	{
		reset();
	}
	
	public void onReload(IResourceManager resources)
	{
	}
	
	protected abstract STATE getState();
	
	protected abstract String compute(STATE state);
	
	public void update()
	{
		STATE st = getState();
		if(!Objects.equals(this.state, st))
		{
			this.state = st;
			String nv = compute(st);
			if(!Objects.equals(nv, value))
			{
				value = nv;
				hasChanged = true;
			}
		}
	}
	
	protected void reset()
	{
		state = null;
		value = null;
	}
	
	public String getValue()
	{
		if(value == null)
		{
			value = compute(state = getState());
			hasChanged = false;
		}
		return value;
	}
	
	void setProgram(VariableShaderProgram program)
	{
		if(this.program != null)
			throw new IllegalStateException(this + " is already assigned to shader program " + this.program);
		this.program = program;
	}
	
	public VariableShaderProgram getProgram()
	{
		return program;
	}
	
	@Override
	public String toString()
	{
		return "ShaderVar{" +
		       "key='" + key + '\'' +
		       ", program=" + program +
		       '}';
	}
}