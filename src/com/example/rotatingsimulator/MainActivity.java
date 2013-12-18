package com.example.rotatingsimulator;

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
		//mainView.setOnTouchListener(this);
		testPrintOut.setText("start");
		
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
