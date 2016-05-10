package keyword.keyboard;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.Keyboard.Key;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;

import java.util.List;

import keyword.keyboard.animation.AnimationListen;
import keyword.keyboard.animation.GoneAnimation;
import keyword.keyboard.animation.VisibilityAnimation;


public class KeyboardUtil {

	private KeyboardView keyboardView;
	private Keyboard k2;// 数字键盘
	private EditText edit;
	private boolean showleftAndright = true;
	
	private MyOnkeys myOnkeys;
	
	private boolean isx = true;//（用于身份证号码输入时显示）

	public KeyboardUtil(Activity act, Context ctx) {

		k2 = new Keyboard(ctx, R.xml.symbols);
		keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
		keyboardView.setKeyboard(k2);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);
		
	}

    /***********
     *
     * @param act
     * @param ctx
     * @param isx
     */
	public KeyboardUtil(Activity act, Context ctx,boolean isx) {
		this.isx = isx;
		k2 = new Keyboard(ctx, R.xml.symbols);
		keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
		keyboardView.setKeyboard(k2);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);
		
	}



    /************
     *
     * @param act
     * @param ctx
     * @param numb 自定义修改加减的数字
     */
	public KeyboardUtil(Activity act, Context ctx,int numb) {
		k2 = new Keyboard(ctx, R.xml.symbols);
		keyboardView = (KeyboardView) act.findViewById(R.id.keyboard_view);
		keyboardView.setKeyboard(k2);
		keyboardView.setEnabled(true);
		keyboardView.setPreviewEnabled(false);
		keyboardView.setOnKeyboardActionListener(listener);
		List<Key> keys = k2.getKeys();
		for (Key key:keys) {
			if(key.codes[0] ==57419){
				key.label ="+"+numb;
			}else if(key.codes[0] ==57421){
				key.label ="-"+numb;
				break ;
			}
		}
        showleftAndright = false;
		
	}

    /********
     * 设置当前输入的EditText
     * @param edit
     */
    public void setEditText(EditText edit){
        this.edit = edit;
    }


    /***********
     * 显示左右移动光标的按钮
     */
    public void displayleftAndrightNum(int numb){
        List<Key> keys = k2.getKeys();
        for (Key key:keys) {
            if(key.codes[0] ==57419){
                key.label ="+"+numb;
            }else if(key.codes[0] ==57421){
                key.label ="-"+numb;
                break ;
            }
        }
        showleftAndright = false;
        keyboardView.invalidateKey(3);
        keyboardView.invalidateKey(7);
    }


    /***********
     * 显示左右移动光标的按钮
     */
    public void displayleftAndright(){
        List<Key> keys = k2.getKeys();
        for (Key key:keys) {
            if(key.codes[0] ==57419){
                key.label =null;
            }else if(key.codes[0] ==57421){
                key.label =null;
                break ;
            }
        }
        showleftAndright = true;
        keyboardView.invalidateKey(3);
        keyboardView.invalidateKey(7);

    }
	

	private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
		@Override
		public void swipeUp() {
		}

		@Override
		public void swipeRight() {
		}

		@Override
		public void swipeLeft() {
		}

		@Override
		public void swipeDown() {
		}

		@Override
		public void onText(CharSequence text) {
		}

		@Override
		public void onRelease(int primaryCode) {
		}

		@Override
		public void onPress(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			Editable editable = edit.getText();
			int start = edit.getSelectionStart();
			if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
				hideKeyboard();
			} else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
				if (editable != null && editable.length() > 0) {
					if (start > 0) {
						editable.delete(start - 1, start);
					}
				}
			} else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
				 
				 
			} else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {
				if(isx)
					editable.insert(start,Character.toString((char) 120));
			} else if (primaryCode == 57419) { // go left
				if (start > 0&&showleftAndright) {
                    edit.setSelection(start - 1);

				}else if(myOnkeys!=null){
					myOnkeys.LeftKeys(edit,editable, primaryCode, keyCodes);
				}
			} else if (primaryCode == 57421) { // go right
				if (start < edit.length()&&showleftAndright) {
                    edit.setSelection(start + 1);
				}else if(myOnkeys !=null){
					myOnkeys.RightKeys(edit,editable, primaryCode, keyCodes);
				}
			} else {
				editable.insert(start, Character.toString((char) primaryCode));
			}
			
		}
	};
	private GoneAnimation goneAnimation;


    /********
     * 显示自定义键盘没有动画效果
     */
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    /********
     * 显示自定义的软件盘动画显示效果
     * @param view
     */
    public void showKeyboard(View view) {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
              keyboardView.setVisibility(View.VISIBLE);
              view.getLayoutParams().height=view.getMeasuredHeight();
              VisibilityAnimation animation = new VisibilityAnimation(view, keyboardView,view.getMeasuredHeight(),view.getMeasuredHeight());
              animation.setDuration(300);
              view.startAnimation(animation);
        }
    }
    

    
    
    
    public void setMyOnkeys(MyOnkeys myOnkeys) {
		this.myOnkeys = myOnkeys;
	}

	public boolean hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.GONE);
            return true;
        }else {
        	return false;
        }
    }

    /********
     * 隐藏自定义软件盘，无动画效果
     * @return
     */
    public boolean hideAnimationboard(){
    	
    	return hideKeyboard(keyboardView);
    }

    /******
     * 隐藏软件盘动画效果
     * @param view
     * @return
     */
    public boolean hideKeyboard(View view) {
        int visibility = keyboardView.getVisibility();
       
         
        if (visibility == View.VISIBLE&&goneAnimation ==null) {
            goneAnimation = new GoneAnimation(view,keyboardView,200,(view.getMeasuredHeight()-keyboardView.getMeasuredHeight()));
            goneAnimation.setAnimationListener(animationListen);
        	keyboardView.startAnimation(goneAnimation);
            return true;
        }else {
        	return false;
        }
    }
    /******
     * 隐藏软件盘动画效果加透明对动画
     * @param view
     * @return
     */
    public boolean hideKeyboard(View view,boolean ap) {
        int visibility = keyboardView.getVisibility();
       
         
        if (visibility == View.VISIBLE&&goneAnimation ==null) {
            goneAnimation = new GoneAnimation(view,keyboardView,200,view.getMeasuredHeight()-keyboardView.getMeasuredHeight(),ap);
            goneAnimation.setAnimationListener(animationListen);
        	keyboardView.startAnimation(goneAnimation);
            return true;
        }else {
        	return false;
        }
    }
    
    private AnimationListen animationListen = new AnimationListen(){
    	
    	
    	
    	
    	
		@Override
		public void onAnimationEnd(Animation animation) {
			// TODO Auto-generated method stub
			super.onAnimationEnd(animation);
			goneAnimation = null;
		}
		
		
		
		
    	
    };
    
    private boolean isword(String str){
    	String wordstr = "abcdefghijklmnopqrstuvwxyz";
    	if (wordstr.indexOf(str.toLowerCase())>-1) {
			return true;
		}
    	return false;
    }
    
    
    public interface MyOnkeys{
    	public void RightKeys(View view ,Editable editable, int primaryCode, int[] keyCodes);
    	public void LeftKeys(View view ,Editable editable, int primaryCode, int[] keyCodes);
    }

}
