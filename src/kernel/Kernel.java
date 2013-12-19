package kernel;

import java.util.Random;

import android.content.Context;
import android.widget.RelativeLayout;

public class Kernel 
{
	private Ball chest[][];
	public Kernel(Context context,RelativeLayout mainView) 
	{
		// TODO Auto-generated constructor stub
		chest = new Ball[5][6];
		for(int i=0;i<5;i++)
			for(int j=0;j<6;j++)
				chest[i][j] = new Ball((int)(Math.random()*6),context); 
		initView(mainView);
	}
	private void initView(RelativeLayout mainView)
	{
		int width = mainView.getWidth() , height = mainView.getHeight();
		for(int i=0;i<5;i++)
			for(int j=0;j<6;j++)
			{
				RelativeLayout.LayoutParams params = chest[i][j].getLayoutParams();
				params.height=50;
				params.width=50;
				params.setMargins(50*j, 50*i, 0, 0);
				chest[i][j].getImageView().setLayoutParams(params);
				mainView.addView(chest[i][j].getImageView());
			}
	}

}
