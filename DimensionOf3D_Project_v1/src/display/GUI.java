package display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class GUI {
	public int x, y;
	public int width, height;
	public int layerOrder;
	public Color clr;
	public boolean show = true;
	public BufferedImage img;
	public Rectangle frame;
	
	public GUI() {
		x = 0;
		y = 0;
		layerOrder = 0;
		
		width = 50;
		height = 50;
		
		clr = new Color(0, 0, 0);
		frame = new Rectangle();
	}
	
	public void generate(Graphics g) {
		frame.x = x;
		frame.y = y;
		frame.width = width;
		frame.height = height;
		
		if(show)
		{
			if(img != null) {
				g.drawImage(img, frame.x, frame.y, frame.width, frame.height, null);
			} else {
				g.setColor(clr);
				g.fillRect(frame.x, frame.y, frame.width, frame.height);
			}
		}
	}
}
