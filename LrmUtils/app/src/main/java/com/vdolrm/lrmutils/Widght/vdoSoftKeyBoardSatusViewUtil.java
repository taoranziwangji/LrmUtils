package com.vdolrm.lrmutils.Widght;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;


public abstract class vdoSoftKeyBoardSatusViewUtil implements vdoSoftKeyBoardSatusView.SoftkeyBoardListener {
	
	public abstract View getSoftKeyBoradUnderThisView();
	public abstract ScrollView getScrollView();
	public abstract View getWillHideView();

	@Override
	public void keyBoardStatus(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyBoardVisable(int move) {
		// TODO Auto-generated method stub
		Log.e("demo", "open");
		getSoftKeyBoradUnderThisView().getScrollY();
		Message message=new Message();
		message.what=WHAT_SCROLL;
		message.obj=move;
		handler.sendMessageDelayed(message, 100);
	}

	@Override
	public void keyBoardInvisable(int move) {
		// TODO Auto-generated method stub
		Log.e("demo", "close");
		handler.sendEmptyMessageDelayed(WHAT_BTN_VISABEL, 100);
	}


	
	final int WHAT_SCROLL=0,WHAT_BTN_VISABEL=WHAT_SCROLL+1;
	Handler handler=new Handler(){

		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			switch (msg.what) {
			case WHAT_SCROLL:
				int move=(Integer) msg.obj;
				Log.d("lrm", "move=" + move);
				if(getWillHideView()!=null)
					getWillHideView().setVisibility(View.GONE);
				getScrollView().smoothScrollBy(0, move);
				break;
			case WHAT_BTN_VISABEL:
				if(getWillHideView()!=null)
					getWillHideView().setVisibility(View.VISIBLE);
				break;
			}
		}
		
	};

}
