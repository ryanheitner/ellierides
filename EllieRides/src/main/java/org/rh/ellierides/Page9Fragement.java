package org.rh.ellierides;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import junit.framework.Assert;

import org.rh.util.TextDrawable;
import org.rh.util.Utilities;

import java.util.Random;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page9Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {


    private final int dadX = 511;
    private final int dadY = 89;
    private final int ellieX = 72;
    private final int ellieY = 324;

    public Page9Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page9);


        final Matrix matrixDad = new Matrix();
        final Matrix matrixClock = new Matrix();
        final Matrix matrixHour = new Matrix();
        final Matrix matrixMinute = new Matrix();
        final Matrix matrixPainting = new Matrix();
        final Matrix matrixEllie = new Matrix();


        // use the translations from IPAD XCode
        matrixDad.preTranslate(dadX*_p,_p* dadY);
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixClock.preTranslate(52*_p,_p* 30);
        matrixHour.preTranslate(150*_p,_p* 128);
        matrixMinute.preTranslate(148*_p,_p* 127);
        matrixPainting.preTranslate(330*_p,_p* 220);
        final LayeredImageView.Layer paintingLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.painting9), matrixPainting);
        final LayeredImageView.Layer clockLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.clock9), matrixClock);
        final LayeredImageView.Layer hourLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.hour9), matrixHour);
        final LayeredImageView.Layer minuteLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.minute9), matrixMinute);
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie9), matrixEllie);
        final LayeredImageView.Layer dadLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.dad9), matrixDad);

        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImage(matrixClock, clockLayer, event) ||
                                touchedImage(matrixHour, hourLayer, event) || touchedImage(matrixMinute, minuteLayer, event)) {
                            animateClock();
                        } else if (touchedImage(matrixDad, dadLayer, event)) {
                            animateDad();
                        } else if (touchedImage(matrixEllie, ellieLayer, event)) {
                            animateEllie();
                        } else if (touchedImage(matrixPainting, paintingLayer, event)) {
                            animatePaiting(event);
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

            private void animatePaiting(MotionEvent event) {
                final float xTouch = event.getX();
                final float yTouch = event.getY();


                Drawable drawable;
                Random r = new Random();
                int x = r.nextInt(7);
                String fileName = "animal" + x;
                int drawableResourceId = res.getIdentifier(fileName, "drawable", MyApplication.getAppContext().getPackageName());
                drawable = res.getDrawable(drawableResourceId);
                Assert.assertNotNull("Must no be null", drawable);

                drawable.setBounds(0, 0, (drawable.getIntrinsicWidth()),(drawable.getIntrinsicHeight()));

                final float[] values = new float[9];
                matrixPainting.getValues(values);
                float paintingX = values[Matrix.MTRANS_X];
                float paintingY = values[Matrix.MTRANS_Y];


                final Matrix matrixAnimal = new Matrix();
                float originX = (xTouch - (0.5f * drawable.getIntrinsicWidth())) *  imageWidthProportion;
                float originY = (yTouch - (0.5f * drawable.getIntrinsicHeight())) * imageHeightProportion;

                float maxX = (paintingX + paintingLayer.getDrawable().getIntrinsicWidth() - (0.5f * drawable.getIntrinsicWidth()));
                float maxY = (paintingY + paintingLayer.getDrawable().getIntrinsicHeight() - (0.5f * drawable.getIntrinsicHeight()));
                originX = Math.min(originX, maxX);
                originY = Math.min(originY, maxY);
                matrixAnimal.preTranslate(originX, originY);
                layeredImageView.addLayer(drawable, matrixAnimal);
            }


            private void animateEllie() {
                Random r = new Random();
                int x = r.nextInt((int)(100*_p) - (int)(50*_p));
                Point origin = Utilities.getOrigin(matrixEllie);
                float xPos = origin.x + 142*_p + x;
                float yPos = origin.y;
                TextDrawable d = new TextDrawable(MyApplication.getAppContext());
                d.setText("?");
                d.setTextColor(Color.BLACK);
                /*
                TextView tv = (TextView) findViewById(R.id.appname);
Typeface face = Typeface.createFromAsset(getAssets(),
            "fonts/epimodem.ttf");
tv.setTypeface(face);
                 */
                Typeface typeface = Typeface.SERIF;
                d.setTypeface(typeface, 2);
                d.setTextSize(64*_p);
                d.setBounds((int)(100*_p),(int) (100*_p),(int) (200*_p),(int)(200*_p));
                final Matrix matrixQuestion = new Matrix();
                matrixQuestion.preTranslate(xPos, yPos);
                final LayeredImageView.Layer questionLayer = layeredImageView.addLayer(d, matrixQuestion);
                questionLayer.setStartPoint(null);

                ObjectAnimator anim = ObjectAnimator.ofInt(questionLayer, "Yoffset", 0, questionLayer.getYoffset());
                anim.setInterpolator(new AccelerateInterpolator());
                anim.setDuration(2000);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        layeredImageView.removeLayer(questionLayer);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                anim.start();

            }

            private void animateClock() {
                playSound(R.raw.fx_tick);
                final Runnable hourRotate = new Runnable() {
                    @Override
                    public void run() {
                        Animation clock = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_hour2);
                        hourLayer.startLayerAnimation(clock);
                    }
                };
                final Runnable minuteRotate = new Runnable() {
                    @Override
                    public void run() {
                        Animation clock = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_minute3);
                        minuteLayer.startLayerAnimation(clock);
                    }
                };
                hourRotate.run();
                minuteRotate.run();
            }

            private void animateDad() {
                playSound(R.raw.fx_fart);
            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

