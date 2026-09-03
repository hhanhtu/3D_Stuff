package variables;

import java.util.HashMap;
import java.util.Vector;

import objects.Triangle2D;

public class Vector3D
{
	public double x, y, z, w;
	
	public static Vector3D zero = new Vector3D(0, 0, 0);
	public static Vector3D one  = new Vector3D(1, 1, 1);
	
	public static Vector3D look	 = new Vector3D(0, 0, 1);
	public static Vector3D right = new Vector3D(1, 0, 0);
	public static Vector3D up	 = new Vector3D(0,-1, 0);
	
	public Vector3D(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = 1;
	}
	
	public static Vector3D IntersectPlane(Vector3D planeP, Vector3D planeN, Vector3D start, Vector3D end, double t)
	{
		planeN.Normalise();
		
		double planeD = Vector3D.DotProduct(planeN, planeP);
		
		double ad = Vector3D.DotProduct(start, planeN);
		double bd = Vector3D.DotProduct(end  , planeN);
		
		t  = (planeD - ad) / (bd - ad);
		
		Vector3D StE = end.Sub(start);
		Vector3D intersect = StE.Mul(t);
		
		return start.Add(intersect);
	}
	
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Vector> TriangleClippingInPlane(Vector3D planeP, Vector3D planeN, Triangle2D in)
	{
		HashMap<String, Vector> result = new HashMap<>();
		
		planeN.Normalise();
		
		Vector3D[] inPoint  = new Vector3D[3]; 	int nInPointCount  = 0; inPoint [0] = Vector3D.zero; inPoint [1] = Vector3D.zero; inPoint [2] = Vector3D.zero;
		Vector3D[] outPoint = new Vector3D[3];	int nOutPointCount = 0; outPoint[0] = Vector3D.zero; outPoint[1] = Vector3D.zero; outPoint[2] = Vector3D.zero;
		
		Vector2D[] inText  = new Vector2D[3]; 	int nInTextCount  = 0; inText [0]	 = Vector2D.zero; inText [1] = Vector2D.zero; inText [2] = Vector2D.zero;
		Vector2D[] outText = new Vector2D[3];	int nOutTextCount = 0; outText[0]	 = Vector2D.zero; outText[1] = Vector2D.zero; outText[2] = Vector2D.zero;
		
		double d0 = Vector3D.DotProduct(planeN, in.p[0]) - Vector3D.DotProduct(planeN, planeP);
		double d1 = Vector3D.DotProduct(planeN, in.p[1]) - Vector3D.DotProduct(planeN, planeP);
		double d2 = Vector3D.DotProduct(planeN, in.p[2]) - Vector3D.DotProduct(planeN, planeP);
		
		if(d0 >= 0) {inPoint [nInPointCount++]	 = in.p[0]; inText[nInTextCount++]	 = in.t[0];
		} else		{outPoint[nOutPointCount++]	 = in.p[0]; outText[nOutTextCount++] = in.t[0];
		}
		if(d1 >= 0) {inPoint [nInPointCount++]	 = in.p[1]; inText[nInTextCount++]	 = in.t[1];
		} else		{outPoint[nOutPointCount++]	 = in.p[1]; outText[nOutTextCount++] = in.t[1];
		}
		if(d2 >= 0) {inPoint [nInPointCount++]	 = in.p[2]; inText[nInTextCount++]	 = in.t[2];
		} else		{outPoint[nOutPointCount++]	 = in.p[2]; outText[nOutTextCount++] = in.t[2];
		}
		
		Vector<Integer> niop = new Vector<>(); Vector<Double> d012 = new Vector<>(); Vector<Triangle2D> tris = new Vector<>(); Vector<Integer> ntri = new Vector<>();
		
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
			out1.parent = in.parent;
			
			ntri.add(1);
			tris.add(out1);
			result.put("n_tris"		, ntri);
			result.put("Triangles"	, tris);
		}
		
		if(nInPointCount == 1 && nOutPointCount == 2)
		{
			Triangle2D out1 = new Triangle2D(in.p[0] ,in.p[1], in.p[2]);
			out1.clr = in.clr;
			out1.parent = in.parent;
			
			out1.p[0] = inPoint	[0];
			out1.t[0] = inText	[0];
			
			double t = 0;
			
			out1.p[1] = Vector3D.IntersectPlane(planeP, planeN, inPoint[0], outPoint[0], t);
			out1.t[1].x = t * (outText[0].x - inText[0].x) + inText[0].x;
			out1.t[1].y = t * (outText[0].y - inText[0].y) + inText[0].y;
			
			out1.p[2] = Vector3D.IntersectPlane(planeP, planeN, inPoint[0], outPoint[1], t);
			out1.t[2].x = t * (outText[1].x - inText[0].x) + inText[0].x;
			out1.t[2].y = t * (outText[1].y - inText[0].y) + inText[0].y;
			
			ntri.add(1);
			tris.add(out1);
			result.put("n_tris"		, ntri);
			result.put("Triangles"	, tris);
		}
		
		if(nInPointCount == 2 && nOutPointCount == 1)
		{
			double t = 0;
			
			Triangle2D out1 = new Triangle2D(inPoint[0] ,inPoint[1] ,Vector3D.IntersectPlane(planeP, planeN, inPoint[0], outPoint[0], t));
			out1.t[0] = inText[0];
			out1.t[1] = inText[1];
			
			out1.t[2].x = t * (outText[0].x - inText[0].x) + inText[0].x;
			out1.t[2].y = t * (outText[0].y - inText[0].y) + inText[0].y;
			
			Triangle2D out2 = new Triangle2D(inPoint[1] ,out1.p [2] ,Vector3D.IntersectPlane(planeP, planeN, inPoint[1], outPoint[0], t));
			out2.t[0] = inText[1];
			out2.t[1] = outText[2];
			
			out2.t[2].x = t * (outText[0].x - inText[1].x) + inText[1].x;
			out2.t[2].y = t * (outText[0].y - inText[1].y) + inText[1].y;
			
			out1.clr = in.clr;
			out2.clr = in.clr;
			
			out1.parent = in.parent;
			out2.parent = in.parent;
			
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
	
	public Vector3D Normalise()
	{
		double l = Vector3D.D(this);
		
		return new Vector3D(this.x/l, this.y/l, this.z/l);
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
	
	public Vector3D Add(Vector3D v2)
	{
		return new Vector3D(this.x + v2.x, this.y + v2.y, this.z + v2.z);
	}
	public Vector3D Sub(Vector3D v2)
	{
		return new Vector3D(this.x - v2.x, this.y - v2.y, this.z - v2.z);
	}
	
	public Vector3D Mul(double i)
	{
		return new Vector3D(this.x * i, this.y * i, this.z * i);
	}
	public Vector3D Div(double i)
	{
		return new Vector3D(this.x / i, this.y / i, this.z / i);
	}
	
	public Vector3D Mul(Vector3D v2)
	{
		return new Vector3D(this.x * v2.x, this.y * v2.y, this.z * v2.z);
	}
	public Vector3D Div(Vector3D v2)
	{
		return new Vector3D(this.x / v2.x, this.y / v2.y, this.z / v2.z);
	}
	
	public static Vector3D Add(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
	}
	public static Vector3D Sub(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
	}
	
	public static Vector3D Mul(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
	}
	public static Vector3D Div(Vector3D v1, Vector3D v2)
	{
		return new Vector3D(v1.x / v2.x, v1.y / v2.y, v1.z / v2.z);
	}
}
