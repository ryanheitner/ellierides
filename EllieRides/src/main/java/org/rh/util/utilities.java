package org.rh.util;

import android.graphics.Matrix;
import android.graphics.Point;

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

}
