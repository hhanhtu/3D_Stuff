package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import objects.MeshPart;
import objects.Presets;
import objects.SuperObject;
import objects.Triangle2D;
import variables.Matrix4x4;
import variables.Vector3D;

@SuppressWarnings("rawtypes")
public class AssetManager
{
	public Panel pn;
	
	public HashMap<String, Vector> mesh = new HashMap<>();
	public boolean				   WIREFRAME = false;
	
	private Vector<MeshPart>		obj 					= new Vector<>();
	public  Vector<Triangle2D>		GLOBALTRIANGLEFRAMES 	= new Vector<>();
	
	public AssetManager(Panel pn)
	{
		this.pn = pn;
		
		mesh.clear();
		
		loadAsset();
	}
	
	public void loadAsset()
	{
//		Vector<MeshPart> m = new Vector<>();
//		
//		for(int k = -1; k <= 1; k++)
//		{
//			if(k != 0)
//			{
//				for(int v = -1; v <= 1; v++)
//				{
//					if(v != 0)
//					{
//						for(int j = 1; j < 10; j++)
//						{
//							for(int i = 1; i < 10; i++)
//							{
//								MeshPart c = new MeshPart(pn);
//								c.LoadFromObjectFile("cube");
//								c.scale = 3;
//								c.offset = new Vector3D(i*k*c.size().x*2 + 5, j*v*c.size().y*2 + 5, 0);
//								if(i%2 == 0)
//									c.clr = Color.GRAY;
//								
//								m.add(c);
//							}
//						}
//					}
//				}
//			}
//		}
		
		Vector<MeshPart> m = Presets.platform();

		MeshPart cube = new MeshPart();
		cube.LoadFromObjectFile("cube");
		cube.clr = Color.RED;
		cube.scale = 2;
		cube.offset = new Vector3D(0, -20, 0);
		m.add(cube);
		
		for(int k = -1; k <= 1; k++)
		{
			if(k != 0)
			{
				for(int i = 0; i < 2; i++)
				{
					MeshPart pillar1 = new MeshPart();
					pillar1.LoadFromObjectFile("pillar");
					pillar1.clr = Color.GREEN;
					pillar1.scale = 2;
					pillar1.name = "Pillar";
					pillar1.offset = new Vector3D(15 * k, 0, i * 15);
					pillar1.rX = 180;
					
					if(i%2 == 0) pillar1.clr = Color.BLUE;
					
					MeshPart pillar2 = new MeshPart();
					pillar2.LoadFromObjectFile("pillar");
					pillar2.clr = Color.GREEN;
					pillar2.scale = 2;
					pillar2.name = "Pillar";
					pillar2.offset = new Vector3D(15 * k, 0,-i * 15);
					pillar2.rX = 180;
					
					if(i%2 == 0) pillar2.clr = Color.BLUE;
					
					m.add(pillar2);
					m.add(pillar1);
				}
			}
		}
		
		mesh.put("Workspace", m);
	}

	public void updateAll()
	{
	}
	
	public void generateAll(Graphics g)
	{
		GLOBALTRIANGLEFRAMES.clear();
		obj.clear();
		
		for(Map.Entry<String, Vector> i: mesh.entrySet())
		{
			for(Object o: i.getValue())
			{
				MeshPart m = (MeshPart)o;
				m.generate(this);
				
				obj.add(m);
			}
		}
		
		GLOBALTRIANGLEFRAMES.sort((Triangle2D t1, Triangle2D t2) -> {
			double z1 = (t1.p[0].z + t1.p[1].z + t1.p[2].z) / 3;
			double z2 = (t2.p[0].z + t2.p[1].z + t2.p[2].z) / 3;
			
			if(z1 > z2) return -1;
			if(z1 < z2) return  1;
			
			return 0;
		});
		
		for(Triangle2D tri: GLOBALTRIANGLEFRAMES)
		{
			Triangle2D.fill(g, tri);
			if(WIREFRAME)
				Triangle2D.draw(g, tri, Color.ORANGE);
		}
		
		if(WIREFRAME)
		{
			for(MeshPart m: obj)
			{
				Vector3D pointTransformed = Matrix4x4.MultiplyMatrixVector(SuperObject.matWorld, m.offset);
				Vector3D pointViewed	  = Matrix4x4.MultiplyMatrixVector(pn.plr.camera.vCam, pointTransformed);
				Vector3D point 			  = Matrix4x4.MultiplyMatrixVector(SuperObject.mat, pointViewed);
				
				point = Vector3D.Div(point, point.w);
				point = Vector3D.Add(point, pn.plr.camera.viewOffset);
				
				g.setColor(Color.RED);
				g.fillOval((int)(point.x * Panel.root.panel[0]/2 - 4), (int)(point.y * Panel.root.panel[1]/2  - 4), 8, 8);
			}
		}
	}
}
