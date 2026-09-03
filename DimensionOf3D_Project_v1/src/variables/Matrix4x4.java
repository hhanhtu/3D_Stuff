package variables;

public class Matrix4x4 {
	public double[][] m;
	
	public Matrix4x4()
	{
		m = new double[4][4];
	}
	
	public Vector3D MultiplyMatrixVector(Vector3D i)
	{
		Vector3D v = new Vector3D(0, 0, 0);
		v.x = i.x * this.m[0][0] + i.y * this.m[1][0] + i.z * this.m[2][0] + this.m[3][0];
		v.y = i.x * this.m[0][1] + i.y * this.m[1][1] + i.z * this.m[2][1] + this.m[3][1];
		v.z = i.x * this.m[0][2] + i.y * this.m[1][2] + i.z * this.m[2][2] + this.m[3][2];
		v.w = i.x * this.m[0][3] + i.y * this.m[1][3] + i.z * this.m[2][3] + this.m[3][3];
		
		return v;
	}
	
	public static Matrix4x4 create()
	{
		Matrix4x4 m = new Matrix4x4();
		
		m.m[0][0] = 1;
		m.m[1][1] = 1;
		m.m[2][2] = 1;
		m.m[3][3] = 1;
		
		return m;
	}
	
	public static Matrix4x4 createTranslation(double x, double y, double z)
	{
		Matrix4x4 m = Matrix4x4.create();
		
		m.m[3][0] = x;
		m.m[3][1] = y;
		m.m[3][2] = z;
		
		return m;
	}
	
	public static Matrix4x4 PointAt(Vector3D p, Vector3D t, Vector3D u)
	{
		Vector3D nf = t.Sub(p);
		nf.Normalise();
		
		Vector3D a  = nf.Mul(Vector3D.DotProduct(u, nf));
		Vector3D nu = u.Sub(a).Normalise();
		
		Vector3D nr = Vector3D.Cross(nu, nf);
		
		Matrix4x4 m = new Matrix4x4();
		m.m[0][0] = nr.x;	m.m[0][1] = nr.y;   m.m[0][2] = nr.z;
		m.m[1][0] = nu.x;	m.m[1][1] = nu.y;   m.m[1][2] = nu.z;
		m.m[2][0] = nf.x;	m.m[2][1] = nf.y;   m.m[2][2] = nf.z;
		m.m[3][0] = p .x;	m.m[3][1] = p .y;	m.m[3][2] = p .z;
		m.m[3][3] = 1;
		
		return m;
	}
	
	public Matrix4x4 quickInvert()
	{
		Matrix4x4 m = new Matrix4x4();
		m.m[0][0] =  this.m[0][0];	 m.m[0][1] = this.m[1][0];  m.m[0][2] = this.m[2][0];
		m.m[1][0] =  this.m[0][1];	 m.m[1][1] = this.m[1][1];  m.m[1][2] = this.m[2][1];
		m.m[2][0] =  this.m[0][2];	 m.m[2][1] = this.m[1][2];	m.m[2][2] = this.m[2][2];
		
		m.m[3][0] =-(this.m[3][0] * m.m[0][0] + this.m[3][1] * m.m[1][0] + this.m[3][2] * m.m[2][0]);
		m.m[3][1] =-(this.m[3][0] * m.m[0][1] + this.m[3][1] * m.m[1][1] + this.m[3][2] * m.m[2][1]);
		m.m[3][2] =-(this.m[3][0] * m.m[0][2] + this.m[3][1] * m.m[1][2] + this.m[3][2] * m.m[2][2]);
		m.m[3][3] = 1;
		
		return m;
	}
	
	public Matrix4x4 Mul(Matrix4x4 m2)
	{
		Matrix4x4 m = new Matrix4x4();
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				m.m[i][j] = this.m[i][0] * m2.m[0][j] + this.m[i][1] * m2.m[1][j] + this.m[i][2] * m2.m[2][j] + this.m[i][3] * m2.m[3][j];
			}
		}
		
		return m;
	}
	
	public static Matrix4x4 Mul(Matrix4x4 m1, Matrix4x4 m2)
	{
		Matrix4x4 m = new Matrix4x4();
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				m.m[i][j] = m1.m[i][0] * m2.m[0][j] + m1.m[i][1] * m2.m[1][j] + m1.m[i][2] * m2.m[2][j] + m1.m[i][3] * m2.m[3][j];
			}
		}
		
		return m;
	}
}
