package keyword.keyboard.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/*******
 * 
 * @author asus
 *
 */
public class GoneAnimation extends Animation{
	
	private View view;
	private int initialHeight;
	private int height;
	private View hightView ;
	private int ap = 1;

	public GoneAnimation(View view) {
		this.view = view;
		this.initialHeight = view.getMeasuredHeight();
		this.hightView = view;
		this.setDuration(100);
	}
	
	public GoneAnimation(View view,View hideviwe ,int time ,int height) {
		this.view = view;
		this.initialHeight = view.getMeasuredHeight();
		this.height = height;
		this.setDuration(time);
		this.hightView = hideviwe;
	}
	
	public GoneAnimation(View view,View hideviwe ,int time ,int height,boolean ap) {
		this.view = view;
		this.initialHeight = view.getMeasuredHeight();
		this.height = height;
		this.setDuration(time);
		this.hightView = hideviwe;
		this.hightView.setVisibility(View.GONE);
		this.ap =ap?1:0;
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime,
			Transformation t) {
		// TODO Auto-generated method stub
		if (interpolatedTime == 1) {
			view.getLayoutParams().height = height;
			view.requestLayout();
			hightView.setVisibility(View.GONE);
			hightView.setAlpha(1);
		} else {
			view.getLayoutParams().height = initialHeight- (int) (initialHeight * interpolatedTime)+(int)(height*interpolatedTime);
			view.requestLayout();
			//hightView.setAlpha((1-interpolatedTime)*ap);
		}
		
	}

	@Override
	public boolean willChangeBounds() {
		return true;
	}
}
