package org.rh.ellierides;


import android.animation.ObjectAnimator;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page3Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    final int ellieX = 170;
    final int ellieY = 440;

    public Page3Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page3);

        final Matrix matrixSammy = new Matrix();
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixMom = new Matrix();

        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p, ellieY*_p);
        matrixMom.preTranslate(124*_p, 21*_p);
        matrixSammy.preTranslate(455*_p, 430*_p);

        final LayeredImageView.Layer sammyLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.sammyp3), matrixSammy);
        final LayeredImageView.Layer momLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.momp3), matrixMom);
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie3a), matrixEllie);


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

            private void animateMom() {
                playSound(R.raw.fxv_hello2);
            }

            private void animateSammy() {
                playSound(R.raw.fxv_hello);
            }

            private void animateEllie() {
                final int offset = 85;
                playSound(R.raw.fx_magic);
                Drawable drawable;
                if (!ellieLayer.isOn()) {
                    drawable = reScaleBitmap(R.drawable.ellie3b, ellieX, ellieY);
                    drawable.setBounds(ellieLayer.getDrawable().getBounds());
                    ellieLayer.setDrawable(drawable);

                    ObjectAnimator anim = ObjectAnimator.ofInt(ellieLayer, "alpha", 255, 70);
                    anim.setDuration(1000);
                    anim.start();

                    ellieLayer.setStartPoint(null);
                    ObjectAnimator anim2 = ObjectAnimator.ofInt(ellieLayer, "Xoffset", 0, offset);
                    anim2.setDuration(1000);
                    anim2.start();


                } else {
                    drawable = reScaleBitmap(R.drawable.ellie3a, ellieX, ellieY);
                    drawable.setBounds(ellieLayer.getDrawable().getBounds());
                    ellieLayer.setDrawable(drawable);

                    ellieLayer.setStartPoint(null);
                    ObjectAnimator anim2 = ObjectAnimator.ofInt(ellieLayer, "Xoffset", 0, offset * -1);
                    anim2.setDuration(1000);
                    anim2.start();
                }
                ellieLayer.setOn(!ellieLayer.isOn());

            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

