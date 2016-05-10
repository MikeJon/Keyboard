package keyword.keyboard;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.view.View.OnClickListener;


public class MainActivity extends ActionBarActivity {


    private KeyboardUtil keyboardUtil;

    private EditText edText;
    private EditText editText;
    private View bottomView;
    private int num =1000;  //自加减的数字大小

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edText = (EditText) findViewById(R.id.edit);
        editText = (EditText) findViewById(R.id.edit_num);
        bottomView = findViewById(R.id.rl_bottom);
        //隐藏键盘
        hideInputType(edText);
        hideInputType(editText);
    }


    /**
     * *****
     * 处理返回按钮是否需要隐藏键盘
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyboardUtil != null && keyboardUtil.hideKeyboard(bottomView)) {

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 判断当前系统版本，选择使用何种方式隐藏默认键盘
     */
    private void hideInputType(final EditText edText) {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        edText.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (keyboardUtil == null) {
                    keyboardUtil = new KeyboardUtil(MainActivity.this, MainActivity.this, num);
                    keyboardUtil.setMyOnkeys(myOnkeys);
                }

                keyboardUtil.setEditText(edText);
                switch (edText.getId()) {
                    case R.id.edit:
                        keyboardUtil.displayleftAndright();
                        break;
                    case R.id.edit_num:
                        keyboardUtil.displayleftAndrightNum(num);
                        break;
                }

                //显示自定义的软键盘
                keyboardUtil.showKeyboard(bottomView);
            }
        });
        edText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (keyboardUtil != null) {
                        //设置当前获取的EDITTEXT
                        keyboardUtil.setEditText(edText);
                        switch (edText.getId()) {
                            case R.id.edit:
                                keyboardUtil.displayleftAndright();
                                break;
                            case R.id.edit_num:
                                keyboardUtil.displayleftAndrightNum(num);
                                break;
                        }
                    }
                }
            }
        });
        if (SDK_INT <= 10) {
            // 屏蔽默认输入法
            edText.setInputType(InputType.TYPE_NULL);

        } else {
            //反射的方法实现避免弹出系统自带的软键盘
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            /**方法一*/
            try {
                Class<EditText> cls = EditText.class;
                java.lang.reflect.Method setShowSoftInputOnFocus;

                if (SDK_INT >= 16) {
                    // 4.2
                    setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                } else {
                    // 4.0
                    setShowSoftInputOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                }

                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(edText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * ******
     * 监听自定义键盘的按钮做出特殊处理
     */
    private KeyboardUtil.MyOnkeys myOnkeys = new KeyboardUtil.MyOnkeys() {

        @Override
        public void RightKeys(View view, Editable editable, int primaryCode, int[] keyCodes) {
            // TODO Auto-generated method stub
            switch (view.getId()) {
                case R.id.edit:

                    break;
                case R.id.edit_num:

                    String s = editable.toString();
                    try {
                        int m = Integer.valueOf(s).intValue();
                        m = m - num;
                        if (m > 0) {
                            s = m + "";
                            editText.setText(s);
                            editText.setSelection(s.length());
                        } else {
                            editText.setText(null);
                        }

                    } catch (Exception e) {

                    }
                    break;
            }

        }

        @Override
        public void LeftKeys(View view, Editable editable, int primaryCode, int[] keyCodes) {
            switch (view.getId()) {
                case R.id.edit:
                    break;
                case R.id.edit_num:
                    String s = editable.toString() + "";

                    try {
                        if (s == null || s.equals(""))
                            s = "0";
                        int m = Integer.valueOf(s).intValue();
                        m = m + num;
                        s = m + "";
                        editText.setText(m + "");
                        editText.setSelection(s.length());
                    } catch (Exception e) {

                    }
                    break;
            }
        }
    };

}
