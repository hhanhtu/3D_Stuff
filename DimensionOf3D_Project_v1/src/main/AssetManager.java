package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import objects.MeshPart;
import variables.Vector3D;

@SuppressWarnings("rawtypes")
public class AssetManager
{
	public Panel pn;
	
	public HashMap<String, Vector> mesh = new HashMap<>();
	
	public AssetManager(Panel pn)
	{
		this.pn = pn;
		
		mesh.clear();
		
		loadAsset();
	}
	
	public void loadAsset()
	{
		Vector<MeshPart> m = new Vector<>();
		
		MeshPart obj = new MeshPart(pn);
		obj.clr = Color.WHITE;
		obj.offset = new Vector3D(0, 0, 5);
		obj.LoadFromObjectFile("test");
		
		obj.rX = 180;
		
		m.add(obj);
		
		mesh.put("Group1", m);
	}
	
	public void generateAll(Graphics2D g2)
	{
		for(Map.Entry<String, Vector> i: mesh.entrySet())
		{
			for(Object m: i.getValue())
			{
				MeshPart o = (MeshPart)m;
				o.generate(g2);
				
			}
		}
	}
}
