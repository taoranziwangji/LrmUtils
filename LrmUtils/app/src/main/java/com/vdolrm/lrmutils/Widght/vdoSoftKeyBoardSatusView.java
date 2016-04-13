package com.vdolrm.lrmutils.Widght;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * 实现点击edtitext输入文字时 输入法正好弹出在某个按钮（如登录）的下边
 * 该监听在类容不能滚动的情况下无效,即必须有scrollview等
 *
 */
public class vdoSoftKeyBoardSatusView extends LinearLayout {

	private final int  CHANGE_SIZE=100;
	public vdoSoftKeyBoardSatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public vdoSoftKeyBoardSatusView(Context context) {
		super(context);
		init();
	}
	
	private void init()
	{
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		Log.i("demo", "w :" + w);
		Log.i("demo", "h :" + h);
		Log.i("demo", "oldw :" + oldw);
		Log.i("demo", "oldh :" + oldh);
		
		if(oldw==0||oldh==0)
			return;
		
		if(boardListener!=null)
		{
			boardListener.keyBoardStatus(w, h, oldw, oldh);
			if(oldw!=0&&h-oldh<-CHANGE_SIZE)
			{
				boardListener.keyBoardVisable(Math.abs(h - oldh));
			}
			
			if(oldw!=0&&h-oldh>CHANGE_SIZE)
			{
				boardListener.keyBoardInvisable(Math.abs(h - oldh));
			}
		}
	}
	
	
	public interface SoftkeyBoardListener{
		public void keyBoardStatus(int w, int h, int oldw, int oldh);
		public void keyBoardVisable(int move);
		public void keyBoardInvisable(int move);
	}
	
	SoftkeyBoardListener boardListener;
	public void setSoftKeyBoardListener(SoftkeyBoardListener boardListener)
	{
		this.boardListener=boardListener;
	}
}
