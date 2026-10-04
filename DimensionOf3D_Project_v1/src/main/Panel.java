package main;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import display.GuiManager;
import display.Images;
import display.UI;
import entity.Player;
import inputSystem.InputHandler;
import inputSystem.MMotionHandler;
import objects.MeshPart;
import objects.SuperObject;
import variables.Matrix4x4;
import variables.Vector3D;

public class Panel extends Canvas implements Runnable
{
	private static final long serialVersionUID = -8067777132710632548L;
	
	public static Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int FPS  = 120;
	public static final int px	 = 16;

	public static double elapseTime = 0;
	public static boolean GAMEPAUSE = true;
	
	public static class Window
	{
		public double[] panel = new double[2];
		
		public Window()
		{
			if(Main.FULLSCREEN)
			{
				panel[0] = screen.width;
				panel[1] = screen.height;
			} else
			{
				panel[0] = Main.Panel.x * px;
				panel[1] = Main.Panel.y * px;
			}
		}
	}
	public static class Plane
	{
		public static class topPlane
		{
			public Vector3D unit = new Vector3D( 0, 1, 0);
			public Vector3D view = new Vector3D( 0, 0, 0);
		}
		
		public static class bottomPlane
		{
			public Vector3D unit = new Vector3D( 0,-1, 0);
			public Vector3D view = new Vector3D( 0, Panel.root.panel[1] - 1, 0);
		}
		
		public static class rightPlane
		{
			public Vector3D unit = new Vector3D( 1, 0, 0);
			public Vector3D view = new Vector3D( 0, 0, 0);
		}
		
		public static class leftPlane
		{
			public Vector3D unit = new Vector3D(-1, 0, 0);
			public Vector3D view = new Vector3D( Panel.root.panel[0] - 1, 0, 0);
		}
		
		public topPlane		 topPlane	 = new topPlane();
		public bottomPlane	 bottomPlane = new bottomPlane();
		public rightPlane	 rightPlane	 = new rightPlane();
		public leftPlane	 leftPlane	 = new leftPlane();
	}
	
	public void startThread()
	{
		thr = new Thread(this);
		thr.start();
		
		createBufferStrategy(2);
		bs = this.getBufferStrategy();
	}
	
	private double	 dIntV	 = 0;
	private double 	 delta	 = 0;
	private double	 last    = 0;
	private long	 timer   = 0;
	
	public static Window root 	= new Window();
	public static Plane plane	= new Plane();
	
	public Thread	  thr;
	public BufferStrategy bs;
	
	public static InputHandler		input	= new InputHandler();
	public static MMotionHandler	mMotion = new MMotionHandler();
	
	public AssetManager 	obj 	= new AssetManager(this);
	public Player			plr		= new Player(this);
	public static GuiManager gui 	= new GuiManager();
	public LightEnvironment light 	= new LightEnvironment(this);
	
	public static int tick 			= 1;
	
	public Panel()
	{
		this.setPreferredSize 		(new Dimension((int)root.panel[0], (int)root.panel[1]));
		this.setBackground    		(Color.black);
		this.addKeyListener   		(input);
//		this.addMouseListener 		(mAction);
		this.addMouseMotionListener	(mMotion);
//		this.addMouseWheelListener  (mWheel);
		this.setFocusable    		(true);
	}
	
	public void update()
	{
		if(!GAMEPAUSE)
		{
			light.time.m+=tick;
			
			light.update();
			plr.update();
		}
	}

	@Override
	public void run()
	{
		// panel loop
		int count = 0;
		
		last = (double)System.nanoTime();
		dIntV	= 1e9/(double)FPS;
		
		while(thr != null) {
			GAMEPAUSE = false;
			
			double curTime = (double)System.nanoTime();
			elapseTime = (curTime - last)/1e9;
			if(elapseTime*10 < FPS/4)
				GAMEPAUSE = true;

			delta  += (curTime - last)/dIntV;
			timer  += (curTime - last);
			
			last = curTime;
			
			while(delta >= 1)
			{
				count++;
				
				if(count >= FPS)
					break;
				
				long startFrame = System.nanoTime();
				this.update();
				
				gui.ui[3].text = String.format("Time - %02d:%02d:%02d", light.time.h, light.time.m, light.time.s);
				gui.ui[1].setAttribute("vector3d", plr.position);
				gui.gui[0].x = (int)(mMotion.coordinate.x - gui.gui[0].width/2);
				gui.gui[0].y = (int)(mMotion.coordinate.y - gui.gui[0].height/2);
				
				if(bs != null)
					drawStack();
				
				gui.ui[0].setAttribute("double", ((double)System.nanoTime() - (double)startFrame)/1000000.0);
				
				delta--;
			}
			
			if(timer >= 1e9) {
//				System.out.println(count);
				timer  = 0;
				count  = 0;
			}
		}
	}
	
	private void clearDrawConfiguration(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int)root.panel[0], (int)root.panel[1]);
		g2.setStroke(new BasicStroke(0f));
	}

	private void drawStack() {
		Graphics g = (Graphics) bs.getDrawGraphics();
		Graphics2D g2 = (Graphics2D)g;
		clearDrawConfiguration(g);
		
		g.setColor(Color.WHITE);
		
		obj.generateAll(g);
		gui.generateAll(g);
		
		gui.ui[gui.ui.length - 1] = null;
		if(GAMEPAUSE)
		{
			g.setColor(new Color(150, 150, 150, (int)(255*0.5)));
			g.fillRect(0, 0, (int)root.panel[0], (int)root.panel[1]);
			
			gui.ui[gui.ui.length - 1] = new UI();
			gui.ui[gui.ui.length - 1].size = 20;
			gui.ui[gui.ui.length - 1].text = "[GAME PAUSE]";
			gui.ui[gui.ui.length - 1].isCenter = true;
			gui.ui[gui.ui.length - 1].xT = (int)root.panel[0]/2;
			gui.ui[gui.ui.length - 1].yT = (int)root.panel[1]/2;
			gui.ui[gui.ui.length - 1].style = Font.BOLD;
			gui.ui[gui.ui.length - 1].displayGUI = true;
			
			gui.ui[gui.ui.length - 1].generate(g);
		}
		
		bs.show();
		g.dispose();
	}
}