package main;

import java.util.HashMap;

import variables.Matrix4x4;
import variables.Theta;
import variables.Vector3D;

public class Camera
{
	public Vector3D p;
	public Theta 	rotation;
	
	public HashMap<String, Vector3D> face = new HashMap<>();
	
	private Vector3D 	vUp;
	private Vector3D 	vT ;
	
	public  Matrix4x4 	vCam = new Matrix4x4();
	private Matrix4x4 	mCam = new Matrix4x4();
	
	public Vector3D viewOffset = new Vector3D(1, 1, 0);
	public Vector3D screenAdjust;
	
	public Panel pn;
	
	public static double fN 	 = 0.1;
	public static double fF 	 = 1000;
	public static double AR 	 = 0;
	public static double fFov	 = 90;
	public static double fFovRad= Math.toRadians(fFov);
	
	public Camera(Panel pn)
	{
		this.pn = pn;
		
		p		 = Vector3D.zero;
		rotation = new Theta	(0, 0, 0);
		
		screenAdjust = new Vector3D(Panel.root.panel[0]/2, Panel.root.panel[1]/2, 0);
		
		face.put("look"	, Vector3D.look	);
		face.put("up"	, Vector3D.up	);
		face.put("right", Vector3D.right);
	}
	
	public void update()
	{
		AR		 = Panel.root.panel[1]/Panel.root.panel[0];
		fFovRad  = Math.toRadians(fFov);
		
		Theta.updateRotation(rotation);
		
		vUp = Vector3D.up;
		
		Matrix4x4 camXYZ = Matrix4x4.Mul(Matrix4x4.Mul(rotation.matRotX, rotation.matRotY), rotation.matRotZ);
		
		face.put("look"	, Matrix4x4.MultiplyMatrixVector(camXYZ, Vector3D.look	));
		face.put("up"	, Matrix4x4.MultiplyMatrixVector(camXYZ, Vector3D.up	));
		face.put("right", Matrix4x4.MultiplyMatrixVector(camXYZ, Vector3D.right	));
		
		vT   = Vector3D.Add(p, face.get("look"));
		
		mCam = Matrix4x4.PointAt(p, vT, vUp);
		vCam = Matrix4x4.quickInvert(mCam);
	}
}
