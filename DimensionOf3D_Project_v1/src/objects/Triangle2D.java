package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

import variables.Vector3D;

public class Triangle2D
{
	public Vector3D[] p;
	public Color clr;
	
	public Triangle2D(Vector3D p1, Vector3D p2, Vector3D p3)
	{
		p = new Vector3D[3];
		p[0] = p1;
		p[1] = p2;
		p[2] = p3;
	}
	
	public static void draw(Graphics2D g2, Triangle2D tri, Color clr)
	{
		g2.setStroke(new BasicStroke(1f));
		g2.setColor(clr);
		
		g2.drawLine((int)tri.p[0].x, (int)tri.p[0].y, (int)tri.p[1].x, (int)tri.p[1].y);
		g2.drawLine((int)tri.p[1].x, (int)tri.p[1].y, (int)tri.p[2].x, (int)tri.p[2].y);
		g2.drawLine((int)tri.p[2].x, (int)tri.p[2].y, (int)tri.p[0].x, (int)tri.p[0].y);
	}
	
	public static void fill(Graphics2D g2, Triangle2D tri)
	{
		g2.setColor(tri.clr);
		
		Polygon pol = new Polygon();
		pol.addPoint((int)tri.p[0].x, (int)tri.p[0].y);
		pol.addPoint((int)tri.p[1].x, (int)tri.p[1].y);
		pol.addPoint((int)tri.p[2].x, (int)tri.p[2].y);
		
		g2.fill(pol);
	}
}
