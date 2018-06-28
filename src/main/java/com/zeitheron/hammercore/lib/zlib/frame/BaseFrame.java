package com.zeitheron.hammercore.lib.zlib.frame;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BaseFrame extends JFrame
{
	public final JPanel panel = new JPanel();
	private final Map<String, Component> comps = new HashMap<String, Component>();
	
	public BaseFrame()
	{
		this.setTitle("PenguCode " + this.getClass().getSimpleName());
		this.setResolutionByScreenSize(0.5f);
		this.setDefaultCloseOperation(3);
		this.add(this.panel);
	}
	
	public JPanel getPanel()
	{
		return this.panel;
	}
	
	public void addComponent(String id, Component comp)
	{
		this.comps.put(id, comp);
		this.panel.add(comp);
		this.repaint();
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(String id, Class<T> type)
	{
		if(this.comps.containsKey(id) && type.isAssignableFrom(this.comps.get(id).getClass()))
			return (T) this.comps.get(id);
		return null;
	}
	
	public void setResolutionByScreenSize(float mult)
	{
		Point center = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
		this.setResolution((int) (center.getX() * 2.0 * (double) mult), (int) (center.getY() * 2.0 * (double) mult));
	}
	
	public void setResolution(int w, int h)
	{
		this.getContentPane().setPreferredSize(new Dimension(w, h));
		this.pack();
		this.setLocationRelativeTo(null);
	}
}
