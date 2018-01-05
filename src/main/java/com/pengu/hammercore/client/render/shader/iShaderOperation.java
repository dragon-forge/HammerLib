package com.pengu.hammercore.client.render.shader;

public interface iShaderOperation
{
	boolean load(ShaderProgram program);
	
	void operate(ShaderProgram program);
	
	int operationID();
}