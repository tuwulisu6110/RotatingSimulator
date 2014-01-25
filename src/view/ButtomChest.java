package view;

import junit.framework.Assert;
import kernel.Ball;
import kernel.Kernel;

import com.example.rotatingsimulator.R;

import android.content.Context;
import android.graphics.Point;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ButtomChest implements AnimationListener
{
	private Point chestDimension;
	private ImageView chestImageView[][];
	private RelativeLayout.LayoutParams ballParams[][];
	private AnimationSet animationSet[][];
	private RelativeLayout mainView;
	private Point ballSize;
	private Point screenSize;
	private int mainViewHeight;
	private Kernel kernel;
	private Context context;
	private int numOfStartedAnimation;
	private int numOfEndedAnimaion;
	private ImageView roamingBallView;
	
	public ButtomChest(Context c,Kernel k,RelativeLayout mv,Point ss,int h) 
	{
		// TODO Auto-generated constructor stub
		chestDimension = new Point(6,5);
		context = c;
		kernel = k;
		mainView = mv;
		screenSize = ss;
		ballSize = new Point(screenSize.x/6, screenSize.x/6);
		mainViewHeight = h;
		animationSet = new AnimationSet[kernel.getChestHeight()][kernel.getChestWidth()];
		for(int i=0;i<kernel.getChestHeight();i++)
			for(int j=0;j<kernel.getChestWidth();j++)
				animationSet[i][j] = new AnimationSet(true);
		roamingBallView = new ImageView(context);
		createChest();
		
	}
	public boolean clickBallCheckout(Point id,int x, int y)
	{
		int i = id.y , j = id.x;
		if(x>chestImageView[i][j].getLeft()&&x<chestImageView[i][j].getLeft()+chestImageView[i][j].getWidth())
			if(y>chestImageView[i][j].getTop()&&y<chestImageView[i][j].getTop()+chestImageView[i][j].getHeight())
				return true;
		return false;
	}
	public void roamingStart()
	{
		mainView.addView(roamingBallView);
	}
	public void setRoamingBallParam(RelativeLayout.LayoutParams params)
	{
		mainView.updateViewLayout(roamingBallView, params);
	}
	public void roamingEnd()
	{
		mainView.removeView(roamingBallView);
	}
	public void setChestBallImageSource(int x,int y,int type)
	{
		chestImageView[y][x].setImageResource(typeRefImage(type));
	}
	public void setRoamingBallImageSource(int type)
	{
		roamingBallView.setImageResource(typeRefImage(type));
	}
	public void refreshImage(Point target)
	{
		Assert.assertTrue(target.toString(), target.x>=0&&target.x<chestDimension.x&&target.y>=0&&target.y<chestDimension.y);
		int type = kernel.getTypeByIndex(target.x, target.y);
		chestImageView[target.y][target.x].setImageResource(typeRefImage(type));
	}
	private void createChest()
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
				ballParams[i][j].setMargins(ballParams[i][j].width*j, mainViewHeight-(ballParams[i][j].height*5)+ ballParams[i][j].height*i, 0, 0);
				//
				chestImageView[i][j].setLayoutParams(ballParams[i][j]);
				mainView.addView(chestImageView[i][j]);
				
			}
		}
	}
	private void resetImageViewPosition(Point target)
	{
		Assert.assertTrue(target.toString(), target.x>=0&&target.x<chestDimension.x&&target.y>=0&&target.y<chestDimension.y);
		int i = target.y,j = target.x;
		chestImageView[i][j].setLayoutParams(ballParams[i][j]);
	}
	private void resetBallMarginsToDefaultPosition(Point target)
	{
		Assert.assertTrue(target.toString(), target.x>=0&&target.x<chestDimension.x&&target.y>=0&&target.y<chestDimension.y);
		int i = target.y,j = target.x;
		ballParams[i][j].setMargins(ballParams[i][j].width*j, mainViewHeight-(ballParams[i][j].height*5)+ ballParams[i][j].height*i, 0, 0);
	}

	public void dropingBallRutine()
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
							Animation.ABSOLUTE,ballSize.y*numOfEmpty[j]);
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
							Animation.ABSOLUTE,-1*ballSize.y*(i+startDropPosition[i][j]),
							Animation.ABSOLUTE,-1*ballSize.y*(i+1-(numOfEmpty[j]-startDropPosition[i][j]+1)));
					translateAnimation.setDuration(800);
					translateAnimation.setAnimationListener(this);
					animationSet[i][j].addAnimation(translateAnimation);
					//animationSet[i][j].setFillAfter(true);
					chestImageView[i][j].startAnimation(translateAnimation);
				}
			}
	}
	private int typeRefImage(int type)
	{
		Assert.assertTrue(String.valueOf(type), type == Ball.FIRE || type == Ball.WATER || type == Ball.GRASS || type == Ball.LIGHT || type == Ball.DARK || type == Ball.HEART || type == -1);
		switch(type)
		{
		case Ball.FIRE:
			return R.drawable.fire;
		case Ball.WATER:
			return R.drawable.water;
		case Ball.GRASS:
			return R.drawable.grass;
		case Ball.LIGHT:
			return R.drawable.light;
		case Ball.DARK:
			return R.drawable.dark;
		case Ball.HEART:
			return R.drawable.heart;
		case -1:
			return R.drawable.white;
		}
		return -1;
	}
	@Override
	public void onAnimationEnd(Animation animation) 
	{
		// TODO Auto-generated method stub
		numOfEndedAnimaion++;
		if(numOfEndedAnimaion == numOfStartedAnimation)
		{
			Point p = new Point();
			for(int i=0;i<kernel.getChestHeight();i++)
				for(int j=0;j<kernel.getChestWidth();j++)
				{
					p.set(j, i);
					resetBallMarginsToDefaultPosition(p);
					resetImageViewPosition(p);
					refreshImage(p);
				}
			
			if(kernel.breakChain())
				dropingBallRutine();
			
		}
	}
	@Override
	public void onAnimationRepeat(Animation animation) 
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onAnimationStart(Animation animation) 
	{
		// TODO Auto-generated method stub
		
	}

}
