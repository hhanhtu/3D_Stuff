package entity;

import inputSystem.InputHandler;
import main.Camera;
import main.Collision;
import main.Panel;
import main.Physic;
import variables.Theta;
import variables.Vector3D;

public class Entity
{
	public Panel		pn;
	public Camera 		camera;
	public InputHandler input;
	public Physic 		phy;
	public Collision	col;
	
	public Vector3D position = Vector3D.zero;
	public Theta 	rotation = new Theta(0, 0, 0);
	
	public double height;
	public double weight;
	public double speed;
	
}
