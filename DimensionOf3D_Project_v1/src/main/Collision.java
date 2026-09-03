package main;

import java.util.Vector;

import entity.Player;
import objects.MeshPart;
import objects.Triangle2D;
import variables.Vector3D;

public class Collision
{
	public static int checkEntityCollision(Player plr, int outF, int outR, int outU)
	{
		Vector3D forward	 = plr.position.Add(new Vector3D(plr.camera.face.look .x * plr.speed, 0, plr.camera.face.look .z * plr.speed));
		Vector3D backward 	 = plr.position.Sub(new Vector3D(plr.camera.face.look .x * plr.speed, 0, plr.camera.face.look .z * plr.speed));
		Vector3D rightward	 = plr.position.Add(new Vector3D(plr.camera.face.right.x * plr.speed, 0, plr.camera.face.right.z * plr.speed));
		Vector3D leftward 	 = plr.position.Sub(new Vector3D(plr.camera.face.right.x * plr.speed, 0, plr.camera.face.right.z * plr.speed));
		Vector3D upward		 = plr.position.Add(new Vector3D(0, plr.camera.face.up.y * plr.speed, 0));
		Vector3D downward	 = plr.position.Add(new Vector3D(0, plr.camera.face.up.y * plr.speed, 0));
		
		for(MeshPart m: plr.pn.obj.obj)
		{
			if(m.collision)
			{
				if(outR != 0)
				{
					if(outR > 0)
					{
						if(plr.position.y - plr.height/2 < m.offset.y + m.size().y
						&& rightward.x - plr.pn.px/4 - plr.speed/2 < m.offset.x + m.size().x
						&& rightward.z - plr.pn.px/4 - plr.speed/2 < m.offset.z + m.size().z
						&& rightward.x - plr.pn.px/4 + plr.speed/2 > m.offset.x - m.size().x*2
						&& rightward.z - plr.pn.px/4 + plr.speed/2 > m.offset.z - m.size().z*2)
							return 0;
					}
					
					if(outR < 0)
					{
						if(plr.position.y - plr.height/2 < m.offset.y + m.size().y
						&& leftward.x - plr.pn.px/4 - plr.speed/2 < m.offset.x + m.size().x
						&& leftward.z - plr.pn.px/4 - plr.speed/2 < m.offset.z + m.size().z
						&& leftward.x - plr.pn.px/4 + plr.speed/2 > m.offset.x - m.size().x*2
						&& leftward.z - plr.pn.px/4 + plr.speed/2 > m.offset.z - m.size().z*2)
							return 0;
					}
				}
				
				if(outF != 0)
				{
					if(outF > 0)
					{
						if(plr.position.y - plr.height/2 < m.offset.y + m.size().y
						&& forward.x - plr.pn.px/4 - plr.speed/2 < m.offset.x + m.size().x
						&& forward.z - plr.pn.px/4 - plr.speed/2 < m.offset.z + m.size().z
						&& forward.x - plr.pn.px/4 + plr.speed/2 > m.offset.x - m.size().x*2
						&& forward.z - plr.pn.px/4 + plr.speed/2 > m.offset.z - m.size().z*2)
							return 0;
					}
					
					if(outF < 0)
					{
						if(plr.position.y - plr.height/2 < m.offset.y + m.size().y
						&& backward.x - plr.pn.px/4 - plr.speed/2 < m.offset.x + m.size().x
						&& backward.z - plr.pn.px/4 - plr.speed/2 < m.offset.z + m.size().z
						&& backward.x - plr.pn.px/4 + plr.speed/2 > m.offset.x - m.size().x*2
						&& backward.z - plr.pn.px/4 + plr.speed/2 > m.offset.z - m.size().z*2)
							return 0;
					}
				}
			}
		}
		
		return 1;
	}
	
}
