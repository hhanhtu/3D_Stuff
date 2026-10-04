package objects;

import java.awt.Color;
import java.util.Vector;

import main.Panel;
import variables.Vector3D;

public class Presets {
	
	public static Vector<MeshPart> platform_THICK()
	{
		Vector<MeshPart> m = new Vector<>();
		
		for(int k = -1; k <= 1; k++)
		{
			if(k != 0)
			{
				MeshPart floor1 = new MeshPart();
				floor1.LoadFromObjectFile("floor2");
				floor1.scale = 2;
				floor1.name = "floor";
				floor1.offset = new Vector3D(k*floor1.size().x*2, -floor1.size().y*2, 0);
				floor1.clr = Color.WHITE;
				
				MeshPart floor2 = new MeshPart();
				floor2.LoadFromObjectFile("floor2");
				floor2.scale = 2;
				floor2.name = "floor";
				floor2.offset = new Vector3D(0, -floor2.size().y*2, k*floor2.size().z*2);
				floor2.clr = Color.WHITE;
				
				MeshPart floor3 = new MeshPart();
				floor3.LoadFromObjectFile("floor2");
				floor3.scale = 2;
				floor3.name = "floor";
				floor3.offset = new Vector3D(k*floor1.size().x*2, -floor3.size().y*2, k*floor3.size().z*2);
				floor3.clr = Color.GRAY;
				
				MeshPart floor4 = new MeshPart();
				floor4.LoadFromObjectFile("floor2");
				floor4.scale = 2;
				floor4.name = "floor";
				floor4.offset = new Vector3D(k*floor1.size().x*2, -floor4.size().y*2, -k*floor3.size().z*2);
				floor4.clr = Color.GRAY;
				
				m.add(floor1);
				m.add(floor2);
				m.add(floor3);
				m.add(floor4);
			}
		}
		
		MeshPart floor1 = new MeshPart();
		floor1.LoadFromObjectFile("floor2");
		floor1.scale = 2;
		floor1.name = "floor";
		floor1.offset = new Vector3D(0, -floor1.size().y*2, 0);
		floor1.clr = Color.GRAY;
		
		m.add(floor1);
		
		return m;
	}
	
	public static Vector<MeshPart> platform_FLAT(int RADIOUS)
	{
		Vector<MeshPart> m = new Vector<>();
		
		for(int i = 1; i < RADIOUS; i++)
		{
			for(int k = -1; k <= 1; k++)
			{
				if(k != 0)
				{
					MeshPart floor1 = new MeshPart();
					floor1.LoadFromObjectFile("flatFloor");
					floor1.scale = 2;
					floor1.name = "floor";
					floor1.offset = new Vector3D(i*k*floor1.size().x*2, 0, 0);
					floor1.clr = Color.WHITE;
					floor1.collision = false;
					
					MeshPart floor2 = new MeshPart();
					floor2.LoadFromObjectFile("flatFloor");
					floor2.scale = 2;
					floor2.name = "floor";
					floor2.offset = new Vector3D(0, 0, i*k*floor2.size().z*2);
					floor2.clr = Color.WHITE;
					floor2.collision = false;
					
					MeshPart floor3 = new MeshPart();
					floor3.LoadFromObjectFile("flatFloor");
					floor3.scale = 2;
					floor3.name = "floor";
					floor3.offset = new Vector3D(i*k*floor1.size().x*2, 0, i*k*floor3.size().z*2);
					floor3.clr = Color.GRAY;
					floor3.collision = false;
					
					MeshPart floor4 = new MeshPart();
					floor4.LoadFromObjectFile("flatFloor");
					floor4.scale = 2;
					floor4.name = "floor";
					floor4.offset = new Vector3D(i*k*floor1.size().x*2, 0, -i*k*floor3.size().z*2);
					floor4.clr = Color.GRAY;
					floor4.collision = false;
					
					for(int v = -1; v <= 1; v++)
					{
						if(v != 0)
						{
							for(int j = 1; j < i; j++)
							{
								MeshPart floor1a = new MeshPart();
								floor1a.LoadFromObjectFile("flatFloor");
								floor1a.scale = 2;
								floor1a.name = "floor";
								floor1a.offset = new Vector3D(i*k*floor1.size().x*2, 0, j*v*floor1.size().z*2);
								floor1a.clr = Color.GRAY;
								floor1a.collision = false;
								
								MeshPart floor2a = new MeshPart();
								floor2a.LoadFromObjectFile("flatFloor");
								floor2a.scale = 2;
								floor2a.name = "floor";
								floor2a.offset = new Vector3D(j*k*floor1.size().x*2, 0, i*v*floor1.size().z*2);
								floor2a.clr = Color.GRAY;
								floor2a.collision = false;
								
								if(i*j*v*k%2 == 0)
								{
									floor1a.clr = Color.WHITE;
									floor2a.clr = Color.WHITE;
								}
								
								m.add(floor1a);
								m.add(floor2a);
							}
						}
					}
					
					if(i*k%2 == 0)
					{
						floor1.clr = Color.GRAY;
						floor2.clr = Color.GRAY;
					}
					
					m.add(floor1);
					m.add(floor2);
					m.add(floor3);
					m.add(floor4);
				}
			}
		}
		
		MeshPart floor1 = new MeshPart();
		floor1.LoadFromObjectFile("flatFloor");
		floor1.scale = 2;
		floor1.name = "floor";
		floor1.offset = new Vector3D(0, 0, 0);
		floor1.clr = Color.GRAY;
		
		floor1.hitbox = new Vector3D(floor1.size().x*RADIOUS*3.5, floor1.size().y, floor1.size().z*RADIOUS*3.5);
		
		m.add(floor1);
		
		return m;
	}
	
}
