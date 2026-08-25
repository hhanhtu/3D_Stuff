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
	public boolean				   WIREFRAME = false;
	
	public AssetManager(Panel pn)
	{
		this.pn = pn;
		
		mesh.clear();
		
		loadAsset();
	}
	
	public void loadAsset()
	{
		Vector<MeshPart> m = new Vector<>();
		
		MeshPart obj1 = new MeshPart(pn);
		MeshPart obj2 = new MeshPart(pn);
		
		obj1.clr = Color.GRAY;
		obj1.offset = new Vector3D(0, 0, 5);
		obj1.scale = 2;
		obj1.LoadFromObjectFile("room3");
		obj1.name = "room3";
		obj1.rX = 180;
		
		obj2.clr = Color.RED;
		obj2.offset = new Vector3D(0, -20, 5);
		obj2.scale = 2;
		obj2.LoadFromObjectFile("cube");
		obj2.name = "cube";
		
		obj2.configuration = () -> {
			obj2.rY++;
			obj2.rX++;
			obj2.rZ+=0.5;
		};
		
		m.add(obj1);
		m.add(obj2);
		
		mesh.put("Banana", m);
	}
	
	public void assetsUpdate()
	{
		for(Map.Entry<String, Vector> i: mesh.entrySet())
		{
			for(Object m: i.getValue())
			{
				MeshPart o = (MeshPart)m;
				o.updateMatrix();
				
			}
		}
	}
	
	public void generateAll(Graphics2D g2)
	{
		for(Map.Entry<String, Vector> i: mesh.entrySet())
		{
			for(Object m: i.getValue())
			{
				MeshPart o = (MeshPart)m;
				o.wireframe = WIREFRAME;
				o.generate(g2);
			}
		}
	}
}
