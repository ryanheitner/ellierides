package org.rh.ellierides;


import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page8Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {


    private final int dadX = 220;
    private final int dadY = 89;

    public Page8Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page8);


        final Matrix matrixMomDad = new Matrix();
        final Matrix matrixClock = new Matrix();
        final Matrix matrixPendulum = new Matrix();
        final Matrix matrixHour = new Matrix();
        final Matrix matrixMinute = new Matrix();
        final Matrix matrixPot1 = new Matrix();
        final Matrix matrixPot2 = new Matrix();
        final Matrix matrixPot3 = new Matrix();
        final Matrix matrixTap = new Matrix();
        final Matrix matrixKitchen = new Matrix();
        final Matrix matrixSink = new Matrix();
        final Matrix matrixMouse = new Matrix();
        final Matrix matrixWater = new Matrix();


        // use the translations from IPAD XCode
        matrixMomDad.preTranslate(dadX*_p,_p* dadY);
        matrixClock.preTranslate(52*_p,_p* 26);
        matrixPendulum.preTranslate(124*_p,_p* 195);
        matrixHour.preTranslate(132*_p,_p* 112);
        matrixMinute.preTranslate(123*_p,_p* 52);
        matrixPot1.preTranslate(667*_p,_p* 41);
        matrixPot2.preTranslate(750*_p,_p* 36);
        matrixPot3.preTranslate(872*_p,_p* 36);
        matrixTap.preTranslate(830*_p,_p* 300);
        matrixWater.preTranslate(841*_p,_p* 365);
        matrixKitchen.preTranslate(550*_p,_p* -150);
        matrixSink.preTranslate(558*_p,_p* 361);
        matrixMouse.preTranslate(980*_p,_p* 319);
        final LayeredImageView.Layer clockLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.clock8), matrixClock);
        final LayeredImageView.Layer hourLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.hour8), matrixHour);
        final LayeredImageView.Layer minuteLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.minute8), matrixMinute);
        final LayeredImageView.Layer pendulumLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.pendulum8), matrixPendulum);
        final LayeredImageView.Layer sinkLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.sink8), matrixSink);
        layeredImageView.addLayer(res.getDrawable(R.drawable.kitchen8), matrixKitchen);
        final LayeredImageView.Layer pot1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.pot8a), matrixPot1);
        final LayeredImageView.Layer pot2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.pot8b), matrixPot2);
        final LayeredImageView.Layer pot3Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.pot8b), matrixPot3);
        final LayeredImageView.Layer mouseLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.mouse), matrixMouse);
        final LayeredImageView.Layer tap1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.tap8), matrixTap);
        final LayeredImageView.Layer tap2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.tap8b), matrixTap);
        final LayeredImageView.Layer waterLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.water), matrixWater);
        final LayeredImageView.Layer momDad1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.dadmom8), matrixMomDad);
        final LayeredImageView.Layer momDad2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.dadmom8b), matrixMomDad);
        momDad2Layer.setAlpha(0);
        tap2Layer.setAlpha(0);
        mouseLayer.setAlpha(0);
        waterLayer.setAlpha(0);

        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImage(matrixClock, clockLayer, event) || touchedImage(matrixPendulum, pendulumLayer, event) ||
                                touchedImage(matrixHour, hourLayer, event) || touchedImage(matrixMinute, minuteLayer, event)) {
                            animateHickoryDickory();
                        } else if ((touchedImage(matrixMomDad, momDad1Layer, event) || (touchedImage(matrixMomDad, momDad2Layer, event)))) {
                            animateMomDad();
                        }  else if (touchedImage(matrixPot1, pot1Layer, event)) {
                            animatePot1();
                        } else if (touchedImage(matrixPot2, pot2Layer, event)) {
                            animatePot2();
                        } else if (touchedImage(matrixPot3, pot3Layer, event)) {
                            animatePot3();
                        } else if (touchedImageBigger(matrixSink, sinkLayer, event,10)) {
                            animateTap();
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

            private void animatePot1() {
                playSound(R.raw.fx_dong3);
                final Runnable shake = new Runnable() {
                    @Override
                    public void run() {
                        Animation shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake3);
                        pot1Layer.startLayerAnimation(shakeAnim);
                    }
                };
                shake.run();
            }

            private void animatePot2() {
                playSound(R.raw.fx_dong2);
                final Runnable shake = new Runnable() {
                    @Override
                    public void run() {
                        Animation shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake3);
                        pot2Layer.startLayerAnimation(shakeAnim);
                    }
                };
                shake.run();
            }

            private void animatePot3() {
                playSound(R.raw.fx_dong);
                final Runnable shake = new Runnable() {
                    @Override
                    public void run() {
                        Animation shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake3);
                        pot3Layer.startLayerAnimation(shakeAnim);
                    }
                };
                shake.run();
            }

            private void animateTap() {
                if (!tap1Layer.isOn()) {
                    tap1Layer.setAlpha(0);
                    tap2Layer.setAlpha(255);
                    waterLayer.setAlpha(255);
                    playSound(R.raw.fx_tap);
                    new Handler().postDelayed(mLaunchTask, 3000);
                } else {
                    stopWater();
                }
                tap1Layer.setOn(!tap1Layer.isOn());

            }

            private Runnable mLaunchTask = new Runnable() {
                public void run() {
                    stopWater();
                }
            };

            private void stopWater() {
                tap1Layer.setAlpha(255);
                waterLayer.setAlpha(0);
                tap2Layer.setAlpha(0);
                stopMediaPlayer();
            }

            private void animateHickoryDickory() {
                playSound(R.raw.fx_hickory);
                mouseLayer.setAlpha(255);
                final Runnable mouseRun = new Runnable() {
                    @Override
                    public void run() {
                        Animation mouseAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.hickory2);
                        Animation.AnimationListener listener = new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                mouseLayer.setAlpha(0);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }
                        };
                        assert mouseAnim != null;
                        mouseAnim.setAnimationListener(listener);
                        mouseLayer.startLayerAnimation(mouseAnim);

                    }
                };
                final Runnable hourRotate = new Runnable() {
                    @Override
                    public void run() {
                        Animation clock = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_hour3);
                        hourLayer.startLayerAnimation(clock);
                    }
                };
                final Runnable minuteRotate = new Runnable() {
                    @Override
                    public void run() {
                        Animation clock = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_minute);
                        minuteLayer.startLayerAnimation(clock);
                    }
                };
                final Runnable pendulumRotate = new Runnable() {
                    @Override
                    public void run() {
                        Animation rotateAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.pendulum);
                        pendulumLayer.startLayerAnimation(rotateAnimation);
                    }
                };

                mouseRun.run();
                hourRotate.run();
                minuteRotate.run();
                pendulumRotate.run();

            }


            private void animateMomDad() {
                if (!momDad1Layer.isOn()) {
                    momDad1Layer.setAlpha(0);
                    momDad2Layer.setAlpha(255);
                    playSound(R.raw.fx_kiss);
                } else {
                    momDad1Layer.setAlpha(255);
                    momDad2Layer.setAlpha(0);
                }
                momDad1Layer.setOn(!momDad1Layer.isOn());
            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

