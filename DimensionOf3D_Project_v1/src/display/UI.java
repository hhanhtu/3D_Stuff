package display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Panel;
import variables.Attribute;

public class UI extends Attribute{
	public Font		fontStyle;
	public String 	font;
	public String 	text;
	public Color 	textColor;
	public Color	backgroundColor;
	
	public int style;
	public int size;
	public int xT, yT;
	public boolean isCenter;
	
	public  boolean showMsg = true;
	private int		msgCounter = 0;
	public boolean displayGUI = true;
	public int duration;
	public int layerOrder;
	
	public Runnable configuration;
	
	public UI() {
		layerOrder = 0;
		isCenter = false;
		duration = 2;
		text = "[Blank]";
		style = Font.PLAIN;
		size = 40;
		textColor = Color.WHITE;
		backgroundColor = null;
		xT = 0; yT = 0;
		font = "Arial";
		fontStyle = new Font(font, style, size);
	}
	
	public void updateFont() {
		fontStyle = new Font(font, style, size);
	}
	
	public void showMessage(String msg) {
		showMsg = true;
		if(!msg.isEmpty()) {
			text = msg;
		}
	}
	
	public void generate(Graphics g) {
		if(configuration != null)
			configuration.run();
		
		int textLength = (int) g.getFontMetrics().getStringBounds(text, g).getWidth();
		int textWidth = (int) g.getFontMetrics().getStringBounds(text, g).getHeight();
		
		if(backgroundColor != null && showMsg)
		{
			g.setColor(backgroundColor);
			g.fillRect(xT - 2, yT - textWidth - 2, textLength + 4, textWidth + 4);
		}
		
		g.setFont(fontStyle);
		g.setColor(textColor);
		
		if(displayGUI) {
			if(isCenter) {
				isCenter = false;
				xT = xT - textLength/2;
			}
			
			if(showMsg)
				g.drawString(text, xT, yT);
		}
		
		if(showMsg && !displayGUI) {
			if(isCenter) {
				isCenter = false;
				xT = xT - textLength/2;
			}
			g.drawString(text, xT, yT);
			
			msgCounter++;
			
			if(msgCounter > Panel.FPS*duration) {
				msgCounter = 0;
				showMsg = false;
			}
		}
		
	}
}
