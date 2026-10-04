package entity;

import inputSystem.InputHandler;
import main.Camera;
import main.Panel;
import main.Physic;
import variables.Attribute;
import variables.Theta;
import variables.Vector3D;

public class Entity extends Attribute
{
	public class Direction
	{
		public Vector3D unit = Vector3D.zero;
		public String 	dir	 = "front";
		
		public Entity entt;
		
		public Direction(Entity entt)
		{
			this.entt = entt;
		}
		
		public void update()
		{
			if(input.keyCode.indexOf("W") != -1)
				{dir = "front";	 unit = Vector3D.look;}
			if(input.keyCode.indexOf("S") != -1)
				{dir = "back";	 unit = Vector3D.back;}
			if(input.keyCode.indexOf("A") != -1)
				{dir = "left";	 unit = Vector3D.left;}
			if(input.keyCode.indexOf("D") != -1)
				{dir = "right";	 unit = Vector3D.right;}
			if(input.keyCode.indexOf("SPACE") != -1)
				{dir = "up";	 unit = Vector3D.up;}
			if(input.keyCode.indexOf("Shift") != -1)
				{dir = "down";	 unit = Vector3D.down;}
			if(input.keyCode.indexOf("W") != -1 && input.keyCode.indexOf("A") != -1)
				{dir = "front-left";	unit = Vector3D.FL;}
			if(input.keyCode.indexOf("W") != -1 && input.keyCode.indexOf("D") != -1)
				{dir = "front-right";	unit = Vector3D.FR;}
			if(input.keyCode.indexOf("S") != -1 && input.keyCode.indexOf("A") != -1)
				{dir = "back-left";		unit = Vector3D.BL;}
			if(input.keyCode.indexOf("S") != -1 && input.keyCode.indexOf("D") != -1)
				{dir = "back-right";	unit = Vector3D.BR;}
			if(input.keyCode.indexOf("W") != -1 && input.keyCode.indexOf("SPACE") != -1)
				{dir = "front-up";		unit = Vector3D.FU;}
			if(input.keyCode.indexOf("W") != -1 && input.keyCode.indexOf("Shift") != -1)
				{dir = "front-down";	unit = Vector3D.FD;}
			if(input.keyCode.indexOf("S") != -1 && input.keyCode.indexOf("SPACE") != -1)
				{dir = "back-up";		unit = Vector3D.BU;}
			if(input.keyCode.indexOf("S") != -1 && input.keyCode.indexOf("Shift") != -1)
				{dir = "back-down";		unit = Vector3D.BD;}
		}
	}
	
	public Panel		pn;
	public Camera 		camera;
	public InputHandler input;
	public Physic 		phy;
	public Direction 	direction = new Direction(this);
	
	public Vector3D position = Vector3D.zero;
	public Theta 	rotation = new Theta(0, 0, 0);
	
	public double height;
	public double weight;
	public double speed;
	
}
