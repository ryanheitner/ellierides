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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import junit.framework.Assert;

import org.rh.util.CountDownTimer;

import java.util.Random;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page20Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    private final int ellieX = 100;
    private final int ellieY = 305;
    private final int dadX = 120;
    private final int dadY = 19;
    private CountDownTimer countDownTimer;


    public Page20Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page20);
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixDad = new Matrix();
        final Matrix matrixGrass = new Matrix();

        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixDad.preTranslate(dadX*_p,_p* dadY);
        matrixGrass.preTranslate(0*_p,_p* 423);

        final LayeredImageView.Layer grassLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.grass20), matrixGrass);
        final LayeredImageView.Layer dad1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.dad20), matrixDad);
        final LayeredImageView.Layer dad2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.dad20b), matrixDad);
        dad2Layer.setAlpha(0);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ellie20a);
        final AnimationDrawable animationDrawableEllie = (AnimationDrawable) res.getDrawable(R.drawable.anim_ellie20);
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
                        if (touchedImage(matrixEllie, ellieLayer, event)) {
                            animateEllie();
                        } else if (touchedImage(matrixGrass, grassLayer, event)) {
                            animateGrass(event);
                        } else if ((touchedImage(matrixDad, dad1Layer, event) || (touchedImage(matrixDad, dad2Layer, event)))) {
                            animateDad();
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
                playSound(R.raw.fxv_hooray);
                countDownTimer = new CountDownTimer(3500, 500) {
                    int i = 0;

                    @Override
                    public void onTick(long millisUntilFinished) {
                        i++;
                        if (i % 2 == 1) {
                            dad1Layer.setAlpha(0);
                            dad2Layer.setAlpha(255);
                            playSound(R.raw.fx_clap);
                        } else {
                            dad1Layer.setAlpha(255);
                            dad2Layer.setAlpha(0);
                        }
                    }

                    @Override
                    public void onFinish() {
                        dad1Layer.setAlpha(255);
                        dad2Layer.setAlpha(0);
                    }
                }.start();

            }

            private void animateEllie() {
                animationDrawableEllie.stop();
                animationDrawableEllie.start();

                final Runnable ride = new Runnable() {
                    @Override
                    public void run() {
                        Animation rideAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.ellie20);
                        ellieLayer.startLayerAnimation(rideAnim);
                    }
                };
                ride.run();


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

                drawable.setBounds(0, 0, (drawable.getIntrinsicWidth()), (drawable.getIntrinsicHeight()));

                final float[] values = new float[9];
                matrixGrass.getValues(values);
                float grassX = values[Matrix.MTRANS_X];
                float grassY = values[Matrix.MTRANS_Y];


                final Matrix matrixFlower = new Matrix();
                float originX = (xTouch - (0.5f * drawable.getIntrinsicWidth())) * imageWidthProportion;
                float originY = (yTouch - (0.5f * drawable.getIntrinsicHeight())) * imageHeightProportion;

                float maxX = (grassX + grassLayer.getDrawable().getIntrinsicWidth() - (0.5f * drawable.getIntrinsicWidth()))  ;
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
    @Override
    public void stopAllTimers() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

}

