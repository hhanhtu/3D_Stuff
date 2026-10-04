package variables;

import java.util.HashMap;

public class Attribute {
	public String type;
	public Object value;
	
	public void setAttribute(String type, Object value)
	{
		try
		{
			switch(type.toLowerCase()){
			case "string":
				this.value = (String)	value;
			case "int":
				this.value = (int)		value;
			case "double":
				this.value = (double)	value;
			case "vector3d":
				this.value = (Vector3D)	value;
			case "object":
				this.value = (Object)	value;
			case "boolean":
				this.value = (boolean)	value;
			}
		} catch(Exception e)
		{
			if(type == null)
				System.out.println("!! Enter Type of Value to set Attribute [Type == null]");
			if(value == null)
				System.out.println("!! Enter Value to set Attribute [Value == null]");
		}
	}
	
	public Object getAttribute()
	{
		return value;
	}
}
