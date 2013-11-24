package org.rh.ellierides;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page12Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    final int ellieX = 39;
    final int ellieY = 69;
    final int dadX = 384;
    final int dadY = 43;
    final int bikeX = 139;
    final int bikeY = 185;
    final int wheelX = 170;
    final int wheelY = 587;
    final int pedalRX = 395;
    final int pedalRY = 547;
    final int pedalLX = 515;
    final int pedalLY = 540;
    final int seatX = 257;
    final int seatY = 298;

    public Page12Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page12);

        final Matrix matrixEllie = new Matrix();
        final Matrix matrixDad = new Matrix();
        final Matrix matrixBike = new Matrix();
        final Matrix matrixSeat = new Matrix();
        final Matrix matrixPedalLeft = new Matrix();
        final Matrix matrixPedalRight = new Matrix();
        final Matrix matrixWheel = new Matrix();


        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixDad.preTranslate(dadX*_p,_p* dadY);
        matrixBike.preTranslate(bikeX*_p,_p* bikeY);
        matrixSeat.preTranslate(seatX*_p,_p* seatY);
        matrixPedalRight.preTranslate(pedalRX*_p,_p* pedalRY);
        matrixPedalLeft.preTranslate(pedalLX*_p,_p* pedalLY);
        matrixWheel.preTranslate(wheelX*_p,_p* wheelY);

        final LayeredImageView.Layer dadLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.dad12), matrixDad);
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie12), matrixEllie);
        final LayeredImageView.Layer pedalLeftLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.pedal12l), matrixPedalLeft);
        final LayeredImageView.Layer seatLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.seat12), matrixSeat);
        final LayeredImageView.Layer bikeLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.bike12b), matrixBike);
        final LayeredImageView.Layer pedalRightLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.pedal12r), matrixPedalRight);
        final LayeredImageView.Layer wheelLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.wheel12r), matrixWheel);


        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImage(matrixSeat, seatLayer, event)) {
                            animateSeat();
                        } else if (touchedImage(matrixWheel, wheelLayer, event)) {
                            animateWheel();
                        } else if (touchedImage(matrixPedalLeft, pedalLeftLayer, event)) {
                            animatePedalLeft();
                        } else if (touchedImage(matrixPedalRight, pedalRightLayer, event)) {
                            animatePedalRight();
                        } else if (touchedImage(matrixBike, bikeLayer, event)) {
                            animateBike();
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

            private void animateBike() {
                this.animatePedalLeft();
                this.animatePedalRight();
                this.animateSeat();
                this.animateWheel();
            }

            private void animatePedalRight() {
                if (!pedalRightLayer.isOn()) {
                    PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("Xoffset", 0, pedalRightLayer.getDrawable().getIntrinsicWidth());  // positive left
                    PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("Yoffset", 0, pedalRightLayer.getDrawable().getIntrinsicWidth() * -1 / 2);  // positive is up
                    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(pedalRightLayer, pvhX, pvhY);
                    animator.setDuration(1000);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    animator.start();
                } else {
                    matrixPedalRight.reset();
                    matrixPedalRight.preTranslate(pedalRX*_p,_p* pedalRY);
                    layeredImageView.invalidate();
                }
                pedalRightLayer.setOn(!pedalRightLayer.isOn());
            }

            private void animatePedalLeft() {
                if (!pedalLeftLayer.isOn()) {
                    PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("Xoffset", 0, (int) (pedalLeftLayer.getDrawable().getIntrinsicWidth() / 2.5f));  // positive left
                    PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("Yoffset", 0, (int) (pedalLeftLayer.getDrawable().getIntrinsicWidth() / -1.8f));  // positive is up
                    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(pedalLeftLayer, pvhX, pvhY);
                    animator.setDuration(1000);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    animator.start();
                } else {
                    matrixPedalLeft.reset();
                    matrixPedalLeft.preTranslate(pedalLX*_p,_p* pedalLY);
                    layeredImageView.invalidate();
                }
                pedalLeftLayer.setOn(!pedalLeftLayer.isOn());
            }

            private void animateWheel() {
                if (!wheelLayer.isOn()) {
                    PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("Xoffset", 0,(int)(_p*50));  // positive left
                    PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("Yoffset", 0,(int)(_p*-30));  // positive is up
                    PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofFloat("Rotate", 45.0f);
                    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(wheelLayer, pvhX, pvhY, pvhRotate);
                    animator.setDuration(1000);
                    animator.start();
                } else {
                    matrixWheel.reset();
//                    wheelLayer.setMyRotate(0);
                    matrixWheel.preTranslate(wheelX*_p,_p* wheelY);
                    layeredImageView.invalidate();
                }
                wheelLayer.setOn(!wheelLayer.isOn());

            }

            private void animateSeat() {
                int x = (int) (seatLayer.getDrawable().getIntrinsicWidth() / -15.0);
                int y = (int) (seatLayer.getDrawable().getIntrinsicWidth() / -4.0);
                if (!seatLayer.isOn()) {
                    PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("Xoffset", 0, x);  // positive left
                    PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("Yoffset", 0, y);
                    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(seatLayer, pvhX, pvhY);
                    animator.setDuration(1000);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    animator.start();
                } else {
                    PropertyValuesHolder pvhX = PropertyValuesHolder.ofInt("Xoffset", x, 0);  // positive left
                    PropertyValuesHolder pvhY = PropertyValuesHolder.ofInt("Yoffset", y, 0);
                    ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(seatLayer, pvhX, pvhY);
                    animator.setDuration(1000);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    animator.start();
                }
                seatLayer.setOn(!seatLayer.isOn());

            }


            private void animateDad() {
                playSound(R.raw.fxv_you_are_ready);
            }

            private void animateEllie() {
                playSound(R.raw.fxv_i_dont_want_to);
            }


        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

