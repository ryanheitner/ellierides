package org.rh.ellierides;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import org.rh.util.Utilities;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page1Fragement extends MyFragment {

    public Page1Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setLayeredViewImage(R.drawable.page1);

        final Matrix matrixWindow = new Matrix();
        final Matrix matrixDoorbell = new Matrix();
        final Matrix matrixClock = new Matrix();
        final Matrix matrixMouse = new Matrix();
        final Matrix matrixBuffet = new Matrix();
        final Matrix matrixChair = new Matrix();
        final Matrix matrixTracks = new Matrix();
        final Matrix matrixDoll = new Matrix();
        final Matrix matrixGlass1 = new Matrix();
        final Matrix matrixGlass2 = new Matrix();
        final Matrix matrixGlass3 = new Matrix();
        final Matrix matrixBall = new Matrix();
        final Matrix matrixTruck = new Matrix();
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixTrain = new Matrix();
        final Matrix matrixBook = new Matrix();
        final Matrix matrixPendulum = new Matrix();
        final Matrix matrixHour = new Matrix();
        final Matrix matrixMinute = new Matrix();



        // use the translations from IPAD XCode
        matrixWindow.preTranslate(152*_p, 95*_p); // pixels to offset
        matrixDoorbell.preTranslate(604*_p, 48*_p); // pixels to offset
        matrixClock.preTranslate(826*_p, 103*_p);
        matrixMouse.preTranslate(71*_p, 438*_p);
        matrixBuffet.preTranslate(177*_p, 295*_p);
        matrixChair.preTranslate(-21*_p, 314*_p);
        matrixTracks.preTranslate(0*_p, 702*_p);
        matrixDoll.preTranslate(471*_p, 523*_p);
        matrixGlass1.preTranslate(203*_p, 361*_p);
        matrixGlass2.preTranslate(228*_p, 361*_p);
        matrixGlass3.preTranslate(255*_p, 361*_p);
        matrixBall.preTranslate(635*_p, 586*_p);
        matrixTruck.preTranslate(117*_p, 530*_p);
        matrixEllie.preTranslate(266*_p, 314*_p);
        matrixBook.preTranslate(38*_p, 582*_p);
        matrixPendulum.preTranslate(860*_p, 175*_p);
        matrixTrain.preTranslate(101*_p, 647*_p);
        matrixHour.preTranslate(864*_p, 127*_p);
        matrixMinute.preTranslate(846*_p, 122*_p);

        final LayeredImageView.Layer windowLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.window), matrixWindow);
        final LayeredImageView.Layer doorbellLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.doorbell), matrixDoorbell);
        final LayeredImageView.Layer clockLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.clock), matrixClock);
        final LayeredImageView.Layer buffetLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.buffet), matrixBuffet);
        final LayeredImageView.Layer mouseLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.mouse), matrixMouse);
        final LayeredImageView.Layer chairLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.chair), matrixChair);
        layeredImageView.addLayer(res.getDrawable(R.drawable.tracks), matrixTracks);
        final LayeredImageView.Layer dollLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.doll), matrixDoll);
        final LayeredImageView.Layer glass1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.blue_ball), matrixGlass1);
        final LayeredImageView.Layer glass2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.blue_ball), matrixGlass2);
        final LayeredImageView.Layer glass3Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.blue_ball), matrixGlass3);
        final LayeredImageView.Layer bookLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.books), matrixBook);
        final LayeredImageView.Layer pendulumLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.pendulum), matrixPendulum);
        final LayeredImageView.Layer ballLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ball), matrixBall);
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ellie1a);
        final AnimationDrawable animationDrawable = (AnimationDrawable) res.getDrawable(R.drawable.anim_run_ellie);
        assert animationDrawable != null;
        animationDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(animationDrawable, matrixEllie);
        final LayeredImageView.Layer truckLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.truck), matrixTruck);

        final LayeredImageView.Layer trainLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.train), matrixTrain);
        final LayeredImageView.Layer hourLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.hour_hand), matrixHour);
        final LayeredImageView.Layer minuteLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.minute_hand), matrixMinute);


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
                        } else if (touchedImage(matrixBall, ballLayer, event)) {
                            animateBall();
                        } else if (touchedImage(matrixDoll, dollLayer, event)) {
                            animateDoll();
                        } else if (touchedImage(matrixDoorbell, doorbellLayer, event)) {
                            animateDoorbell();
                        } else if (touchedImage(matrixChair, chairLayer, event)) {
                            animateMouse();
                        } else if (touchedImage(matrixBook, bookLayer, event)) {
                            animateBook();
                        } else if (touchedImageLite(matrixClock, clockLayer, event) || touchedImageLite(matrixPendulum, pendulumLayer, event) ||
                                touchedImageLite(matrixHour, hourLayer, event) || touchedImageLite(matrixMinute, minuteLayer, event)) {
                            animateHickoryDickory();
                        } else if (touchedImage(matrixTrain, trainLayer, event)) {
                            animateTrain();
                        } else if (touchedImage(matrixTruck, truckLayer, event)) {
                            animateTruck();
                        } else if (touchedImage(matrixWindow, windowLayer, event)) {
                            animateBird();
                        } else if (touchedImage(matrixBuffet, buffetLayer, event)) {
                            animateGlass();
                        }
//    matrixDoorbell.setValues(values);
                        layeredImageView.invalidate();

                        return true;
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        return true;
                }
//                }
                return false;

            }

            private void animateGlass() {
                int number;
                if (glass1Layer.isValid()) {
                    number = 0;
                } else if (glass2Layer.isValid()) {
                    number = 1;
                } else if (glass3Layer.isValid()) {
                    number = 2;
                } else {
                    return;
                }
                animateGlassNumber(number);
            }

            private void animateGlassNumber(int glassNumber) {
                final int glassNum = glassNumber;
                final Runnable fall = new Runnable() {
                    @Override
                    public void run() {
                        Animation fallAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.glass);
                        Animation.AnimationListener listener = new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                switch (glassNum) {
                                    case 0:
                                        layeredImageView.removeLayer(glass1Layer);
                                        break;
                                    case 1:
                                        layeredImageView.removeLayer(glass2Layer);
                                        break;
                                    case 2:
                                        layeredImageView.removeLayer(glass3Layer);
                                        break;
                                    default:
                                        break;
                                }

                                playSound(R.raw.fx_glass_breaking);
                                final Matrix matrixBrokenGlass = new Matrix();
                                matrixBrokenGlass.preTranslate(203*_p + glassNum * 27*_p, 486*_p);
                                layeredImageView.addLayer(res.getDrawable(R.drawable.broken_glass), matrixBrokenGlass);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        };
                        assert fallAnim != null;
                        fallAnim.setAnimationListener(listener);
                        switch (glassNum) {
                            case 0:
                                glass1Layer.startLayerAnimation(fallAnim);
                                break;
                            case 1:
                                glass2Layer.startLayerAnimation(fallAnim);
                                break;
                            case 2:
                                glass3Layer.startLayerAnimation(fallAnim);
                                break;
                            default:
                                break;
                        }
                    }
                };
                fall.run();
            }

            void animateBook() {
                playSound(R.raw.fxv_book1);
                Point origin = Utilities.getOrigin(matrixBook);
                float imageWidth = bookLayer.getDrawable().getBounds().width();
                float imageHeight = bookLayer.getDrawable().getBounds().height();
                final float[] values = new float[9];
                matrixBook.getValues(values);
                float height = values[Matrix.MSCALE_Y] * imageHeight;


                final Matrix matrixPirate = new Matrix();
                matrixPirate.preTranslate(origin.x, origin.y - (float) (4.0 * height));
                final LayeredImageView.Layer pirateLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.pirate), matrixPirate);

                final Runnable fadePirate = new Runnable() {
                    @Override
                    public void run() {
                        Animation fadeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                        Animation.AnimationListener listener = new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                layeredImageView.removeLayer(pirateLayer);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        };
                        assert fadeAnim != null;
                        fadeAnim.setAnimationListener(listener);

                        pirateLayer.startLayerAnimation(fadeAnim);
                    }
                };

                fadePirate.run();
            }

            private void animateHickoryDickory() {
                playSound(R.raw.fx_hickory);
                final Runnable mouseRun = new Runnable() {
                    @Override
                    public void run() {
                        Animation mouse = AnimationUtils.loadAnimation(getActivity(), R.anim.hickory);
                        mouseLayer.startLayerAnimation(mouse);
                    }
                };
                final Runnable hourRotate = new Runnable() {
                    @Override
                    public void run() {
                        Animation clock = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_hour);
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

            private void animateTrain() {
                playSound(R.raw.fx_train_noise);
                final Runnable train = new Runnable() {
                    @Override
                    public void run() {
                        Animation mouse = AnimationUtils.loadAnimation(getActivity(), R.anim.train);
                        trainLayer.startLayerAnimation(mouse);
                    }
                };
                train.run();
            }

            private void animateMouse() {
                playSound(R.raw.fx_mousesqueak);
                final Runnable mouseRun = new Runnable() {
                    @Override
                    public void run() {
                        Animation mouse = AnimationUtils.loadAnimation(getActivity(), R.anim.run_mouse);
                        mouseLayer.startLayerAnimation(mouse);
                    }
                };
                mouseRun.run();

            }

            private void animateDoorbell() {
                playSound(R.raw.fx_doorbell);
                final Runnable shake = new Runnable() {
                    @Override
                    public void run() {
                        Animation shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake2);
                        doorbellLayer.startLayerAnimation(shakeAnim);
                    }
                };
                shake.run();
            }

            private void animateTruck() {
                playSound(R.raw.fx_tuning_car);

                final Runnable drive = new Runnable() {
                    @Override
                    public void run() {
                        Point origin = Utilities.getOrigin(matrixTruck);
                        TranslateAnimation translateAnimation = new TranslateAnimation(0, getImageScreenWidth() - origin.x, 0, getImageScreenHeight() - origin.y);
                        translateAnimation.setDuration(1750);
                        translateAnimation.setFillAfter(true);
                        translateAnimation.setInterpolator(getActivity(), android.R.anim.accelerate_interpolator);
                        truckLayer.startLayerAnimation(translateAnimation);
                    }
                };
                drive.run();
            }

            private void animateDoll() {
                playSound(R.raw.fxv_doll);
            }

            private void animateBird() {
                playSound(R.raw.fx_bird_song);

                Point origin = Utilities.getOrigin(matrixWindow);
                float imageWidth = windowLayer.getDrawable().getBounds().width();
                float imageHeight = windowLayer.getDrawable().getBounds().height();
                final float[] values = new float[9];
                matrixWindow.getValues(values);
                float width = values[Matrix.MSCALE_X] * imageWidth;
                float height = values[Matrix.MSCALE_Y] * imageHeight;


                final Matrix matrixBird = new Matrix();
                matrixBird.preTranslate(origin.x + (0.35f * width), origin.y +  (0.53f * height));
                final LayeredImageView.Layer birdLayer = layeredImageView.addLayer(0, res.getDrawable(R.drawable.little_blue_bird), matrixBird);

                final Runnable shake = new Runnable() {
                    @Override
                    public void run() {
                        Animation shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
                        Animation.AnimationListener listener = new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                layeredImageView.removeLayer(birdLayer);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        };
                        assert shakeAnim != null;
                        shakeAnim.setAnimationListener(listener);

                        birdLayer.startLayerAnimation(shakeAnim);
                    }
                };

                shake.run();

            }

            private void animateBall() {
                final Runnable bounce = new Runnable() {
                    @Override
                    public void run() {
                        Point origin = Utilities.getOrigin(matrixBall);
                        TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, origin.y * -1);
                        translateAnimation.setDuration(750);
                        translateAnimation.setRepeatCount(3);
                        translateAnimation.setRepeatMode(TranslateAnimation.REVERSE);
                        translateAnimation.setFillAfter(true);
                        translateAnimation.setInterpolator(getActivity(), android.R.anim.accelerate_decelerate_interpolator);

                        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                playSound(R.raw.fx_boing);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                                playSound(R.raw.fx_boing);
                            }
                        };
                        translateAnimation.setAnimationListener(animationListener);
                        ballLayer.startLayerAnimation(translateAnimation);
                    }
                };
                bounce.run();
            }

            private void animateEllie() {
                playSound(R.raw.fx_fire_engine);

                animationDrawable.stop();
                animationDrawable.start();

                final Runnable ellieRun = new Runnable() {
                    @Override
                    public void run() {
                        Animation runEllie = AnimationUtils.loadAnimation(getActivity(), R.anim.run_ellie1);
                        ellieLayer.startLayerAnimation(runEllie);
                    }
                };
                ellieRun.run();

            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }
}

