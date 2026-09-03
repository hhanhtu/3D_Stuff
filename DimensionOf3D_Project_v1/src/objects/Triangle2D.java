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
	public String 	  parent;
	public int 		  id;
	
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
		id	   = -1;
		
		clr		   = Color.WHITE;
		LightLevel = 0;
	}
	
	public static Triangle2D getTrianglesFromClipResult(HashMap<String, Vector> c, int j)
	{
		Triangle2D tri = (Triangle2D)c.get("Triangles").get(j);
		
		return tri;
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
				
				this.clr = new Color((int)(r/pn.light.DARKNESS		* Math.abs(this.LightLevel)),
									(int)(g/pn.light.DARKNESS		* Math.abs(this.LightLevel)),
									(int)(b/(pn.light.DARKNESS)		* Math.abs(this.LightLevel)));
				
				if(pn.light.state == "night")
					this.clr = Color.BLACK;
			}
		}
	}
	
	public void draw(Graphics g, Color clr)
	{
		g.setColor(clr);
		
		g.drawLine((int)this.p[0].x, (int)this.p[0].y, (int)this.p[1].x, (int)this.p[1].y);
		g.drawLine((int)this.p[1].x, (int)this.p[1].y, (int)this.p[2].x, (int)this.p[2].y);
		g.drawLine((int)this.p[2].x, (int)this.p[2].y, (int)this.p[0].x, (int)this.p[0].y);
	}
	
	public void fill(Graphics g)
	{
		g.setColor(this.clr);
		
		pol.reset();
		
		pol.addPoint((int)this.p[0].x, (int)this.p[0].y);
		pol.addPoint((int)this.p[1].x, (int)this.p[1].y);
		pol.addPoint((int)this.p[2].x, (int)this.p[2].y);
		
		g.fillPolygon(pol);
	}
	
	public static void draw(Graphics g, Triangle2D tri, Color clr)
	{
		g.setColor(clr);
		
		g.drawLine((int)tri.p[0].x, (int)tri.p[0].y, (int)tri.p[1].x, (int)tri.p[1].y);
		g.drawLine((int)tri.p[1].x, (int)tri.p[1].y, (int)tri.p[2].x, (int)tri.p[2].y);
		g.drawLine((int)tri.p[2].x, (int)tri.p[2].y, (int)tri.p[0].x, (int)tri.p[0].y);
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
