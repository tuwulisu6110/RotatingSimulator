package com.example.rotatingsimulator;

import view.ButtomChest;
import junit.framework.Assert;
import kernel.Ball;
import kernel.Kernel;
import android.R.string;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements OnTouchListener
{
	TextView testPrintOut;
	RelativeLayout.LayoutParams params;
	RelativeLayout mainView;
	Kernel kernel;
	private ImageView chestImageView[][];
	private ImageView roamingBallView;
	private RelativeLayout.LayoutParams ballParams[][];
	private AnimationSet animationSet[][];
	private Point chestDimension;
	private Point ballSize;
	private boolean controlABall;
	private Point controlBallId;
	private Point previousBallId;
	private Point pressedBallId;
	private int mainViewHeight;
	private boolean firstRun;
	private int numOfStartedAnimation;
	private int numOfEndedAnimaion;
	private ButtomChest buttomChest;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		//data setting
		chestDimension = new Point(6,5);
		
		//data setting end
		
		setContentView(R.layout.activity_main);
		testPrintOut = (TextView) findViewById(R.id.testPrintOut);
		mainView = (RelativeLayout)findViewById(R.id.mainView);
		
		Display display = getWindowManager().getDefaultDisplay();
		Point screenSize = new Point();
		display.getSize(screenSize);
		
		
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.width = screenSize.x/6;
		params.height = screenSize.x/6;
		
		ballSize = new Point(screenSize.x/6, screenSize.x/6);
		
		kernel = new Kernel(chestDimension);
		
		controlBallId = new Point();
		previousBallId = new Point();
		pressedBallId = new Point();
		roamingBallView = new ImageView(this);
		
		mainView.setOnTouchListener(this);
		testPrintOut.setText("start");
		
		animationSet = new AnimationSet[kernel.getChestHeight()][kernel.getChestWidth()];
		for(int i=0;i<kernel.getChestHeight();i++)
			for(int j=0;j<kernel.getChestWidth();j++)
				animationSet[i][j] = new AnimationSet(true);
		
		firstRun = true;
		
		final RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainView);
		final ViewTreeObserver observer= layout.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(
		    new ViewTreeObserver.OnGlobalLayoutListener() {
		        @Override
		            public void onGlobalLayout() {
		                //Log.d("Log", "Height: " + layout.getHeight());
			        	if(firstRun)
			        	{
			                mainViewHeight = layout.getHeight();
			                //createChest();
			        		createChest();
			                firstRun = false;
			        	}
		            }
		        });
		
		
		
	}
	
	private int typeRefImage(int type)
	{
		Assert.assertTrue(String.valueOf(type), type == Ball.FIRE || type == Ball.WATER || type == Ball.GRASS || type == Ball.LIGHT || type == Ball.DARK || type == Ball.HEART );
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
		}
		return -1;
	}
	
	private void createChest()
	{
		
		Display display = getWindowManager().getDefaultDisplay();
		Point screenSize = new Point();
		display.getSize(screenSize);
		buttomChest = new ButtomChest(this,kernel,mainView,screenSize,mainViewHeight);
		/*
		int h=kernel.getChestHeight(),w=kernel.getChestWidth();
		chestImageView = new ImageView[h][w];
		ballParams = new RelativeLayout.LayoutParams[h][w];
		for(int i=0;i<kernel.getChestHeight();i++)
		{
			for(int j=0;j<kernel.getChestWidth();j++)
			{
				chestImageView[i][j] = new ImageView(this);
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
		*/
	}
/*
	private void resetImageViewPosition(Point target)
	{
		Assert.assertTrue(target.toString(), target.x>=0&&target.x<chestDimension.x&&target.y>=0&&target.y<chestDimension.y);
		int i = target.y,j = target.x;
		chestImageView[i][j].setLayoutParams(ballParams[i][j]);
	}
	private void refreshImage(Point target)
	{
		Assert.assertTrue(target.toString(), target.x>=0&&target.x<chestDimension.x&&target.y>=0&&target.y<chestDimension.y);
		int type = kernel.getTypeByIndex(target.x, target.y);
		chestImageView[target.y][target.x].setImageResource(typeRefImage(type));
	}
	private void resetBallMarginsToDefaultPosition(Point target)
	{
		Assert.assertTrue(target.toString(), target.x>=0&&target.x<chestDimension.x&&target.y>=0&&target.y<chestDimension.y);
		int i = target.y,j = target.x;
		ballParams[i][j].setMargins(ballParams[i][j].width*j, mainViewHeight-(ballParams[i][j].height*5)+ ballParams[i][j].height*i, 0, 0);
	}
	private void dropingBallRutine()
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
	}*/
	/*
	private void checkChain()
	{
		if(kernel.breakChain())
		{
			testPrintOut.setText("HaveChain");
			dropingBallRutine();

		}
		else
			testPrintOut.setText("NoChain");
	}*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		// TODO Auto-generated method stub
		String s=String.valueOf(event.getX())+" "+String.valueOf(event.getY());
		//testPrintOut.setText(s);
		//Log.d("mainview",s);
		int x=(int)event.getX(),y=(int)event.getY();
		params.setMargins(x-40, y-40, 0, 0);
		//iv.setPadding(x, y, 0, 0);
		
		boolean touchABall = false;
		for(int i=0;i<kernel.getChestHeight();i++)
			for(int j=0;j<kernel.getChestWidth();j++)
			{
				if(buttomChest.clickBallCheckout(new Point(j,i), x, y))
				{
					touchABall = true;
					
					if(event.getAction() == MotionEvent.ACTION_DOWN)
					{
						controlBallId.set(j, i);
						previousBallId.set(j, i);
						buttomChest.setChestBallImageSource(j, i, -1);
						buttomChest.setRoamingBallImageSource(kernel.getTypeByIndex(j, i));
						buttomChest.roamingStart();
						/*
						chestImageView[i][j].setImageResource(R.drawable.white);
						int type = kernel.getTypeByIndex(j, i);
						roamingBallView.setImageResource(typeRefImage(type));
						mainView.addView(roamingBallView, params);*/
						controlABall = true;
					}
					if(event.getAction() == MotionEvent.ACTION_MOVE)
					{
						pressedBallId.set(j, i);
						if(i!=previousBallId.y||j!=previousBallId.x)
						{
							kernel.exchange(new Point(j,i), previousBallId);
							buttomChest.setChestBallImageSource(j, i, -1);
							//chestImageView[i][j].setImageResource(R.drawable.white);
							buttomChest.refreshImage(previousBallId);
							previousBallId.set(j, i);
						}
					}
				}
			}
		if(event.getAction() == MotionEvent.ACTION_MOVE&&touchABall)
		{
			buttomChest.setRoamingBallParam(params);
			//mainView.updateViewLayout(roamingBallView, params);
		}
		if(event.getAction() == MotionEvent.ACTION_UP&&controlABall)
		{
			controlABall = false;
			buttomChest.refreshImage(pressedBallId);
			/*chestImageView[pressedBallId.y][pressedBallId.x].setImageResource(
					typeRefImage(kernel.getTypeByIndex(pressedBallId.x, pressedBallId.y)));*/
			if(kernel.breakChain())
				buttomChest.dropingBallRutine();

			buttomChest.roamingEnd();
			
		}
		return true;
	}
}
