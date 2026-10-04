package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Vector;

import javax.imageio.ImageIO;

import main.AssetManager;
import main.Panel;
import variables.Matrix4x4;
import variables.Vector2D;
import variables.Vector3D;

@SuppressWarnings("rawtypes")
public class Triangle2D
{
	public Vector3D[] p;
	public Vector2D[] t;
	
	public Color	  clr;
	public double	  LightLevel;
	public boolean	  Shading;
	public MeshPart	  parent;
	
	public Vector3D   centroid;
	public Vector3D   normal;
	
	public Triangle2D triProjected  ;
	public Triangle2D triTransformed;
	public Triangle2D triView 	  	;
	
	private static Polygon pol = new Polygon();
	
	public Triangle2D(Vector3D p1, Vector3D p2, Vector3D p3, Vector2D t1, Vector2D t2, Vector2D t3)
		{set(p1, p2, p3, t1, t2, t3);}
	public Triangle2D(Vector3D p1, Vector3D p2, Vector3D p3)
		{set(p1, p2, p3, null, null, null);}
	public Triangle2D()
		{set(null, null, null, null, null, null);}
	
	private void set(Vector3D p1, Vector3D p2, Vector3D p3, Vector2D t1, Vector2D t2, Vector2D t3)
	{
		p	 = new Vector3D[3];
		t	 = new Vector2D[3];
		
		p[0] = p1; t[0] = t1; if(t1 == null) t[0] = Vector2D.zero; if(p1 == null) p[0] = Vector3D.zero;
		p[1] = p2; t[1] = t2; if(t2 == null) t[1] = Vector2D.zero; if(p2 == null) p[1] = Vector3D.zero;
		p[2] = p3; t[2] = t3; if(t3 == null) t[2] = Vector2D.zero; if(p3 == null) p[2] = Vector3D.zero;
		
		parent = null;
		
		normal	   = Vector3D.zero;
		centroid   = Vector3D.zero;
		clr		   = Color.WHITE;
		LightLevel = 0;
	}
	
	public void createProjected()
	{
		triProjected   = new Triangle2D();
		triTransformed = new Triangle2D();
		triView 	   = new Triangle2D();
	}
	public void clearProjected()
	{triProjected.clearPoint(); triTransformed.clearPoint(); triView.clearPoint();}
	
	public void clearPoint()
	{p[0] = Vector3D.zero; p[1] = Vector3D.zero; p[2] = Vector3D.zero;}
	
	public void setPoint(Vector3D p1, Vector3D p2, Vector3D p3)
	{p[0] = p1; p[1] = p2; p[2] = p3;}
	
	public static Triangle2D getTrianglesFromClipResult(HashMap<String, Vector> c, int j)
	{
		return (Triangle2D)c.get("Triangles").get(j);
	}
	
	public void updateCentroid()
	{
		centroid.x = (this.p[0].x + this.p[1].x + this.p[2].x)/3;
		centroid.y = (this.p[0].y + this.p[1].y + this.p[2].y)/3;
		centroid.z = (this.p[0].z + this.p[1].z + this.p[2].z)/3;
	}
	
	public static Vector3D centroid(Triangle2D triangle)
	{
		return new Vector3D(
				(triangle.p[0].x + triangle.p[1].x + triangle.p[2].x)/3,
				(triangle.p[0].y + triangle.p[1].y + triangle.p[2].y)/3,
				(triangle.p[0].z + triangle.p[1].z + triangle.p[2].z)/3
				);
	}
	
	public void SetColor(Panel pn)
	{
		int r = this.clr.getRed();
		int g = this.clr.getGreen();
		int b = this.clr.getBlue();
		
		int nR = (int)(r/pn.light.DARKNESS		*this.LightLevel);
		int nG = (int)(g/pn.light.DARKNESS		*this.LightLevel);
		int nB = (int)(b/(pn.light.DARKNESS/2)	*this.LightLevel);
		
		int dR = (int)(r*this.LightLevel);
		int dG = (int)(g*this.LightLevel);
		int dB = (int)(b*this.LightLevel);
		
		if(this.Shading)
		{
			// make a color library please :: check color in library --IFNOT-> create new color and store. next time check again then use the color in library
			try
			{
				this.clr = new Color(dR, dG, dB);
				
				if(pn.light.state == "night")
					this.clr = new Color(nR, nG, nB);
			} catch(Exception e)
			{
//				tri.clr = Color.BLACK;
				
				this.clr = new Color((int)(r/pn.light.DARKNESS	* Math.abs(this.LightLevel)),
									 (int)(g/pn.light.DARKNESS	* Math.abs(this.LightLevel)),
									 (int)(b/pn.light.DARKNESS	* Math.abs(this.LightLevel)));
				
				if(pn.light.state == "night")
					this.clr = Color.BLACK;
			}
		}
	}
	
	public void draw(Graphics g)
	{
		g.setColor(Color.lightGray);
		if(clr.getRed() >= 10 && clr.getBlue() >= 10 && clr.getGreen() >= 10)
			g.setColor(Color.darkGray);
		
		g.drawLine((int)p[0].x, (int)p[0].y, (int)p[1].x, (int)p[1].y);
		g.drawLine((int)p[1].x, (int)p[1].y, (int)p[2].x, (int)p[2].y);
		g.drawLine((int)p[2].x, (int)p[2].y, (int)p[0].x, (int)p[0].y);
		
//		g.setColor(Color.BLUE);
//		g.fillOval((int)Triangle2D.centroid(this).x - 1, (int)Triangle2D.centroid(this).y - 1, 2, 2);
	}
	
	public void fill(Graphics g)
	{
		g.setColor(clr);
		
		pol.reset();
		
		pol.addPoint((int)p[0].x, (int)p[0].y);
		pol.addPoint((int)p[1].x, (int)p[1].y);
		pol.addPoint((int)p[2].x, (int)p[2].y);
		
		if(pol.intersects(Panel.gui.gui[0].frame))
			Panel.mMotion.hoveringObject = parent.name;
		
		g.fillPolygon(pol);
	}
	
	public static void draw(Graphics g, Triangle2D tri, Color clr)
	{
		tri.updateCentroid();
		
		g.setColor(clr);
		
		g.drawLine((int)tri.p[0].x, (int)tri.p[0].y, (int)tri.p[1].x, (int)tri.p[1].y);
		g.drawLine((int)tri.p[1].x, (int)tri.p[1].y, (int)tri.p[2].x, (int)tri.p[2].y);
		g.drawLine((int)tri.p[2].x, (int)tri.p[2].y, (int)tri.p[0].x, (int)tri.p[0].y);
		
		g.setColor(Color.BLUE);
		g.drawOval((int)tri.centroid.x - 2, (int)tri.centroid.y - 2, 5, 5);
	}
	
	public static void fill(Graphics g, Triangle2D tri)
	{
		g.setColor(tri.clr);
		
		pol.reset();
		
		pol.addPoint((int)tri.p[0].x, (int)tri.p[0].y);
		pol.addPoint((int)tri.p[1].x, (int)tri.p[1].y);
		pol.addPoint((int)tri.p[2].x, (int)tri.p[2].y);
		
		g.fillPolygon(pol);
	}
}
