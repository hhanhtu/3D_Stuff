package inputSystem;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import variables.Vector2D;

public class MMotionHandler implements MouseMotionListener
{
	public Vector2D coordinate = new Vector2D(0, 0);
	public String hoveringObject = "";

	@Override
	public void mouseDragged(MouseEvent e) {
		coordinate.x = e.getX();
		coordinate.y = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		coordinate.x = e.getX();
		coordinate.y = e.getY();
	}

	public void reset()
	{
		coordinate = Vector2D.zero;
		hoveringObject = "";
	}
}
