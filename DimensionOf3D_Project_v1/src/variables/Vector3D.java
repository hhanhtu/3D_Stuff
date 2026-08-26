package variables;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Vector;

import objects.Triangle2D;

public class Vector3D
{
	public double x, y, z, w;
	
	public Vector3D(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1;
	}
	
	public static Vector3D IntersectPlane(Vector3D planeP, Vector3D planeN, Vector3D start, Vector3D end)
	{
		planeN = Vector3D.Normalise(planeN);
		
		double planeD = Vector3D.DotProduct(planeN, planeP);
		
		double ad = Vector3D.DotProduct(start, planeN);
		double bd = Vector3D.DotProduct(end  , planeN);
		
		double t  = (planeD - ad) / (bd - ad);
		
		Vector3D StE = Vector3D.Sub(end, start);
		Vector3D intersect = Vector3D.Mul(StE, t);
		
		return Vector3D.Add(start, intersect);
	}
	
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Vector> TriangleClippingInPlane(Vector3D planeP, Vector3D planeN, Triangle2D in)
	{
		HashMap<String, Vector> result = new HashMap<>();
		
		planeN = Vector3D.Normalise(planeN);
		
		Vector3D[] inPoint  = new Vector3D[3]; 	int nInPointCount  = 0;
		Vector3D[] outPoint = new Vector3D[3];	int nOutPointCount = 0;
		
		double d0 = Vector3D.DotProduct(planeN, in.p[0]) - Vector3D.DotProduct(planeN, planeP);
		double d1 = Vector3D.DotProduct(planeN, in.p[1]) - Vector3D.DotProduct(planeN, planeP);
		double d2 = Vector3D.DotProduct(planeN, in.p[2]) - Vector3D.DotProduct(planeN, planeP);
		
		if(d0 >= 0) {inPoint [nInPointCount++]	 = in.p[0];
		} else		{outPoint[nOutPointCount++]	 = in.p[0];
		}
		if(d1 >= 0) {inPoint [nInPointCount++]	 = in.p[1];
		} else		{outPoint[nOutPointCount++]	 = in.p[1];
		}
		if(d2 >= 0) {inPoint [nInPointCount++]	 = in.p[2];
		} else		{outPoint[nOutPointCount++]	 = in.p[2];
		}
		
		Vector<Integer>		 niop = new Vector<>();
		Vector<Double>		 d012 = new Vector<>();
		Vector<Triangle2D>	 tris = new Vector<>();
		Vector<Integer>		 ntri = new Vector<>();
		
		niop.add(nInPointCount);
		niop.add(nOutPointCount);
		
		d012.add(d0); d012.add(d1); d012.add(d2);
		
		result.put("NumbersOfPoint" , niop);
		result.put("Distance"		, d012);
		
		if(nInPointCount == 0)
		{
			ntri.add(0);
			tris.add(null);
			
			result.put("n_tris"		, ntri);
			result.put("Triangles"	, tris);
		}
		
		if(nInPointCount == 3)
		{
			Triangle2D out1 = new Triangle2D(in.p[0] ,in.p[1], in.p[2]);
			out1.clr = in.clr;
			
			ntri.add(1);
			tris.add(out1);
			
			result.put("n_tris"		, ntri);
			result.put("Triangles"	, tris);
		}
		
		if(nInPointCount == 1 && nOutPointCount == 2)
		{
			Triangle2D out1 = new Triangle2D(in.p[0] ,in.p[1], in.p[2]);
			out1.clr = in.clr;
			
			out1.p[0] = inPoint[0];
			out1.p[1] = Vector3D.IntersectPlane(planeP, planeN, inPoint[0], outPoint[0]);
			out1.p[2] = Vector3D.IntersectPlane(planeP, planeN, inPoint[0], outPoint[1]);
			
			ntri.add(1);
			tris.add(out1);
			
			result.put("n_tris"		, ntri);
			result.put("Triangles"	, tris);
		}
		
		if(nInPointCount == 2 && nOutPointCount == 1)
		{
			Triangle2D out1 = new Triangle2D(inPoint[0] ,inPoint[1] ,Vector3D.IntersectPlane(planeP, planeN, inPoint[0], outPoint[0]));
			Triangle2D out2 = new Triangle2D(inPoint[1] ,out1.p [2] ,Vector3D.IntersectPlane(planeP, planeN, inPoint[1], outPoint[0]));

			out1.clr = in.clr;
			out2.clr = in.clr;
			
			ntri.add(2);
			tris.add(out1);
			tris.add(out2);
			
			result.put("n_tris", ntri);
			result.put("Triangles", tris);
		}
		
		return result;
	}
	
	public static double D(Vector3D p)
	{
		return Math.sqrt(p.x*p.x + p.y*p.y + p.z*p.z);
	}
	
	public static double DotProduct_abs(Vector3D v1, Vector3D v2, int t)
	{
		if(t == 1)
		{
			return Math.abs(v1.x)*v2.x + Math.abs(v1.y)*v2.y + Math.abs(v1.z)*v2.z;
		}
		if(t == 2)
		{
			return v1.x*Math.abs(v2.x) + v1.y*Math.abs(v2.y) + v1.z*Math.abs(v2.z);
		}
		
		return Math.abs(v1.x*v2.x) + Math.abs(v1.y*v2.y) + Math.abs(v1.z*v2.z);
	}
	
	public static Vector3D Normalise(Vector3D p)
	{
		double l = Vector3D.D(p);
		
		return new Vector3D(p.x/l, p.y/l, p.z/l);
	}
	
	public static Vector3D Dot(Vector3D normal, Vector3D p)
	{
		Vector3D D = new Vector3D(0, 0, 0);
		D.x = normal.x * p.x;
		D.y = normal.y * p.y;
		D.z = normal.z * p.z;
		
		return D;
	}
	
	public static double DotProduct(Vector3D v1, Vector3D v2)
	{
		return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
	}
	
	public static Vector3D Cross(Vector3D line1, Vector3D line2)
	{
		Vector3D C = new Vector3D(0, 0, 0);
		C.x = line1.y * line2.z - line1.z * line2.y;
		C.y = line1.z * line2.x - line1.x * line2.z;
		C.z = line1.x * line2.y - line1.y * line2.x;
		
		return C;
	}
	
	public static Vector3D Line(Vector3D p1, Vector3D p2)
	{
		Vector3D L = new Vector3D(0, 0, 0);
		L.x = p1.x - p2.x;
		L.y = p1.y - p2.y;
		L.z = p1.z - p2.z;
		
		return L;
	}
	
	public static Vector3D Add(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	public static Vector3D Sub(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}
	
	public static Vector3D Mul(Vector3D v1, double i)
	{
		return new Vector3D(v1.x * i, v1.y * i, v1.z * i);
	}
	public static Vector3D Div(Vector3D v1, double i)
	{
		return new Vector3D(v1.x / i, v1.y / i, v1.z / i);
	}
	
	public static Vector3D MultiplyVector(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
	}
	public static Vector3D DivideVector(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z);
	}
	
	public static Vector3D zero()
	{
		return new Vector3D(0, 0, 0);
	}
	public static Vector3D one()
	{
		return new Vector3D(1, 1, 1);
	}
}
