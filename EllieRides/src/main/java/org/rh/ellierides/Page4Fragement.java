package org.rh.ellierides;


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
public class Page4Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    private final int ellieX = 250;
    private final int ellieY = 420;
    private final int sammyX = 480;
    private final int sammyY = 357;

    public Page4Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page4);


        final Matrix matrixSammy = new Matrix();
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixMom = new Matrix();

        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixMom.preTranslate(100*_p, 28*_p);
        matrixSammy.preTranslate(sammyX*_p,_p*sammyY);

        final LayeredImageView.Layer sammyLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.sammyp4), matrixSammy);
        final LayeredImageView.Layer momLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.momp4), matrixMom);
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.elliep4a), matrixEllie);


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
                playSound(R.raw.fxv_this_is_sammy);
            }

            private void animateSammy() {
                playSound(R.raw.fxv_really_fast);
                Drawable drawable;
                if (!sammyLayer.isOn()) {
                    drawable = reScaleBitmap(R.drawable.sammyp4b, sammyX, sammyY);
                    drawable.setBounds(sammyLayer.getDrawable().getBounds());

                } else {
                    drawable = reScaleBitmap(R.drawable.sammyp4, sammyX, sammyY);
                    drawable.setBounds(sammyLayer.getDrawable().getBounds());
                }
                sammyLayer.setDrawable(drawable);
                sammyLayer.setOn(!sammyLayer.isOn());
            }

            private void animateEllie() {
                playSound(R.raw.fxv_five_half);
                Drawable drawable;
                if (!ellieLayer.isOn()) {
                    drawable = reScaleBitmap(R.drawable.elliep4b, ellieX, ellieY);
                    drawable.setBounds(ellieLayer.getDrawable().getBounds());
                    ellieLayer.setDrawable(drawable);
                } else {
                    drawable = reScaleBitmap(R.drawable.elliep4a, ellieX, ellieY);
                    drawable.setBounds(ellieLayer.getDrawable().getBounds());
                    drawable.setAlpha(255);
                    ellieLayer.setDrawable(drawable);
                }
                ellieLayer.setOn(!ellieLayer.isOn());

            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

