package main;

import java.util.HashMap;

import entity.Player;
import variables.Matrix4x4;
import variables.Theta;
import variables.Vector3D;

public class Camera
{
	public Vector3D p;
	public Theta 	rotation;
	
	public class Face
	{
		public Vector3D look, right, up;
		
		public Face()
		{
			look	 = Vector3D.look;
			right	 = Vector3D.right;
			up		 = Vector3D.up;
		}
	}
	
	private Vector3D 	vUp;
	private Vector3D 	vT ;
	
	public  Matrix4x4 	vCam = new Matrix4x4();
	private Matrix4x4 	mCam = new Matrix4x4();
	
	public Vector3D viewOffset = new Vector3D(1, 1, 0);
	public Vector3D screenAdjust;
	
	public Face face;
	
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
		
		face = new Face();
	}
	
	public void update(Player user)
	{
		p = user.position;
		
		AR		 = Panel.root.panel[1]/Panel.root.panel[0];
		fFovRad  = Math.toRadians(fFov);
		
		rotation.updateRotation();
		
		vUp = Vector3D.up;
		
		Matrix4x4 matRotXYZ = rotation.matRotX.Mul(rotation.matRotY.Mul(rotation.matRotZ));
		
//		face.put("look"	, matRotXYZ.MultiplyMatrixVector(Vector3D.look	));
//		face.put("up"	, matRotXYZ.MultiplyMatrixVector(Vector3D.up	));
//		face.put("right", matRotXYZ.MultiplyMatrixVector(Vector3D.right	));
		
		face.look 	= matRotXYZ.MultiplyMatrixVector(Vector3D.look	);
		face.up 	= matRotXYZ.MultiplyMatrixVector(Vector3D.up	);
		face.right	= matRotXYZ.MultiplyMatrixVector(Vector3D.right	);
		
		vT   = p.Add(face.look);
		
		mCam = Matrix4x4.PointAt(p, vT, vUp);
		vCam = mCam.quickInvert();
	}
}
