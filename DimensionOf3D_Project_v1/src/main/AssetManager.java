package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.HashMap;
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
	public Vector<Triangle2D> 	   GLOBALTRIANGLEFRAMES 	= new Vector<>();
	public Vector<Triangle2D> 	   GLOBALTRIANGLETRANFORMED = new Vector<>();
	
	public AssetManager(Panel pn)
	{
		this.pn = pn;
		
		mesh.clear();
		
		loadAsset();
	}
	
	public void loadAsset()
	{
		Vector<MeshPart> m = Presets.platform(pn);
		
		for(int k = -1; k <= 1; k++)
		{
			if(k != 0)
			{
				for(int i = 0; i < 2; i++)
				{
					MeshPart pillar1 = new MeshPart(pn);
					pillar1.LoadFromObjectFile("pillar");
					pillar1.clr = Color.GREEN;
					pillar1.scale = 2;
					pillar1.name = "Pillar";
					pillar1.offset = new Vector3D(15 * k, 0, i * 15/2);
					pillar1.rX = 180;
					
					if(i%2 == 0) pillar1.clr = Color.BLUE;
					
					MeshPart pillar2 = new MeshPart(pn);
					pillar2.LoadFromObjectFile("pillar");
					pillar2.clr = Color.GREEN;
					pillar2.scale = 2;
					pillar2.name = "Pillar";
					pillar2.offset = new Vector3D(15 * k, 0,-i * 15/2);
					pillar2.rX = 180;
					
					if(i%2 == 0) pillar2.clr = Color.BLUE;
					
					m.add(pillar2);
					m.add(pillar1);
				}
			}
		}
		
		mesh.put("Workspace", m);
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
		GLOBALTRIANGLEFRAMES.clear();
		
		Vector<MeshPart> obj  = new Vector<>();

		for(Map.Entry<String, Vector> i: mesh.entrySet())
		{
			for(Object o: i.getValue())
			{
				MeshPart m = (MeshPart)o;
				m.generate();
				
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
		
		for(Triangle2D triToRaster: GLOBALTRIANGLEFRAMES)
		{
			int r = triToRaster.clr.getRed();
			int g = triToRaster.clr.getGreen();
			int b = triToRaster.clr.getBlue();
			
			if(triToRaster.parent != "floor")
			{
				try {
					triToRaster.clr = new Color(
							(int)(r*triToRaster.LightLevel),
							(int)(g*triToRaster.LightLevel),
							(int)(b*triToRaster.LightLevel)
							);
				} catch(Exception e)
				{
					triToRaster.clr = new Color(
							(int)(0*triToRaster.LightLevel),
							(int)(0*triToRaster.LightLevel),
							(int)(0*triToRaster.LightLevel)
							);
				}
			}
			
			Vector<Triangle2D> trs = new Vector<>();
			
			trs.addLast(triToRaster);
			int nTris = 1;
			
			for(int p = 0; p < 4; p++)
			{
				int nTrsAdd = 0;
				
				while(nTris > 0)
				{
					Triangle2D t = trs.getFirst();
					trs.removeFirst();
					nTris--;
					
					HashMap<String, Vector> c0 = Vector3D.TriangleClippingInPlane(SuperObject.topPlane(pn)		, new Vector3D( 0, 1, 1), t);
					HashMap<String, Vector> c1 = Vector3D.TriangleClippingInPlane(SuperObject.bottomPlane(pn)	, new Vector3D( 0,-1, 1), t);
					HashMap<String, Vector> c2 = Vector3D.TriangleClippingInPlane(SuperObject.rightPlane(pn)	, new Vector3D( 1, 0, 1), t);
					HashMap<String, Vector> c3 = Vector3D.TriangleClippingInPlane(SuperObject.leftPlane(pn)		, new Vector3D(-1, 0, 1), t);
					
					switch(p)
					{
					case 0: nTrsAdd = (int)c0.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++) {
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c0, j));
							Triangle2D.fill(g2, Triangle2D.getTrianglesFromClipResult(c0, j));
							if(WIREFRAME)
								Triangle2D.draw(g2, Triangle2D.getTrianglesFromClipResult(c0, j), Color.ORANGE);
							}
					break;
					case 1: nTrsAdd = (int)c1.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++) {
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c1, j));
							Triangle2D.fill(g2, Triangle2D.getTrianglesFromClipResult(c1, j));
							if(WIREFRAME)
								Triangle2D.draw(g2, Triangle2D.getTrianglesFromClipResult(c1, j), Color.ORANGE);
							}
					break;
					case 2: nTrsAdd = (int)c2.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++) {
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c2, j));
							Triangle2D.fill(g2, Triangle2D.getTrianglesFromClipResult(c2, j));
							if(WIREFRAME)
								Triangle2D.draw(g2, Triangle2D.getTrianglesFromClipResult(c2, j), Color.ORANGE);
							}
					break;
					case 3: nTrsAdd = (int)c3.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++) {
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c3, j));
							Triangle2D.fill(g2, Triangle2D.getTrianglesFromClipResult(c3, j));
							if(WIREFRAME)
								Triangle2D.draw(g2, Triangle2D.getTrianglesFromClipResult(c3, j), Color.ORANGE);
							}
					break;
					}
				}
				
				nTris = trs.size();
			}
		}
	}
}
