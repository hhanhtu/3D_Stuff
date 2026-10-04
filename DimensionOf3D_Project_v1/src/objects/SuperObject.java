package objects;

import java.awt.Color;
import java.util.Vector;

import main.AssetManager;
import main.Camera;
import main.Panel;
import variables.Attribute;
import variables.Matrix4x4;
import variables.Theta;
import variables.Vector3D;

public class SuperObject extends Attribute
{
	public Vector<Triangle2D> tris;
	public Color 			  clr;
	
	public Vector3D 		  offset;
	public Vector3D 		  anchoredPoint;
	
	public double 			  scale;
	
	public double 			  rX, rY, rZ;
	
	public Vector3D 		  hitbox;
	public double	 		  distanceToEntity = 0;
	
	public String 			  name;
	public boolean 			  BRIGHT = false;
	public boolean			  onScreen  = false;
	public boolean			  collision = true;
	public boolean 			  highlight = false;
	
	public Runnable 		  configuration;
	
	public static Matrix4x4	mat		 = new Matrix4x4();
	public Matrix4x4 matWorld		 = Matrix4x4.create();
	public Theta 		t 		 = new Theta(0, 0, 0);
	private Matrix4x4	matTrans = Matrix4x4.createTranslation(0, 0, 0);
	
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
