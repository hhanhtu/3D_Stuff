package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.JPanel;

import inputSystem.InputHandler;
import objects.Cube;
import objects.MeshPart;
import objects.Triangle2D;
import variables.Matrix4x4;
import variables.Vector3D;

public class Panel extends JPanel implements Runnable
{
	
	private static final long serialVersionUID = -8067777132710632548L;
	
	public class Window
	{
		public double[] panel	= new double[2];
		public double[] screen	= new double[2];
	}
	
	public void startThread()
	{
		thr = new Thread(this);
		thr.start();
	}
	
	public 	int FPS 	 = 60;
	public	int px		 = 16;
	private int WIDTH	 = 70;
	private int HEIGHT	 = 46;
	private double delta = 0;
	
	public double fN 	 = 0.1;
	public double fF 	 = 1000;
	public double AR 	 = 0;
	public double fFov	 = 90;
	public double fFovRad= Math.toRadians(fFov);

	public Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	
	public Window root = new Window();
	public Thread thr;
	public Graphics2D g2;
	public InputHandler input = new InputHandler();
	
	public Camera camera = new Camera();
	public Vector3D lightDirection = new Vector3D(0, 0,-1);
	public Vector3D viewOffset 	   = new Vector3D(1, 1, 0);
	
	public MeshPart obj = new MeshPart(this);
	
	public Panel()
	{
		root.panel[0] = WIDTH	*px;
		root.panel[1] = HEIGHT	*px;
		
		this.setPreferredSize 		(new Dimension((int)root.panel[0], (int)root.panel[1]));
		this.setBackground    		(Color.black);
		this.setDoubleBuffered		(true);
		this.addKeyListener   		(input);
//		this.addMouseListener 		(mAction);
//		this.addMouseMotionListener	(mMotion);
//		this.addMouseWheelListener  (mWheel);
		this.setFocusable    		(true);
		
		obj.clr = Color.WHITE;
		obj.offset = new Vector3D(0, 0, 15);
		
//		obj.rY = 45;
//		obj.rZ = 90;
		obj.rX = 180;
		
		obj.LoadFromObjectFile("test");
	}
	
	public void update()
	{
		root.screen[0] = screen.getWidth ();
		root.screen[1] = screen.getHeight();
		
		AR		= root.panel[1]/root.panel[0];
		fFovRad = Math.toRadians(fFov);
		
		if(input.keyCode.indexOf("SPACE") != -1)
			camera.p.y -= .5;
		if(input.keyCode.indexOf("Shift") != -1)
			camera.p.y += .5;
		
		if(input.keyCode.indexOf("A") != -1)
			camera.p.x -= .5;
		if(input.keyCode.indexOf("D") != -1)
			camera.p.x += .5;
		
		Vector3D forward = Vector3D.Mul(camera.look, .25);
		
		if(input.keyCode.indexOf("W") != -1)
			camera.p =  Vector3D.Add(camera.p, forward);
		if(input.keyCode.indexOf("S") != -1)
			camera.p =  Vector3D.Sub(camera.p, forward);
		
		if(input.keyCode.indexOf("J") != -1)
			camera.rotation.y -= 1;
		if(input.keyCode.indexOf("L") != -1)
			camera.rotation.y += 1;
		
		if(input.keyCode.indexOf("I") != -1)
			camera.rotation.x += 1;
		if(input.keyCode.indexOf("K") != -1)
			camera.rotation.x -= 1;
		
		obj.updateMatrix();
	}

	@Override
	public void run()
	{
		// panel loop
		double	 dIntV	 = 1000000000/FPS;
		double 	 delta	 = 0;
		long	 last    = System.nanoTime();
		long	 timer   = 0;
		int		 dCount  = 0;
		long	 curTime;
				
		while(thr != null) {
			curTime = System.nanoTime();
			delta  += (curTime - last)/dIntV;
			timer  += (curTime - last);
			
			if(delta >= 1) {
				this.update();
				
				repaint();
				
				delta  -- ;
				dCount ++ ;
			}
			if(timer >= 1000000000) {
				dCount = 0;
				timer  = 0;
			}
			
			last = curTime;
		}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g2 = (Graphics2D) g;
		g2.setColor(Color.WHITE);
		
		obj.generate(g2);
		
		g2.dispose();
	}
}