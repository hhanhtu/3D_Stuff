package main;

import variables.Theta;
import variables.Vector3D;

public class Camera
{
	public Vector3D p;
	public Vector3D look;
	public Theta 	rotation;
	
	public Camera()
	{
		p	 = new Vector3D(0, 0, 0);
		look = new Vector3D(0, 0, 1);
		
		rotation = new Theta(0, 0, 0);
	}
}
