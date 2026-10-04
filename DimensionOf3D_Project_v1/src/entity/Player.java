package entity;

import java.awt.Color;
import java.awt.Graphics;

import main.Camera;
import main.Panel;
import main.Physic;
import objects.MeshPart;
import objects.SuperObject;
import variables.Matrix4x4;
import variables.Vector3D;

public class Player extends Entity
{
	private boolean wireframe 	= false;
	private int 	wft 	  	= 0;
	
	private boolean hitbox 		= false;
	private int 	hbt 	  	= 0;
	
	private boolean tagname		= false;
	private int 	tnt 	  	= 0;
	
	private boolean godmode 	= false;
	private boolean gmAction 	= false;
	private int 	gmt 	  	= 0;
	
	public class isCollision
	{
		public int 	F = 1;public int 	B = 1;
		public int 	R = 1;public int 	L = 1;
		public int 	U = 1;public int	D = 1;
		
		public boolean colliding = false;
		
		public void reset()
		{
			colliding = false;
			
			F = 1; B = 1;
			R = 1; L = 1;
			U = 1; D = 1;
		}
	}
	
	private boolean jumpRequest = true;
	private int 	jumpCooldown= 0;
	
	public isCollision isCollision = new isCollision();
	
	public Player(Panel pn)
	{
		this.pn		= pn;
		this.camera = new Camera(pn);
		this.phy 	= new Physic(this);
		this.input	= pn.input;
		
		this.height = 1* Panel.px;
		this.speed  = 1;
		
		position = new Vector3D(0, height*4 + Panel.px*3, -Panel.px*10);
//		rotation.x -= Math.toRadians(-85);
		
		update();
	}
	
	public void update()
	{
		speed = 0.5 * Panel.elapseTime;
		phy.velocity = Vector3D.down.Mul(-.55 * Panel.elapseTime);
		
		isCollision.reset();
		direction.update();
		Physic.checkEntityCollision(this);
		
		UpdateUserInput();
		if(!gmAction)
			phy.linearVelocity();
		
		if(isCollision.F
		+  isCollision.B 
		+  isCollision.R
		+  isCollision.L
		== 0)
			position.y += 1;
		
		camera.update(this);
	}
	
	public void UpdateUserInput()
	{
		if(input.keyCode.contains("F3"))
		{
			if(input.rkCode.contains("B"))
			{
				if(!hitbox)
				{
					hitbox = true;
					
					if(pn.obj.HITBOX)
						pn.obj.HITBOX = false;
					else if(!pn.obj.HITBOX)
						pn.obj.HITBOX = true;
				}
				
				return;
			} else if(input.rkCode.contains("T"))
			{
				if(!tagname)
				{
					tagname = true;
					
					if(pn.obj.TAGNAME)
						pn.obj.TAGNAME = false;
					else if(!pn.obj.TAGNAME)
						pn.obj.TAGNAME = true;
				}
				return;
			} else if(input.rkCode.contains("G"))
			{
				if(!godmode)
				{
					godmode = true;
					
					if(gmAction)
						gmAction = false;
					else if(!gmAction)
						gmAction = true;
				}
				return;
			} else
			{
				if(!wireframe)
				{
					wireframe = true;
					
					if(pn.obj.WIREFRAME)
						pn.obj.WIREFRAME = false;
					else if(!pn.obj.WIREFRAME)
						pn.obj.WIREFRAME = true;
				}
				return;
			}
		}
		
		if(wireframe)
		{
			wft++;
			
			if(wft >= Panel.FPS/10/4)
			{
				wft = 0;
				wireframe = false;
			}
		}
		if(hitbox)
		{
			hbt++;
			
			if(hbt >= Panel.FPS/10/4)
			{
				hbt = 0;
				hitbox = false;
			}
		}
		if(tagname)
		{
			tnt++;
			
			if(tnt >= Panel.FPS/10/4)
			{
				tnt = 0;
				tagname = false;
			}
		}
		if(godmode)
		{
			gmt++;
			
			if(gmt >= Panel.FPS/10/4)
			{
				gmt = 0;
				godmode = false;
			}
		}
		
		if(input.keyCode.contains("Ctrl"))
		{
			if(input.rkCode.contains("ra"))
			{
				Panel.tick++;
				System.out.println(Panel.tick);
			}
			if(input.rkCode.contains("la"))
			{
				Panel.tick--;
				System.out.println(Panel.tick);
			}
		}
		
		Vector3D forward	 = Vector3D.look.Mul(camera.face.look .z * speed).Add(Vector3D.right.Mul(camera.face.look .x * speed));
		Vector3D rightward	 = Vector3D.look.Mul(camera.face.right.z * speed).Add(Vector3D.right.Mul(camera.face.right.x * speed));
		
		if(input.keyCode.indexOf("SPACE") != -1)
			if(gmAction)
			{
				position.y += height;				
			} else
			{
				if(jumpRequest)
				{
					jumpRequest = false;
					jumpCooldown= 0;
					
					for(int a = 1; a <= 2; a++)
					{
						position.y += height/1.5 * a * isCollision.U;
					}
				}
			}
		if(input.keyCode.indexOf("Shift") != -1)
			position.y -= 7;
		
		if(!isCollision.colliding)
		{
			if(input.keyCode.indexOf("A") != -1)
				position =  Vector3D.Add(position, rightward);
			if(input.keyCode.indexOf("D") != -1)
				position =  Vector3D.Sub(position, rightward);
			
			if(input.keyCode.indexOf("W") != -1)
			{
				position =  Vector3D.Add(position, forward);
			}
			if(input.keyCode.indexOf("S") != -1)
			{
				position =  Vector3D.Sub(position, forward);
			}
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
		
		if(!jumpRequest)
		{
			jumpCooldown++;
			
			if(jumpCooldown >= Panel.FPS/10)
				jumpRequest = true;
		}
		
		input.clearReleasedKey();
	}
}
