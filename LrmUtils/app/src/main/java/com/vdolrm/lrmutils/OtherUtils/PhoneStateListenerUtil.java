package com.vdolrm.lrmutils.OtherUtils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;


/**
 * 匿名内部类
 * @author Administrator
 *
 */
public abstract class PhoneStateListenerUtil {
	
	private MediaPlayer mediaPlayer;
	private Context context;
	
	public abstract void notifyAllPause();
	public abstract void notifyAllPlay();
	/**
	 * 停止一切如录音、计时等功能
	 */
	public abstract void stopOther();
	
	
	public PhoneStateListenerUtil(){
		
	}
	
	public PhoneStateListenerUtil(Context c,MediaPlayer mp){
		context = c;
		mediaPlayer = mp;
	}

	private  boolean mResumeAfterCall = false;  
	private PhoneStateListener mPhoneStateListener = new PhoneStateListener() {
	    @Override
	    public void onCallStateChanged(int state, String incomingNumber) {
	    	
	    	/*try{
	    		//监听到来电 把跟录音相关的逻辑都取消 比如录音功能 计时等
		    	if(mp3EncodeClient!=null){
					mp3EncodeClient.stop();
				}
		    	
		    	
	    	}catch(RuntimeException e){
	    		e.printStackTrace();
	    	}
	    	*/
	    	
	    	stopOther();
	    	
	    	if(mediaPlayer==null || !mediaPlayer.isPlaying()){
	    		return ;
	    	}
	        if (state == TelephonyManager.CALL_STATE_RINGING) {  //来电时响铃状态
	            AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
	            int ringvolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
	            if (ringvolume > 0) {  
	                mResumeAfterCall = (mediaPlayer.isPlaying() || mResumeAfterCall);  
	                try {  
	                    mediaPlayer.pause();  
	                    notifyAllPause();
	                  
	                } catch (Exception e) {
	                    e.printStackTrace();  
	                }  
	            }  
	        } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {  //接听状态
	            // pause the music while a conversation is in progress  
	            mResumeAfterCall = (mediaPlayer.isPlaying() || mResumeAfterCall);  
	            try {  
	            	mediaPlayer.pause(); 
	            	notifyAllPause();
	            	
	            } catch (Exception e) {
	                e.printStackTrace();  
	            }  
	        } else if (state == TelephonyManager.CALL_STATE_IDLE) {  //挂断状态
	            // start playing again  
	            if (mResumeAfterCall) {  
	                // resume playback only if music was playing  
	                // when the call was answered  
	                try {  
	                	mediaPlayer.start(); 
	                	notifyAllPlay();
	                	
	                } catch (Exception e) {
	                    // TODO Auto-generated catch block  
	                    e.printStackTrace();  
	                }  
	                mResumeAfterCall = false;  
	            }  
	        }  
	    }  
	}; 
	
	
	/**
	 * 监听电话状态
	 * @param c
	 */
	public void beginListen(Context c){
		 //监听电话状态		
		TelephonyManager tmgr = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
	    tmgr.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		
	}
	
	/**
	 * 注销电话监听状态
	 * @param c
	 */
	public  void stopListen(Context c){
		//注销电话监听状态
		TelephonyManager tmgr = (TelephonyManager)c.getSystemService(Context.TELEPHONY_SERVICE);
	    tmgr.listen(mPhoneStateListener, 0); 
	}
	
	
}
