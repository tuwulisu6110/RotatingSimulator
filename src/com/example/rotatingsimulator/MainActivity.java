package com.example.rotatingsimulator;

import view.ButtomChest;
import view.UpperChest;
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
	RelativeLayout.LayoutParams params;
	RelativeLayout mainView;
	Kernel kernel;

	private Point chestDimension;
	private Point ballSize;
	private boolean controlABall;
	private Point controlBallId;
	private Point previousBallId;
	private Point pressedBallId;
	private int mainViewHeight;
	private boolean firstRun;
	private ButtomChest buttomChest;
	private UpperChest upperChest;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		//data setting
		chestDimension = new Point(6,5);
		
		//data setting end
		
		setContentView(R.layout.activity_main);
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
		
		mainView.setOnTouchListener(this);
		
		
		firstRun = true;
		
		final RelativeLayout layout = (RelativeLayout) findViewById(R.id.mainView);
		final ViewTreeObserver observer= layout.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(
		    new ViewTreeObserver.OnGlobalLayoutListener() {
		        @Override
		            public void onGlobalLayout() {
			        	if(firstRun)
			        	{
			                mainViewHeight = layout.getHeight();
			        		createChest();
			                firstRun = false;
			        	}
		            }
		        });
		
		
		
	}
	
	private void createChest()
	{
		
		Display display = getWindowManager().getDefaultDisplay();
		Point screenSize = new Point();
		display.getSize(screenSize);
		buttomChest = new ButtomChest(this,kernel,mainView,screenSize,mainViewHeight);
		buttomChest.createChest();
		upperChest = new UpperChest(this,kernel,mainView,screenSize,mainViewHeight);
		upperChest.createChest();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		// TODO Auto-generated method stub
		int x=(int)event.getX(),y=(int)event.getY();
		params.setMargins(x-40, y-40, 0, 0);
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
						controlABall = true;
					}
					if(event.getAction() == MotionEvent.ACTION_MOVE)
					{
						pressedBallId.set(j, i);
						if(i!=previousBallId.y||j!=previousBallId.x)
						{
							kernel.exchange(new Point(j,i), previousBallId);
							buttomChest.setChestBallImageSource(j, i, -1);
							buttomChest.refreshImage(previousBallId);
							previousBallId.set(j, i);
						}
					}
				}
			}
		if(event.getAction() == MotionEvent.ACTION_MOVE&&touchABall)
		{
			buttomChest.setRoamingBallParam(params);
		}
		if(event.getAction() == MotionEvent.ACTION_UP&&controlABall)
		{
			controlABall = false;
			
			buttomChest.roamingEnd();
			buttomChest.refreshImage(pressedBallId);
			
			buttomChest.breakChain();

			
			
		}
		return true;
	}
}
