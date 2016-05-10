package keyword.keyboard.animation;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout.LayoutParams;
/******
 * 
 * @author asus
 *
 */
public class VisibilityAnimation extends Animation{
	
	private View view;
	private int initialHeight;
	private int ohight = 0;
	private View ovight ;
	private View showview;
	
	private boolean iswrap = true;

	public VisibilityAnimation(View view, int hight) {
		this.view = view;
		view.setVisibility(View.VISIBLE);
		this.initialHeight = hight;
		this.setDuration(100);
		this.showview = view;
	}
	
	public VisibilityAnimation(View view, int hight,boolean iswrap) {
		this.view = view;
		view.setVisibility(View.VISIBLE);
		this.initialHeight = hight;
		this.setDuration(100);
		this.showview = view;
		this.iswrap = iswrap;
	}
	 
	
	public VisibilityAnimation(View view, View showview ,int hight,int ohight){
		
		this.ohight = ohight;
		this.view = view;
		view.setVisibility(View.VISIBLE);
		this.initialHeight = hight;
		this.setDuration(200);
		this.showview = showview;
	}

	@Override
	protected void applyTransformation(float interpolatedTime,
			Transformation t) {
		// TODO Auto-generated method stub
		showview.setAlpha(interpolatedTime);
		if (interpolatedTime == 1) {
			if(iswrap)
				view.getLayoutParams().height =LayoutParams.WRAP_CONTENT;
			view.requestLayout();
		} else if(interpolatedTime<1){
			view.getLayoutParams().height = (int) (initialHeight * interpolatedTime)+ohight-(int)(ohight*interpolatedTime);
			view.requestLayout();
		}
	}

	@Override
	public boolean willChangeBounds() {
		return true;
	}

}
