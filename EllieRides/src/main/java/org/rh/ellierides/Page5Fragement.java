package org.rh.ellierides;


import android.animation.ObjectAnimator;
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
public class Page5Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    private final int ellieX = 200;
    private final int ellieY = 350;
    private final int sammyX = 630;
    private final int sammyY = 330;

    public Page5Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page5);


        final Matrix matrixSammy = new Matrix();
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixMom = new Matrix();
        final Matrix matrixThink = new Matrix();

        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixMom.preTranslate(50*_p,_p* -45);
        matrixThink.preTranslate(240*_p,_p* 0);
        matrixSammy.preTranslate(sammyX*_p,_p* sammyY);

        final LayeredImageView.Layer sammyLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.sammyp5), matrixSammy);
        final LayeredImageView.Layer momLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.momp5), matrixMom);
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.elliep5), matrixEllie);
        final LayeredImageView.Layer thinkLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.thinkp5), matrixThink);
        thinkLayer.setAlpha(0);


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
                        } else if (touchedImage(matrixMom, momLayer, event)) {
                            animateMom();
                        } else if (touchedImage(matrixSammy, sammyLayer, event)) {
                            animateSammy();
                        } else if (touchedImage(matrixThink, thinkLayer, event)) {
                            hideThink();
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

            private void hideThink() {
                thinkLayer.setAlpha(0);
            }

            private void animateMom() {
                playSound(R.raw.fxv_get_your_bike);
            }

            private void animateSammy() {
                playSound(R.raw.fxv_without_wheel);
            }

            private void animateEllie() {
                if (!ellieLayer.isOn()) {
                    playSound(R.raw.fx_laugh);
                    ObjectAnimator anim = ObjectAnimator.ofInt(thinkLayer, "alpha", 0, 255);
                    anim.setDuration(7000);
                    anim.start();
                } else {
                    thinkLayer.setAlpha(0);
                }
                ellieLayer.setOn(!ellieLayer.isOn());

            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

