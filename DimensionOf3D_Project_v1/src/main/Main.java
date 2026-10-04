package main;

import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import display.Images;
import variables.Vector2D;

public class Main
{

	public static boolean FULLSCREEN = false;
	
	public static Vector2D Panel = new Vector2D(70, 46);
	
	public static Images img = new Images();

	public static void main(String[] args) 
	{
		JFrame root = new JFrame();
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		root.setResizable(false);
		if(FULLSCREEN)
		{
			root.setUndecorated(true);
			root.setExtendedState(JFrame.MAXIMIZED_BOTH);
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(root);
		}
		root.setIconImage(img.i[1]);
		root.setTitle("3D ENGINE V1");
		
		Panel pn = new Panel();
		root.add (pn);
		
		root.setVisible			  (true);
		root.pack();
		root.setLocationRelativeTo(null);
		
		pn.startThread();
	}

}
