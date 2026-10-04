package variables;

public class Theta
{
	public double x, y, z;
	
	public Matrix4x4 matRotX;
	public Matrix4x4 matRotY;
	public Matrix4x4 matRotZ;
	
	public Matrix4x4 matRotXYZ;
	
	public Theta(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		
		matRotX = new Matrix4x4();
		matRotY = new Matrix4x4();
		matRotZ = new Matrix4x4();
		
		matRotXYZ = new Matrix4x4();
	}

	public void updateRotation()
	{
		matRotZ.m[0][0] = Math.cos(this.z);
		matRotZ.m[0][1] = Math.sin(this.z);
		matRotZ.m[1][0] =-Math.sin(this.z);
		matRotZ.m[1][1] = Math.cos(this.z);
		matRotZ.m[2][2] = 1;
		matRotZ.m[3][3] = 1;
		
		matRotY.m[0][0] = Math.cos(this.y);
		matRotY.m[0][2] =-Math.sin(this.y);
		matRotY.m[1][1] = 1;
		matRotY.m[2][0] = Math.sin(this.y);
		matRotY.m[2][2] = Math.cos(this.y);
		matRotY.m[3][3] = 1;
		
		matRotX.m[0][0] = 1;
		matRotX.m[1][1] = Math.cos(this.x);
		matRotX.m[1][2] = Math.sin(this.x);
		matRotX.m[2][1] =-Math.sin(this.x);
		matRotX.m[2][2] = Math.cos(this.x);
		matRotX.m[3][3] = 1;
		
		matRotXYZ = matRotX.Mul(matRotY.Mul(matRotZ));
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
