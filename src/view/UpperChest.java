package view;

import kernel.Kernel;
import android.content.Context;
import android.graphics.Point;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class UpperChest extends ButtomChest 
{

	public UpperChest(Context c, Kernel k, RelativeLayout mv, Point ss, int h) 
	{
		super(c, k, mv, ss, h);
		// TODO Auto-generated constructor stub
		
	}
	@Override
	public void createChest()
	{
		chestImageView = new ImageView[kernel.getChestHeight()][kernel.getChestWidth()];
		ballParams = new RelativeLayout.LayoutParams[kernel.getChestHeight()][kernel.getChestWidth()];
		for(int i=0;i<kernel.getChestHeight();i++)
		{
			for(int j=0;j<kernel.getChestWidth();j++)
			{
				chestImageView[i][j] = new ImageView(context);
				ballParams[i][j] = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

				int type = kernel.getTypeByIndex(j, i);
				chestImageView[i][j].setImageResource(typeRefImage(type));
				
				ballParams[i][j].width = screenSize.x/6;
				ballParams[i][j].height = screenSize.x/6;
				ballParams[i][j].setMargins(ballParams[i][j].width*j, ballParams[i][j].height*4 - ballParams[i][j].height*i, 0, 0);
				//
				chestImageView[i][j].setLayoutParams(ballParams[i][j]);
				mainView.addView(chestImageView[i][j]);
				
			}
		}
		isChestExist = true;
	}
	@Override
	protected void dropingBallRutine()
	{
		int numOfEmpty[] = new int[kernel.getChestWidth()];
		int startDropPosition[][] = new int[kernel.getChestHeight()][kernel.getChestWidth()];
		
		numOfEndedAnimaion = 0;
		numOfStartedAnimation = 0;
		for(int j=0;j<kernel.getChestWidth();j++)
		{
			numOfEmpty[j] = 0;
			
		}
		for(int i=kernel.getChestHeight()-1;i>=0;i--)
			for(int j=0;j<kernel.getChestWidth();j++)
			{
				startDropPosition[i][j] = 0;
				if(kernel.getStateByIndex(j, i))
				{
					numOfEmpty[j]++;
					startDropPosition[i][j] = numOfEmpty[j];
					kernel.generateBall(new Point(j,i));
					refreshImage(new Point(j,i));
				}
				else if(numOfEmpty[j]!=0)
				{
					animationSet[i][j].reset();
					numOfStartedAnimation++;
					TranslateAnimation translateAnimation = new TranslateAnimation(
							Animation.ABSOLUTE,0,
							Animation.ABSOLUTE,0,
							Animation.ABSOLUTE,0,
							Animation.ABSOLUTE, -1 * ballSize.y*numOfEmpty[j]);
					translateAnimation.setDuration(800);
					translateAnimation.setAnimationListener(this);
					animationSet[i][j].addAnimation(translateAnimation);
					//animationSet[i][j].setFillAfter(true);						
					chestImageView[i][j].startAnimation(translateAnimation);
					for(int k=i;k<i+numOfEmpty[j];k++)
						kernel.exchange(new Point(j,k),new Point(j,k+1));
				}
				
			}
		for(int i=kernel.getChestHeight()-1;i>=0;i--)
			for(int j=0;j<kernel.getChestWidth();j++)
			{
				if(kernel.getStateByIndex(j, i))
				{
					
					numOfStartedAnimation++;
					TranslateAnimation translateAnimation = new TranslateAnimation(
							Animation.ABSOLUTE,0,
							Animation.ABSOLUTE,0,
							Animation.ABSOLUTE,1*ballSize.y*(i+startDropPosition[i][j]),
							Animation.ABSOLUTE,1*ballSize.y*(i+1-(numOfEmpty[j]-startDropPosition[i][j]+1)));
					translateAnimation.setDuration(800);
					translateAnimation.setAnimationListener(this);
					animationSet[i][j].addAnimation(translateAnimation);
					//animationSet[i][j].setFillAfter(true);
					chestImageView[i][j].startAnimation(translateAnimation);
				}
			}
	}
}
