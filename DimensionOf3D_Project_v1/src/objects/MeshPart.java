package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import org.joml.*;
import java.lang.Math;

import main.Panel;
import variables.Matrix4x4;
import variables.Theta;
import variables.Vector3D;

@SuppressWarnings("rawtypes")
public class MeshPart extends SuperObject
{
	public MeshPart(Panel pn)
	{
		this.pn = pn;

		tris = new Vector<>();
		
		offset			= new Vector3D(0, 0, 0);
		anchoredPoint	= new Vector3D(0, 0, 0);
		location 		= new Vector3D(0, 0, 0);
		
		clr  = Color.WHITE;
		name = "Mesh Part";
		size = Vector3D.zero();
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
	
	public void generate()
	{
		update();
		
		for(Triangle2D tri:tris)
		{
			Triangle2D triProjected   = new Triangle2D(Vector3D.zero(), Vector3D.zero(), Vector3D.zero());
			Triangle2D triTransformed = new Triangle2D(Vector3D.zero(), Vector3D.zero(), Vector3D.zero());
			Triangle2D triView 		  = new Triangle2D(Vector3D.zero(), Vector3D.zero(), Vector3D.zero());
			
			triTransformed.p[0] = Matrix4x4.MultiplyMatrixVector(matWorld, Vector3D.Add(tri.p[0], Vector3D.Mul(anchoredPoint, scale)));
			triTransformed.p[1] = Matrix4x4.MultiplyMatrixVector(matWorld, Vector3D.Add(tri.p[1], Vector3D.Mul(anchoredPoint, scale)));
			triTransformed.p[2] = Matrix4x4.MultiplyMatrixVector(matWorld, Vector3D.Add(tri.p[2], Vector3D.Mul(anchoredPoint, scale)));
			
			triTransformed.p[0] = Vector3D.Add(Vector3D.Mul(triTransformed.p[0], scale), offset);
			triTransformed.p[1] = Vector3D.Add(Vector3D.Mul(triTransformed.p[1], scale), offset);
			triTransformed.p[2] = Vector3D.Add(Vector3D.Mul(triTransformed.p[2], scale), offset);
			
			Vector3D line1  = Vector3D.Line(triTransformed.p[1], triTransformed.p[0]);
			Vector3D line2  = Vector3D.Line(triTransformed.p[2], triTransformed.p[0]);
			Vector3D normal = Vector3D.Normalise(Vector3D.Cross(line1, line2));
			
			triView.p[0] = Matrix4x4.MultiplyMatrixVector(pn.plr.camera.vCam, triTransformed.p[0]);
			triView.p[1] = Matrix4x4.MultiplyMatrixVector(pn.plr.camera.vCam, triTransformed.p[1]);
			triView.p[2] = Matrix4x4.MultiplyMatrixVector(pn.plr.camera.vCam, triTransformed.p[2]);
			
			HashMap<String, Vector> result =  Vector3D.TriangleClippingInPlane(new Vector3D(0, 0, .05), new Vector3D(0, 0, 1), triView);
			
			pn.obj.GLOBALTRIANGLETRANFORMED.add(triTransformed);
			
			for(int i = 0; i < (int)result.get("n_tris").get(0); i++)
			{
				Triangle2D rTri = (Triangle2D)result.get("Triangles").get(i);
				
				triProjected.p[0] = Matrix4x4.MultiplyMatrixVector(mat, rTri.p[0]);
				triProjected.p[1] = Matrix4x4.MultiplyMatrixVector(mat, rTri.p[1]);
				triProjected.p[2] = Matrix4x4.MultiplyMatrixVector(mat, rTri.p[2]);
				
				triProjected.p[0] = Vector3D.Div(triProjected.p[0], triProjected.p[0].w);
				triProjected.p[1] = Vector3D.Div(triProjected.p[1], triProjected.p[1].w);
				triProjected.p[2] = Vector3D.Div(triProjected.p[2], triProjected.p[2].w);
				
				triProjected.p[0] = Vector3D.Add(triProjected.p[0], pn.plr.camera.viewOffset);
				triProjected.p[1] = Vector3D.Add(triProjected.p[1], pn.plr.camera.viewOffset);
				triProjected.p[2] = Vector3D.Add(triProjected.p[2], pn.plr.camera.viewOffset);
				
				triProjected.p[0].x *= pn.root.panel[0]/2;
				triProjected.p[1].x *= pn.root.panel[0]/2;
				triProjected.p[2].x *= pn.root.panel[0]/2;
				
				triProjected.p[0].y *= pn.root.panel[1]/2;
				triProjected.p[1].y *= pn.root.panel[1]/2;
				triProjected.p[2].y *= pn.root.panel[1]/2;
				
				Vector3D camRay = Vector3D.Sub(triTransformed.p[0], pn.plr.camera.p);
				
				if(Vector3D.DotProduct(normal, camRay) < 0)		// < 0 : view outside surface		|| > 0 : view inside surface
				{
					Vector3D dL = Vector3D.Normalise(pn.light.direction);
					if(pn.light.state.equals("night"))
						dL = Vector3D.Normalise(Vector3D.Mul(pn.light.direction, -1));
					
					double dp = Vector3D.DotProduct(normal, dL);
					
					triProjected.LightLevel = dp;
					triProjected.parent = name;
					triProjected.clr = clr;
					
					pn.obj.GLOBALTRIANGLEFRAMES.addLast(triProjected);
				}
			}
		}
	}
	
	public Vector3D size()
	{
		Vector<Double> coordinatesX = new Vector<>();
		Vector<Double> coordinatesY = new Vector<>();
		Vector<Double> coordinatesZ = new Vector<>();
		
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
