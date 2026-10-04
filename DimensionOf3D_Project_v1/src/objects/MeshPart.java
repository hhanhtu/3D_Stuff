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
	
	private Vector<Triangle2D> triToRender	 = new Vector<>();
	
	private Vector3D size;
	
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
	
	public void generateTriangle2D(AssetManager obj)
	{
		Panel pn = obj.pn;
		
		update();
		
		result.clear();
		triToRender.clear();
		
		for(Triangle2D tri:tris)
		{
			tri.clearProjected();
			
			tri.triTransformed.p[0] = matWorld.MultiplyMatrixVector(tri.p[0].Add(anchoredPoint.Mul(scale)));
			tri.triTransformed.p[1] = matWorld.MultiplyMatrixVector(tri.p[1].Add(anchoredPoint.Mul(scale)));
			tri.triTransformed.p[2] = matWorld.MultiplyMatrixVector(tri.p[2].Add(anchoredPoint.Mul(scale)));
			
			tri.triTransformed.p[0] = tri.triTransformed.p[0].Mul(scale).Add(offset);
			tri.triTransformed.p[1] = tri.triTransformed.p[1].Mul(scale).Add(offset);
			tri.triTransformed.p[2] = tri.triTransformed.p[2].Mul(scale).Add(offset);
			
			tri.triTransformed.t[0] = tri.t[0];
			tri.triTransformed.t[1] = tri.t[1];
			tri.triTransformed.t[2] = tri.t[2];
			
			Vector3D line1  = Vector3D.Line(tri.triTransformed.p[1], tri.triTransformed.p[0]);
			Vector3D line2  = Vector3D.Line(tri.triTransformed.p[2], tri.triTransformed.p[0]);
			tri.triTransformed.normal = Vector3D.Cross(line1, line2).Normalise();
			
			tri.triView.p[0] = pn.plr.camera.vCam.MultiplyMatrixVector(tri.triTransformed.p[0]);
			tri.triView.p[1] = pn.plr.camera.vCam.MultiplyMatrixVector(tri.triTransformed.p[1]);
			tri.triView.p[2] = pn.plr.camera.vCam.MultiplyMatrixVector(tri.triTransformed.p[2]);
			
			tri.triView.t[0] = tri.triTransformed.t[0];
			tri.triView.t[1] = tri.triTransformed.t[1];
			tri.triView.t[2] = tri.triTransformed.t[2];
			
			result =  Vector3D.TriangleClippingInPlane(Vector3D.look.Mul(0.5), Vector3D.look, tri.triView);
			
			for(int i = 0; i < (int)result.get("n_tris").get(0); i++)
			{
				Triangle2D rTri = (Triangle2D)result.get("Triangles").get(i);
				
				tri.triProjected.p[0] = SuperObject.mat.MultiplyMatrixVector(rTri.p[0]);
				tri.triProjected.p[1] = SuperObject.mat.MultiplyMatrixVector(rTri.p[1]);
				tri.triProjected.p[2] = SuperObject.mat.MultiplyMatrixVector(rTri.p[2]);
				    
				tri.triProjected.t[0] = rTri.t[0];
				tri.triProjected.t[1] = rTri.t[1];
				tri.triProjected.t[2] = rTri.t[2];
				
				tri.triProjected.p[0] = tri.triProjected.p[0].Div(tri.triProjected.p[0].w);
				tri.triProjected.p[1] = tri.triProjected.p[1].Div(tri.triProjected.p[1].w);
				tri.triProjected.p[2] = tri.triProjected.p[2].Div(tri.triProjected.p[2].w);
				                                             
				tri.triProjected.p[0] = tri.triProjected.p[0].Add(pn.plr.camera.viewOffset);
				tri.triProjected.p[1] = tri.triProjected.p[1].Add(pn.plr.camera.viewOffset);
				tri.triProjected.p[2] = tri.triProjected.p[2].Add(pn.plr.camera.viewOffset);
				
				tri.triProjected.p[0].x *= Panel.root.panel[0]/2;
				tri.triProjected.p[1].x *= Panel.root.panel[0]/2;
				tri.triProjected.p[2].x *= Panel.root.panel[0]/2;
				    
				tri.triProjected.p[0].y *= Panel.root.panel[1]/2;
				tri.triProjected.p[1].y *= Panel.root.panel[1]/2;
				tri.triProjected.p[2].y *= Panel.root.panel[1]/2;
				
				Vector3D camRay = tri.triTransformed.p[0].Sub(pn.plr.camera.p);
				
				if(Vector3D.DotProduct(tri.triTransformed.normal, camRay) < 0)		// < 0 : view outside surface		|| > 0 : view inside surface
				{
					Vector3D dL = pn.light.direction.Normalise();
					
					if(pn.light.state.equals("night"))
						dL = pn.light.direction.Mul(-1).Normalise();
					
					tri.triProjected.LightLevel = Vector3D.DotProduct(tri.triTransformed.normal, dL);
					tri.triProjected.parent = this;
					tri.triProjected.clr = clr;
					tri.triProjected.Shading = !BRIGHT;
					
					tri.triProjected.SetColor(pn);
					
					obj.GLOBALTRIANGLEFRAMES.add(tri.triProjected);
				}
			}
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
						
						Triangle2D tri = new Triangle2D(verts.get(f[0] - 1),
														verts.get(f[1] - 1),
														verts.get(f[2] - 1));
						tri.createProjected();
						
						tris.add(tri);
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
