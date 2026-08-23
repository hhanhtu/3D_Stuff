package inputSystem;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;



public class InputHandler extends InputCodeReader implements KeyListener
{
	public Vector<String> keyCode = new Vector<>();
	
	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if(!keyCode.contains(readKey(e.getKeyCode())))
		{
			keyCode.add(readKey(e.getKeyCode()));
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		Vector<String> kC_Clone = new Vector<>();
		
		for(String k:keyCode)
		{
			if(k != null)
			{
				if(!k.equals(readKey(e.getKeyCode())))
				{
					kC_Clone.add(k);
				}
			}
		}
		
		keyCode = kC_Clone;
	}

}
