package org.rh.ellierides;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Iterator;

public class LayeredImageView extends ImageView {
    private final float[] values = new float[9];
    private ArrayList<Layer> mLayers;
    private Matrix mDrawMatrix;
    private Resources mResources;

    public LayeredImageView(Context context) {
        super(context);
        init();
    }

// --Commented out by Inspection START (11/14/13 10:02 AM):
//    public LayeredImageView(Context context, AttributeSet set) {
//        super(context, set);
//        init();
//
//        int[] attrs = {
//                android.R.attr.src
//        };
//        TypedArray a = context.obtainStyledAttributes(set, attrs);
//        TypedValue outValue = new TypedValue();
//        assert a != null;
//        if (a.getValue(0, outValue)) {
//            setImageResource(outValue.resourceId);
//        }
//        a.recycle();
//    }
// --Commented out by Inspection STOP (11/14/13 10:02 AM)

    private void init() {
        mLayers = new ArrayList<Layer>();
        mDrawMatrix = new Matrix();
        mResources = new LayeredImageViewResources();
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        who.setBounds(0, 0, who.getIntrinsicWidth(), who.getIntrinsicHeight());
        super.scheduleDrawable(who, what, when);
    }

    @Override
    protected boolean verifyDrawable(Drawable dr) {
        for (int i = 0; i < mLayers.size(); i++) {
            Layer layer = mLayers.get(i);
            if (layer.drawable == dr) {
                return true;
            }
        }
        return super.verifyDrawable(dr);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void invalidateDrawable(Drawable dr) {
        if (verifyDrawable(dr)) {
            invalidate();
        } else {
            super.invalidateDrawable(dr);
        }
    }

    @Override
    public Resources getResources() {
        return mResources;
    }

    @Override
    public void setImageBitmap(Bitmap bm) throws RuntimeException {
        String detailMessage = "setImageBitmap not supported, use: setImageDrawable() " +
                "or setImageResource()";
        throw new RuntimeException(detailMessage);
    }

    @Override
    public void setImageURI(Uri uri) throws RuntimeException {
        String detailMessage = "setImageURI not supported, use: setImageDrawable() " +
                "or setImageResource()";
        throw new RuntimeException(detailMessage);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = getImageMatrix();
        assert matrix != null;
        matrix.getValues(values);

        boolean pendingAnimations = false;
        for (int i = 0; i < mLayers.size(); i++) {
            mDrawMatrix.set(matrix);

            Layer layer = mLayers.get(i);
            if (layer.matrix != null) {
                mDrawMatrix.preConcat(layer.matrix);
                mDrawMatrix.getValues(values);
            }
            if (layer.animation == null) {
                draw(canvas, layer.drawable, mDrawMatrix);
//                  draw(canvas, layer.drawable, mDrawMatrix, 255);
            } else {
                Animation a = layer.animation;
                if (!a.isInitialized()) {
                    Rect bounds = layer.drawable.getBounds();
                    Drawable parentDrawable = getDrawable();
                    if (parentDrawable != null) {
                        Rect parentBounds = parentDrawable.getBounds();
                        a.initialize(bounds.width(), bounds.height(), parentBounds.width(), parentBounds.height());
                    } else {
                        a.initialize(bounds.width(), bounds.height(), 0, 0);
                    }
                }
                long currentTime = AnimationUtils.currentAnimationTimeMillis();
                boolean running = a.getTransformation(currentTime, layer.transformation);
                if (running) {
                    // animation is running: draw animation frame
                    Matrix animationFrameMatrix = layer.transformation.getMatrix();
                    mDrawMatrix.preConcat(animationFrameMatrix);
                    // int alpha = (int) (255 * layer.transformation.getAlpha());
                    draw(canvas, layer.drawable, mDrawMatrix);
                    pendingAnimations = true;
                } else {
                    boolean xTrans = false;
                    boolean yTrans = false;
                    layer.animation = null;
                    if (layer.isFillAfter()) {
                        Matrix animationFrameMatrix = layer.transformation.getMatrix();
                        // layer.matrix.getValues(values);
                        animationFrameMatrix.getValues(values);
                        if (values[Matrix.MTRANS_X] != 0) {
                            xTrans = true;
                        }
                        if (values[Matrix.MTRANS_Y] != 0) {
                            yTrans = true;
                        }

                        mDrawMatrix.preConcat(animationFrameMatrix);
                        mDrawMatrix.getValues(values);
                        float x = values[Matrix.MTRANS_X];
                        float y = values[Matrix.MTRANS_Y];

                        layer.matrix.preConcat(animationFrameMatrix);
                        layer.matrix.getValues(values);

                        if (xTrans)
                            values[Matrix.MTRANS_X] = x;
                        if (yTrans)
                            values[Matrix.MTRANS_Y] = y;

                        layer.matrix.setValues(values);
                        layer.matrix.getValues(values);

                        // layer.matrix.preConcat(animationFrameMatrix);
                        // layer.matrix.getValues(values);
                        // mDrawMatrix.set(layer.matrix);
                    }
                    draw(canvas, layer.drawable, mDrawMatrix);
                }
            }
        }
        if (pendingAnimations) {
            // invalidate if any pending animations
            invalidate();
        }
    }

    private void draw(Canvas canvas, Drawable drawable, Matrix matrix, int alpha) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.concat(matrix);
        drawable.setAlpha(alpha);
        drawable.draw(canvas);
        canvas.restore();
    }

    private void draw(Canvas canvas, Drawable drawable, Matrix matrix) {
        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.concat(matrix);
        drawable.draw(canvas);
        canvas.restore();
    }

    public Layer addLayer(Drawable d, Matrix m) {
        Layer layer = new Layer(d, m);
        d.setAlpha(255);
        mLayers.add(layer);
        invalidate();
        return layer;
    }

    public Layer addLayer(Drawable d) {
        return addLayer(d, null);
    }

    public Layer addLayer(int idx, Drawable d, Matrix m) {
        Layer layer = new Layer(d, m);
        mLayers.add(idx, layer);
        invalidate();
        return layer;
    }

    public Layer addLayer(int idx, Drawable d) {
        return addLayer(idx, d, null);
    }

    public void addLayer(Layer layer) {
        mLayers.add(layer);
        invalidate();
    }

    public void removeLayer(Layer layer) {
        layer.valid = false;
        mLayers.remove(layer);
    }

    public void removeIndex(int ind) {
        removeLayer(mLayers.get(mLayers.size()));
    }

    public void removeTopLayer() {
        removeLayer(mLayers.get(mLayers.size()));
    }

    public void removeAllLayers() {
        Iterator<Layer> iter = mLayers.iterator();
        while (iter.hasNext()) {
            Layer layer = iter.next();
            layer.valid = false;
            iter.remove();
        }
        invalidate();
    }

    public void stopAllAnimations() {
        int index = 0;
        if (mLayers != null) {
            for (Layer layer : mLayers) {
                if (layer.isValid() && layer.isAnimated()) {
                    layer.stopLayerAnimation(index);
                }
                index++;
            }
        }
    }

    public int getLayersSize() {
        return mLayers.size();
    }

    @Override
    protected boolean onSetAlpha(int alpha) {
        return true;
    }

    public class Layer extends Drawable {
        private Drawable drawable;
        private Animation animation;
        private final Transformation transformation;
        private Matrix matrix;
        private boolean valid;
        private Point startPoint;
        private boolean on;
        private boolean fillAfter;
        // private float myRotate;

        private Layer(Drawable d, Matrix m) {
            drawable = d;
            transformation = new Transformation();
            matrix = m;
            valid = true;
            fillAfter = false;
            on = false;

            Rect bounds = d.getBounds();
            if (bounds.isEmpty()) {
                if (d instanceof BitmapDrawable) {
                    int right = d.getIntrinsicWidth();
                    int bottom = d.getIntrinsicHeight();
                    d.setBounds(0, 0, right, bottom);
                } else {
                    String detailMessage = "drawable bounds are empty, use d.setBounds()";
                    throw new RuntimeException(detailMessage);
                }
            }
            d.setCallback(LayeredImageView.this);
        }

        public void startLayerAnimation(Animation a) throws RuntimeException {
            if (!valid) {
                String detailMessage = "this layer has already been removed";
                throw new RuntimeException(detailMessage);
            }
            transformation.clear();
            animation = a;
            if (a != null) {
                a.start();
            }
            invalidate();
        }

        public boolean isOn() {
            return on;
        }

        public void setOn(boolean on) {
            this.on = on;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(Boolean valid) {
            this.valid = valid;
        }

        public Matrix getMatrix() {
            return matrix;
        }

        public void setMatrix(Matrix matrix) {
            this.matrix = matrix;
        }

        public Boolean isAnimated() {
            return (animation != null);
        }

        public boolean isFillAfter() {
            return fillAfter;
        }

        public void setFillAfter(boolean fillAfter) {
            this.fillAfter = fillAfter;
        }

        public void stopLayerAnimation(int idx) throws RuntimeException {
            if (!valid) {
                String detailMessage = "this layer has already been removed";
                throw new RuntimeException(detailMessage);
            }
            if (animation != null) {
                animation = null;
                invalidate();
            }
        }

        public Drawable getDrawable() {
            return drawable;
        }

        public void setDrawable(Drawable drawable) {
            if (!this.drawable.getBounds().isEmpty() && drawable.getBounds().isEmpty()) {
                Rect rect = this.drawable.getBounds();
                drawable.setBounds(rect);
            }
            this.drawable = drawable;
        }

        @Override
        public void draw(Canvas canvas) {

        }

        @Override
        public void setAlpha(int alpha) {
            this.drawable.setAlpha(alpha);
            invalidate();
        }

        public Point getStartPoint() {
            if (startPoint == null) {
                this.setStartPoint(null);
            }
            return startPoint;
        }

        public void setStartPoint(Point startPoint) {
            // this is the point animation will start
            if (startPoint == null) {
                final float[] values = new float[9];
                this.matrix.getValues(values);
                this.startPoint = new Point((int) values[Matrix.MTRANS_X], (int) values[Matrix.MTRANS_Y]);
            } else {
                this.startPoint = startPoint;
            }
        }

        public int getXoffset() {
            final float[] values = new float[9];
            this.matrix.getValues(values);
            float xTrans = values[Matrix.MTRANS_X];
            return Math.round(xTrans);
        }

        public void setXoffset(int x) {
            int xOffset = (getStartPoint().x - this.getXoffset()) - x;
            if (xOffset == 0) return;
            this.matrix.preTranslate(xOffset, 0);
            invalidate();
        }

        public void setXoffsetCumulative(int x) {
            int xOffset = (getStartPoint().x - this.getXoffset()) - x;
            if (xOffset == 0) return;
            this.matrix.preTranslate(xOffset, 0);
            setStartPoint(null);
            invalidate();
        }

        public int getYoffset() {
            final float[] values = new float[9];
            this.matrix.getValues(values);
            float yTrans = values[Matrix.MTRANS_Y];
            return Math.round(yTrans);
        }

        public void setYoffset(int y) {
            int yOffset = (getStartPoint().y - this.getYoffset() - y);
            if (yOffset == 0) return;
            this.matrix.preTranslate(0, yOffset);
            invalidate();
        }

        public void setYoffsetCumulative(int y) {
            int yOffset = (getStartPoint().y - this.getYoffset() - y);
            if (yOffset == 0) return;
            this.matrix.preTranslate(0, yOffset);
            setStartPoint(null);
            invalidate();
        }

//        public float getRotate() {
//            return this.getMyRotate();
//        }
//
//        public void setRotate(float rotation) {
//            float toRotate = rotation - getRotate();
//            final float[] values = new float[9];
//            this.matrix.getValues(values);
//            matrix.postRotate(toRotate);
//            setMyRotate(toRotate + getRotate());
//            invalidate();
//        }
//
//        public float getMyRotate() {
//            return myRotate;
//        }
//
//        public void setMyRotate(float myRotate) {
//            this.myRotate = myRotate;
//        }


//
//        public void setY(int y) {
//            this.matrix.preTranslate(0,y);
//            invalidate();
//        }

        @Override
        public void setColorFilter(ColorFilter cf) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }

    }

    private class LayeredImageViewResources extends Resources {
        private LayeredImageViewResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
            super(assets, metrics, config);
        }
        public LayeredImageViewResources() {
            //noinspection ConstantConditions,ConstantConditions
            super(getContext().getAssets(), new DisplayMetrics(), getContext().getResources().getConfiguration());
        }

        @Override
        public Drawable getDrawable(int id) throws NotFoundException {
            Drawable d = super.getDrawable(id);
            if (d instanceof BitmapDrawable) {
                BitmapDrawable bd = (BitmapDrawable) d;
                bd.getBitmap().setDensity(DisplayMetrics.DENSITY_DEFAULT);
                bd.setTargetDensity(DisplayMetrics.DENSITY_DEFAULT);
            }
            return d;
        }
    }
}