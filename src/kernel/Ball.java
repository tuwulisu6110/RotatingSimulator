package kernel;

import com.example.rotatingsimulator.R;

import android.R.string;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Ball
{
	public static final int FIRE = 0;
	public static final int WATER = 1;
	public static final int GRASS = 2;
	public static final int LIGHT = 3;
	public static final int DARK = 4;
	public static final int HEART = 5;
	private int type;
	private boolean state;
	public Ball(int t)
	{
		type = t;
		state = false;
	}
	public void setType(int t)
	{
		type = t;
	}
	public int getType()
	{
		return type;
	}
	public void setState(boolean s)
	{
		state = s;
	}
	public boolean getState()
	{
		return state;
	}
	public void exchange(Ball aite)
	{
		int buffer = type;
		type = aite.getType();
		aite.setType(buffer);
		boolean bufferb = state;
		state = aite.getState();
		aite.setState(bufferb);
	}
}
