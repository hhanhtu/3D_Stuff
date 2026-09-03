package main;

import entity.Player;
import objects.MeshPart;
import variables.Vector3D;

public class Physic {
	public Vector3D velocity = Vector3D.zero;
	
	public Player plr;
	public MeshPart obj;
	
	public Physic(Player plr)
	{set(plr, null);}
	public Physic(MeshPart obj)
	{set(null, obj);}
	
	private void set(Player plr, MeshPart obj)
	{
		if(plr != null)
		{
			this.plr = plr;
		}
		if(obj != null)
		{
			this.obj = obj;
		}
	}
	
	public void linearVelocity() // rename soon
	{
		if(plr != null)
		{
			for(MeshPart m: plr.pn.obj.obj)
			{
				if(m.collision)
				{
					if(plr.position.y - plr.height <= m.offset.y + m.size().y
					&&(plr.position.x + plr.pn.px/8 >= m.offset.x - m.size().x && plr.position.x - plr.pn.px/8 <= m.offset.x + m.size().x)
					&&(plr.position.z + plr.pn.px/8 >= m.offset.z - m.size().z && plr.position.z - plr.pn.px/8 <= m.offset.z + m.size().z))
					{
						this.velocity = Vector3D.zero;	
					}
				}
			}
		}
		
		if(!this.velocity.equals(Vector3D.zero))
			plr.position = plr.position.Add(this.velocity);
	}
}
