package variables;

public class Vector2D {
	public double x, y;
	public double w = 1;
	
	public static Vector2D zero = new Vector2D(0, 0);
	public static Vector2D one	= new Vector2D(1, 1);
	
	public static Vector2D poY = new Vector2D(0, 1);
	public static Vector2D noY = new Vector2D(0,-1);
	
	public static Vector2D poX = new Vector2D( 1, 0);
	public static Vector2D noX = new Vector2D(-1, 0);
	
	public Vector2D(double x, double y)
	{
		this.x = x; this.y = y;
	}
	
	public static double DistanceOf(Vector2D v1, Vector2D v2)
	{
		return Math.sqrt(Math.pow((v2.x - v1.x), 2) + Math.pow((v2.y - v1.y), 2));
	}
	
	public Vector2D Add(Vector2D v2)
	{
		return new Vector2D(this.x + v2.x, this.y + v2.y);
	}
	public Vector2D Sub(Vector2D v2)
	{
		return new Vector2D(this.x - v2.x, this.y - v2.y);
	}
	
	public Vector2D Mul(double i)
	{
		return new Vector2D(this.x * i, this.y * i);
	}
	public Vector2D Div(double i)
	{
		return new Vector2D(this.x / i, this.y / i);
	}
	
	public Vector2D Mul(Vector2D v2)
	{
		return new Vector2D(this.x * v2.x, this.y * v2.y);
	}
	public Vector2D Div(Vector2D v2)
	{
		return new Vector2D(this.x / v2.x, this.y / v2.y);
	}
	
	public static Vector2D Add(Vector2D v1, Vector2D v2)
	{
		return new Vector2D(v1.x + v2.x, v1.y + v2.y);
	}
	public static Vector2D Sub(Vector2D v1, Vector2D v2)
	{
		return new Vector2D(v1.x - v2.x, v1.y - v2.y);
	}
	
	public static Vector2D Mul(Vector2D v, double i)
	{
		return new Vector2D(v.x * i, v.y * i);
	}
	public static Vector2D Div(Vector2D v, double i)
	{
		return new Vector2D(v.x / i, v.y / i);
	}
	
	public static Vector2D Mul(Vector2D v1, Vector2D v2)
	{
		return new Vector2D(v1.x * v2.x, v1.y * v2.y);
	}
	public static Vector2D Div(Vector2D v1, Vector2D v2)
	{
		return new Vector2D(v1.x / v2.x, v1.y / v2.y);
	}
}
