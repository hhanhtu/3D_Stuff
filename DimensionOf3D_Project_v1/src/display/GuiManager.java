package display;

import java.awt.Color;
import java.awt.Graphics;

import main.LightEnvironment.Clock;
import main.Panel;
import variables.Vector3D;

public class GuiManager {
	public GUI[] gui;
	public UI[] ui;
	
	public GuiManager()
	{
		ui = new UI [10];
		gui= new GUI[10];
		
		loadAssets();
	}
	
	public void loadAssets()
	{
		gui[0] = new GUI();
		gui[0].width = 10;
		gui[0].height = 10;
		gui[0].clr = new Color(255, 255, 255, 0);
		
		ui[2] = new UI();
		ui[2].size = 12;
		ui[2].textColor = Color.WHITE;
		ui[2].backgroundColor = new Color(100, 100, 100, (int)(255*0.5));
		ui[2].updateFont();
		ui[2].configuration = ()->{
			ui[2].xT = (int)Panel.mMotion.coordinate.x;
			ui[2].yT = (int)Panel.mMotion.coordinate.y;
			ui[2].text = String.format("[%s]", Panel.mMotion.hoveringObject);
		};
		
		ui[0] = new UI();
		ui[0].text = "FPS: ";
		ui[0].textColor = Color.WHITE;
		ui[0].size = 12;
		ui[0].xT = 10;
		ui[0].yT = 20;
		ui[0].setAttribute("double", 0);
		ui[0].configuration = ()->{
			if(ui[0].getAttribute() != null)
			{
				if((double)ui[0].getAttribute() >= Panel.FPS+0.5)
					ui[0].textColor = Color.RED;
				else
					ui[0].textColor = Color.WHITE;
			}
			ui[0].text = String.format("FPS: %.1f", ui[0].getAttribute());
		};
		ui[0].updateFont();
		
		ui[1] = new UI();
		ui[1].text = "FPS: ";
		ui[1].textColor = Color.WHITE;
		ui[1].size = 12;
		ui[1].xT = 10;
		ui[1].yT = 32;
		ui[1].setAttribute("vector3d", Vector3D.zero);
		ui[1].configuration = ()->{
			if(ui[1].getAttribute() != null)
			{
				Vector3D pos = (Vector3D)ui[1].getAttribute();
				ui[1].text = String.format("Position(%.0f, %.0f, %.0f)",-pos.x, pos.y, pos.z);
			} else
			{
				ui[1].text = String.format("Position(null)");
			}
		};
		ui[1].updateFont();
		
		ui[3] = new UI();
		ui[3].text = "Time: ";
		ui[3].textColor = Color.WHITE;
		ui[3].size = 12;
		ui[3].xT = 10;
		ui[3].yT = 44;
		ui[3].updateFont();
		
		ui[4] = new UI();
		ui[4].text = "Tick: ";
		ui[4].textColor = Color.WHITE;
		ui[4].size = 12;
		ui[4].xT = 10;
		ui[4].yT = 56;
		ui[4].configuration = ()->{
			ui[4].text = String.format("Tick: %02d", Panel.tick);
		};
		ui[4].updateFont();
	}
	
	public void generateAll(Graphics g) {
		int id_gui = 0;
		int id_ui = 0;
		
		while(id_gui < gui.length && id_ui < ui.length) {
			if(gui[id_gui] != null) {
				gui[id_gui].generate(g);
			}
			if(ui[id_ui] != null) {
				ui[id_ui].displayGUI = true;
				ui[id_ui].generate(g);
			}
			if(id_gui < gui.length) {
				id_gui++;
			}
			if(id_ui < ui.length) {
				id_ui++;
			}
		}
	}
}
