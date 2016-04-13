package com.vdolrm.lrmutils.ImageUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

/**
 * TODO 此工具类都没有经过测试，可能会导致内存溢出的问题
 * Created by Administrator on 2016/3/30.
 */
public class ImageDecodeUtil {

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
            Uri uri = Uri.parse("file://"+Contant.mulu_file_cache+filename);

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

            MyLog.d(MyLog.printBaseInfo()+"放大2放大后:weidht=" + resizeBmp.getWidth() + ",height=" + resizeBmp.getHeight());

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
     * 图片压缩
     * @param lujing 缓存纯路径，不带文件名
     * @param bm
     * @param size
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
        Bitmap newbm = BitmapFactory.decodeFile(lujing+tmp_name, options) ;
        //这个时候newbm是空的
	        /*if(newbm ==null)
	        	return null;*/
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        MyLog.d(MyLog.printBaseInfo()+"realw="+realWidth+",realH="+realHeight);
        // 计算缩放比
        int scale = (int)((realHeight > realWidth ? realHeight : realWidth) / size);//假如size=400，是不是400-800之间的就不压缩?
        MyLog.d(MyLog.printBaseInfo()+"scale="+scale);
        if (scale <= 0){
            scale = 1;
        }

        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        newbm = BitmapFactory.decodeFile(lujing+tmp_name, options);
        if(newbm ==null)
            return null;
        MyLog.d(MyLog.printBaseInfo()+"ImageUtil.newbn="+newbm);
        int w = newbm.getWidth();
        int h = newbm.getHeight();
        MyLog.d(MyLog.printBaseInfo()+"缩略图高度："+h+",宽度："+w);
        return newbm;
    }


    /**
     * 图片压缩
     * @param path 完整路径
     * @param size
     * @return
     */
    public static Bitmap decodeBitmap(String path,int size){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap newbm = BitmapFactory.decodeFile(path, options) ;

        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        MyLog.d(MyLog.printBaseInfo()+"realw="+realWidth+",realH="+realHeight);
        // 计算缩放比
        int scale = (int)((realHeight > realWidth ? realHeight : realWidth) / size);//强转int直接舍弃小数点后面的数不是四舍五入
        MyLog.d(MyLog.printBaseInfo()+"scale="+scale);
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
        newbm = BitmapFactory.decodeFile(path, options);//scale==53时返回空
        if(newbm ==null){

            return null;
        }
        int w = newbm.getWidth();
        int h = newbm.getHeight();
        MyLog.d(MyLog.printBaseInfo()+"缩略图高度："+h+",宽度："+w);
        return newbm;
    }


    /**
     *
     * 图片压缩
     *@param lujing 缓存纯路径，不带文件名
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
        Bitmap newbm = BitmapFactory.decodeFile(lujing+tmp_name, options) ;
        // bm = BitmapFactory.decodeFile(Contant.mulu_file_cache+tmp_name, options) ;
        //这个时候newbm是空的
		        /*if(newbm ==null)
		        	return null;*/
        // 通过这个bitmap获取图片的宽和高
        //  Bitmap bitmap = BitmapFactory.decodeFile(ALBUM_PATH+name, options);
        float realWidth = options.outWidth;
        float realHeight = options.outHeight;
        MyLog.d(MyLog.printBaseInfo()+"realw="+realWidth+",realH="+realHeight);
        // 计算缩放比
        int scale = (int)((realHeight > realWidth ? realHeight : realWidth) / size);
        MyLog.d(MyLog.printBaseInfo()+"scale="+scale);

        if (scale <= 0)
        {
            scale = 1;
        }

        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        // 注意这次要把options.inJustDecodeBounds 设为 false,这次图片是要读取出来的。
        newbm = BitmapFactory.decodeFile(lujing+tmp_name, options);
        if(newbm ==null)
            return null;
        MyLog.d(MyLog.printBaseInfo()+"ImageUtil.newbn="+newbm);
        int w = newbm.getWidth();
        int h = newbm.getHeight();
        MyLog.d(MyLog.printBaseInfo()+"缩略图高度："+h+",宽度："+w);
        return lujing+tmp_name;
    }



    /**
     * 图片压缩
     * @param path 完整路径（路径+名字）
     *
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


    /**
     * 缩放图片
     * @param c
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    public static Drawable zoomDrawable(Context c,Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        // drawable转换成bitmap
        Bitmap oldbmp = ImageUtils.drawableToBitmap(drawable);
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

}
