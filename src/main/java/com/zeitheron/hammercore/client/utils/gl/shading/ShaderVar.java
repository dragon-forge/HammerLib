package com.zeitheron.hammercore.client.utils.gl.shading;


import java.util.Objects;

public abstract class ShaderVar
{
	public boolean hasChanged;
	final String key;
	String value;
	VariableShaderProgram program;

	public ShaderVar(String key)
	{
		this.key = key;
	}

	protected abstract String getCurrentValue();

	public void update()
	{
		String nv = getCurrentValue();
		if(!Objects.equals(nv, value))
		{
			value = nv;
			hasChanged = true;
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