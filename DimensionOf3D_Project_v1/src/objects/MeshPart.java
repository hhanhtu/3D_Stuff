package objects;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Vector;

import main.AssetManager;
import main.Panel;
import variables.Vector3D;

@SuppressWarnings("rawtypes")
public class MeshPart extends SuperObject
{
	public HashMap<String, Vector> result = new HashMap<>();
	private Vector<Triangle2D> trs			 = new Vector<>();
	private Vector<Triangle2D> triToRender	 = new Vector<>();
	
	private HashMap<String, Vector> c0 = new HashMap<>();
	private HashMap<String, Vector> c1 = new HashMap<>();
	private HashMap<String, Vector> c2 = new HashMap<>();
	private HashMap<String, Vector> c3 = new HashMap<>();
	
	private Vector<Double> coordinatesX = new Vector<>();
	private Vector<Double> coordinatesY = new Vector<>();
	private Vector<Double> coordinatesZ = new Vector<>();
	
	public MeshPart()
	{
		tris = new Vector<>();
		
		offset			= new Vector3D(0, 0, 0);
		anchoredPoint	= new Vector3D(0, 0, 0);
		
		clr  = Color.WHITE;
		name = "Mesh Part";
		size = Vector3D.zero;
		scale  = 1;
		
		rX = 0;
		rY = 0;
		rZ = 0;
	}
	/*
									SQUARE BY HAND
		Vector<Triangle2D> tris = new Vector<>();
		// Front
		tris.add(new Triangle2D(new Vector3D(0, 0, 0), new Vector3D(0, 1, 0), new Vector3D(1, 1, 0)));
		tris.add(new Triangle2D(new Vector3D(0, 0, 0), new Vector3D(1, 1, 0), new Vector3D(1, 0, 0)));
		// Right
		tris.add(new Triangle2D(new Vector3D(1, 0, 0), new Vector3D(1, 1, 0), new Vector3D(1, 1, 1)));
		tris.add(new Triangle2D(new Vector3D(1, 0, 0), new Vector3D(1, 1, 1), new Vector3D(1, 0, 1)));
		// Back
		tris.add(new Triangle2D(new Vector3D(1, 0, 1), new Vector3D(1, 1, 1), new Vector3D(0, 1, 1)));
		tris.add(new Triangle2D(new Vector3D(1, 0, 1), new Vector3D(0, 1, 1), new Vector3D(0, 0, 1)));
		// Left
		tris.add(new Triangle2D(new Vector3D(0, 0, 1), new Vector3D(0, 1, 1), new Vector3D(0, 1, 0)));
		tris.add(new Triangle2D(new Vector3D(0, 0, 1), new Vector3D(0, 1, 0), new Vector3D(0, 0, 0)));
		// Top
		tris.add(new Triangle2D(new Vector3D(0, 1, 0), new Vector3D(0, 1, 1), new Vector3D(1, 1, 1)));
		tris.add(new Triangle2D(new Vector3D(0, 1, 0), new Vector3D(1, 1, 1), new Vector3D(1, 1, 0)));
		// Bottom
		tris.add(new Triangle2D(new Vector3D(1, 0, 1), new Vector3D(0, 0, 1), new Vector3D(0, 0, 0)));
		tris.add(new Triangle2D(new Vector3D(1, 0, 1), new Vector3D(0, 0, 0), new Vector3D(1, 0, 0)));
	 */
	
	public void generate(AssetManager obj)
	{
		update();
		
		result.clear();
		triToRender.clear();
		c0.clear(); c1.clear(); c2.clear(); c3.clear();
		
		int id = 0;
		
		Triangle2D.Transformed3Dto2D(tris, this, obj, triToRender);
		
		for(Triangle2D tri: triToRender)
		{
			tri.id = id;
			
			trs.clear();
			int nTrsAdd = 0;
			
			trs.addLast(tri);
			int nTris = 1;
			
			for(int p = 0; p < 4; p++)
			{
				while(nTris > 0)
				{
					Triangle2D t = trs.getFirst();
					trs.removeFirst();
					nTris--;
					
					c0 = Vector3D.TriangleClippingInPlane(SuperObject.topPlane		, Panel.plane.topPlane		, t);
					c1 = Vector3D.TriangleClippingInPlane(SuperObject.bottomPlane	, Panel.plane.bottomPlane	, t);
					c2 = Vector3D.TriangleClippingInPlane(SuperObject.rightPlane	, Panel.plane.rightPlane	, t);
					c3 = Vector3D.TriangleClippingInPlane(SuperObject.leftPlane		, Panel.plane.leftPlane		, t);
					
					switch(p)
					{
					case 0: nTrsAdd = (int)c0.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++) {
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c0, j));
							obj.GLOBALTRIANGLEFRAMES.add(trs.getLast());
							}
					break;
					case 1: nTrsAdd = (int)c1.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++) {
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c1, j));
							obj.GLOBALTRIANGLEFRAMES.add(trs.getLast());
							}
					break;
					case 2: nTrsAdd = (int)c2.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++) {
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c2, j));
							obj.GLOBALTRIANGLEFRAMES.add(trs.getLast());
							}
					break;
					case 3: nTrsAdd = (int)c3.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++) {
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c3, j));
							obj.GLOBALTRIANGLEFRAMES.add(trs.getLast());
							}
					break;
					}
				}
				
				nTris = trs.size();
			}
			
			id++;
		}
	}
	
	public Vector3D size()
	{
		coordinatesX.clear();
		coordinatesY.clear();
		coordinatesZ.clear();
		
		for(Triangle2D tri: tris)
		{
			coordinatesX.add(tri.p[0].x * scale);	coordinatesY.add(tri.p[0].y * scale);	coordinatesZ.add(tri.p[0].z * scale);
			coordinatesX.add(tri.p[1].x * scale);   coordinatesY.add(tri.p[1].y * scale);   coordinatesZ.add(tri.p[1].z * scale);
			coordinatesX.add(tri.p[2].x * scale);   coordinatesY.add(tri.p[2].y * scale);   coordinatesZ.add(tri.p[2].z * scale);
		}
		
		coordinatesX.sort(Comparator.reverseOrder());
		coordinatesY.sort(Comparator.reverseOrder());
		coordinatesZ.sort(Comparator.reverseOrder());
		
		size.x = coordinatesX.getFirst();
		size.y = coordinatesY.getFirst();
		size.z = coordinatesZ.getFirst();
		
		return size;
	}
	
	public void update()
	{
		if(configuration != null)
		{
			configuration.run();
		}
		
		UpdateMatrix(rX, rY, rZ);
	}
	
	public void LoadFromObjectFile(String name)
	{
		try
		{
			InputStream IS = getClass().getResourceAsStream(String.format("/object/%s.obj", name));
			BufferedReader BR = new BufferedReader(new InputStreamReader(IS));
			
			if(BR.ready())
			{
				Vector<Vector3D> verts = new Vector<>();
				
				String line = BR.readLine().toLowerCase();
				
				while((line = BR.readLine()) != null)
				{
					String var[]  = line.split(" ");
					
					if(var[0].equals("v"))
					{
						Vector3D v = new Vector3D(Double.parseDouble(var[1]) * scale,
												  Double.parseDouble(var[2]) * scale,
												  Double.parseDouble(var[3]) * scale);
						
						verts.add(v);
					}
					
					if(var[0].equals("f"))
					{
						int[] f = new int[3];
						
						f[0] = Integer.parseInt(var[1]);
						f[1] = Integer.parseInt(var[2]);
						f[2] = Integer.parseInt(var[3]);
						
						tris.add(new Triangle2D(verts.get(f[0] - 1), verts.get(f[1] - 1), verts.get(f[2] - 1)));
					}
				}
				
				BR.close();
			}
			
		} catch(Exception e)
		{
			System.out.println("File unknown:: Check again...\n");
			e.printStackTrace();
		}
	}
}
