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

public class MeshPart extends SuperObject
{
	private Matrix4x4	mat;
	private Theta 		t;
	
	private Matrix4x4 	matTrans; 
	private Matrix4x4 	matWorld; 
	
	public MeshPart(Panel pn)
	{
		this.pn = pn;

		t 	 = new Theta(0, 0, 0);
		mat  = new Matrix4x4();
		tris = new Vector<>();
		
		offset = new Vector3D(0, 0, 3);
		size   = new Vector3D(1, 1, 1);
		scale  = 1;
		
		matTrans = Matrix4x4.createTranslation(0, 0, 0); 
		matWorld = Matrix4x4.create();                   
		
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
	
	@SuppressWarnings("rawtypes")
	public void generate(Graphics2D g2)
	{
		updateMatrix();
		
		Theta.updateRotation(t);
		
		Vector<Triangle2D> triToRender = new Vector<>();
		
		for(Triangle2D tri:tris)
		{
			Triangle2D triProjected   = new Triangle2D(new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), new Vector3D(0, 0, 0));
			Triangle2D triTransformed = new Triangle2D(new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), new Vector3D(0, 0, 0));
			Triangle2D triView 		  = new Triangle2D(new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), new Vector3D(0, 0, 0));
			
			triTransformed.p[0] = Matrix4x4.MultiplyMatrixVector(matWorld, tri.p[0]);
			triTransformed.p[1] = Matrix4x4.MultiplyMatrixVector(matWorld, tri.p[1]);
			triTransformed.p[2] = Matrix4x4.MultiplyMatrixVector(matWorld, tri.p[2]);
			
			triTransformed.p[0] = Vector3D.Mul(Vector3D.Add(triTransformed.p[0], offset), scale);
			triTransformed.p[1] = Vector3D.Mul(Vector3D.Add(triTransformed.p[1], offset), scale);
			triTransformed.p[2] = Vector3D.Mul(Vector3D.Add(triTransformed.p[2], offset), scale);
			
			Vector3D line1  = Vector3D.Line(triTransformed.p[1], triTransformed.p[0]);
			Vector3D line2  = Vector3D.Line(triTransformed.p[2], triTransformed.p[0]);
			Vector3D normal = Vector3D.Normalise(Vector3D.Cross(line1, line2));
			
			triView.p[0] = Matrix4x4.MultiplyMatrixVector(pn.plr.camera.vCam, triTransformed.p[0]);
			triView.p[1] = Matrix4x4.MultiplyMatrixVector(pn.plr.camera.vCam, triTransformed.p[1]);
			triView.p[2] = Matrix4x4.MultiplyMatrixVector(pn.plr.camera.vCam, triTransformed.p[2]);
			
			HashMap<String, Vector> result =  Vector3D.TriangleClippingInPlane(g2, new Vector3D(0, 0, .05), new Vector3D(0, 0, 1), triView);
			
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
				
				Vector3D camRay = Vector3D.Sub(triTransformed.p[0], Vector3D.Add(pn.plr.camera.p, pn.lightDirection));
				
				if(Vector3D.DotProduct(normal, camRay) < 0)		// < 0 : view outside surface		|| > 0 : view inside surface
				{
					Vector3D dL = Vector3D.Normalise(Vector3D.Add(pn.plr.camera.p, pn.lightDirection));
					
					double dp = Vector3D.DotProduct(normal, dL);
					
					triProjected.clr		= clr;
					triProjected.LightLevel = dp;
					
					triToRender.addLast(triProjected);
				}
			}
		}
		
		triToRender.sort((Triangle2D t1, Triangle2D t2) -> {
			double z1 = (t1.p[0].z + t1.p[1].z + t1.p[2].z) / 3;
			double z2 = (t2.p[0].z + t2.p[1].z + t2.p[2].z) / 3;
			
			if(z1 > z2) return -1;
			if(z1 < z2) return  1;
			
			return 0;
		});
		
		for(Triangle2D tri: triToRender)
		{
			int r = tri.clr.getRed();
			int g = tri.clr.getGreen();
			int b = tri.clr.getBlue();
			
			if(tri.LightLevel < 0)
			{
				tri.clr = new Color(
						Math.abs((int)(r * tri.LightLevel)),
						Math.abs((int)(g * tri.LightLevel)),
						Math.abs((int)(b * tri.LightLevel))
						);
			} else {
				tri.clr = new Color(
						(int)(r * tri.LightLevel),
						(int)(g * tri.LightLevel),
						(int)(b * tri.LightLevel)
						);
			}
			
		}
		
		for(Triangle2D triToRaster: triToRender)
		{
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
					
					HashMap<String, Vector> c0 = Vector3D.TriangleClippingInPlane(g2, new Vector3D(0, 0				  		 , 2), new Vector3D( 0, 1, 1), t);
					HashMap<String, Vector> c1 = Vector3D.TriangleClippingInPlane(g2, new Vector3D(0, pn.root.panel[1] - 1	 , 2), new Vector3D( 0,-1, 1), t);
					HashMap<String, Vector> c2 = Vector3D.TriangleClippingInPlane(g2, new Vector3D(0,						0, 2), new Vector3D( 1, 0, 1), t);
					HashMap<String, Vector> c3 = Vector3D.TriangleClippingInPlane(g2, new Vector3D(pn.root.panel[0] - 1,	0, 2), new Vector3D(-1, 0, 1), t);
					
					switch(p)
					{
					case 0: nTrsAdd = (int)c0.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++)
							trs.addLast((Triangle2D)c0.get("Triangles").get(j));
					break;
					case 1: nTrsAdd = (int)c1.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++)
							trs.addLast((Triangle2D)c1.get("Triangles").get(j));
					break;
					case 2: nTrsAdd = (int)c2.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++)
							trs.addLast((Triangle2D)c2.get("Triangles").get(j));
					break;
					case 3: nTrsAdd = (int)c3.get("n_tris").get(0);
						for(int j = 0; j < nTrsAdd; j++)
							trs.addLast((Triangle2D)c3.get("Triangles").get(j));
					break;
					}
				}
				
				nTris = trs.size();
			}
			
			for(Triangle2D tri: trs)
			{
				// render solid
				Triangle2D.fill(g2, tri);
				
				// wire frame
//				Triangle2D.draw(g2, tri, new Color(255 - tri.clr.getRed(), 255 - tri.clr.getGreen(), 255 - tri.clr.getBlue()));
			}
		}
	}
	
	public void updateMatrix()
	{
		t.x = Math.toRadians(rX);
		t.y = Math.toRadians(rY);
		t.z = Math.toRadians(rZ);
		
		matWorld = Matrix4x4.Mul(Matrix4x4.Mul(t.matRotZ, t.matRotX), t.matRotY);
		matWorld = Matrix4x4.Mul(matWorld, matTrans);
		
		Matrix4x4.updateMat(mat, pn.plr.camera.AR, pn.plr.camera.fFovRad, pn.plr.camera.fF, pn.plr.camera.fN);
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
		}
	}
}
