package main;

import java.awt.Color;
import java.util.HashMap;
import java.util.Vector;

import objects.MeshPart;
import variables.Vector3D;
import variables.Matrix4x4;
import variables.Theta;

public class LightEnvironment {
	private Panel pn;
	
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
	private Theta	 rotation ;
	
	public double rX, rY, rZ;
	
	public String state = "day";
	public double DARKNESS = 5;
	
	private MeshPart sun;
	private MeshPart moon;
	
	public LightEnvironment(Panel pn)
	{
		this.pn = pn;
		
		rotation  = new Theta	(0, 0, 0);
		direction = new Vector3D(0, 0, -150);
		
		rX = 0;
		rY = 0;
		rZ = 0;
		
		sun = new MeshPart(pn);
		sun.LoadFromObjectFile("ball_lowQuality");
		sun.offset = direction;
		sun.clr = new Color(255, 255, 150);
		sun.scale = 10;
		sun.name = "Sun";
		
		moon = new MeshPart(pn);
		moon.LoadFromObjectFile("ball_lowQuality");
		moon.offset = direction;
		moon.clr = new Color(150, 150, 255);
		moon.scale = 10;
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
		direction = new Vector3D(0, 0, -150);
		
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
		direction = Matrix4x4.MultiplyMatrixVector(matRotXYZ, direction);
		
		sun.offset = direction;
	}
	
}
