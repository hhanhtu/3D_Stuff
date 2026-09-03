package variables;

public class Vector2D {
	public double x, y;
	
	public static Vector2D zero = new Vector2D(0, 0);
	public static Vector2D one	= new Vector2D(1, 1);
	
	public Vector2D(double x, double y)
	{
		this.x = x; this.y = y;
	}
}
