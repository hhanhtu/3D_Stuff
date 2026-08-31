package objects;

import java.awt.Color;
import java.util.Vector;

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
	
	public Vector3D			  size;
	public double 			  scale;
	
	public double 			  rX, rY, rZ;
	
	public String 			  name;
	
	public Runnable 		  configuration;
	
	public static Matrix4x4	mat		 = new Matrix4x4();
	public static Matrix4x4 matWorld = Matrix4x4.create();
	private Theta 		t 		 = new Theta(0, 0, 0);
	private Matrix4x4	matTrans = Matrix4x4.createTranslation(0, 0, 0);
	
	public static Vector3D topPlane		= new Vector3D(0, 0, 2);
	public static Vector3D bottomPlane	= new Vector3D(0, Panel.root.panel[1] - 1, 2);
	public static Vector3D rightPlane	= new Vector3D(0, 0, 2);
	public static Vector3D leftPlane	= new Vector3D(Panel.root.panel[0] - 1, 0, 2);

	public void UpdateMatrix(double rX, double rY, double rZ)
	{
		t.x = Math.toRadians(rX);
		t.y = Math.toRadians(rY);
		t.z = Math.toRadians(rZ);
		
		Theta.updateRotation(t);
		
		matWorld = Matrix4x4.Mul(Matrix4x4.Mul(t.matRotZ, t.matRotX), t.matRotY);
		matWorld = Matrix4x4.Mul(matWorld, matTrans);
		
		Matrix4x4.updateMat(mat, Camera.AR, Camera.fFovRad, Camera.fF, Camera.fN);
	}
}
