package kernel;

import java.util.Random;

import android.content.Context;
import android.graphics.Point;
import android.widget.RelativeLayout;

public class Kernel 
{
	private Ball chest[][];
	private int chestWidth;
	private int chestHeight;
	public Kernel(Point dimension) 
	{
		chestWidth = dimension.x;
		chestHeight = dimension.y;
		chest = new Ball[chestHeight][chestWidth];
		for(int i=0;i<5;i++)
			for(int j=0;j<6;j++)
				chest[i][j] = new Ball((int)(Math.random()*6)); 
	}
	public int getTypeByIndex(int x,int y)
	{
		return chest[y][x].getType();
	}
	public int getChestHeight()
	{
		return chestHeight;
	}
	public int getChestWidth()
	{
		return chestWidth;
	}

}
