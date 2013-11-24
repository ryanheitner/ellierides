package org.rh.ellierides;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
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
import android.view.animation.TranslateAnimation;

import org.rh.util.CountDownTimer;
import org.rh.util.Utilities;

import java.util.Random;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page14Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    private final int ellieX = 231;
    private final int ellieY = 420;
    private final int dadX = 301;
    private final int dadY = 188;
    private CountDownTimer countDownTimer;
    private CountDownTimer countDownTimer2;


    public Page14Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page14);
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixDad = new Matrix();
        final Matrix matrixDog = new Matrix();
        final Matrix matrixCat = new Matrix();
        final Matrix matrixButterfly1 = new Matrix();
        final Matrix matrixButterfly2 = new Matrix();
        final Matrix matrixBird1 = new Matrix();
        final Matrix matrixBall = new Matrix();
        final Matrix matrixBird2 = new Matrix();


        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixDad.preTranslate(dadX*_p,_p* dadY);
        matrixDog.preTranslate(733*_p,_p* 438);
        matrixCat.preTranslate(380*_p,_p* 583);
        matrixButterfly1.preTranslate(581*_p,_p* 220);
        matrixButterfly2.preTranslate(550*_p,_p* 370);
        matrixBall.preTranslate(722*_p,_p* 358);
        matrixBird1.preTranslate(60*_p,_p* 72);
        matrixBird2.preTranslate(850*_p,_p* 605);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dog14);
        final AnimationDrawable animationDrawableDog = (AnimationDrawable) res.getDrawable(R.drawable.anim_dog);
        assert animationDrawableDog != null;
        animationDrawableDog.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer dogLayer = layeredImageView.addLayer(animationDrawableDog, matrixDog);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bird14b);
        final AnimationDrawable animationDrawableBird = (AnimationDrawable) res.getDrawable(R.drawable.anim_bird);
        assert animationDrawableBird != null;
        animationDrawableBird.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer bird1Layer = layeredImageView.addLayer(animationDrawableBird, matrixBird1);

        final LayeredImageView.Layer dadLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.dad14), matrixDad);
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie14), matrixEllie);
        final LayeredImageView.Layer catLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.cat14), matrixCat);
        final LayeredImageView.Layer butterfly1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.butterfly14), matrixButterfly1);
        final LayeredImageView.Layer butterfly2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.butterfly14b), matrixButterfly2);
        final LayeredImageView.Layer ballLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ball14), matrixBall);


        final LayeredImageView.Layer bird2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.bird14), matrixBird2);


        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImage(matrixCat, catLayer, event)) {
                            animateCat();
                        } else if (touchedImage(matrixDog, dogLayer, event)) {
                            animateDog();
                        } else if (touchedImage(matrixBird1, bird1Layer, event)) {
                            animateBird1();
                        } else if (touchedImage(matrixBird2, bird2Layer, event)) {
                            animateBird2();
                        } else if (touchedImageBigger(matrixBall, ballLayer, event, 50)) {
                            animateBall();
                        } else if (touchedImageBigger(matrixButterfly1, butterfly1Layer, event, 25)) {
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

            private void animateBird1() {
                animationDrawableBird.stop();
                animationDrawableBird.start();

                TranslateAnimation translateAnimation = new TranslateAnimation(0, getImageScreenWidth(), 0, 0);
                translateAnimation.setDuration(3000);
                translateAnimation.setInterpolator(getActivity(), android.R.anim.linear_interpolator);
                bird1Layer.startLayerAnimation(translateAnimation);
            }

            private void animateBird2() {
                playSound(R.raw.fx_chirp);
                final Runnable shake = new Runnable() {
                    @Override
                    public void run() {
                        Animation birdShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake2);
                        bird2Layer.startLayerAnimation(birdShake);
                    }
                };
                shake.run();
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


/*
private void animateButterfly2() {
countDownTimer2 = new CountDownTimer(30000, 200) {
public void onTick(long millisUntilFinished) {
final Runnable fly = new Runnable() {
@Override
public void run() {
Random r = new Random();
TranslateAnimation translateAnimation = new TranslateAnimation(0, r.nextInt(90) - 45, 0, r.nextInt(90) - 41);
translateAnimation.setDuration(180);
butterfly2Layer.startLayerAnimation(translateAnimation);
butterfly2Layer.setFillAfter(true);
Animation.AnimationListener animationListener = new Animation.AnimationListener() {
@Override
public void onAnimationStart(Animation animation) {

}

@Override
public void onAnimationEnd(Animation animation) {

}

@Override
public void onAnimationRepeat(Animation animation) {
}
};
translateAnimation.setAnimationListener(animationListener);
}
};
fly.run();
}

public void onFinish() {
}
}.start();
}
*/

            private void animateBall() {
                final Runnable ball = new Runnable() {
                    @Override
                    public void run() {
                        Animation ballAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.ball);
                        ballLayer.startLayerAnimation(ballAnim);
                    }
                };
                ball.run();
            }

            private void animateCat() {
                playSound(R.raw.fx_meow);
            }


            private void animateDog() {
                animationDrawableDog.stop();
                animationDrawableDog.setCallback(new AnimationDrawableCallback(animationDrawableDog, animationDrawableDog.getCallback()) {
                    @Override
                    public void scheduleDrawable(Drawable who, Runnable what, long when) {
                        animationDrawableDog.setBounds(0, 0, who.getIntrinsicWidth(), who.getIntrinsicHeight());
                        if (when % 3 == 0)
                            playSound(R.raw.fx_bark);
                        super.scheduleDrawable(who, what, when);
                    }

                    @Override
                    public void onAnimationComplete() {
                        animationDrawableDog.stop();
                    }
                });
                animationDrawableDog.start();

                final Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        Point origin = Utilities.getOrigin(matrixDog);
                        TranslateAnimation translateAnimation = new TranslateAnimation(0, origin.x * 2 / -3, 0, 0);
                        translateAnimation.setDuration(1750);
                        translateAnimation.setRepeatCount(1);
                        translateAnimation.setRepeatMode(TranslateAnimation.REVERSE);
                        translateAnimation.setInterpolator(getActivity(), android.R.anim.accelerate_decelerate_interpolator);

                        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        };
                        translateAnimation.setAnimationListener(animationListener);
                        dogLayer.startLayerAnimation(translateAnimation);
                    }
                };
                run.run();
            }

            private void animateDad() {
                playSound(R.raw.fxv_go_before_dark);
            }

            private void animateEllie() {
                playSound(R.raw.fxv_not_at_all);
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

