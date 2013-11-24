package org.rh.ellierides;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import junit.framework.Assert;

import org.rh.util.CountDownTimer;
import org.rh.util.TextDrawable;
import org.rh.util.Utilities;

import java.util.Random;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page10Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {
    private final int dadX = 420;
    private final int dadY = 27;
    private final int ellieX = 40;
    private final int ellieY = 278;

    public Page10Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page10);


        final Matrix matrixDad = new Matrix();
        final Matrix matrixPainting = new Matrix();
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixHelmet = new Matrix();


        // use the translations from IPAD XCode
        matrixDad.preTranslate(dadX*_p,_p* dadY);
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixHelmet.preTranslate(350*_p,_p* 556);
        matrixPainting.preTranslate(196*_p,_p* 75);
        final LayeredImageView.Layer paintingLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.painting9), matrixPainting);
        final LayeredImageView.Layer helmetLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.helmet130), matrixHelmet);
//        final LayeredImageView.Layer ellieLeftClosedLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie10leftclosed), matrixEllie);
//        final LayeredImageView.Layer ellieLeftOpenLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie10leftopen), matrixEllie);
//        final LayeredImageView.Layer ellieRightClosedLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie10rightclosed), matrixEllie);
//        final LayeredImageView.Layer ellieRightOpenLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie10rightopen), matrixEllie);
        final LayeredImageView.Layer dadLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.dad10), matrixDad);

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ellie10);
        final AnimationDrawable animationDrawable = (AnimationDrawable) res.getDrawable(R.drawable.anim_count_ellie);
        assert animationDrawable != null;
        animationDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(animationDrawable, matrixEllie);


        final AnimationDrawable animationDrawable2 = (AnimationDrawable) res.getDrawable(R.drawable.anim_count_ellie_right);
        assert animationDrawable2 != null;
        animationDrawable2.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer ellie2Layer = layeredImageView.addLayer(animationDrawable2, matrixEllie);
        ellie2Layer.setAlpha(0);
        dadLayer.setOn(false);


        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImage(matrixDad, dadLayer, event)) {
                            if (!ellieLayer.isOn()) {
                                animateDad();
                            }
                        } else if (touchedImage(matrixEllie, ellieLayer, event)) {
                            if (!ellieLayer.isOn()) {
                                animateEllie();
                            }
                        } else if (touchedImage(matrixPainting, paintingLayer, event)) {
                            animatePainting(event);
                        } else if (touchedImage(matrixHelmet, helmetLayer, event)) {
                            animateHelmet();
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

            private void animateHelmet() {
                Random r = new Random();
                int x = r.nextInt(5);
                String fileName = "helmet13" + x;
                int drawableResourceId = res.getIdentifier(fileName, "drawable", MyApplication.getAppContext().getPackageName());
                Drawable drawable = res.getDrawable(drawableResourceId);
//                drawable.setBounds(helmetLayer.getDrawable().getBounds());
                helmetLayer.setDrawable(drawable);
            }

            private void animatePainting(MotionEvent event) {
                final float xTouch = event.getX();
                final float yTouch = event.getY();


                Drawable drawable;
                Random r = new Random();
                int x = r.nextInt(7);
                String fileName = "animal" + x;
                int drawableResourceId = res.getIdentifier(fileName, "drawable", MyApplication.getAppContext().getPackageName());
                drawable = res.getDrawable(drawableResourceId);
                Assert.assertNotNull("Must no be null", drawable);

                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                final float[] values = new float[9];
                matrixPainting.getValues(values);
                float paintingX = values[Matrix.MTRANS_X];
                float paintingY = values[Matrix.MTRANS_Y];


                final Matrix matrixAnimal = new Matrix();
                float originX = (xTouch - (0.5f * drawable.getIntrinsicWidth())) * imageWidthProportion;
                float originY = (yTouch - (0.5f * drawable.getIntrinsicHeight())) * imageHeightProportion;

                float maxX = (paintingX + paintingLayer.getDrawable().getIntrinsicWidth() - (0.5f * drawable.getIntrinsicWidth())) ;
                float maxY = (paintingY + paintingLayer.getDrawable().getIntrinsicHeight() - (0.5f * drawable.getIntrinsicHeight()));
                originX = Math.min(originX, maxX);
                originY = Math.min(originY, maxY);
                matrixAnimal.preTranslate(originX, originY);
                layeredImageView.addLayer(drawable, matrixAnimal);
            }


            private void animateEllie() {
                ellieLayer.setOn(true);
                if (!dadLayer.isOn()) {
                    ellieLayer.setAlpha(255);
                    ellie2Layer.setAlpha(0);
                    animateEllieDraw(animationDrawable);
                } else {
                    ellieLayer.setAlpha(0);
                    ellie2Layer.setAlpha(255);
                    animateEllieDraw(animationDrawable2);
                }
                final Integer interval = 750;
                new CountDownTimer(750 * 11, interval) {
                    int count;
                    boolean start;

                    public void onTick(long millisUntilFinished) {
                        if (!start) {
                            start = true;
                            count = dadLayer.isOn() ? 0 : 11;
                        }
                        if (!dadLayer.isOn()) {
                            talkEllie("" + --count);
                        } else {
                            talkEllie("" + ++count);

                        }
                    }

                    public void onFinish() {
                        ellieLayer.setOn(false);
                    }

                    // generate a text drawable in a random position about xOrigin
                    public void talkEllie(String text) {
                        playSound("s_" + text);

                        Random r = new Random();
                        int x = r.nextInt((int)(100*_p) - (int)(50*_p));
                        Point origin = Utilities.getOrigin(matrixEllie);
                        float xPos = origin.x + ellieLayer.getDrawable().getIntrinsicWidth() / 2.0f + x;
                        float yPos = origin.y;
                        TextDrawable d = new TextDrawable(MyApplication.getAppContext());
                        d.setText(text);
                        d.setTextColor(Color.BLACK);
                        Typeface typeface = Typeface.SERIF;
                        d.setTypeface(typeface, 2);
                        d.setTextSize(64*_p);
                        d.setBounds(0, 0, ellieLayer.getDrawable().getIntrinsicWidth(), ellieLayer.getDrawable().getIntrinsicHeight());
                        final Matrix matrixTalk = new Matrix();
                        matrixTalk.preTranslate(xPos, yPos);

                        final LayeredImageView.Layer talkLayer = layeredImageView.addLayer(d, matrixTalk);
                        talkLayer.setStartPoint(null);
                        ObjectAnimator anim = ObjectAnimator.ofInt(talkLayer, "Yoffset", 0, talkLayer.getYoffset());

                        anim.setInterpolator(new LinearInterpolator());
                        anim.setDuration(2000);
                        anim.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                layeredImageView.removeLayer(talkLayer);
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
                }.start();
            }

            private void animateEllieDraw(AnimationDrawable drawable) {
                drawable.stop();
                drawable.start();
            }

            private void animateDad() {
                if (!dadLayer.isOn()) {
                    playSound(R.raw.fxv_stand_one_leg);
                } else {
                    playSound(R.raw.fxv_stand_other_leg);
                }
                dadLayer.setOn(!dadLayer.isOn());
            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

