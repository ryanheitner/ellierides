package org.rh.ellierides;


import android.graphics.Matrix;
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
public class Page15Fragement extends MyFragment implements MediaPlayer.OnCompletionListener, View.OnTouchListener {

    private final int ellieDadX = 150;
    private final int ellieDadY = 60;
    private final Matrix matrixEllieDad = new Matrix();
    private final Matrix matrixGrass = new Matrix();
    private final Matrix matrixSnail = new Matrix();
    private Matrix matrixButterfly1 = new Matrix();
    private Matrix matrixButterfly2 = new Matrix();
    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimer2;
    private LayeredImageView.Layer grassLayer;
    private LayeredImageView.Layer snailLayer;
    private LayeredImageView.Layer butterfly1Layer;
    private LayeredImageView.Layer butterfly2Layer;
    private LayeredImageView.Layer ellieDad1Layer;
    private LayeredImageView.Layer ellieDad2Layer;

    public Page15Fragement() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page15);
        // use the translations from IPAD XCode
        matrixEllieDad.preTranslate(ellieDadX*_p,_p* ellieDadY);
        matrixGrass.preTranslate(0*_p,_p* 415);
        matrixSnail.preTranslate(960*_p,_p* 608);
        matrixButterfly1.preTranslate(650*_p,_p* 200);
        matrixButterfly2.preTranslate(112*_p,_p* 464);

        grassLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.grass15), matrixGrass);
        snailLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.snail), matrixSnail);
        butterfly1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.butterfly14), matrixButterfly1);
        butterfly2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.butterfly14b), matrixButterfly2);
        ellieDad1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.dadellie15), matrixEllieDad);
        ellieDad2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.dadellie15b), matrixEllieDad);
        ellieDad2Layer.setAlpha(0);

        super.addNavigationButtons();
        layeredImageView.setOnTouchListener(this);
        return layeredImageView;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (navigate(event)) return true;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if ((touchedImage(matrixEllieDad, ellieDad1Layer, event) || (touchedImage(matrixEllieDad, ellieDad2Layer, event)))) {
                    animateEllieDad();
                } else if (touchedImageBigger(matrixButterfly1, butterfly1Layer, event, 25)) {
                    animateButterfly1();
                } else if (touchedImageBigger(matrixButterfly2, butterfly2Layer, event, 50)) { // allow a greater tolerance for touch
                    animateButterfly2();
                } else if (touchedImageBigger(matrixSnail, snailLayer, event, 200)) { // allow a greater tolerance for touch
                    animateSnail();
                } else if (touchedImage(matrixGrass, grassLayer, event)) {
                    animateGrass(event);
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

    private void animateGrass(MotionEvent event) {
        final float xTouch = event.getX();
        final float yTouch = event.getY();


        Drawable drawable;
        Random r = new Random();
        int x = r.nextInt(7) + 1;
        String fileName = "flower" + x;
        int drawableResourceId = res.getIdentifier(fileName, "drawable", MyApplication.getAppContext().getPackageName());
        drawable = res.getDrawable(drawableResourceId);
        Assert.assertNotNull("Must no be null", drawable);

        drawable.setBounds(0, 0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());

        final float[] values = new float[9];
        matrixGrass.getValues(values);
        float grassX = values[Matrix.MTRANS_X];
        float grassY = values[Matrix.MTRANS_Y];


        final Matrix matrixFlower = new Matrix();
        float originX = (xTouch - (0.5f * drawable.getIntrinsicWidth())) * imageWidthProportion;
        float originY = (yTouch - (0.5f * drawable.getIntrinsicHeight())) * imageHeightProportion;

        float maxX = (grassX + grassLayer.getDrawable().getIntrinsicWidth() - (0.5f * drawable.getIntrinsicWidth()));
        float maxY = (grassY + grassLayer.getDrawable().getIntrinsicHeight() - (0.5f * drawable.getIntrinsicHeight()));
        originX = Math.min(originX, maxX);
        originY = Math.min(originY, maxY);
        matrixFlower.preTranslate(originX, originY);
        layeredImageView.addLayer(1, drawable, matrixFlower);
    }

    private void animateSnail() {
        final Runnable slide = new Runnable() {
            @Override
            public void run() {
                Animation snailAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.snail);
                snailLayer.startLayerAnimation(snailAnim);
            }
        };
        slide.run();
    }

    private void animateEllieDad() {
        if (!ellieDad1Layer.isOn()) {
            ellieDad2Layer.setAlpha(255);
            ellieDad1Layer.setAlpha(0);
        } else {
            ellieDad2Layer.setAlpha(0);
            ellieDad1Layer.setAlpha(255);
        }
        ellieDad1Layer.setOn(!ellieDad1Layer.isOn());
    }

    private void animateButterfly1() {
        countDownTimer = new CountDownTimer(30000, 40) {
            final int width = butterfly1Layer.getDrawable().getIntrinsicWidth();
            Random r = new Random();
            public void onTick(long millisUntilFinished) {
                butterfly1Layer.setXoffsetCumulative(r.nextInt(width/3)  - width/6 );
                butterfly1Layer.setYoffsetCumulative(r.nextInt(width/3)  - (width*9/60) );
            }
            public void onFinish() {
            }
        }.start();
    }


    private void animateButterfly2() {
        countDownTimer2 = new CountDownTimer(30000, 40) {
            final int width = butterfly2Layer.getDrawable().getIntrinsicWidth();
            Random r = new Random();
            public void onTick(long millisUntilFinished) {
                butterfly2Layer.setXoffsetCumulative(r.nextInt(width/3)  - width/6 );
                butterfly2Layer.setYoffsetCumulative(r.nextInt(width/3)  - (width*9/60) );
            }
            public void onFinish() {
            }
        }.start();
    }


    @Override
    public void stopAllTimers() {
        if (countDownTimer != null)
            countDownTimer.cancel();
        if (countDownTimer2 != null)
            countDownTimer2.cancel();
    }
}

