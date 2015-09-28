package com.dennisxu.lib.core.image;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import com.nostra13.universalimageloader.core.process.BitmapProcessor;

/**
 * 圆头像处理器
 * 
 * @author: xuyang
 * @date: 2014-9-15 下午5:05:19
 */
public class RoundedHeadProcessor implements BitmapProcessor {

    @Override
    public Bitmap process( Bitmap bitmap ) {
        Bitmap output = Bitmap
                .createBitmap(bitmap.getHeight(), bitmap.getWidth(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        bitmap.recycle();
        return output;
    }
}
