package com.vdolrm.lrmutils.Widght;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

/**
* 不可拖动的seekbar
*/
public class vdoSeekBarNoScroll extends SeekBar {

        public vdoSeekBarNoScroll(Context context) {
                super(context);
        }

        public vdoSeekBarNoScroll(Context context, AttributeSet attrs) {
                this(context, attrs, android.R.attr.seekBarStyle);
        }

        public vdoSeekBarNoScroll(Context context, AttributeSet attrs, int defStyle) {
                super(context, attrs, defStyle);
        }
        
        /**
         * onTouchEvent 是在 SeekBar 继承的抽象类 AbsSeekBar 里
         * 你可以看下他们的继承关系
         */
        @Override
        public boolean onTouchEvent(MotionEvent event) {
                // TODO Auto-generated method stub
                
            //    return super.onTouchEvent(event);
        	//原来是要将TouchEvent传递下去的,我们不让它传递下去就行了
                return false ;//禁止滑动
        }
        
       
        

}