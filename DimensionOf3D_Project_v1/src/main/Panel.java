package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JPanel;

import entity.Player;
import inputSystem.InputHandler;
import objects.MeshPart;
import objects.SuperObject;
import variables.Matrix4x4;
import variables.Vector3D;

public class Panel extends Canvas implements Runnable
{
	private static final long serialVersionUID = -8067777132710632548L;
	
	public static class Window
	{
		public double[] panel	= new double[2];
		public double[] screen	= new double[2];
	}
	public static class Plane
	{
		public Vector3D topPlane	 = new Vector3D( 0, 1, 1);
		public Vector3D bottomPlane	 = new Vector3D( 0,-1, 1);
		public Vector3D rightPlane	 = new Vector3D( 1, 0, 1);
		public Vector3D leftPlane	 = new Vector3D(-1, 0, 1);
	}
	
	public void startThread()
	{
		thr = new Thread(this);
		thr.start();
		
		createBufferStrategy(2);
		bs = this.getBufferStrategy();
	}
	
	public 	int FPS 	 = 60;
	public	int px		 = 16;
	private int WIDTH	 = 70;
	private int HEIGHT	 = 46;
	
	private double	 dIntV	 = 1000000000/FPS;
	private double 	 delta	 = 0;
	private long	 last    = System.nanoTime();
	private long	 timer   = 0;
	
	public static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	public static Window root = new Window();
	public static Plane plane = new Plane();
	
	public Thread	  thr;
	public BufferStrategy bs;
	
	public InputHandler		input	= new InputHandler();
	
	public AssetManager 	obj 	= new AssetManager(this);
	public Player			plr		= new Player(this);
	public LightEnvironment light 	= new LightEnvironment(this);
	
	public Panel()
	{
		root.panel[0] = WIDTH	*px;
		root.panel[1] = HEIGHT	*px;
		
		this.setPreferredSize 		(new Dimension((int)root.panel[0], (int)root.panel[1]));
		this.setBackground    		(Color.black);
		this.addKeyListener   		(input);
//		this.addMouseListener 		(mAction);
//		this.addMouseMotionListener	(mMotion);
//		this.addMouseWheelListener  (mWheel);
		this.setFocusable    		(true);
		
		plr.camera.p.y = -px*2 - 5;
//		plr.camera.rotation.y = Math.toRadians(90);
	}
	
	public void update()
	{
		root.screen[0] = screen.getWidth ();
		root.screen[1] = screen.getHeight();
		
		light.rX -= 0.5*2/10;
		light.rY -= 0.0625*2/10;
		
		light.update();
		obj.updateAll();
		plr.update();
	}

	@Override
	public void run()
	{
		// panel loop
		long	 curTime;
		
		while(thr != null) {
			curTime = System.nanoTime();
			delta  += (curTime - last)/dIntV;
			timer  += (curTime - last);
			
			if(delta >= 1) {
				this.update();
				
				if(bs != null)
				{
					Graphics g = (Graphics) bs.getDrawGraphics();
					
					g.setColor(Color.BLACK);
					g.fillRect(0, 0, (int)root.panel[0], (int)root.panel[1]);
					
					g.setColor(Color.WHITE);
					
					obj.generateAll(g);
					
					bs.show();
					
					g.dispose();
				}
				
				delta  -- ;
			}
			if(timer >= 1000000000) {
				timer  = 0;
			}
			
			last = curTime;
		}
	}
	
//	public void paintComponent(Graphics g)
//	{
//		super.paintComponent(g);
//		g.setColor(Color.WHITE);
//		
//		obj.generateAll(g);
//		
//		g.dispose();
//	}
}