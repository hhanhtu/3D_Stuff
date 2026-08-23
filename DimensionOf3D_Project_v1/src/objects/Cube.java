package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

import main.Panel;
import variables.Vector3D;

public class Cube extends SuperObject
{
	public Vector3D anchoredOffset;
	
	public Cube(Panel pn) {
		this.pn = pn;
		
		this.mp		 = new MeshPart(pn);
		
		this.anchoredOffset = new Vector3D(0, 0, 0);
		this.offset	 = new Vector3D(0, 0, 0);
		this.clr	 = Color.WHITE;
		this.size	 = new Vector3D(1, 1, 1);
		this.scale	 = 1;
		
		update();
	}
	
	public void update() {
		mp.offset = this.offset;
		mp.clr	  = this.clr;
		mp.size   = this.size;
		mp.scale  = this.scale;
		
		mp.updateMatrix();
		
		Vector<Triangle2D> tris = new Vector<>();
		// Front
		tris.add(new Triangle2D(new Vector3D(0, 0, 0), new Vector3D(0, 1, 0), new Vector3D(1, 1, 0)));
		tris.add(new Triangle2D(new Vector3D(0, 0, 0), new Vector3D(1, 1, 0), new Vector3D(1, 0, 0)));
		// Right
		tris.add(new Triangle2D(new Vector3D(1, 0, 0), new Vector3D(1, 1, 0), new Vector3D(1, 1, 1)));
		tris.add(new Triangle2D(new Vector3D(1, 0, 0), new Vector3D(1, 1, 1), new Vector3D(1, 0, 1)));
		// Back
		tris.add(new Triangle2D(new Vector3D(1, 0, 1), new Vector3D(1, 1, 1), new Vector3D(0, 1, 1)));
		tris.add(new Triangle2D(new Vector3D(1, 0, 1), new Vector3D(0, 1, 1), new Vector3D(0, 0, 1)));
		// Left
		tris.add(new Triangle2D(new Vector3D(0, 0, 1), new Vector3D(0, 1, 1), new Vector3D(0, 1, 0)));
		tris.add(new Triangle2D(new Vector3D(0, 0, 1), new Vector3D(0, 1, 0), new Vector3D(0, 0, 0)));
		// Top
		tris.add(new Triangle2D(new Vector3D(0, 1, 0), new Vector3D(0, 1, 1), new Vector3D(1, 1, 1)));
		tris.add(new Triangle2D(new Vector3D(0, 1, 0), new Vector3D(1, 1, 1), new Vector3D(1, 1, 0)));
		// Bottom
		tris.add(new Triangle2D(new Vector3D(1, 0, 1), new Vector3D(0, 0, 1), new Vector3D(0, 0, 0)));
		tris.add(new Triangle2D(new Vector3D(1, 0, 1), new Vector3D(0, 0, 0), new Vector3D(1, 0, 0)));
		
		mp.tris = tris;
	}
	
	public void generate(Graphics2D g2) {
		mp.generate(g2);
	}
}
