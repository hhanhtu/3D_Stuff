package entity;

import main.Camera;
import main.Panel;
import variables.Vector3D;

public class Player extends Entity
{
	private boolean wireframe = false;
	private int 	wft 	  = 0;
	
	public Player(Panel pn)
	{
		this.pn		= pn;
		this.camera = new Camera(pn);
		this.input	= pn.input;
		
		this.height = 2;
		this.speed  = 1;
	}
	
	public void update()
	{
		speed = pn.FPS/14;
		
		UpdateUserInput();
		
		camera.update();
	}
	
	public void UpdateUserInput()
	{
		if(input.keyCode.indexOf("F3") != -1)
		{
			if(!wireframe)
			{
				wireframe = true;
				
				if(pn.obj.WIREFRAME)
					pn.obj.WIREFRAME = false;
				else if(!pn.obj.WIREFRAME)
					pn.obj.WIREFRAME = true;
			}
		}
		
		if(wireframe)
		{
			wft++;
			
			if(wft >= pn.FPS)
			{
				wft = 0;
				wireframe = false;
			}
		}
		
		Vector3D forward = new Vector3D(camera.face.get("look")	.x * speed, 0, camera.face.get("look") .z * speed);
		Vector3D sideward= new Vector3D(camera.face.get("right").x * speed, 0, camera.face.get("right").z * speed);
		Vector3D upward  = new Vector3D(0, camera.face.get("up").y * speed, 0);
		
		if(input.keyCode.indexOf("SPACE") != -1)
			camera.p =  Vector3D.Sub(camera.p, upward);
		if(input.keyCode.indexOf("Shift") != -1)
			camera.p =  Vector3D.Add(camera.p, upward);;
		
		if(input.keyCode.indexOf("A") != -1)
			camera.p =  Vector3D.Sub(camera.p, sideward);
		if(input.keyCode.indexOf("D") != -1)
			camera.p =  Vector3D.Add(camera.p, sideward);
		
		if(input.keyCode.indexOf("W") != -1)
			camera.p =  Vector3D.Add(camera.p, forward);
		if(input.keyCode.indexOf("S") != -1)
			camera.p =  Vector3D.Sub(camera.p, forward);
		
		if(input.keyCode.indexOf("J") != -1)
			camera.rotation.y -= Math.toRadians(speed);
		if(input.keyCode.indexOf("L") != -1)
			camera.rotation.y += Math.toRadians(speed);
		
		if(input.keyCode.indexOf("I") != -1)
			if(camera.rotation.x + Math.toRadians(speed) <= Math.toRadians(90))
					camera.rotation.x += Math.toRadians(speed);
		if(input.keyCode.indexOf("K") != -1)
			if(camera.rotation.x - Math.toRadians(speed) >= Math.toRadians(-90))
				camera.rotation.x -= Math.toRadians(speed);
	}
	
}
