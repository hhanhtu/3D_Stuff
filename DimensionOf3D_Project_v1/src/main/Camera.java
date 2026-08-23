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
	
	public Panel pn;
	
	public double fN 	 = 0.1;
	public double fF 	 = 1000;
	public double AR 	 = 0;
	public double fFov	 = 90;
	public double fFovRad= Math.toRadians(fFov);
	
	public Camera(Panel pn)
	{
		this.pn = pn;
		
		p		 = new Vector3D (0, 0, 0);
		rotation = new Theta	(0, 0, 0);
		
		face.put("look"	, new Vector3D(0, 0, 1));
		face.put("up"	, new Vector3D(0, 1, 0));
		face.put("right", new Vector3D(1, 0, 0));
	}
	
	public void update()
	{
		AR		 = pn.root.panel[1]/pn.root.panel[0];
		fFovRad  = Math.toRadians(fFov);
		
		Theta.updateRotation(rotation);
		
		vUp = new Vector3D(0, 1, 0);
		
		Matrix4x4 camXYZ = Matrix4x4.Mul(Matrix4x4.Mul(rotation.matRotX, rotation.matRotY), rotation.matRotZ);
		
		face.put("look"	, Matrix4x4.MultiplyMatrixVector(camXYZ, new Vector3D(0, 0, 1)));
		face.put("up"	, Matrix4x4.MultiplyMatrixVector(camXYZ, new Vector3D(0, 1, 0)));
		face.put("right", Matrix4x4.MultiplyMatrixVector(camXYZ, new Vector3D(1, 0, 0)));
		
		vT   = Vector3D.Add(p, face.get("look"));
		
		mCam = Matrix4x4.PointAt(p, vT, vUp);
		vCam = Matrix4x4.quickInvert(mCam);
	}
}
