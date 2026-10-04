package display;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Images {
	public BufferedImage i[];
	
	public Images()
	{
		i = new BufferedImage[100];
		
		loadAssets();
	}
	
	private void loadAssets()
	{
		try
		{
			i[0] = ImageIO.read(getClass().getResourceAsStream("/texture/grass.png"));
			i[1] = ImageIO.read(getClass().getResourceAsStream("/images/redcar.jpg"));
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
