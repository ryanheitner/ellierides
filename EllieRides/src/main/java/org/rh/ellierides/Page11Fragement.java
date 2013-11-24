package org.rh.ellierides;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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

import org.rh.util.CountDownTimer;
import org.rh.util.TextDrawable;
import org.rh.util.Utilities;

import java.util.Random;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page11Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {
    private final int dadX = 470;
    private final int dadY = 52;
    private final int ellieX = 30;
    private final int ellieY = 181;

    public Page11Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page11);
        final Matrix matrixDad = new Matrix();
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixHelmet = new Matrix();


        // use the translations from IPAD XCode
        matrixDad.preTranslate(dadX*_p,_p* dadY);
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixHelmet.preTranslate(230*_p,_p* 556);

        final LayeredImageView.Layer helmetLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.helmet130), matrixHelmet);
        final LayeredImageView.Layer dadLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.dad11), matrixDad);

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ellie11a);
        final AnimationDrawable animationDrawable = (AnimationDrawable) res.getDrawable(R.drawable.anim_fly_ellie);
        assert animationDrawable != null;
        animationDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(animationDrawable, matrixEllie);

        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImage(matrixDad, dadLayer, event)) {
                            animateDad();
                        } else if (touchedImage(matrixEllie, ellieLayer, event)) {
                            animateEllie();
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
                helmetLayer.setDrawable(drawable);
            }


            private void animateEllie() {
                ellieLayer.setOn(true);
                animateEllieDraw(animationDrawable);

                final Integer interval = 200;
                new CountDownTimer(100 * 30, interval) {
                    int count;
                    boolean start;

                    public void onTick(long millisUntilFinished) {
                        if (new Random().nextInt(2) == 1)
                            talkEllie("" + new Random().nextInt(500));
                    }

                    public void onFinish() {
                        ellieLayer.setOn(false);
                    }

                    // generate a text drawable in a random position about xOrigin
                    public void talkEllie(String text) {
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
                        ObjectAnimator anim = ObjectAnimator.ofInt(talkLayer, "Yoffset", 0, talkLayer.getYoffset() + (int) d.getTextSize());
                        anim.setInterpolator(new LinearInterpolator());
                        anim.setDuration(400);
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

                PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("Xoffset", 0,(int) (-400*_p));  // positive left
                PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("Yoffset", 0, (int)(100*_p));  // positive is up
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(ellieLayer, pvhX, pvhY);
                animator.setDuration(1000);
                animator.start();
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("Xoffset",(int)(_p*-400),(int)(_p* -800));  // positive left
                        PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("Yoffset",(int) (_p*100),(int)(_p* -40));  // positive is up
                        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(ellieLayer, pvhX, pvhY);
                        animator.setDuration(1000);
                        animator.start();
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("Xoffset",(int)(_p* 300), 0);  // positive left
                                PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("Yoffset",(int)(_p* -40), 0);  // positive is up
                                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(ellieLayer, pvhX, pvhY);
                                animator.setDuration(1000);
                                animator.start();
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });


            }


            private void animateEllieDraw(AnimationDrawable drawable) {
                drawable.stop();
                drawable.start();
            }

            private void animateDad() {
                playSound(R.raw.fxv_count_500);
            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }




}

