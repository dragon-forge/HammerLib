package com.zeitheron.hammercore.client.render.shader;

public interface IShaderOperation
{
	boolean load(ShaderProgram program);
	
	void operate(ShaderProgram program);
	
	int operationID();
}