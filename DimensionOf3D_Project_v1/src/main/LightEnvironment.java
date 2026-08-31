package main;

import java.awt.Color;
import java.util.HashMap;
import java.util.Vector;

import objects.MeshPart;
import variables.Vector3D;
import variables.Matrix4x4;
import variables.Theta;

public class LightEnvironment {
	public class Clock
	{
		public double h, m, s;
		public Clock(int h, int m, int s)
		{
			this.h = h;
			this.m = m;
			this.s = s;
		}
	}
	
	public  Vector3D direction;
	public  Vector3D anchoredPoint;
	private Theta	 rotation ;
	
	public double rX, rY, rZ;
	
	public String state = "day";
	public double DARKNESS = 5;
	
	private MeshPart sun;
	private MeshPart moon;
	
	public LightEnvironment(Panel pn)
	{
		rotation  	  = new Theta	(0, 0, 0);
		direction	  = new Vector3D(0, 0, -150);
		anchoredPoint = new Vector3D(0, 0, -150);
		
		rX = 0;
		rY = 0;
		rZ = 0;
		
		sun = new MeshPart();
		sun.LoadFromObjectFile("ball_lowQuality");
		sun.offset = direction;
		sun.clr = new Color(255, 255, 150);
		sun.scale = 5;
		sun.name = "Sun";
		
		moon = new MeshPart();
		moon.LoadFromObjectFile("ball_lowQuality");
		moon.offset = direction;
		moon.clr = new Color(75, 75, 255);
		moon.scale = 5;
		moon.name = "Moon";
		
		sun.configuration = () -> {
			sun.offset = direction;
		};
		
		moon.configuration = () -> {
			moon.offset = Vector3D.Mul(direction, -1);
		};
		
		Vector<MeshPart> m = new Vector<>();
		m.add(sun);
		m.add(moon);
		pn.obj.mesh.put("Light", m);
	}
	
	public void update()
	{
		state = "day";
		
		if(Math.abs(rX) >= 180)
			state = "night";
		if(Math.abs(rX) >= 360)
		{
			state = "day";
			rX = 0;
		}
		
		rotation.x = Math.toRadians(rX);
		rotation.y = Math.toRadians(rY);
		rotation.z = Math.toRadians(rZ);
		
		Theta.updateRotation(rotation);
		
		Matrix4x4 matRotXYZ = Matrix4x4.Mul(Matrix4x4.Mul(rotation.matRotX, rotation.matRotY), rotation.matRotZ);
		direction = Matrix4x4.MultiplyMatrixVector(matRotXYZ, anchoredPoint);
		
		sun.offset = direction;
	}
	
}
