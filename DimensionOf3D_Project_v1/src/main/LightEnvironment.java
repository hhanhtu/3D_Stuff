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
		public int h, m, s;
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
	public Clock time;
	
	public String transition = "sun rise";
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
		
		time = new Clock(6, 0, 0);
		
		sun = new MeshPart();
		sun.LoadFromObjectFile("ball_lowQuality");
		sun.offset = direction;
		sun.clr = new Color(255, 255, 150);
		sun.scale = 30;
		sun.name = "Sun";
		sun.collision = false;
		sun.BRIGHT = true;
		
		moon = new MeshPart();
		moon.LoadFromObjectFile("ball_lowQuality");
		moon.offset = direction;
		moon.clr = new Color(200, 200, 255);
		moon.scale = 30;
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
		if(time.s >= 60)
		{
			time.s = 0;
			time.m++;
		}
		if(time.m >= 60)
		{
			time.m = 0;
			time.h++;
		}
		if(time.h > 24)
			time.h = 1;
		
		rX = (time.h * 3600.0 + time.m * 60.0 + time.s)*360.0/(24.0*60.0*60.0) - 90;
		
		if(time.h <= 5 || time.h >= 17)
			transition = "sun set";
		else
			transition = "sun rise";
		
		if(time.h <= 5 || time.h >= 18)
			state = "night";
		else
			state = "day";
		
		rotation.x = Math.toRadians(rX);
		rotation.y = Math.toRadians(rY);
		rotation.z = Math.toRadians(rZ);
		
		rotation.updateRotation();
		
		Matrix4x4 matRotXYZ = rotation.matRotX.Mul(rotation.matRotY.Mul(rotation.matRotZ));
		direction = matRotXYZ.MultiplyMatrixVector(anchoredPoint);
		
		sun.offset = direction;
	}
	
}
