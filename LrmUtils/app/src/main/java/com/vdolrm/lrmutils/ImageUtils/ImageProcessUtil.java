package com.vdolrm.lrmutils.ImageUtils;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;


import com.vdolrm.lrmutils.Contant;
import com.vdolrm.lrmutils.FileUtils.FileUtil;
import com.vdolrm.lrmutils.LogUtils.MyLog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageProcessUtil {
	
	
	/**
	 *背景平铺
	 * */ 
	public static Bitmap createRepeater(int width, Bitmap src){
		int count = (width + src.getWidth() - 1) / src.getWidth();
		 
		Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		for(int idx = 0; idx < count; ++ idx){
		canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
		}
		 
		return bitmap;
		}

	/**
	 * 圆角图片
	 * 第一个参数是传入需要转化成圆角的图片，第二个参数是圆角的度数，数值越大，圆角越大*/ 
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242; 
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels; 
		paint.setAntiAlias(true); 
		canvas.drawARGB(0, 0, 0, 0); 
		paint.setColor(color); 
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint); 
		return output; 
		}	
	
	/**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
            if (width <= height) {
                    roundPx = width / 2;
                    top = 0;
                    bottom = width;
                    left = 0;
                    right = width;
                    height = width;
                    dst_left = 0;
                    dst_top = 0;
                    dst_right = width;
                    dst_bottom = width;
            } else {
                    roundPx = height / 2;
                    float clip = (width - height) / 2;
                    left = clip;
                    right = width - clip;
                    top = 0;
                    bottom = height;
                    width = height;
                    dst_left = 0;
                    dst_top = 0;
                    dst_right = height;
                    dst_bottom = height;
            }
           
            Bitmap output=null;
            try{
            output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
            final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
            final RectF rectF = new RectF(dst);
            paint.setAntiAlias(true);             
            canvas.drawARGB(0, 0, 0, 0);
            

            paint.setColor(color);
	        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    canvas.drawBitmap(bitmap, src, dst, paint);	//recycled
            }catch(OutOfMemoryError e){
            	e.printStackTrace();
            }catch(RuntimeException e){
            	e.printStackTrace();
            }
		    
		    //画白色圆圈(不画边框把下边的去掉就行)
		    /*paint.reset();
		    paint.setColor(Color.WHITE);
		    paint.setStyle(Paint.Style.STROKE);
		    paint.setStrokeWidth(4);//宽度4 //太大了会变形
		    paint.setAntiAlias(true);
	        canvas.drawCircle(width / 2, width / 2, width / 2 - 4 / 2, paint);*/

            return output;
    }
    

    /** 图片放大的method */  
    /**
     * @param activity
     * @param bmp
     * @param minNum 最低尺寸标准
     * @return
     */
    public static Bitmap FangdaBitmap(Activity activity,Bitmap bmp,int minNum) {
    	
    	 int bWidth=bmp.getWidth();  
         int bHeight=bmp.getHeight();  
                  
         int temp = bWidth > bHeight ? bWidth : bHeight;//找出最大的那条边  //原来头像那个是160
         
         if(temp>=minNum){ 
         	return bmp;
         }
         
    	try{
			ContentResolver cr = activity.getContentResolver();
			String filename = System.currentTimeMillis()+"";
			try {
				boolean b = FileUtil.saveBitmapToFile(Contant.mulu_file_cache, bmp, filename, false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Uri uri = Uri.parse("file://" + Contant.mulu_file_cache + filename);
			
			//try{
				InputStream in = null;
				try {
					in = cr.openInputStream(uri);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
	            BitmapFactory.Options options = new BitmapFactory.Options();
	            options.inJustDecodeBounds = true;
	            BitmapFactory.decodeStream(in, null, options);
				try{
				in.close();
				}
				catch (IOException e){
				e.printStackTrace();
				}
				
	            int mWidth = options.outWidth;
	            int mHeight = options.outHeight;
	            
	            int sWidth  = minNum;//40
	            int sHeight = minNum;//40
	            
		        int s = 1;
		        while ((mWidth / s > sWidth * 2) || (mHeight / s > sHeight * 2))
		        {
		            s *= 2;
		        }
	            options = new BitmapFactory.Options();
		        options.inSampleSize = s;
				try {
					in = cr.openInputStream(uri);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
				try{
				in.close();
				}
				catch (IOException e){
				e.printStackTrace();
				}
				if(null  == bitmap){
					//Toast.makeText(this, "Head is not set successful,Decode bitmap failure", 2000);
					return null;
				}
				//原始图片的尺寸
				int bmpWidth  = bitmap.getWidth();
				int bmpHeight = bitmap.getHeight();
				
				//缩放图片的尺寸
				float scaleWidth  = (float) sWidth / bmpWidth;
				float scaleHeight = (float) sHeight / bmpHeight;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				
				//产生缩放后的Bitmap对象
				Bitmap resizeBmp = Bitmap.createBitmap(
						bitmap, 0, 0, bmpWidth, bmpHeight, matrix, false);
			//	bitmap.recycle();

				/*
				 * //Bitmap to byte[]
				byte[] photoData = bitmap2Bytes(resizeBitmap);
				
				//save file
				String fileName = "/sdcard/test.jpg";
				FileUtil.writeToFile(fileName, photoData);*/
				
				 MyLog.d("lrm", "放大2放大后:weidht=" + resizeBmp.getWidth() + ",height=" + resizeBmp.getHeight());
				 
			     return resizeBmp;
    	}catch(RuntimeException e){
    		e.printStackTrace();
    		return null;
    	}
    	
    }
    
    
    /**
	 * 
	 * 图片压缩
	 * @return
	 */
    public static Bitmap decodeBitmap(Bitmap bm,int size){
    	return decodeBitmap(Contant.mulu_file_cache,bm,size);
    }
    
    /**
	 * 
	 * 图片压缩
	 * 
	 * @return
	 */
	 public static Bitmap decodeBitmap(String lujing,Bitmap bm,int size){
		 
		 	if(bm ==null)
		 		return null;
		 	String tmp_name="";
		 	tmp_name = System.currentTimeMillis()+"";
		 	try {
				FileUtil.saveBitmapToFile(lujing,bm, tmp_name,false);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true; 
	        Bitmap newbm = BitmapFactory.decodeFile(lujing + tmp_name, options) ;
	        //这个时候newbm是空的
	        /*if(newbm ==null)
	        	return null;*/  
	        float realWidth = options.outWidth;  
	        float realHeight = options.outHeight;  
	        MyLog.d("lrm","realw="+realWidth+",realH="+realHeight);
	     // 计算缩放比             
	        int scale = (int)((realHeight > realWidth ? realHeight : realWidth) / size);//假如size=400，是不是400-800之间的就不压缩?
	        MyLog.d("lrm","scale="+scale);
	        if (scale <= 0){  
	            scale = 1;  
	        }
	        
	        options.inSampleSize = scale;  
	        options.inJustDecodeBounds = false;  
	        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。        
	        newbm = BitmapFactory.decodeFile(lujing + tmp_name, options);
	        if(newbm ==null)
	        	return null;
	        MyLog.d("lrm","ImageProcessUtil.newbn="+newbm);
	        int w = newbm.getWidth();  
	        int h = newbm.getHeight(); 
	        MyLog.d("lrm","缩略图高度："+h+",宽度："+w);
	        return newbm;  
	    }
	
	 
	 /**name为全路径*/
	 public static Bitmap decodeBitmap(String name,int size){

	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true; 
	        Bitmap newbm = BitmapFactory.decodeFile(name, options) ;
	        
	        float realWidth = options.outWidth;  
	        float realHeight = options.outHeight;  
	        MyLog.d("lrm","realw="+realWidth+",realH="+realHeight);
	     // 计算缩放比             
	        int scale = (int)((realHeight > realWidth ? realHeight : realWidth) / size);//强转int直接舍弃小数点后面的数不是四舍五入 
	        MyLog.d("lrm","scale="+scale);	        
	        if (scale <= 0){  
	            scale = 1;  
	        	}
	        
	        
	     /*  if(scale>14){ //尝试防止scale过大时返回null的问题
	        double b = Math.log(scale)/Math.log(2);
			System.out.println(b);
			int c = new BigDecimal(b).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
			System.out.println(c);
			double dd = Math.pow(2,c);
			System.out.println((int)dd);
	       }*/
	       
	       
	        options.inSampleSize = scale;  
	        options.inJustDecodeBounds = false;  
	        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。        
	        newbm = BitmapFactory.decodeFile(name, options);//scale==53时返回空
	        if(newbm ==null){
	        
	        	return null;
	        }
	        int w = newbm.getWidth();  
	        int h = newbm.getHeight(); 
	        MyLog.d("lrm","缩略图高度："+h+",宽度："+w);
	        return newbm;  
	    }
	 
	 
	 /**
		 * 
		 * 图片压缩
		 * 
		 * @return 返回路径+名字
		 */
		 public static String decodeBitmap2(String lujing,Bitmap bm,int size){
			 
			 	if(bm ==null)
			 		return null;
			 	String tmp_name="";
			 	//tmp_name = System.currentTimeMillis()+".jpg";
			 	tmp_name = System.currentTimeMillis()+"";
			 	try {
					FileUtil.saveBitmapToFile(lujing,bm, tmp_name,false);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        BitmapFactory.Options options = new BitmapFactory.Options();
		        options.inJustDecodeBounds = true; 
		        Bitmap newbm = BitmapFactory.decodeFile(lujing + tmp_name, options) ;
		      // bm = BitmapFactory.decodeFile(Contant.mulu_file_cache+tmp_name, options) ;
		        //这个时候newbm是空的
		        /*if(newbm ==null)
		        	return null;*/
		        // 通过这个bitmap获取图片的宽和高         
		      //  Bitmap bitmap = BitmapFactory.decodeFile(ALBUM_PATH+name, options);   
		        float realWidth = options.outWidth;  
		        float realHeight = options.outHeight;  
		        MyLog.d("lrm","realw="+realWidth+",realH="+realHeight);
		     // 计算缩放比             
		        int scale = (int)((realHeight > realWidth ? realHeight : realWidth) / size);
		        MyLog.d("lrm","scale="+scale);
		        
		        if (scale <= 0)  
		        {  
		            scale = 1;  
		        }
		        
		        options.inSampleSize = scale;  
		        options.inJustDecodeBounds = false;  
		        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。        
		        newbm = BitmapFactory.decodeFile(lujing + tmp_name, options);
		        if(newbm ==null)
		        	return null;
		        MyLog.d("lrm","ImageProcessUtil.newbn="+newbm);
		        int w = newbm.getWidth();  
		        int h = newbm.getHeight(); 
		        MyLog.d("lrm","缩略图高度："+h+",宽度："+w);
		        return lujing+tmp_name;  
		    }
	 
		 
		 
		 /**
			 * 
			 * 图片压缩
			 * 
			 * @return 只需传路径
			 * （多选相册的那个demo里的压缩方法）
			 */
		 public static Bitmap decodeBitmap(String path) throws IOException {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(in, null, options);
				in.close();
				int i = 0;
				Bitmap bitmap = null;
				while (true) {
					if ((options.outWidth >> i <= 1000)&& (options.outHeight >> i <= 1000)) {
						in = new BufferedInputStream(new FileInputStream(new File(path)));
						options.inSampleSize = (int) Math.pow(2.0D, i);
						options.inJustDecodeBounds = false;
						bitmap = BitmapFactory.decodeStream(in, null, options);
						break;
					}
					i += 1;
				}
				return bitmap;
			}
		 
		 
		 
		 public static Drawable zoomDrawable(Context c,Drawable drawable, int w, int h) {
			    int width = drawable.getIntrinsicWidth();  
			    int height = drawable.getIntrinsicHeight();  
			    // drawable转换成bitmap  
			    Bitmap oldbmp = drawableToBitmap(drawable);
			    // 创建操作图片用的Matrix对象  
			    Matrix matrix = new Matrix();
			    // 计算缩放比例  
			    float sx = ((float) w / width);  
			    float sy = ((float) h / height);  
			    // 设置缩放比例  
			    matrix.postScale(sx, sy);  
			    // 建立新的bitmap，其内容是对原bitmap的缩放后的图  
			    Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
						matrix, true);
			    //return new BitmapDrawable(newbmp);  
			    return new BitmapDrawable(c.getResources(),newbmp);
			}
		 
		 
		 public static Bitmap drawableToBitmap(Drawable drawable) {

			 try{
		        Bitmap bitmap = Bitmap.createBitmap(

						drawable.getIntrinsicWidth(),

						drawable.getIntrinsicHeight(),

						drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888

								: Config.RGB_565);

		        Canvas canvas = new Canvas(bitmap);

		        //canvas.setBitmap(bitmap);

		        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

		        drawable.draw(canvas);

		        return bitmap;
			 }catch(IllegalArgumentException e){
				 e.printStackTrace();
			 }catch(Exception e){
				 e.printStackTrace();
			 }catch(OutOfMemoryError e){
				 e.printStackTrace();
			 }
			return null;

		}
		 
		 
		 
		 
		 /**
		     * 读取图片属性：旋转的角度
		     * @param path 图片绝对路径
		     * @return degree旋转的角度
		     */
		public static int readPictureDegree(String path) {
		    int degree  = 0;
		    try {
		            ExifInterface exifInterface = new ExifInterface(path);
		            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		            switch (orientation) {
		            case ExifInterface.ORIENTATION_ROTATE_90:
		                    degree = 90;
		                    break;
		            case ExifInterface.ORIENTATION_ROTATE_180:
		                    degree = 180;
		                    break;
		            case ExifInterface.ORIENTATION_ROTATE_270:
		                    degree = 270;
		                    break;
		            }
		    } catch (IOException e) {
		            e.printStackTrace();
		    }
		    return degree;
		}
		 
		 
		 /** 
		  * 旋转图片 
		  * @param angle 
		  * @param bitmap 
		  * @return Bitmap 
		  */  
		 public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		     //旋转图片 动作  
		     Matrix matrix = new Matrix();;
		     matrix.postRotate(angle);  
		     System.out.println("angle2=" + angle);
		     // 创建新的图片  
		     Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					 bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		     return resizedBitmap;
		     
		 }
		 
		 
		 
		 public static Bitmap decodeUriAsBitmap(Activity activity,Uri uri){
				Bitmap bitmap = null;
				try {
					bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					return null;
				}catch(RuntimeException e){
					e.printStackTrace();
					return null;
				}
				return bitmap;
			}
		 
		 
	
}
