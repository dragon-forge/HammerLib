package com.zeitheron.hammercore.lib.zlib.io.pipelib;

public interface IFlowListener
{
	PipeFlow onFlow(FlowSide target, PipeFlow flow);
}