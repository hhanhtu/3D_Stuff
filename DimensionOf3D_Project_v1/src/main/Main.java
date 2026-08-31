package main;

import org.joml.*;

import javax.swing.JFrame;

public class Main
{

	public static void main(String[] args) 
	{
		JFrame root = new JFrame();
		root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		root.setResizable			 (false);
		root.setTitle				 ("3D ENGINE V1");
		
		Panel pn= new Panel();
		root.add (pn);
		root.setVisible			  (true);
		
		root.pack();
		
		root.setLocationRelativeTo(null);
		
		pn.startThread();
	}

}
