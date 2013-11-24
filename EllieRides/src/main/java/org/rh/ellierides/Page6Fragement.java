package org.rh.ellierides;


import android.animation.ObjectAnimator;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page6Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    private final int ellieX = 180;
    private final int ellieY = 380;
    private final int momX = 370;
    private final int momY = 50;

    public Page6Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page6);


        final Matrix matrixEllie = new Matrix();
        final Matrix matrixMom = new Matrix();
        final Matrix matrixBuffet = new Matrix();
        final Matrix matrixMinute = new Matrix();
        final Matrix matrixHour = new Matrix();
        final Matrix matrixWindow = new Matrix();
        final Matrix matrixBird = new Matrix();
        final Matrix matrixDoorOpen = new Matrix();
        final Matrix matrixDoorClosed = new Matrix();


        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixMom.preTranslate(momX*_p,_p* momY);
        matrixBuffet.preTranslate(0*_p,_p* 343);
        matrixWindow.preTranslate(-5*_p,_p* 0);
        matrixBird.preTranslate(82*_p,_p* 290);
        matrixMinute.preTranslate(171*_p,_p* 388);
        matrixHour.preTranslate(186*_p,_p* 390);
        matrixDoorOpen.preTranslate(620*_p,_p* 0);
        matrixDoorClosed.preTranslate(777*_p,_p* 0);

        final LayeredImageView.Layer birdLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.little_blue_bird), matrixBird);
        final LayeredImageView.Layer windowLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.windowp6), matrixWindow);
        final LayeredImageView.Layer buffetLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.buffet6), matrixBuffet);
        final LayeredImageView.Layer minuteLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.minutep6), matrixMinute);
        final LayeredImageView.Layer hourLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.hourp6), matrixHour);
        final LayeredImageView.Layer mom1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.momp6), matrixMom);
        final LayeredImageView.Layer mom2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.momp6b), matrixMom);
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie6), matrixEllie);
        final LayeredImageView.Layer doorOpenLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.invisible_door_open), matrixDoorOpen);
        final LayeredImageView.Layer doorClosedLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.invisible_door_open), matrixDoorClosed);
        doorOpenLayer.setOn(true);
        mom2Layer.setAlpha(0);
        birdLayer.setAlpha(0);


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
                        } else if ((touchedImage(matrixMom, mom1Layer, event) || (touchedImage(matrixMom, mom2Layer, event)))) {
                            animateMom();
                        } else if (touchedImage(matrixWindow, windowLayer, event)) {
                            animateBird();
                        } else if (touchedImage(matrixBuffet, buffetLayer, event)) {
                            animateBuffet();
                        } else if (touchedImageLite(matrixDoorOpen, doorOpenLayer, event)) {
                            animateDoorOpen();
                        } else if (touchedImageLite(matrixDoorClosed, doorClosedLayer, event)) {
                            animateDoorClosed();
//                        } else if (doorTouched(matrixDoorOpen, doorOpenLayer, event)) {
//                            animateDoorOpen();
//                        } else if (doorTouched(matrixDoorClosed, doorClosedLayer, event)) {
//                            animateDoorClosed();

                        }
                        //    matrixDoorbell.setValues(values);
                        layeredImageView.invalidate();

                        return true;
                    case MotionEvent.ACTION_OUTSIDE:
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        return true;
                }
                return false;
            }

            private void animateDoorClosed() {
                if (!doorOpenLayer.isOn()) {
                    layeredImageView.setImageResource(R.drawable.page6);
                    playSound(R.raw.fx_dooropen);
                    doorOpenLayer.setOn(true);
                }
            }

            private void animateDoorOpen() {
                if (doorOpenLayer.isOn()) {
                    layeredImageView.setImageResource(R.drawable.page6b);
                    playSound(R.raw.fx_doorslam);
                    doorOpenLayer.setOn(false);
                }
            }

//            public Boolean doorTouched(Matrix matrix, LayeredImageView.Layer layer, MotionEvent event) {
//                final float xTouch = event.getX();
//                final float yTouch = event.getY();
//// Used to get the values of a Matrix
//                final float[] values = new float[9];
//                matrix.getValues(values);
//                float originX = values[Matrix.MTRANS_X];
//                float originY = values[Matrix.MTRANS_Y];
//
//
//                float imageWidth = layer.getDrawable().getBounds().width();
//                float imageHeight = layer.getDrawable().getBounds().height();
//                float width = values[Matrix.MSCALE_X] * imageWidth;
//                float height = values[Matrix.MSCALE_Y] * imageHeight;
//
//                if ((xTouch >= originX && xTouch <= originX + width) &&
//                        (yTouch >= originY && yTouch <= originY + height)) {
//                    return true;
//
//                }
//                return false;
//            }


            private void animateMom() {
                if (!mom1Layer.isOn()) {
                    mom1Layer.setAlpha(0);
                    mom2Layer.setAlpha(255);
                } else {
                    mom1Layer.setAlpha(255);
                    mom2Layer.setAlpha(0);
                }
                mom1Layer.setOn(!mom1Layer.isOn());
            }

            private void animateBird() {
                playSound(R.raw.fx_bird_song);
                birdLayer.setAlpha(255);
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
                                ObjectAnimator anim = ObjectAnimator.ofInt(birdLayer, "alpha", 255, 0);
                                anim.setDuration(1000);
                                anim.start();
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


            private void animateEllie() {
                playSound(R.raw.fxv_sore_head);
            }

            private void animateBuffet() {
                playSound(R.raw.fx_tick);
                final Runnable hourRotate = new Runnable() {
                    @Override
                    public void run() {
                        Animation clock = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_hour4);
                        hourLayer.startLayerAnimation(clock);
                    }
                };
                final Runnable minuteRotate = new Runnable() {
                    @Override
                    public void run() {
                        Animation clock = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_minute2);
                        minuteLayer.startLayerAnimation(clock);
                    }
                };
                hourRotate.run();
                minuteRotate.run();
            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

