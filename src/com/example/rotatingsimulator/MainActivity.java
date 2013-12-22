package com.example.rotatingsimulator;

import junit.framework.Assert;
import kernel.Ball;
import kernel.Kernel;
import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Point;
import android.text.style.UpdateLayout;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
	private Point chestDimension;
	private boolean controlABall;
	private Point controlBallId;
	private Point previousBallId;
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
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		//mainView.addView(iv);
		kernel = new Kernel(chestDimension);
		mainView.setOnTouchListener(this);
		testPrintOut.setText("start");
		createChest();
		controlBallId = new Point();
		previousBallId = new Point();
		roamingBallView = new ImageView(this);
		
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
				ballParams[i][j].setMargins(ballParams[i][j].width*j, ballParams[i][j].height*i, 0, 0);
				chestImageView[i][j].setLayoutParams(ballParams[i][j]);
				mainView.addView(chestImageView[i][j]);
				
			}
		}
	}

	private void refreshImage(Point target)
	{
		Assert.assertTrue(target.toString(), target.x>=0&&target.x<chestDimension.x&&target.y>=0&&target.y<chestDimension.y);
		int type = kernel.getTypeByIndex(target.x, target.y);
		chestImageView[target.y][target.x].setImageResource(typeRefImage(type));
	}
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
		Log.d("mainview",s);
		int x=(int)event.getX(),y=(int)event.getY();
		params.setMargins(x-40, y-40, 0, 0);
		//iv.setPadding(x, y, 0, 0);
		
		boolean touchABall = false;
		for(int i=0;i<kernel.getChestHeight();i++)
			for(int j=0;j<kernel.getChestWidth();j++)
			{
				//Log.d("pos",String.valueOf(chestImageView[i][j].getHeight()));
				if(x>chestImageView[i][j].getLeft()&&x<chestImageView[i][j].getLeft()+chestImageView[i][j].getWidth())
					if(y>chestImageView[i][j].getTop()&&y<chestImageView[i][j].getTop()+chestImageView[i][j].getHeight())
					{
						testPrintOut.setText(String.valueOf(j)+" "+String.valueOf(i));
						touchABall = true;
						if(event.getAction() == MotionEvent.ACTION_DOWN)
						{
							controlBallId.set(j, i);
							previousBallId.set(j, i);
							chestImageView[i][j].setImageResource(R.drawable.white);
							int type = kernel.getTypeByIndex(j, i);
							roamingBallView.setImageResource(typeRefImage(type));
							mainView.addView(roamingBallView, params);
							controlABall = true;
						}
						if(event.getAction() == MotionEvent.ACTION_MOVE)
						{
							if(i!=previousBallId.y&&j!=previousBallId.x)
							{
								kernel.exchange(new Point(j,i), previousBallId);
								chestImageView[i][j].setImageResource(R.drawable.white);
								refreshImage(previousBallId);
								previousBallId.set(j, i);
							}
						}
					}
			}
		if(event.getAction() == MotionEvent.ACTION_MOVE&&touchABall)
		{
			mainView.updateViewLayout(roamingBallView, params);
		}
		if(event.getAction() == MotionEvent.ACTION_UP&&controlABall)
		{
			controlABall = false;
			mainView.removeView(roamingBallView);
			
		}
		return true;
	}

}
