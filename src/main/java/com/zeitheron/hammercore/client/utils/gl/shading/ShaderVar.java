package com.zeitheron.hammercore.client.utils.gl.shading;


import java.util.Objects;

public abstract class ShaderVar<STATE>
{
	public boolean hasChanged;
	final String key;
	STATE state;
	String value;
	VariableShaderProgram program;
	
	public ShaderVar(String key)
	{
		this.key = key;
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
	
	public String getValue()
	{
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