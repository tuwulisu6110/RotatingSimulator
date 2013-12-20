package com.example.rotatingsimulator;

import kernel.Ball;
import kernel.Kernel;
import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.text.style.UpdateLayout;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity// implements OnTouchListener
{
	TextView testPrintOut;
	RelativeLayout.LayoutParams params;
	RelativeLayout mainView;
	ImageView iv;
	Kernel kernel;
	private ImageView chestImageView[][];
	private RelativeLayout.LayoutParams chestParams[][];
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		testPrintOut = (TextView) findViewById(R.id.testPrintOut);
		mainView = (RelativeLayout)findViewById(R.id.mainView);
		iv = new ImageView(this);
		iv.setImageResource(R.drawable.ic_launcher);
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(100, 100, 0, 0);
		iv.setLayoutParams(params);
		mainView.addView(iv);
		kernel = new Kernel(this,mainView);
		//mainView.setOnTouchListener(this);
		testPrintOut.setText("start");
		createChest();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent e)
	{
		String s=String.valueOf(e.getX())+" "+String.valueOf(e.getY());
		testPrintOut.setText(s);
		int x=(int)e.getX(),y=(int)e.getY();
		params.leftMargin=x;
		params.topMargin=y;
		//iv.setPadding(x, y, 0, 0);
		//mainView.updateViewLayout(iv, params);
		return true;
	}
	private void createChest()
	{
		int h=kernel.getChestHeight(),w=kernel.getChestWidth();
		chestImageView = new ImageView[h][w];
		chestParams = new RelativeLayout.LayoutParams[h][w];
		for(int i=0;i<kernel.getChestHeight();i++)
			for(int j=0;j<kernel.getChestWidth();j++)
			{
				chestImageView[i][j] = new ImageView(this);
				chestParams[i][j] = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
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
				chestParams[i][j].width = 50;
				chestParams[i][j].height = 50;
				chestParams[i][j].setMargins(50*j, 50*i, 0, 0);
				chestImageView[i][j].setLayoutParams(chestParams[i][j]);
				mainView.addView(chestImageView[i][j]);
			}
	}
	/*@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		// TODO Auto-generated method stub
		String s=String.valueOf(event.getX())+" "+String.valueOf(event.getY());
		testPrintOut.setText(s);
		int x=(int)event.getX(),y=(int)event.getY();
		//params.setMargins(x, y, 0, 0);
		iv.setPadding(x, y, 0, 0);
		//mainView.updateViewLayout(iv, params);
		
		return false;
	}*/

}
