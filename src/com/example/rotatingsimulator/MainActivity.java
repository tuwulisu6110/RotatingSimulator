package com.example.rotatingsimulator;

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
	ImageView iv;
	Kernel kernel;
	private ImageView chestImageView[][];
	private RelativeLayout.LayoutParams ballParams[][];
	private Point chestDimension;
	private boolean controlABall;
	private Point controlBallId;
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
		iv = new ImageView(this);
		iv.setImageResource(R.drawable.ic_launcher);
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(100, 100, 0, 0);
		iv.setLayoutParams(params);
		//mainView.addView(iv);
		kernel = new Kernel(chestDimension);
		mainView.setOnTouchListener(this);
		testPrintOut.setText("start");
		createChest();
		controlBallId = new Point();

		
	}
	/*public void sbPressed()//some ball be pressed, called by ImageView i think
	{
		boolean multitouchCheck = false;
		for(int i=0;i<kernel.getChestHeight();i++)
			for(int j=0;j<kernel.getChestWidth();j++)
			{
				if(chestImageView[i][j].isPressed())
				{
					multitouchCheck = true;
					chestImageView[i][j].setPress(false);
					Log.d("ball",String.valueOf(j)+" "+String.valueOf(i));
					testPrintOut.setText(String.valueOf(j)+" "+String.valueOf(i));
				}
			}
	}
	public void resetAllPress()
	{
		for(int i=0;i<kernel.getChestHeight();i++)
			for(int j=0;j<kernel.getChestWidth();j++)
			{
				chestImageView[i][j].setPress(false);
			}
	}*/
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
				switch(type)
				{
				case Ball.FIRE:
					chestImageView[i][j].setImageResource(R.drawable.fire);
					break;
				case Ball.WATER:
					chestImageView[i][j].setImageResource(R.drawable.water);
					break;
				case Ball.GRASS:
					chestImageView[i][j].setImageResource(R.drawable.grass);
					break;
				case Ball.LIGHT:
					chestImageView[i][j].setImageResource(R.drawable.light);
					break;
				case Ball.DARK:
					chestImageView[i][j].setImageResource(R.drawable.dark);
					break;
				case Ball.HEART:
					chestImageView[i][j].setImageResource(R.drawable.heart);
					break;
				}
				
				ballParams[i][j].width = screenSize.x/6;
				ballParams[i][j].height = screenSize.x/6;
				ballParams[i][j].setMargins(ballParams[i][j].width*j, ballParams[i][j].height*i, 0, 0);
				chestImageView[i][j].setLayoutParams(ballParams[i][j]);
				mainView.addView(chestImageView[i][j]);
				
			}
		}
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
		
		controlABall = false;
		for(int i=0;i<kernel.getChestHeight();i++)
			for(int j=0;j<kernel.getChestWidth();j++)
			{
				//Log.d("pos",String.valueOf(chestImageView[i][j].getHeight()));
				if(x>chestImageView[i][j].getLeft()&&x<chestImageView[i][j].getLeft()+chestImageView[i][j].getWidth())
					if(y>chestImageView[i][j].getTop()&&y<chestImageView[i][j].getTop()+chestImageView[i][j].getHeight())
					{
						testPrintOut.setText(String.valueOf(j)+" "+String.valueOf(i));
						controlABall = true;
						if(event.getAction() == MotionEvent.ACTION_DOWN)
						{
							controlBallId.x = j;
							controlBallId.y = i;
						}
					}
			}
		if(event.getAction() == MotionEvent.ACTION_MOVE)
			mainView.updateViewLayout(chestImageView[controlBallId.y][controlBallId.x], params);
		return true;
	}

}
