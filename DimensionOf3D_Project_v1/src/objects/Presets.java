package objects;

import java.awt.Color;
import java.util.Vector;

import main.Panel;
import variables.Vector3D;

public class Presets {
	
	public static Vector<MeshPart> platform(Panel pn)
	{
		Vector<MeshPart> m = new Vector<>();
		
		for(int k = -1; k <= 1; k++)
		{
			if(k != 0)
			{
				MeshPart floor1 = new MeshPart(pn);
				floor1.LoadFromObjectFile("flatFloor");
				floor1.rX = 180;
				floor1.scale = 2;
				floor1.name = "floor";
				floor1.offset = new Vector3D(k*floor1.size().x*2, 1, 0);
				floor1.clr = Color.WHITE;
				
				MeshPart floor2 = new MeshPart(pn);
				floor2.LoadFromObjectFile("flatFloor");
				floor2.rX = 180;
				floor2.scale = 2;
				floor2.name = "floor";
				floor2.offset = new Vector3D(0, 1, k*floor2.size().z*2);
				floor2.clr = Color.WHITE;
				
				MeshPart floor3 = new MeshPart(pn);
				floor3.LoadFromObjectFile("flatFloor");
				floor3.rX = 180;
				floor3.scale = 2;
				floor3.name = "floor";
				floor3.offset = new Vector3D(k*floor1.size().x*2, 1, k*floor3.size().z*2);
				floor3.clr = Color.GRAY;
				
				MeshPart floor4 = new MeshPart(pn);
				floor4.LoadFromObjectFile("flatFloor");
				floor4.rX = 180;
				floor4.scale = 2;
				floor4.name = "floor";
				floor4.offset = new Vector3D(k*floor1.size().x*2, 1, -k*floor3.size().z*2);
				floor4.clr = Color.GRAY;
				
				m.add(floor1);
				m.add(floor2);
				m.add(floor3);
				m.add(floor4);
			}
		}
		
		MeshPart floor1 = new MeshPart(pn);
		floor1.LoadFromObjectFile("flatFloor");
		floor1.rX = 180;
		floor1.scale = 2;
		floor1.name = "floor";
		floor1.offset = new Vector3D(0, 1, 0);
		floor1.clr = Color.GRAY;
		
		m.add(floor1);
		
		return m;
	}
	
}
