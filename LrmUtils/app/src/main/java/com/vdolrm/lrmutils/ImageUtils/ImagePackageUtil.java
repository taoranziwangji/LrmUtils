package com.vdolrm.lrmutils.ImageUtils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 图片拼接工具类
 * Created by vdo on 16/7/14.
 */
public class ImagePackageUtil {

    /**
     * 横向拼接
     * <功能详细描述>
     *
     * @param first
     * @param second
     * @param paintLine 绘制分割线的画笔,可为空
     * @return
     */
    public static Bitmap addBitmapHorizontal(Bitmap first, Bitmap second, Paint paintLine) {
        int width = first.getWidth() + second.getWidth() + (paintLine == null ? 0 : (int)paintLine.getStrokeWidth()+1);
        int height = Math.max(first.getHeight(), second.getHeight());
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        if (paintLine != null) {
            //draw line
            canvas.drawLine(first.getWidth(), 0, first.getWidth(), first.getHeight(), paintLine);
        }

        canvas.drawBitmap(second, first.getWidth() + (paintLine == null ? 0 : paintLine.getStrokeWidth()), 0, null);
        return result;
    }


    /**
     * 纵向拼接
     * <功能详细描述>
     *
     * @param first
     * @param second
     * @param paintLine 绘制分割线的画笔,可为空
     * @return
     */
    public static Bitmap addBitmapVertical(Bitmap first, Bitmap second, Paint paintLine) {
        int width = Math.max(first.getWidth(), second.getWidth());
        int height = first.getHeight() + second.getHeight() + (paintLine == null ? 0 : (int)paintLine.getStrokeWidth()+1);
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, 0, 0, null);
        if (paintLine != null) {
            //draw line
            canvas.drawLine(0, first.getHeight(), first.getWidth(), first.getHeight(), paintLine);
        }

        canvas.drawBitmap(second, 0, first.getHeight() + (paintLine == null ? 0 : paintLine.getStrokeWidth()), null);

        return result;
    }


    /**
     * 4张拼接,(把第一张和第二张竖着拼成新图A,再把第三张和第四张竖着拼成B,再横着拼AB)
     *
     * @param first
     * @param second
     * @param third
     * @param fourth
     * @param paintLine 绘制分割线的画笔,可为空
     * @return
     */
    public static Bitmap addBitmap4(Bitmap first, Bitmap second, Bitmap third, Bitmap fourth, Paint paintLine) {
        Bitmap left = addBitmapVertical(first, second, paintLine);
        Bitmap right = addBitmapVertical(third, fourth, paintLine);

        Bitmap newBm = addBitmapHorizontal(left, right, paintLine);
        return newBm;
    }


}
