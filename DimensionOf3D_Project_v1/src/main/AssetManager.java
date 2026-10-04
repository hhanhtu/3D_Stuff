package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
	public boolean				    WIREFRAME 	= false;
	public boolean				    HITBOX		= false;
	public boolean				    TAGNAME 	= false;
	
	public Vector<MeshPart>			obj 					= new Vector<>();
	public Vector<Triangle2D>		GLOBALTRIANGLEFRAMES	= new Vector<>();
	public Vector<Triangle2D>		GLOBALTRIANGLESVERTEX	= new Vector<>();
	
	public class Background
	{
		public Color clr;
		public BufferedImage img;
		
		public boolean transition = false;
		public double t = 0;
		
		public Runnable configuration;
		
		public Background()
		{setup(Color.BLACK, null);}
		public Background(Color clr)
		{setup(clr, null);}
		public Background(BufferedImage img)
		{setup(Color.BLACK, img);}
		public Background(Color clr, BufferedImage img)
		{setup(clr, img);}
		
		private void setup(Color clr, BufferedImage img)
		{
			this.clr = clr;
			this.img = img;
		}
		
		public void draw(Graphics g)
		{
			Graphics2D g2 = (Graphics2D)g;
			
			g.setColor(clr);
			
			if(configuration != null)
				configuration.run();
			
			g.fillRect(0, 0, (int)Panel.root.panel[0], (int)Panel.root.panel[1]);
			
			if(img != null)
			{
				g2.drawImage(img, 0, 0, (int)Panel.root.panel[0], (int)Panel.root.panel[1], null);
			}
		}
	}
	
	public class c0123
	{
		public HashMap<String, Vector> c0;
		public HashMap<String, Vector> c1;
		public HashMap<String, Vector> c2;
		public HashMap<String, Vector> c3;
		
		public c0123()
		{
			c0 = new HashMap<>();
			c1 = new HashMap<>();
			c2 = new HashMap<>();
			c3 = new HashMap<>();
		}
		
		public void clear()
		{
			c0.clear(); c1.clear(); c2.clear(); c3.clear();
		}
	}
	
	private Vector<Triangle2D> trs = new Vector<>();
	private c0123 c = new c0123();
	
	public Background bg;
	
	public AssetManager(Panel pn)
	{
		this.pn = pn;
		
		mesh.clear();
		
		bg = new Background(new Color(135, 206, 235));
		bg.configuration = ()->{
			int r = bg.clr.getRed();
			int g = bg.clr.getGreen();
			int b = bg.clr.getBlue();
			
			if(pn.light.transition == "sun rise")
			{
				r+=Panel.tick*Panel.elapseTime*0.5;
				g+=Panel.tick*Panel.elapseTime*0.5;
				b+=Panel.tick*Panel.elapseTime*0.5;
				
				if(r >= 135)
					r = 135;
				if(g >= 206)
					g = 206;
				if(b >= 235)
					b = 235;
			}
			if(pn.light.transition == "sun set")
			{
				r-=Panel.tick*Panel.elapseTime*1;
				g-=Panel.tick*Panel.elapseTime*1;
				b-=Panel.tick*Panel.elapseTime*1;
				
				if(r <= 4)
					r = 4;
				if(g <= 26)
					g = 26;
				if(b <= 64)
					b = 64;
			}
			bg.clr = new Color(r, g, b);
		};
		
		loadAsset();
	}
	
	public void loadAsset()
	{
		Vector<MeshPart> f = Presets.platform_FLAT(3);
		
		MeshPart cube1 = new MeshPart();
		cube1.LoadFromObjectFile("cube");
		cube1.clr = Color.RED;
		cube1.scale = 15;
		cube1.name = "cube1";
		cube1.offset = new Vector3D(0, cube1.size().y, 5*Panel.px);

		MeshPart cube2 = new MeshPart();
		cube2.LoadFromObjectFile("cube");
		cube2.clr = Color.BLUE;
		cube2.scale = 10;
		cube2.name = "cube2";
		cube2.offset = new Vector3D(0, cube2.size().y, 5*Panel.px - cube1.size().z - cube2.size().z*4);

		f.add(cube1);
		f.add(cube2);
		
		MeshPart testCube = new MeshPart();
		testCube.LoadFromObjectFile("cube");
		testCube.clr = Color.CYAN;
		testCube.scale = 7;
		testCube.name = "cube";
		testCube.offset = new Vector3D(25, testCube.size().y * 2, 10 * Panel.px);
		testCube.configuration = ()->{
			testCube.rX+=Panel.elapseTime*1 * Panel.tick;
			testCube.rY+=Panel.elapseTime*1 * Panel.tick;
			testCube.rZ+=Panel.elapseTime/2 * Panel.tick;
		};
		
		MeshPart testSphere = new MeshPart();
		testSphere.LoadFromObjectFile("ball_lowQuality");
		testSphere.clr = Color.MAGENTA;
		testSphere.scale = 7;
		testSphere.name = "ball";
		testSphere.offset = new Vector3D(-25, testSphere.size().y * 2, 10 * Panel.px);
		testSphere.configuration = ()->{
			testSphere.rX+=Panel.elapseTime*1 * Panel.tick;
			testSphere.rY+=Panel.elapseTime*1 * Panel.tick;
			testSphere.rZ+=Panel.elapseTime/2 * Panel.tick;
		};
		
		testCube.collision = false;
		testSphere.collision = false;
		
		f.add(testSphere);
		f.add(testCube);
		
//		MeshPart hb = new MeshPart();
//		hb.LoadFromObjectFile("headbanana");
//		hb.clr = new Color(59, 39, 12);
//		hb.scale = 2;
//		hb.name = "head";
//		hb.offset = new Vector3D(-10 * Panel.px, hb.size().y * 2, 10 * Panel.px);
//		
//		MeshPart bb = new MeshPart();
//		bb.LoadFromObjectFile("bananaBody");
//		bb.clr = new Color(227, 207, 87);
//		bb.scale = 2;
//		bb.name = "body";
//		bb.offset = hb.offset;
//		
//		hb.collision = false;
//		bb.collision = false;
//		
//		bb.configuration = ()->{
//			bb.rY+=Panel.elapseTime*1;
//			hb.rY+=Panel.elapseTime*1;
//		};
//		
//		f.add(bb);
//		f.add(hb);
		
		Vector<MeshPart> pillars = new Vector<>();
		
		for(int k = -1; k <= 1; k++)
		{
			if(k != 0)
			{
				for(int j = -1; j <= 1; j++)
				{
					if(j != 0)
					{
						for(int i = 1; i <= 2; i++)
						{
							MeshPart pi1 = new MeshPart();
							pi1.LoadFromObjectFile("pillar");
							pi1.clr = Color.GREEN;
							pi1.scale = 3;
							pi1.name = "pillar";
							pi1.offset = new Vector3D(k*pi1.size().x*Panel.px/2, 0, i*k*j*pi1.size().z*5);
							
							if(i%2 != 0)
								pi1.clr = Color.BLUE;
							
							pillars.add(pi1);
						}
					}
				}
				
				MeshPart pi1 = new MeshPart();
				pi1.LoadFromObjectFile("pillar");
				pi1.clr = Color.GREEN;
				pi1.scale = 3;
				pi1.name = "pillar";
				pi1.offset = new Vector3D(k*pi1.size().x*Panel.px/2, 0, 0);
				
				pillars.add(pi1);
			}
		}
		
		f.addAll(pillars);
		mesh.put("Workspace", f);
		
		for(Map.Entry<String, Vector> i: mesh.entrySet())
		{
			for(Object o: i.getValue())
			{
				MeshPart m = (MeshPart)o;
				
				obj.add(m);
				GLOBALTRIANGLESVERTEX.addAll(m.tris);
			}
		}
	}
	
	private void update()
	{
		GLOBALTRIANGLEFRAMES.clear();
		
		for(Map.Entry<String, Vector> i: mesh.entrySet())
		{
			for(Object o: i.getValue())
			{
				MeshPart m = (MeshPart)o;
				m.generateTriangle2D(this);
			}
		}
	}
	
	public void generateAll(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		update();
		bg.draw(g);
		
		Panel.gui.ui[2].showMsg = TAGNAME;
		
		GLOBALTRIANGLEFRAMES.sort((Triangle2D t1, Triangle2D t2) -> {
			double z1 = (t1.p[0].z + t1.p[1].z + t1.p[2].z) / 3;
			double z2 = (t2.p[0].z + t2.p[1].z + t2.p[2].z) / 3;
			
			if(z1 > z2) return -1;
			if(z1 < z2) return  1;
			
			return 0;
		});
		
		for(Triangle2D tri: GLOBALTRIANGLEFRAMES)
		{
			c	.clear();
			trs	.clear();
			
			if(tri.p[0].x < -Panel.root.panel[0]/2 || tri.p[0].x > Panel.root.panel[0] *1.5)
				tri.parent.onScreen = false; else tri.parent.onScreen = true; 
			if(tri.p[0].y < -Panel.root.panel[1]/2 || tri.p[0].y > Panel.root.panel[1] *1.5)
				tri.parent.onScreen = false; else tri.parent.onScreen = true;
			
			trs.addFirst(tri);
			int nTris = 1;
			
			for(int p = 0; p < 4; p++)
			{
				while(nTris > 0)
				{
					Triangle2D triTarget = trs.getFirst();
					trs.removeFirst();
					nTris--;

					switch(p)
					{
					case 0:
						c.c0 = Vector3D.TriangleClippingInPlane(Panel.plane.topPlane	.view, Panel.plane.topPlane		.unit, triTarget);
						for(int j = 0; j < (int)c.c0.get("n_tris").get(0); j++)
						{
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c.c0, j));

							trs.getLast().fill(g);
							if(WIREFRAME || tri.parent.highlight)
								trs.getLast().draw(g);
						}
						break;
					case 1:
						c.c1 = Vector3D.TriangleClippingInPlane(Panel.plane.bottomPlane	.view, Panel.plane.bottomPlane	.unit, triTarget);
						for(int j = 0; j < (int)c.c1.get("n_tris").get(0); j++)
						{
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c.c1, j));

							trs.getLast().fill(g);
							if(WIREFRAME || tri.parent.highlight)
								trs.getLast().draw(g);
						}
						break;
					case 2:
						c.c2 = Vector3D.TriangleClippingInPlane(Panel.plane.rightPlane	.view, Panel.plane.rightPlane	.unit, triTarget);
						for(int j = 0; j < (int)c.c2.get("n_tris").get(0); j++) 
						{
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c.c2, j));

							trs.getLast().fill(g);
							if(WIREFRAME || tri.parent.highlight)
								trs.getLast().draw(g);
						}
						break;
					case 3:
						c.c3 = Vector3D.TriangleClippingInPlane(Panel.plane.leftPlane	.view, Panel.plane.leftPlane	.unit, triTarget);
						for(int j = 0; j < (int)c.c3.get("n_tris").get(0); j++)
						{
							trs.addLast(Triangle2D.getTrianglesFromClipResult(c.c3, j));
							
							trs.getLast().fill(g);
							if(WIREFRAME || tri.parent.highlight)
								trs.getLast().draw(g);
						}
						break;
					}
				}
				nTris = trs.size();
			}
		}
		
		for(MeshPart m: obj)
		{
			if(m.collision)
			{
				double sX = m.size().x;
				double sY = m.size().y;
				double sZ = m.size().z;
				if(m.hitbox != null)
				{
					sX = m.hitbox.x/2;
					sY = m.hitbox.y/2;
					sZ = m.hitbox.z/2;
				}
				
				double poX = sX; double noX = -sX;
				double poY = sY; double noY = -sY;
				double poZ = sZ; double noZ = -sZ;
				
				Vector<Vector3D> v = new Vector<>();
				Vector<Vector3D> vm= new Vector<>();
				
				for(int i = 0; i < 8; i++)
				{
					switch(i)
					{
					case 0:
						v.add(new Vector3D(poX, poY, poZ, m.offset.w));
						break;                           
					case 1:                              
						v.add(new Vector3D(noX, poY, poZ, m.offset.w));
						break;                           
					case 2:                              
						v.add(new Vector3D(poX, noY, poZ, m.offset.w));
						break;                           
					case 3:                              
						v.add(new Vector3D(noX, noY, poZ, m.offset.w));
						break;                           
					case 4:                              
						v.add(new Vector3D(poX, poY, noZ, m.offset.w));
						break;                           
					case 5:                              
						v.add(new Vector3D(noX, poY, noZ, m.offset.w));
						break;                           
					case 6:                              
						v.add(new Vector3D(poX, noY, noZ, m.offset.w));
						break;                           
					case 7:                              
						v.add(new Vector3D(noX, noY, noZ, m.offset.w));
						break;
					}
				}
				
				if(HITBOX)
				{
					for(Vector3D xyz: v)
					{
//						Vector3D pV = m.matWorld.MultiplyMatrixVector(xyz);
//						pV = pV.Add(m.offset);
						Vector3D pV = xyz.Add(m.offset);
						Vector3D pVview = pn.plr.camera.vCam.MultiplyMatrixVector(pV);
						Vector3D pVp = SuperObject.mat.MultiplyMatrixVector(pVview);
						pVp = pVp.Div(pVp.w);
						pVp = pVp.Add(pn.plr.camera.viewOffset);
						
						pVp = pVp.Mul(Vector3D.right.Mul(Panel.root.panel[0]/2).Add(Vector3D.down.Mul(Panel.root.panel[1]/2)));
						
						if(pVp.x < -Panel.root.panel[0]/2 || pVp.x > Panel.root.panel[0] *1.5)
							break;
						if(pVp.y < -Panel.root.panel[1]/2 || pVp.y > Panel.root.panel[1] *1.5)
							break;
						
						if(!m.onScreen)
							break;
						
						vm.add(pVp);
						
						g.setColor(Color.MAGENTA);
						g.fillOval((int)pVp.x - 5, (int)pVp.y - 5, 10, 10);
					}
					
					g.setColor(Color.WHITE);
					g2.setStroke(new BasicStroke(2f));
					if(vm.size() >= 2 && !vm.isEmpty())
						g.drawLine((int)vm.get(0).x, (int)vm.get(0).y, (int)vm.get(1).x, (int)vm.get(1).y);
					if(vm.size() >= 3 && !vm.isEmpty())
						g.drawLine((int)vm.get(0).x, (int)vm.get(0).y, (int)vm.get(2).x, (int)vm.get(2).y);
					if(vm.size() >= 4 && !vm.isEmpty())
						g.drawLine((int)vm.get(1).x, (int)vm.get(1).y, (int)vm.get(3).x, (int)vm.get(3).y);
					if(vm.size() >= 4 && !vm.isEmpty())
						g.drawLine((int)vm.get(2).x, (int)vm.get(2).y, (int)vm.get(3).x, (int)vm.get(3).y);
					
					if(vm.size() >= 2+4 && !vm.isEmpty())
						g.drawLine((int)vm.get(0+4).x, (int)vm.get(0+4).y, (int)vm.get(1+4).x, (int)vm.get(1+4).y);
					if(vm.size() >= 3+4 && !vm.isEmpty())
						g.drawLine((int)vm.get(0+4).x, (int)vm.get(0+4).y, (int)vm.get(2+4).x, (int)vm.get(2+4).y);
					if(vm.size() >= 4+4 && !vm.isEmpty())
						g.drawLine((int)vm.get(1+4).x, (int)vm.get(1+4).y, (int)vm.get(3+4).x, (int)vm.get(3+4).y);
					if(vm.size() >= 4+4 && !vm.isEmpty())
						g.drawLine((int)vm.get(2+4).x, (int)vm.get(2+4).y, (int)vm.get(3+4).x, (int)vm.get(3+4).y);
					
					if(vm.size() >= 5 && !vm.isEmpty())
						g.drawLine((int)vm.get(0).x, (int)vm.get(0).y, (int)vm.get(4).x, (int)vm.get(4).y);
					if(vm.size() >= 7 && !vm.isEmpty())
						g.drawLine((int)vm.get(2).x, (int)vm.get(2).y, (int)vm.get(6).x, (int)vm.get(6).y);
					if(vm.size() >= 6 && !vm.isEmpty())
						g.drawLine((int)vm.get(1).x, (int)vm.get(1).y, (int)vm.get(5).x, (int)vm.get(5).y);
					if(vm.size() >= 8 && !vm.isEmpty())
						g.drawLine((int)vm.get(3).x, (int)vm.get(3).y, (int)vm.get(7).x, (int)vm.get(7).y);
				}
			}
		}
	}
}
