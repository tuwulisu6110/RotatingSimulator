package kernel;

import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.widget.RelativeLayout;
import junit.framework.Assert;

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
			{
				int type = (int)(Math.random()*6);
				if(i>0&&j>0)
					while(type == chest[i-1][j].getType()||type == chest[i][j-1].getType())
						type = (int)(Math.random()*6);
				else if(i>0)
					while(type == chest[i-1][j].getType())
						type = (int)(Math.random()*6);
				else if(j>0)
					while(type == chest[i][j-1].getType())
						type = (int)(Math.random()*6);
				chest[i][j] = new Ball(type); 
			}
	}
	public int getTypeByIndex(int x,int y)
	{
		return chest[y][x].getType();
	}
	public boolean getStateByIndex(int x,int y)
	{
		return chest[y][x].getState();
	}
	public int getChestHeight()
	{
		return chestHeight;
	}
	public int getChestWidth()
	{
		return chestWidth;
	}
	public void exchange(Point a,Point b)
	{
		Assert.assertTrue(a.toString(),a.x>=0&&a.x<chestWidth&&a.y>=0&&a.y<chestHeight);
		Assert.assertTrue(b.toString(),b.x>=0&&b.x<chestWidth&&b.y>=0&&b.y<chestHeight);
		chest[a.y][a.x].exchange(chest[b.y][b.x]);
	}
	private int checkChain(int xx,int yy)
	{
		int sum = 0;
		Vector<Point> queue = new Vector<Point>();
		queue.add(new Point(xx,yy));
		do
		{
			int x=queue.firstElement().x;
			int y=queue.firstElement().y;
			queue.remove(queue.firstElement());
			int xStart,xEnd,yStart,yEnd,ySum=0,xSum=0;
			if(x-2<0)
				xStart = 0;
			else
				xStart = x-2;
			
			xEnd = x;
			if(x+1>chestWidth-1)
				xEnd = x-2;
			else if(x+2>chestWidth-1)
				xEnd = x-1;

			if(y-2<0)
				yStart = 0;
			else
				yStart = y-2;
			
			yEnd = y;
			if(y+1>chestHeight-1)
				yEnd = y-2;
			else if(y+2>chestHeight-1)
				yEnd = y-1;

			
			for(int i=yStart;i<=yEnd;i++)
				if(chest[i][x].getType()==chest[i+1][x].getType()&&chest[i+2][x].getType()==chest[i+1][x].getType())
				{
					if(!chest[i][x].getState())
					{
						queue.add(new Point(x,i));
						ySum++;
						chest[i][x].setState(true);
					}
					if(!chest[i+1][x].getState())
					{
						queue.add(new Point(x,i+1));
						ySum++;
						chest[i+1][x].setState(true);
					}
					if(!chest[i+2][x].getState())
					{
						queue.add(new Point(x,i+2));
						ySum++;
						chest[i+2][x].setState(true);
					}
				}
			for(int j=xStart;j<=xEnd;j++)
				if(chest[y][j].getType()==chest[y][j+1].getType()&&chest[y][j+2].getType()==chest[y][j+1].getType())
				{
					if(!chest[y][j].getState())
					{
						queue.add(new Point(j,y));
						xSum++;
						chest[y][j].setState(true);
					}
					if(!chest[y][j+1].getState())
					{
						queue.add(new Point(j+1,y));
						xSum++;
						chest[y][j+1].setState(true);
					}
					if(!chest[y][j+2].getState())
					{
						queue.add(new Point(j+2,y));
						xSum++;
						chest[y][j+2].setState(true);
					}		
				}
			sum += xSum;
			sum += ySum;
		}while(!queue.isEmpty());
		return sum;
	}
	public boolean breakChain()//will return whether there are 3 straight ball or not
	{
		boolean anyChain = false;
		for(int i=0;i<chestHeight;i++)
			for(int j=0;j<chestWidth;j++)
				chest[i][j].setState(false);
	
		
		for(int i=0;i<chestHeight;i++)
			for(int j=0;j<chestWidth;j++)
			{
				int chainSize = 0;
				if(!chest[i][j].getState())
					chainSize = checkChain(j, i);
				if(chainSize>0)
				{
					anyChain = true;
					Log.d("ChainSize", String.valueOf(chainSize));
				}
			}

		return anyChain;
		
	}

}
