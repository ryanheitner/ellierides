package org.rh.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import org.rh.ellierides.LayeredImageView;

/**
 * Created by ryanheitner on 10/11/13.
 */
public class Utilities {

    public static Point getOrigin(Matrix matrix) {
        final float[] values = new float[9];
        matrix.getValues(values);
        float originX = values[Matrix.MTRANS_X];
        float originY = values[Matrix.MTRANS_Y];
        return new Point(Math.round(originX), (Math.round(originY)));
    }

    public static boolean touchedImage(Matrix matrix, LayeredImageView.Layer layer, MotionEvent event) {
        final float xTouch = event.getX();
        final float yTouch = event.getY();
// Used to get the values of a Matrix
        final float[] values = new float[9];
        matrix.getValues(values);
        float originX = values[Matrix.MTRANS_X];
        float originY = values[Matrix.MTRANS_Y];


        float imageWidth = layer.getDrawable().getBounds().width();
        float imageHeight = layer.getDrawable().getBounds().height();
        float width = values[Matrix.MSCALE_X] * imageWidth;
        float height = values[Matrix.MSCALE_Y] * imageHeight;

        if ((xTouch >= originX && xTouch <= originX + width) &&
                (yTouch >= originY && yTouch <= originY + height)) {

            int pixX = (int) (xTouch - originX);
            int pixY = (int) (yTouch - originY);
            Bitmap bitmap;
            Drawable drawable = layer.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                bitmap = ((BitmapDrawable) ((AnimationDrawable) drawable).getFrame(0)).getBitmap();
            } else {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            }

            try {
                if (!(bitmap.getPixel(pixX, pixY) == 0)) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }
    public static boolean touchedImageLite(Matrix matrix, LayeredImageView.Layer layer, MotionEvent event) {
        final float xTouch = event.getX();
        final float yTouch = event.getY();
// Used to get the values of a Matrix
        final float[] values = new float[9];
        matrix.getValues(values);
        float originX = values[Matrix.MTRANS_X];
        float originY = values[Matrix.MTRANS_Y];


        float imageWidth = layer.getDrawable().getBounds().width();
        float imageHeight = layer.getDrawable().getBounds().height();
        float width = values[Matrix.MSCALE_X] * imageWidth;
        float height = values[Matrix.MSCALE_Y] * imageHeight;

        if ((xTouch >= originX && xTouch <= originX + width) &&
                (yTouch >= originY && yTouch <= originY + height)) {
            return true;
        }
        return false;
    }
    public static boolean touchedImageBigger(Matrix matrix, LayeredImageView.Layer layer, MotionEvent event,int percentIncrease) {
        final float xTouch = event.getX();
        final float yTouch = event.getY();
// Used to get the values of a Matrix
        final float[] values = new float[9];
        matrix.getValues(values);


        float originX = values[Matrix.MTRANS_X] ;
        float originY = values[Matrix.MTRANS_Y] ;



        final float imageWidth = layer.getDrawable().getBounds().width() ;
        final float imageHeight = layer.getDrawable().getBounds().height() ;
        float width = values[Matrix.MSCALE_X] * imageWidth;
        float height = values[Matrix.MSCALE_Y] * imageHeight;

        final float halfMultiplier = ((percentIncrease / 2.0f) / 100.0f) + 1.0f;

        final float scaledWidth = width * halfMultiplier;
        final float scaledHeight = height * halfMultiplier;
        final float scaledOriginX = originX - (scaledWidth - width);
        final float scaledOriginY = originY - (scaledHeight - height);

        if ((xTouch >= scaledOriginX && xTouch <= scaledOriginX + scaledWidth) &&
                (yTouch >= scaledOriginY && yTouch <= scaledOriginY + scaledHeight)) {
            return true;
        }
        return false;
    }
}
