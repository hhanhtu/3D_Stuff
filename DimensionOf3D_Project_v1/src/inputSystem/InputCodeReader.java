package inputSystem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class InputCodeReader
{
	String[] keyWords;
	String[] userType;
	
	public InputCodeReader()
	{
		keyWords = new String[256];
		userType = new String[3];
		
		setUpWordsMap();
	}
	
	public void setUpWordsMap()
	{
		keyWords[KeyEvent.VK_W] 		= "W";
		keyWords[KeyEvent.VK_S] 		= "S";
		keyWords[KeyEvent.VK_A] 		= "A";
		keyWords[KeyEvent.VK_D] 		= "D";
		
		keyWords[KeyEvent.VK_I] 		= "I";
		keyWords[KeyEvent.VK_K] 		= "K";
		keyWords[KeyEvent.VK_J] 		= "J";
		keyWords[KeyEvent.VK_L] 		= "L";
		
		keyWords[KeyEvent.VK_Q] 		= "Q";
		keyWords[KeyEvent.VK_E] 		= "E";
		keyWords[KeyEvent.VK_Z] 		= "Z";
		keyWords[KeyEvent.VK_C] 		= "C";
		
		keyWords[KeyEvent.VK_R] 		= "R";
		keyWords[KeyEvent.VK_P] 		= "P";
		
		keyWords[KeyEvent.VK_SPACE] 	= "SPACE";
		keyWords[KeyEvent.VK_ESCAPE]	= "ESC";
		keyWords[KeyEvent.VK_CONTROL] 	= "Ctrl";
		keyWords[KeyEvent.VK_SHIFT] 	= "Shift";
		keyWords[KeyEvent.VK_ALT] 		= "Alt";
		keyWords[KeyEvent.VK_UP] 		= "Up";
		keyWords[KeyEvent.VK_DOWN] 		= "Down";
		keyWords[KeyEvent.VK_RIGHT] 	= "Right";
		keyWords[KeyEvent.VK_LEFT] 		= "Left";
		
		keyWords[KeyEvent.VK_F1]		= "F1";
		keyWords[KeyEvent.VK_F2]		= "F2";
		keyWords[KeyEvent.VK_F3]		= "F3";
		keyWords[KeyEvent.VK_F4]		= "F4";
		keyWords[KeyEvent.VK_F5]		= "F5";
		keyWords[KeyEvent.VK_F6]		= "F6";
		keyWords[KeyEvent.VK_F7]		= "F7";
		keyWords[KeyEvent.VK_F8]		= "F8";
		keyWords[KeyEvent.VK_F9]		= "F9";
		keyWords[KeyEvent.VK_F10]		= "F10";
		keyWords[KeyEvent.VK_F11]		= "F11";
		keyWords[KeyEvent.VK_F12]		= "F12";
		
		userType[MouseEvent.BUTTON1-1] 	= "left";
		userType[MouseEvent.BUTTON2-1] 	= "wheel";
		userType[MouseEvent.BUTTON3-1] 	= "right";
	}
	
	public String readKey(int kCode)
	{
		if(keyWords[kCode] == null || kCode > keyWords.length) return null;
		
		String key = keyWords[kCode];
		
		return key;
	}
	public String readAction(int aCode)
	{
		if(userType[aCode-1] == null || aCode-1 > userType.length) return null;
		
		String key = userType[aCode-1];
		
		return key;
	}
}
