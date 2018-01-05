package com.pengu.hammercore.intent;

public interface iIntentHandler<T>
{
	Object execute(String mod, T data);
}