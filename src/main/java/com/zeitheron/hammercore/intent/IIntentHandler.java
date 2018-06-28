package com.zeitheron.hammercore.intent;

public interface IIntentHandler<T>
{
	Object execute(String mod, T data);
}