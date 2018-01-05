package com.pengu.hammercore.client.render.shader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HCShaderPipeline
{
	private static int nextOperationIndex;
	
	public static int registerOperation()
	{
		return nextOperationIndex++;
	}
	
	public static int operationCount()
	{
		return nextOperationIndex;
	}
	
	public class PipelineBuilder
	{
		public PipelineBuilder add(iShaderOperation op)
		{
			ops.add(op);
			return this;
		}
		
		public PipelineBuilder add(iShaderOperation... ops)
		{
			Collections.addAll(HCShaderPipeline.this.ops, ops);
			return this;
		}
		
		public void build()
		{
			rebuild();
		}
	}
	
	private class PipelineNode
	{
		public ArrayList<PipelineNode> deps = new ArrayList<>();
		public iShaderOperation op;
		
		public void add()
		{
			if(op == null)
				return;
			
			for(PipelineNode dep : deps)
				dep.add();
			
			deps.clear();
			sorted.add(op);
			op = null;
		}
	}
	
	private ShaderProgram program;
	
	private ArrayList<iShaderOperation> ops = new ArrayList<>();
	private ArrayList<PipelineNode> nodes = new ArrayList<>();
	private ArrayList<iShaderOperation> sorted = new ArrayList<>();
	private PipelineNode loading;
	private PipelineBuilder builder = new HCShaderPipeline.PipelineBuilder();
	
	public HCShaderPipeline(ShaderProgram program)
	{
		this.program = program;
	}
	
	public void setPipeline(iShaderOperation... ops)
	{
		this.ops.clear();
		Collections.addAll(this.ops, ops);
		rebuild();
	}
	
	public void setPipeline(List<iShaderOperation> ops)
	{
		this.ops.clear();
		this.ops.addAll(ops);
		rebuild();
	}
	
	public void reset()
	{
		ops.clear();
		unbuild();
	}
	
	private void unbuild()
	{
		sorted.clear();
	}
	
	public void rebuild()
	{
		if(ops.isEmpty())
			return;
		while(nodes.size() < operationCount())
			nodes.add(new PipelineNode());
		unbuild();
		
		for(iShaderOperation op : ops)
		{
			loading = nodes.get(op.operationID());
			boolean loaded = op.load(program);
			if(loaded)
				loading.op = op;
		}
		
		for(PipelineNode node : nodes)
			node.add();
	}
	
	public void addRequirement(int opRef)
	{
		loading.deps.add(nodes.get(opRef));
	}
	
	public void operate()
	{
		for(iShaderOperation op : sorted)
			op.operate(program);
	}
	
	public PipelineBuilder builder()
	{
		ops.clear();
		return builder;
	}
}