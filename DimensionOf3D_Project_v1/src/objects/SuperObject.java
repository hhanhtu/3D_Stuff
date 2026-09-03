package objects;

import java.awt.Color;
import java.util.Vector;

import main.AssetManager;
import main.Camera;
import main.Panel;
import variables.Matrix4x4;
import variables.Theta;
import variables.Vector3D;

public class SuperObject
{
	public Vector<Triangle2D> tris;
	public Color 			  clr;
	
	public Vector3D 		  offset;
	public Vector3D 		  anchoredPoint;
	
	public double 			  scale;
	
	public double 			  rX, rY, rZ;
	
	public String 			  name;
	public boolean 			  BRIGHT = false;
	public boolean			  collision = true;
	
	public Runnable 		  configuration;
	
	public static Matrix4x4	mat		 = new Matrix4x4();
	public static Matrix4x4 matWorld = Matrix4x4.create();
	private Theta 		t 		 = new Theta(0, 0, 0);
	private Matrix4x4	matTrans = Matrix4x4.createTranslation(0, 0, 0);
	
	public static void Transformed3Dto2D(Vector<Triangle2D> tris, MeshPart m, AssetManager obj, Vector<Triangle2D> out)
	{
		Panel pn = obj.pn;
		
		for(Triangle2D tri:tris)
		{
			Triangle2D triProjected   = new Triangle2D();
			Triangle2D triTransformed = new Triangle2D();
			Triangle2D triView 	  	  = new Triangle2D();
			
			triTransformed.p[0] = SuperObject.matWorld.MultiplyMatrixVector(tri.p[0].Add(m.anchoredPoint.Mul(m.scale)));
			triTransformed.p[1] = SuperObject.matWorld.MultiplyMatrixVector(tri.p[1].Add(m.anchoredPoint.Mul(m.scale)));
			triTransformed.p[2] = SuperObject.matWorld.MultiplyMatrixVector(tri.p[2].Add(m.anchoredPoint.Mul(m.scale)));
			
			triTransformed.p[0] = triTransformed.p[0].Mul(m.scale).Add(m.offset);
			triTransformed.p[1] = triTransformed.p[1].Mul(m.scale).Add(m.offset);
			triTransformed.p[2] = triTransformed.p[2].Mul(m.scale).Add(m.offset);
			
			triTransformed.t[0] = tri.t[0];
			triTransformed.t[1] = tri.t[1];
			triTransformed.t[2] = tri.t[2];
			
			Vector3D line1  = Vector3D.Line(triTransformed.p[1], triTransformed.p[0]);
			Vector3D line2  = Vector3D.Line(triTransformed.p[2], triTransformed.p[0]);
			Vector3D normal = Vector3D.Cross(line1, line2).Normalise();
			
			triView.p[0] = pn.plr.camera.vCam.MultiplyMatrixVector(triTransformed.p[0]);
			triView.p[1] = pn.plr.camera.vCam.MultiplyMatrixVector(triTransformed.p[1]);
			triView.p[2] = pn.plr.camera.vCam.MultiplyMatrixVector(triTransformed.p[2]);
			
			triView.t[0] = triTransformed.t[0];
			triView.t[1] = triTransformed.t[1];
			triView.t[2] = triTransformed.t[2];
			
			m.result =  Vector3D.TriangleClippingInPlane(new Vector3D(0, 0, .5), Vector3D.look, triView);
			
			for(int i = 0; i < (int)m.result.get("n_tris").get(0); i++)
			{
				Triangle2D rTri = (Triangle2D)m.result.get("Triangles").get(i);
				
				triProjected.p[0] = SuperObject.mat.MultiplyMatrixVector(rTri.p[0]);
				triProjected.p[1] = SuperObject.mat.MultiplyMatrixVector(rTri.p[1]);
				triProjected.p[2] = SuperObject.mat.MultiplyMatrixVector(rTri.p[2]);
				
				triProjected.t[0] = rTri.t[0];
				triProjected.t[1] = rTri.t[1];
				triProjected.t[2] = rTri.t[2];
				
				triProjected.p[0] = triProjected.p[0].Div(triProjected.p[0].w);
				triProjected.p[1] = triProjected.p[1].Div(triProjected.p[1].w);
				triProjected.p[2] = triProjected.p[2].Div(triProjected.p[2].w);
				                                     
				triProjected.p[0] = triProjected.p[0].Add(pn.plr.camera.viewOffset);
				triProjected.p[1] = triProjected.p[1].Add(pn.plr.camera.viewOffset);
				triProjected.p[2] = triProjected.p[2].Add(pn.plr.camera.viewOffset);
				
				triProjected.p[0].x *= Panel.root.panel[0]/2;
				triProjected.p[1].x *= Panel.root.panel[0]/2;
				triProjected.p[2].x *= Panel.root.panel[0]/2;
				
				triProjected.p[0].y *= Panel.root.panel[1]/2;
				triProjected.p[1].y *= Panel.root.panel[1]/2;
				triProjected.p[2].y *= Panel.root.panel[1]/2;
				
				Vector3D camRay = triTransformed.p[0].Sub(pn.plr.camera.p);
				
				if(Vector3D.DotProduct(normal, camRay) < 0)		// < 0 : view outside surface		|| > 0 : view inside surface
				{
					Vector3D dL = pn.light.direction.Normalise();
					
					if(pn.light.state.equals("night"))
						dL = pn.light.direction.Mul(-1).Normalise();
					
					double dp = Vector3D.DotProduct(normal, dL);
					
					triProjected.LightLevel = dp;
					triProjected.parent = m.name;
					triProjected.clr = m.clr;
					triProjected.Shading = !m.BRIGHT;
					
					triProjected.SetColor(pn);
					
					out.addLast(triProjected);
				}
			}
		}
	}
	
	public void UpdateMatrix(double rX, double rY, double rZ)
	{
		t.x = Math.toRadians(rX);
		t.y = Math.toRadians(rY);
		t.z = Math.toRadians(rZ);
		
		t.updateRotation();
		
		matWorld = t.matRotX.Mul(t.matRotY.Mul(t.matRotZ));
		matWorld = matWorld.Mul(matTrans);
		
		mat.m[0][0] = Camera.AR * Camera.fFovRad;
		mat.m[1][1] = Camera.fFovRad;
		mat.m[2][2] = Camera.fF / (Camera.fF - Camera.fN);
		mat.m[3][2] = (-Camera.fF * Camera.fN) / (Camera.fF - Camera.fN);
		mat.m[2][3] = 1;
		mat.m[3][3] = 0;
	}
}
