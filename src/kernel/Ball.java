package kernel;

import android.widget.ImageView;

public class Ball
{
	public static final int FIRE = 0;
	public static final int WATER = 1;
	public static final int WIND = 2;
	public static final int LIGHT = 3;
	public static final int DARK = 4;
	private int type;
	private ImageView imageView;
	public Ball() 
	{
		// TODO Auto-generated constructor stub
		
	}
	public void setType(int t)
	{
		type = t;
	}
}
