package objects;

import java.awt.Color;
import java.util.Vector;

import main.Panel;
import variables.Matrix4x4;
import variables.Theta;
import variables.Vector3D;

public class SuperObject
{
	public Panel 			  pn;
	
	public Vector<Triangle2D> tris;
	public Color 			  clr;
	
	public Vector3D 		  offset;
	public Vector3D 		  anchoredPoint;
	public Vector3D 		  location;
	
	public Vector3D			  size;
	public double 			  scale;
	
	public double 			  rX, rY, rZ;
	
	public String 			  name;
	
	public Runnable 		  configuration;
	
	public static Matrix4x4	mat		 = new Matrix4x4();
	public static Matrix4x4 matWorld = Matrix4x4.create();
	private Theta 		t 		 = new Theta(0, 0, 0);
	private Matrix4x4	matTrans = Matrix4x4.createTranslation(0, 0, 0);
	
	public static Vector3D topPlane		(Panel pn) {return new Vector3D(0, 0				  	 , 2);}
	public static Vector3D bottomPlane	(Panel pn) {return new Vector3D(0, pn.root.panel[1] - 1	 , 2);}
	public static Vector3D rightPlane	(Panel pn) {return new Vector3D(0,						0, 2);}
	public static Vector3D leftPlane	(Panel pn) {return new Vector3D(pn.root.panel[0] - 1,	0, 2);}
	
	public void UpdateMatrix(double rX, double rY, double rZ)
	{
		t.x = Math.toRadians(rX);
		t.y = Math.toRadians(rY);
		t.z = Math.toRadians(rZ);
		
		Theta.updateRotation(t);
		
		matWorld = Matrix4x4.Mul(Matrix4x4.Mul(t.matRotZ, t.matRotX), t.matRotY);
		matWorld = Matrix4x4.Mul(matWorld, matTrans);
		
		Matrix4x4.updateMat(mat, pn.plr.camera.AR, pn.plr.camera.fFovRad, pn.plr.camera.fF, pn.plr.camera.fN);
	}
}
