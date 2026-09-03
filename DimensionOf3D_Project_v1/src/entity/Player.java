package entity;

import java.awt.Color;
import java.awt.Graphics;

import main.Camera;
import main.Collision;
import main.Panel;
import main.Physic;
import objects.MeshPart;
import objects.SuperObject;
import variables.Matrix4x4;
import variables.Vector3D;

public class Player extends Entity
{
	private boolean wireframe = false;
	private int 	wft 	  = 0;
	
	private int 	isFCollision = 1;
	private int 	isRCollision = 1;
	private int 	isUCollision = 1;
	
	public Player(Panel pn)
	{
		this.pn		= pn;
		this.camera = new Camera(pn);
		this.phy 	= new Physic(this);
		this.input	= pn.input;
		
		this.height = 1*pn.px;
		this.speed  = 1;
	}
	
	public void update()
	{
		speed = pn.FPS/30;
		
		phy.velocity = new Vector3D(0, -3, 0);
		
		phy.linearVelocity();
		Collision.checkEntityCollision(this, isFCollision, isRCollision, isUCollision);
		
		UpdateUserInput();
		
		camera.update(this);
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
		
		Vector3D forward = new Vector3D(camera.face.look .x * speed, 0, camera.face.look .z * speed);
		Vector3D sideward= new Vector3D(camera.face.right.x * speed, 0, camera.face.right.z * speed);
//		Vector3D upward  = new Vector3D(0, camera.face.up.y * speed * isUCollision, 0);
		
		if(input.keyCode.indexOf("SPACE") != -1)
			position.y += speed;
		if(input.keyCode.indexOf("Shift") != -1)
			position.y -= speed;
		
		if(input.keyCode.indexOf("A") != -1)
			position =  Vector3D.Add(position, sideward.Mul(Collision.checkEntityCollision(this, 0, 1, 0)));
		if(input.keyCode.indexOf("D") != -1)
			position =  Vector3D.Sub(position, sideward.Mul(Collision.checkEntityCollision(this, 0,-1, 0)));
		
		if(input.keyCode.indexOf("W") != -1)
		{
			position =  Vector3D.Add(position, forward.Mul(Collision.checkEntityCollision(this, 1, 0, 0)));
		}
		if(input.keyCode.indexOf("S") != -1)
		{
			position =  Vector3D.Sub(position, forward.Mul(Collision.checkEntityCollision(this,-1, 0, 0)));
		}
		
		camera.p = position;
		
		if(input.keyCode.indexOf("J") != -1)
			rotation.y += Math.toRadians(speed);
		if(input.keyCode.indexOf("L") != -1)
			rotation.y -= Math.toRadians(speed);
		
		camera.rotation = rotation;
		
		if(input.keyCode.indexOf("I") != -1)
			if(camera.rotation.x - Math.toRadians(speed) >= Math.toRadians(-90))
				camera.rotation.x -= Math.toRadians(speed);
		if(input.keyCode.indexOf("K") != -1)
			if(camera.rotation.x + Math.toRadians(speed) <= Math.toRadians( 90))
				camera.rotation.x += Math.toRadians(speed);
	}
	
}
