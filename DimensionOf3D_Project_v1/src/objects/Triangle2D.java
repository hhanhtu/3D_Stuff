package objects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.HashMap;
import java.util.Vector;

import main.AssetManager;
import main.Panel;
import variables.Matrix4x4;
import variables.Vector3D;

@SuppressWarnings("rawtypes")
public class Triangle2D
{
	public Vector3D[] p;
	public Color	  clr;
	public double	  LightLevel;
	public String 	  parent;
	public int 		  id;
	
	private static Polygon pol = new Polygon();
	
	public Triangle2D(Vector3D p1, Vector3D p2, Vector3D p3)
	{
		p	 = new Vector3D[3];
		p[0] = p1;
		p[1] = p2;
		p[2] = p3;
		
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
	
	public static void SetColor(Triangle2D tri, Panel pn)
	{
		int r = tri.clr.getRed();
		int g = tri.clr.getGreen();
		int b = tri.clr.getBlue();
		
		int nR = (int)(r/pn.light.DARKNESS		*tri.LightLevel);
		int nG = (int)(g/pn.light.DARKNESS		*tri.LightLevel);
		int nB = (int)(b/(pn.light.DARKNESS/2)	*tri.LightLevel);
		
		int dR = (int)(r*tri.LightLevel);
		int dG = (int)(g*tri.LightLevel);
		int dB = (int)(b*tri.LightLevel);
		
		if(tri.parent != "Sun" && tri.parent != "Moon")
		{
			// make a color library please :: check color in library --IFNOT-> create new color and store. next time check again then use the color in library
			
			try
			{
				tri.clr = new Color(dR, dG, dB);
				
				if(pn.light.state == "night")
					tri.clr = new Color(nR, nG, nB);
			} catch(Exception e)
			{
				tri.clr = new Color((int)(r/pn.light.DARKNESS*2		* Math.abs(tri.LightLevel)),
									(int)(g/pn.light.DARKNESS*2		* Math.abs(tri.LightLevel)),
									(int)(b/(pn.light.DARKNESS)		* Math.abs(tri.LightLevel)));
			}
		}
	}
	
	public static void Transformed3Dto2D(Vector<Triangle2D> tris, MeshPart m, AssetManager obj, Vector<Triangle2D> out)
	{
		Panel pn = obj.pn;
		
		for(Triangle2D tri:tris)
		{
			Triangle2D triProjected   = new Triangle2D(Vector3D.zero, Vector3D.zero, Vector3D.zero);
			Triangle2D triTransformed = new Triangle2D(Vector3D.zero, Vector3D.zero, Vector3D.zero);
			Triangle2D triView 	  	  = new Triangle2D(Vector3D.zero, Vector3D.zero, Vector3D.zero);
			
			triTransformed.p[0] = Matrix4x4.MultiplyMatrixVector(SuperObject.matWorld, Vector3D.Add(tri.p[0], Vector3D.Mul(m.anchoredPoint, m.scale)));
			triTransformed.p[1] = Matrix4x4.MultiplyMatrixVector(SuperObject.matWorld, Vector3D.Add(tri.p[1], Vector3D.Mul(m.anchoredPoint, m.scale)));
			triTransformed.p[2] = Matrix4x4.MultiplyMatrixVector(SuperObject.matWorld, Vector3D.Add(tri.p[2], Vector3D.Mul(m.anchoredPoint, m.scale)));
			
			triTransformed.p[0] = Vector3D.Add(Vector3D.Mul(triTransformed.p[0], m.scale), m.offset);
			triTransformed.p[1] = Vector3D.Add(Vector3D.Mul(triTransformed.p[1], m.scale), m.offset);
			triTransformed.p[2] = Vector3D.Add(Vector3D.Mul(triTransformed.p[2], m.scale), m.offset);
			
			Vector3D line1  = Vector3D.Line(triTransformed.p[1], triTransformed.p[0]);
			Vector3D line2  = Vector3D.Line(triTransformed.p[2], triTransformed.p[0]);
			Vector3D normal = Vector3D.Normalise(Vector3D.Cross(line1, line2));
			
			triView.p[0] = Matrix4x4.MultiplyMatrixVector(pn.plr.camera.vCam, triTransformed.p[0]);
			triView.p[1] = Matrix4x4.MultiplyMatrixVector(pn.plr.camera.vCam, triTransformed.p[1]);
			triView.p[2] = Matrix4x4.MultiplyMatrixVector(pn.plr.camera.vCam, triTransformed.p[2]);
			
			m.result =  Vector3D.TriangleClippingInPlane(new Vector3D(0, 0, .05), Vector3D.look, triView);
			
			for(int i = 0; i < (int)m.result.get("n_tris").get(0); i++)
			{
				Triangle2D rTri = (Triangle2D)m.result.get("Triangles").get(i);
				
				triProjected.p[0] = Matrix4x4.MultiplyMatrixVector(SuperObject.mat, rTri.p[0]);
				triProjected.p[1] = Matrix4x4.MultiplyMatrixVector(SuperObject.mat, rTri.p[1]);
				triProjected.p[2] = Matrix4x4.MultiplyMatrixVector(SuperObject.mat, rTri.p[2]);
				
				triProjected.p[0] = Vector3D.Div(triProjected.p[0], triProjected.p[0].w);
				triProjected.p[1] = Vector3D.Div(triProjected.p[1], triProjected.p[1].w);
				triProjected.p[2] = Vector3D.Div(triProjected.p[2], triProjected.p[2].w);
				
				triProjected.p[0] = Vector3D.Add(triProjected.p[0], pn.plr.camera.viewOffset);
				triProjected.p[1] = Vector3D.Add(triProjected.p[1], pn.plr.camera.viewOffset);
				triProjected.p[2] = Vector3D.Add(triProjected.p[2], pn.plr.camera.viewOffset);
				
				triProjected.p[0].x *= Panel.root.panel[0]/2;
				triProjected.p[1].x *= Panel.root.panel[0]/2;
				triProjected.p[2].x *= Panel.root.panel[0]/2;
				
				triProjected.p[0].y *= Panel.root.panel[1]/2;
				triProjected.p[1].y *= Panel.root.panel[1]/2;
				triProjected.p[2].y *= Panel.root.panel[1]/2;
				
				Vector3D camRay = Vector3D.Sub(triTransformed.p[0], pn.plr.camera.p);
				
				if(Vector3D.DotProduct(normal, camRay) < 0)		// < 0 : view outside surface		|| > 0 : view inside surface
				{
					Vector3D dL = Vector3D.Normalise(pn.light.direction);
					
					if(pn.light.state.equals("night"))
						dL = Vector3D.Normalise(Vector3D.Mul(pn.light.direction, -1));
					
					double dp = Vector3D.DotProduct(normal, dL);
					
					triProjected.LightLevel = dp;
					triProjected.parent = m.name;
					triProjected.clr = m.clr;
					
					Triangle2D.SetColor(triProjected, pn);
					
					out.addLast(triProjected);
				}
			}
		}
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
