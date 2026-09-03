package main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import objects.MeshPart;
import objects.Presets;
import objects.SuperObject;
import objects.Triangle2D;
import variables.Matrix4x4;
import variables.Vector2D;
import variables.Vector3D;

@SuppressWarnings("rawtypes")
public class AssetManager
{
	public Panel pn;
	
	public HashMap<String, Vector>  mesh = new HashMap<>();
	public boolean				    WIREFRAME = false;
	
	public Vector<MeshPart>			obj 				= new Vector<>();
	public Vector<Triangle2D>		GLOBALTRIANGLEFRAMES= new Vector<>();
	
	public AssetManager(Panel pn)
	{
		this.pn = pn;
		
		mesh.clear();
		
		loadAsset();
	}
	
	public void loadAsset()
	{
		Vector<MeshPart> m = Presets.platform_FLAT(3);
		
		MeshPart plrDebug = new MeshPart();
		plrDebug.LoadFromObjectFile("cube");
		plrDebug.clr = Color.GREEN;
		plrDebug.scale = pn.px/8;
		plrDebug.name = "Debug";
		plrDebug.offset = pn.plr.position;
		plrDebug.collision = false;
		plrDebug.BRIGHT = true;
		
		plrDebug.configuration = ()->{
			plrDebug.offset = pn.plr.position.Add(new Vector3D(pn.plr.camera.face.look .x * pn.plr.speed, 0, pn.plr.camera.face.look .z * pn.plr.speed)).Sub(new Vector3D(0, pn.plr.height - plrDebug.size().y, 0));
			plrDebug.rY = Math.toDegrees(pn.plr.rotation.y);
		};
		
		m.add(plrDebug);
		
		MeshPart cube = new MeshPart();
		cube.LoadFromObjectFile("cube");
		cube.clr = Color.RED;
		cube.scale = 10;
		cube.name = "cube";
		cube.offset = new Vector3D(0, cube.size().y, 50);
		
		m.add(cube);
		
		int pillarCount = 0;
		
		for(int k = -1; k <= 1; k++)
		{
			if(k != 0)
			{
				for(int i = 0; i < 2; i++)
				{
					MeshPart pillar1 = new MeshPart();
					pillar1.LoadFromObjectFile("pillar");
					pillar1.clr = Color.GREEN;
					pillar1.scale = 4;
					pillar1.name = "Pillar" + pillarCount;
					pillar1.offset = new Vector3D(50 * k, 0, i * 60);
					pillarCount++;
					
					if(i%2 == 0) pillar1.clr = Color.BLUE;
					
					MeshPart pillar2 = new MeshPart();
					pillar2.LoadFromObjectFile("pillar");
					pillar2.clr = Color.GREEN;
					pillar2.scale = 4;
					pillar2.name = "Pillar" + pillarCount;
					pillar2.offset = new Vector3D(50 * k, 0,-i * 60);
					pillarCount++;
					
					if(i%2 == 0) pillar2.clr = Color.BLUE;
					
					m.add(pillar2);
					m.add(pillar1);
				}
			}
		}
		
		mesh.put("Workspace", m);
	}
	
	public void generateAll(Graphics g)
	{
		GLOBALTRIANGLEFRAMES.clear();
		obj.clear();
		
		for(Map.Entry<String, Vector> i: mesh.entrySet())
		{
			for(Object o: i.getValue())
			{
				MeshPart m = (MeshPart)o;
				m.generateTriangle2D(this);
				
				obj.add(m);
			}
		}
		
		GLOBALTRIANGLEFRAMES.sort((Triangle2D t1, Triangle2D t2) -> {
			double z1 = (t1.p[0].z + t1.p[1].z + t1.p[2].z) / 3;
			double z2 = (t2.p[0].z + t2.p[1].z + t2.p[2].z) / 3;
			
			if(z1 > z2) return -1;
			if(z1 < z2) return  1;
			
			return 0;
		});
		
		for(Triangle2D tri: GLOBALTRIANGLEFRAMES)
		{
			tri.fill(g);
			if(WIREFRAME)
				tri.draw(g, Color.PINK);
		}
		
		if(WIREFRAME)
		{
			for(MeshPart m: obj)
			{
				Vector3D pointTransformed = SuperObject.matWorld.MultiplyMatrixVector(m.offset);
				Vector3D pointViewed	  = pn.plr.camera.vCam.MultiplyMatrixVector(pointTransformed);
				Vector3D point 			  = SuperObject.mat.MultiplyMatrixVector(pointViewed);
				
				point.Div(point.w);
				point.Add(pn.plr.camera.viewOffset);
				
				g.setColor(Color.RED);
				g.fillOval((int)(point.x * Panel.root.panel[0]/2 - 4), (int)(point.y * Panel.root.panel[1]/2  - 4), 8, 8);
			}
		}
	}
}
