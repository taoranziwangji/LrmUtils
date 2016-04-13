package com.vdolrm.lrmutils.OtherUtils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**动态控制edittext内字符串的最大长度*/
public class EditTextCharacterLimitUtils {

	/**
	 * @param edt
	 * @param tv_showTheNumNow 用来显示当前edt内有多少个字
	 * @param limit 最大显示的字符上限
	 */
	public static void controlEdit(final EditText edt,final TextView tv_showTheNumNow,final int limit){
		edt.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

           public void onTextChanged(CharSequence s, int start, int before, int count) {
                 temp = s;
            }
       
            public void afterTextChanged(Editable s) {
            	try{
                int number = limit - s.length();
                tv_showTheNumNow.setText("" + number);
                selectionStart = edt.getSelectionStart();
                selectionEnd = edt.getSelectionEnd();
                if (temp.length() > limit) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    edt.setText(s);
                    edt.setSelection(tempSelection);//设置光标在最后
                }
            	}catch(StackOverflowError e){
            		e.printStackTrace();
            	}
            }
		});
		
	}
}
