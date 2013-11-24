package org.rh.ellierides;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import junit.framework.Assert;

import java.util.Random;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page18Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    private final int ellieX = 9;
    private final int ellieY = 186;
    private final int dadX = 600;
    private final int dadY = 180;


    public Page18Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page18);



        final Matrix matrixTree = new Matrix();
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixDad = new Matrix();
        final Matrix matrixGrass = new Matrix();


        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixDad.preTranslate(dadX*_p,_p* dadY);
        matrixGrass.preTranslate(0*_p,_p* 393);
        matrixTree.preTranslate(620*_p,_p* 0);

        final LayeredImageView.Layer grassLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.grass18), matrixGrass);
        final LayeredImageView.Layer dadLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.dad18), matrixDad);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ellie18a);
        final AnimationDrawable animationDrawableEllie = (AnimationDrawable) res.getDrawable(R.drawable.anim_ellie18);
        assert animationDrawableEllie != null;
        animationDrawableEllie.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(animationDrawableEllie, matrixEllie);


        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImage(matrixGrass, grassLayer, event)) {
                            animateGrass(event);
                        } else if (touchedImage(matrixDad, dadLayer, event)) {
                            animateDad();
                        } else if (touchedImage(matrixEllie, ellieLayer, event)) {
                            animateEllie();
                        }
                        layeredImageView.invalidate();

                        return true;
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        return true;
                }
                return false;
            }


            private void animateDad() {
                this.animateEllie();

            }

            private void animateEllie() {

                animationDrawableEllie.stop();
                animationDrawableEllie.start();

                TranslateAnimation translateAnimation = new TranslateAnimation(0, ellieLayer.getDrawable().getIntrinsicWidth() / 3.0f * 2.0f, 0, ellieLayer.getDrawable().getIntrinsicHeight() / 20.0f);
                translateAnimation.setDuration(6000);
                translateAnimation.setInterpolator(getActivity(), android.R.anim.accelerate_decelerate_interpolator);
                ellieLayer.setFillAfter(true);
                ellieLayer.startLayerAnimation(translateAnimation);

            }


            private void animateGrass(MotionEvent event) {
                final float xTouch = event.getX() ;
                final float yTouch = event.getY() ;


                Drawable drawable;
                Random r = new Random();
                int x = r.nextInt(7) + 1;
                String fileName = "flower" + x;
                int drawableResourceId = res.getIdentifier(fileName, "drawable", MyApplication.getAppContext().getPackageName());
                drawable = res.getDrawable(drawableResourceId);
                Assert.assertNotNull("Must no be null", drawable);

                drawable.setBounds(0, 0, (drawable.getIntrinsicWidth()),(drawable.getIntrinsicHeight()));

                final float[] values = new float[9];
                matrixGrass.getValues(values);
                float grassX = values[Matrix.MTRANS_X];
                float grassY = values[Matrix.MTRANS_Y];


                final Matrix matrixFlower = new Matrix();
                float originX = (xTouch - (0.5f * drawable.getIntrinsicWidth())) * imageWidthProportion;
                float originY = (yTouch - (0.5f * drawable.getIntrinsicHeight())) * imageHeightProportion;

                float maxX = (grassX + grassLayer.getDrawable().getIntrinsicWidth() - (0.5f * drawable.getIntrinsicWidth())) ;
                float maxY = (grassY + grassLayer.getDrawable().getIntrinsicHeight() - (0.5f * drawable.getIntrinsicHeight())) ;
                originX = Math.min(originX, maxX);
                originY = Math.min(originY, maxY);
                matrixFlower.preTranslate(originX, originY);
                layeredImageView.addLayer(1,drawable, matrixFlower);
            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

