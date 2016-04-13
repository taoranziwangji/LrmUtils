package com.vdolrm.lrmutils.ImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;


/**
 * 图片模糊工具
 */
public class BlurUtil {

    private static final int DEFAULT_BLUR_RADIUS = 10;


    public static Bitmap apply(Context context, Bitmap sentBitmap) {
        return apply(context, sentBitmap, DEFAULT_BLUR_RADIUS);
    }

    /**
     * @param context
     * @param sentBitmap
     * @param radius Set the radius of the Blur.Supported range 0 < radius <= 25, >25 will be no effects
     * @return
     */
    public static Bitmap apply(Context context, Bitmap sentBitmap, int radius) {
        try {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            final RenderScript rs = RenderScript.create(context);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);

            rs.destroy();
            input.destroy();
            output.destroy();
            script.destroy();

            return bitmap;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return sentBitmap;
    }

}
