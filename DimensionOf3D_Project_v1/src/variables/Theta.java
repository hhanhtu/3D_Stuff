package variables;

public class Theta
{
	public double x, y, z;
	
	public Matrix4x4 matRotX;
	public Matrix4x4 matRotY;
	public Matrix4x4 matRotZ;
	
	public Theta(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		
		matRotX = new Matrix4x4();
		matRotY = new Matrix4x4();
		matRotZ = new Matrix4x4();
	}

	public void updateRotation()
	{
		this.matRotZ.m[0][0] = Math.cos(this.z);
		this.matRotZ.m[0][1] = Math.sin(this.z);
		this.matRotZ.m[1][0] =-Math.sin(this.z);
		this.matRotZ.m[1][1] = Math.cos(this.z);
		this.matRotZ.m[2][2] = 1;
		this.matRotZ.m[3][3] = 1;
		
		this.matRotY.m[0][0] = Math.cos(this.y);
		this.matRotY.m[0][2] =-Math.sin(this.y);
		this.matRotY.m[1][1] = 1;
		this.matRotY.m[2][0] = Math.sin(this.y);
		this.matRotY.m[2][2] = Math.cos(this.y);
		this.matRotY.m[3][3] = 1;
		
		this.matRotX.m[0][0] = 1;
		this.matRotX.m[1][1] = Math.cos(this.x);
		this.matRotX.m[1][2] = Math.sin(this.x);
		this.matRotX.m[2][1] =-Math.sin(this.x);
		this.matRotX.m[2][2] = Math.cos(this.x);
		this.matRotX.m[3][3] = 1;
	}

	public static Matrix4x4 calculateMatrixRotationZ(double z) {
		Matrix4x4 mat = new Matrix4x4();
		
		mat.m[0][0] = Math.cos(z);
		mat.m[0][1] = Math.sin(z);
		mat.m[1][0] =-Math.sin(z);
		mat.m[1][1] = Math.cos(z);
		mat.m[2][2] = 1;
		mat.m[3][3] = 1;
		
		return mat;
	}
	
	public static Matrix4x4 calculateMatrixRotationY(double y) {
		Matrix4x4 mat = new Matrix4x4();
		
		mat.m[0][0] = Math.cos(y);
		mat.m[0][2] =-Math.sin(y);
		mat.m[1][1] = 1;
		mat.m[2][0] = Math.sin(y);
		mat.m[2][2] = Math.cos(y);
		mat.m[3][3] = 1;
		
		return mat;
	}
	
	public static Matrix4x4 calculateMatrixRotationX(double x) {
		Matrix4x4 mat = new Matrix4x4();
		
		mat.m[0][0] = 1;
		mat.m[1][1] = Math.cos(x);
		mat.m[1][2] = Math.sin(x);
		mat.m[2][1] =-Math.sin(x);
		mat.m[2][2] = Math.cos(x);
		mat.m[3][3] = 1;
		
		return mat;
	}
}
