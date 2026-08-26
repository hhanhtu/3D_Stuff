package objects;

import java.awt.Color;
import java.util.Vector;

import main.Panel;
import variables.Matrix4x4;
import variables.Vector3D;

public class SuperObject
{
	public Panel 			  pn;
	
	public Vector<Triangle2D> tris;
	public Color 			  clr;
	
	public Vector3D 		  offset;
	public Vector3D 		  location;
	
	public Vector3D			  size;
	public double 			  scale;
	
	public double 			  rX, rY, rZ;
	
	public String 			  name;
	
	public Runnable 		  configuration;
	
	public Matrix4x4	mat = new Matrix4x4();
	
	public static Vector3D topPlane		(Panel pn) {return new Vector3D(0, 0				  	 , 2);}
	public static Vector3D bottomPlane	(Panel pn) {return new Vector3D(0, pn.root.panel[1] - 1	 , 2);}
	public static Vector3D rightPlane	(Panel pn) {return new Vector3D(0,						0, 2);}
	public static Vector3D leftPlane	(Panel pn) {return new Vector3D(pn.root.panel[0] - 1,	0, 2);}
	
	public void UpdateMatrix()
	{
		Matrix4x4.updateMat(mat, pn.plr.camera.AR, pn.plr.camera.fFovRad, pn.plr.camera.fF, pn.plr.camera.fN);
	}
}
