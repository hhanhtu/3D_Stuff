package entity;

import main.Camera;
import main.Panel;
import variables.Vector3D;

public class Player extends Entity
{
	
	public Player(Panel pn)
	{
		this.pn		= pn;
		this.camera = new Camera(pn);
		this.input	= pn.input;
		
		this.height = 2;
	}
	
	public void update()
	{
		UpdateUserInput();
		
		camera.update();
	}
	
	public void UpdateUserInput()
	{
		Vector3D forward = new Vector3D(camera.face.get("look").x * .25, 0, camera.face.get("look").z * .25);
		Vector3D sideward= new Vector3D(camera.face.get("right").x * .25, 0, camera.face.get("right").z * .25);
		Vector3D upward  = Vector3D.Mul(camera.face.get("up")	, .25);
		
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
			camera.rotation.y -= Math.toRadians(1);
		if(input.keyCode.indexOf("L") != -1)
			camera.rotation.y += Math.toRadians(1);
		
		if(input.keyCode.indexOf("I") != -1)
			camera.rotation.x += Math.toRadians(1);
		if(input.keyCode.indexOf("K") != -1)
			camera.rotation.x -= Math.toRadians(1);
	}
	
}
