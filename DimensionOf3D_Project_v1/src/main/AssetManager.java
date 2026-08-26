package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import objects.MeshPart;
import objects.Triangle2D;
import variables.Matrix4x4;
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
		obj1.clr = new Color(255, 215, 0);
		obj1.scale = 3;
		obj1.LoadFromObjectFile("bananaBody");
		obj1.name = "Banana";
		
		MeshPart obj2 = new MeshPart(pn);
		obj2.clr = new Color(139, 69, 19);
		obj2.scale = 3;
		obj2.LoadFromObjectFile("headbanana");
		obj2.name = "Banana";
		
		obj1.configuration = () -> {
			obj1.rX++;
			obj1.rY++;
			obj1.rZ+=0.5;
		};
		
		obj2.configuration = () -> {
			obj2.rX++;
			obj2.rY++;
			obj2.rZ+=0.5;
		};
		
		m.add(obj2);
		m.add(obj1);
		
		mesh.put("Banana", m);
	}
	
	public void assetsUpdate()
	{
		for(Map.Entry<String, Vector> i: mesh.entrySet())
		{
			for(Object o: i.getValue())
			{
				MeshPart m = (MeshPart)o;
				m.update();
			}
		}
	}
	
	public void generateAll(Graphics2D g2)
	{
		Vector<MeshPart> obj  = new Vector<>();
		
		for(Map.Entry<String, Vector> i: mesh.entrySet())
		{
			for(Object o: i.getValue())
			{
				MeshPart m = (MeshPart)o;
				obj.add(m);
			}
		}
		
		obj.sort((MeshPart m1, MeshPart m2) -> {
			double z1 = m1.location.z;
			double z2 = m2.location.z;
			
			if(z1 > z2) return -1;
			if(z1 < z2) return  1;
			
			return 0;
		});
		
		for(MeshPart m: obj)
		{
			m.wireframe = WIREFRAME;
			
			m.generate(g2, true);
		}
	}
}
