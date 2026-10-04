package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;

import entity.Player;
import objects.MeshPart;
import objects.SuperObject;
import variables.Vector2D;
import variables.Vector3D;

public class Physic {
	public Vector3D velocity = Vector3D.zero;
	
	public Player plr;
	public MeshPart obj;
	
	public Physic(Player plr)
	{set(plr, null);}
	public Physic(MeshPart obj)
	{set(null, obj);}
	
	private void set(Player plr, MeshPart obj)
	{
		if(plr != null)
		{
			this.plr = plr;
		}
		if(obj != null)
		{
			this.obj = obj;
		}
	}
	
	public void linearVelocity() // rename soon
	{
		if(plr != null)
		{
			if(plr.isCollision.D == 0)
			{
				this.velocity = Vector3D.zero;
			}
		}
		
		if(!this.velocity.equals(Vector3D.zero))
			plr.position = plr.position.Add(this.velocity);
	}
	
	public static void checkEntityCollision(Player plr)
	{
		Vector3D forward 	= plr.position.Add(Vector3D.look.Mul(plr.camera.face.look .z * plr.speed).Add(Vector3D.right.Mul(plr.camera.face.look .x * plr.speed)));
		Vector3D backward 	= plr.position.Sub(Vector3D.look.Mul(plr.camera.face.look .z * plr.speed).Add(Vector3D.right.Mul(plr.camera.face.look .x * plr.speed)));
		Vector3D leftward 	= plr.position.Add(Vector3D.look.Mul(plr.camera.face.right.z * plr.speed).Add(Vector3D.right.Mul(plr.camera.face.right.x * plr.speed)));
		Vector3D rightward 	= plr.position.Sub(Vector3D.look.Mul(plr.camera.face.right.z * plr.speed).Add(Vector3D.right.Mul(plr.camera.face.right.x * plr.speed)));
		Vector3D upward 	= plr.position.Add(Vector3D.up	.Mul(plr.camera.face.up   .y * plr.speed));
		Vector3D downward 	= plr.position.Sub(Vector3D.up	.Mul(plr.camera.face.up   .y * plr.speed));
		
		for(MeshPart m: plr.pn.obj.obj)
		{
			if(m.collision)
			{
				double sX = m.size().x;
				double sY = m.size().y;
				double sZ = m.size().z;
				if(m.hitbox != null)
				{
					sX = m.hitbox.x/2;
					sY = m.hitbox.y/2;
					sZ = m.hitbox.z/2;
				}
				
				double pX = m.offset.x + sX; double nX = m.offset.x - sX;
				double pY = m.offset.y + sY; double nY = m.offset.y - sY;
				double pZ = m.offset.z + sZ; double nZ = m.offset.z - sZ;
			
				if((rightward	.x - Panel.px/4 < pX*(1+1/8)
				&&  rightward	.z - Panel.px/4 < pZ*(1+1/8)
				&&  rightward	.x + Panel.px/4 > nX*(1+1/8)
				&&  rightward	.z + Panel.px/4 > nZ*(1+1/8))
				
				&& (leftward	.x - Panel.px/4 < pX*(1+1/8)
				&&  leftward	.z - Panel.px/4 < pZ*(1+1/8)
				&&  leftward	.x + Panel.px/4 > nX*(1+1/8)
				&&  leftward	.z + Panel.px/4 > nZ*(1+1/8))
				
				&& (forward		.x - Panel.px/4 < pX
				&&  forward		.z - Panel.px/4 < pZ
				&&  forward		.x + Panel.px/4 > nX
				&&  forward		.z + Panel.px/4 > nZ)
				                                                
				&& (backward	.x - Panel.px/4 < pX
				&&  backward	.z - Panel.px/4 < pZ
				&&  backward	.x + Panel.px/4 > nX
				&&  backward	.z + Panel.px/4 > nZ))
				{
					if(downward	.y - plr.height <= pY
					&& downward	.y + plr.height >= pY)
						plr.isCollision.D = 0;
					if(upward	.y + plr.height >= nY
					&& upward	.y - plr.height <= pY)
						plr.isCollision.U = 0;
				}
				
				if(downward	.y - plr.height/2 <= m.offset.y + sY
				&& upward	.y + plr.height/2 >= m.offset.y - sY)
				{
					if(rightward	.x - Panel.px/4 < pX*(1+1/8)
					&& rightward	.z - Panel.px/4 < pZ*(1+1/8)
					&& rightward	.x + Panel.px/4 > nX*(1+1/8)
					&& rightward	.z + Panel.px/4 > nZ*(1+1/8))
					{
						plr.isCollision.R = 0;
					} else plr.isCollision.R = 1; 
					
					if(leftward		.x - Panel.px/4 < pX*(1+1/8)
					&& leftward		.z - Panel.px/4 < pZ*(1+1/8)
					&& leftward		.x + Panel.px/4 > nX*(1+1/8)
					&& leftward		.z + Panel.px/4 > nZ*(1+1/8))
					{
						plr.isCollision.L = 0; 
					} else plr.isCollision.L = 1;
					
					if(forward		.x - Panel.px/4 < pX
					&& forward		.z - Panel.px/4 < pZ
					&& forward		.x + Panel.px/4 > nX
					&& forward		.z + Panel.px/4 > nZ)
					{                              
						plr.isCollision.F = 0;     
					} else plr.isCollision.F = 1;  
					                               
					if(backward		.x - Panel.px/4 < pX
					&& backward		.z - Panel.px/4 < pZ
					&& backward		.x + Panel.px/4 > nX
					&& backward		.z + Panel.px/4 > nZ)
					{
						plr.isCollision.B = 0;
					} else plr.isCollision.B = 1;
				}
				
				if(plr.direction.dir.contains("front")	 && plr.isCollision.F == 0)
				{
					plr.isCollision.colliding = true;
					break;
				}
				if(plr.direction.dir.contains("back")	 && plr.isCollision.B == 0)
				{
					plr.isCollision.colliding = true;
					break;
				}
				if(plr.direction.dir.contains("right")	 && plr.isCollision.R == 0)
				{
					plr.isCollision.colliding = true;
					break;
				}
				if(plr.direction.dir.contains("left")	 && plr.isCollision.L == 0)
				{
					plr.isCollision.colliding = true;
					break;
				}
			}
		}
	}
}
