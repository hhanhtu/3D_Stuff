package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
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
	
	public Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	
	public Window	  root = new Window();
	public Thread	  thr;
	public Graphics2D g2;
	
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
		this.setDoubleBuffered		(true);
		this.addKeyListener   		(input);
//		this.addMouseListener 		(mAction);
//		this.addMouseMotionListener	(mMotion);
//		this.addMouseWheelListener  (mWheel);
		this.setFocusable    		(true);
		
//		plr.camera.p.y = -px*2 - 5;
//		plr.camera.rotation.y = Math.toRadians(90);
	}
	
	public void update()
	{
		root.screen[0] = screen.getWidth ();
		root.screen[1] = screen.getHeight();
		
		light.rX -= 0.1;
		light.rY += 0.1/3;
		
		light.update();
		plr.update();
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
		
		obj.generateAll(g2);
		
		g2.dispose();
	}
}