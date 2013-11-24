package org.rh.ellierides;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import org.rh.util.CountDownTimer;

import java.util.Random;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page16Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    private final int ellieX = 20;
    private final int ellieY = 230;
    private final int dadX = 495;
    private final int dadY = 150;
    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimer2;


    public Page16Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page16);


        final Matrix matrixButterfly1 = new Matrix();
        final Matrix matrixButterfly2 = new Matrix();

        final Matrix matrixEllie = new Matrix();
        final Matrix matrixDad = new Matrix();


        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixDad.preTranslate(dadX*_p,_p* dadY);
        matrixButterfly1.preTranslate(292*_p,_p* 358);
        matrixButterfly2.preTranslate(414*_p,_p* 358);

        final LayeredImageView.Layer butterfly1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.butterfly14), matrixButterfly1);
        final LayeredImageView.Layer butterfly2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.butterfly14b), matrixButterfly2);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ellie16a);
        final AnimationDrawable animationDrawableEllie = (AnimationDrawable) res.getDrawable(R.drawable.anim_ellie16);
        assert animationDrawableEllie != null;
        animationDrawableEllie.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(animationDrawableEllie, matrixEllie);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dad16);
        final AnimationDrawable animationDrawableDad = (AnimationDrawable) res.getDrawable(R.drawable.anim_dad16);
        assert animationDrawableDad != null;
        animationDrawableDad.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer dadLayer = layeredImageView.addLayer(animationDrawableDad, matrixDad);


        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImageBigger(matrixButterfly1, butterfly1Layer, event, 25)) {
                            animateButterfly1();
                        } else if (touchedImageBigger(matrixButterfly2, butterfly2Layer, event, 50)) { // allow a greater tolerance for touch
                            animateButterfly2();
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

                animationDrawableDad.stop();
                animationDrawableDad.start();

                animationDrawableEllie.stop();
                animationDrawableEllie.start();
                new Handler().postDelayed(stopEllieAnim, 6000);

                TranslateAnimation translateAnimation = new TranslateAnimation(0, ellieLayer.getDrawable().getIntrinsicWidth(), 0, ellieLayer.getDrawable().getIntrinsicHeight() / 5.5f);
                translateAnimation.setDuration(6000);
                translateAnimation.setInterpolator(getActivity(), android.R.anim.accelerate_interpolator);
                ellieLayer.setFillAfter(true);
                ellieLayer.startLayerAnimation(translateAnimation);
            }

            private Runnable stopEllieAnim = new Runnable() {
                public void run() {
                    animationDrawableDad.stop();
                    animationDrawableEllie.stop();
                }
            };

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

        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }

    @Override
    public void stopAllTimers() {
        if (countDownTimer != null)
            countDownTimer.cancel();
        if (countDownTimer2 != null)
            countDownTimer2.cancel();
    }


}

