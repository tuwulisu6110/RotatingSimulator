package kernel;

import com.example.rotatingsimulator.R;

import android.R.string;
import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Ball
{
	public static final int FIRE = 0;
	public static final int WATER = 1;
	public static final int GRASS = 2;
	public static final int LIGHT = 3;
	public static final int DARK = 4;
	public static final int HEART = 5;
	private int type;
	private ImageView imageView;
	private RelativeLayout.LayoutParams params;
	public Ball(int t,Context context)
	{
		type = t;
		imageView = new ImageView(context);
		params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		setImage();
	}
	public void setType(int t)
	{
		type = t;
		setImage();
	}
	private void setImage()
	{
		switch(type)
		{
		case 0:
			imageView.setImageResource(R.drawable.fire);
			break;
		case 1:
			imageView.setImageResource(R.drawable.water);
			break;
		case 2:
			imageView.setImageResource(R.drawable.grass);
			break;
		case 3:
			imageView.setImageResource(R.drawable.light);
			break;
		case 4:
			imageView.setImageResource(R.drawable.dark);
			break;
		case 5:
			imageView.setImageResource(R.drawable.heart);
			break;
		}
	}
	public ImageView getImageView()
	{
		return imageView;
	}
	public RelativeLayout.LayoutParams getLayoutParams()
	{
		return params;
	}
}
