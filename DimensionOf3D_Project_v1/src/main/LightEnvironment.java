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
		direction	  = new Vector3D(0, 0, -500);
		anchoredPoint = new Vector3D(0, 0, -500);
		
		rX = 0;
		rY = 0;
		rZ = 0;
		
		sun = new MeshPart();
		sun.LoadFromObjectFile("ball_lowQuality");
		sun.offset = direction;
		sun.clr = new Color(255, 255, 150);
		sun.scale = 15;
		sun.name = "Sun";
		sun.collision = false;
		sun.BRIGHT = true;
		
		moon = new MeshPart();
		moon.LoadFromObjectFile("ball_lowQuality");
		moon.offset = direction;
		moon.clr = new Color(75, 75, 255);
		moon.scale = 15;
		moon.name = "Moon";
		moon.collision = false;
		moon.BRIGHT = true;
		
		sun.configuration = () -> {
			sun.offset = direction;
		};
		
		moon.configuration = () -> {
			moon.offset = direction.Mul(-1);
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
		
		rotation.updateRotation();
		
		Matrix4x4 matRotXYZ = rotation.matRotX.Mul(rotation.matRotY.Mul(rotation.matRotZ));
		direction = matRotXYZ.MultiplyMatrixVector(anchoredPoint);
		
		sun.offset = direction;
	}
	
}
