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

public class MeshPart
{
	public Panel 			  pn;
	public Vector<Triangle2D> tris;
	public Color 			  clr;
	public Vector3D 		  offset;
	public Vector3D			  size;
	public double 			  scale;
	public double 			  rX, rY, rZ;
	
	private Vector3D 	vUp; 
	private Vector3D 	vT ;
	
	private Matrix4x4 	vCam = new Matrix4x4();
	private Matrix4x4 	mCam = new Matrix4x4();
	
	private Matrix4x4	mat;
	private Theta 		t;
	
	private Matrix4x4 	matTrans = Matrix4x4.createTranslation(0, 0, 0);
	private Matrix4x4 	matWorld = Matrix4x4.create();
	
	public MeshPart(Panel pn)
	{
		this.pn = pn;

		t 	 = new Theta(0, 0, 0);
		mat  = new Matrix4x4();
		tris = new Vector<>();
		
		offset = new Vector3D(0, 0, 3);
		size   = new Vector3D(1, 1, 1);
		scale  = 1;
		
		rX = 0;
		rY = 0;
		rZ = 0;
		
		vUp = new Vector3D(0, 1, 0);
		Matrix4x4 camY = Theta.calculateMatrixRotationY(Math.toRadians(pn.camera.rotation.y));
		pn.camera.look = Matrix4x4.MultiplyMatrixVector(camY, new Vector3D(0, 0, 1));
		vT  = Vector3D.Add(pn.camera.p, pn.camera.look);
		
		mCam = Matrix4x4.PointAt(pn.camera.p, vT, vUp);
		vCam = Matrix4x4.quickInvert(mCam);
	}
	
	@SuppressWarnings("rawtypes")
	public void generate(Graphics2D g2)
	{
		Theta.updateRotation(t);
		
		Vector<Triangle2D> triToRender = new Vector<>();
		
		for(Triangle2D tri:tris)
		{
			Triangle2D triProjected  = new Triangle2D(new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), new Vector3D(0, 0, 0));
			Triangle2D triTransformed= new Triangle2D(new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), new Vector3D(0, 0, 0));
			Triangle2D triView 		 = new Triangle2D(new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), new Vector3D(0, 0, 0));
			
			triTransformed.p[0] = Matrix4x4.MultiplyMatrixVector(matWorld, tri.p[0]);
			triTransformed.p[1] = Matrix4x4.MultiplyMatrixVector(matWorld, tri.p[1]);
			triTransformed.p[2] = Matrix4x4.MultiplyMatrixVector(matWorld, tri.p[2]);
			
			triTransformed.p[0].z += offset.z;
			triTransformed.p[1].z += offset.z;
			triTransformed.p[2].z += offset.z;
			
			Vector3D line1  = Vector3D.Line(triTransformed.p[1], triTransformed.p[0]);
			Vector3D line2  = Vector3D.Line(triTransformed.p[2], triTransformed.p[0]);
			Vector3D normal = Vector3D.Cross(line1, line2);
			
			double l = Vector3D.D(normal);
			normal.x /= l;
			normal.y /= l;
			normal.z /= l;
			
			triView.p[0] = Matrix4x4.MultiplyMatrixVector(vCam, triTransformed.p[0]);
			triView.p[1] = Matrix4x4.MultiplyMatrixVector(vCam, triTransformed.p[1]);
			triView.p[2] = Matrix4x4.MultiplyMatrixVector(vCam, triTransformed.p[2]);
			
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
				
				triProjected.p[0].x += 1; triProjected.p[0].y += 1;
				triProjected.p[1].x += 1; triProjected.p[1].y += 1;
				triProjected.p[2].x += 1; triProjected.p[2].y += 1;
				
				triProjected.p[0].x *= pn.root.panel[0]/2;
				triProjected.p[1].x *= pn.root.panel[0]/2;
				triProjected.p[2].x *= pn.root.panel[0]/2;
				triProjected.p[0].y *= pn.root.panel[1]/2;
				triProjected.p[1].y *= pn.root.panel[1]/2;
				triProjected.p[2].y *= pn.root.panel[1]/2;
				
				Vector3D camRay = Vector3D.Sub(triTransformed.p[0], pn.camera.p);
				
				if(Vector3D.DotProduct(normal, camRay) < 0)		// < 0 : view outside surface		|| > 0 : view inside surface
				{
					double dL = Vector3D.D(pn.lightDirection);
					pn.lightDirection.x /= dL;
					pn.lightDirection.y /= dL;
					pn.lightDirection.z /= dL;
					
					double dp = normal.x * pn.lightDirection.x + normal.y * pn.lightDirection.y + normal.z * pn.lightDirection.z;
					
					triProjected.clr = new Color((int)(clr.getRed()	 	*dp),
							 					 (int)(clr.getGreen()	*dp),
							 					 (int)(clr.getBlue()	*dp));
					
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
				Triangle2D.fill(g2, tri);
				
				Triangle2D.draw(g2, tri, new Color(255 - tri.clr.getRed(), 255 - tri.clr.getGreen(), 255 - tri.clr.getBlue()));
			}
		}
	}
	
	public void updateMatrix()
	{
		Matrix4x4 camX = Theta.calculateMatrixRotationX(Math.toRadians(pn.camera.rotation.x));
		Matrix4x4 camY = Theta.calculateMatrixRotationY(Math.toRadians(pn.camera.rotation.y));
		Matrix4x4 camZ = Theta.calculateMatrixRotationX(Math.toRadians(pn.camera.rotation.z));
		
		pn.camera.look = Matrix4x4.MultiplyMatrixVector(Matrix4x4.Mul(camZ, Matrix4x4.Mul(camY, camX)), new Vector3D(0, 0, 1));
		vT  = Vector3D.Add(pn.camera.p, pn.camera.look);
		
		mCam = Matrix4x4.PointAt(pn.camera.p, vT, vUp);
		vCam = Matrix4x4.quickInvert(mCam);
		
		rY += 1;
		
		t.x = Math.toRadians(rX);
		t.y = Math.toRadians(rY);
		t.z = Math.toRadians(rZ);
		
		matWorld = Matrix4x4.Mul(Matrix4x4.Mul(t.matRotZ, t.matRotX), t.matRotY);
		matWorld = Matrix4x4.Mul(matWorld, matTrans);
		
		Matrix4x4.updateMat(mat, pn.AR, pn.fFovRad, pn.fF, pn.fN);
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
						
//						System.out.println(f[0]);
						
//						tris.add(new Triangle2D(verts[f[0] - 1], verts[f[1] - 1], verts[f[2] - 1]));
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
