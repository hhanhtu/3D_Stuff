package objects;

import java.awt.Color;
import java.util.Vector;

import main.Panel;
import variables.Vector3D;

public class SuperObject
{
	public Panel 			  pn;
	public Vector<Triangle2D> tris;
	public Color 			  clr;
	public Vector3D 		  offset;
	public Vector3D			  size;
	public double 			  scale;
	public double 			  rX, rY, rZ;
}
