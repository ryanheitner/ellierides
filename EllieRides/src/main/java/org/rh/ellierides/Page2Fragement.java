package org.rh.ellierides;


import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
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
public class Page2Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    public Page2Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page2);

        final Matrix matrixTable = new Matrix();
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixBook1 = new Matrix();
        final Matrix matrixBook2 = new Matrix();
        final Matrix matrixGranny = new Matrix();
        final Matrix matrixBoo = new Matrix();
        final Matrix matrixLamp = new Matrix();

        // use the translations from IPAD XCode
        matrixTable.preTranslate(-37*_p, 255*_p); // pixels to offset
        matrixEllie.preTranslate(55*_p, 400*_p);
        matrixBook1.preTranslate(345*_p, 635*_p);
        matrixBook2.preTranslate(25*_p, 172*_p);
        matrixGranny.preTranslate(240*_p, 140*_p);
        matrixBoo.preTranslate(408*_p, 357*_p);
        matrixLamp.preTranslate(192*_p, 8*_p);

        final LayeredImageView.Layer tableLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.table2), matrixTable);
        final LayeredImageView.Layer book1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.bookp2), matrixBook1);
        final LayeredImageView.Layer book2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.bookp2b), matrixBook2);
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.elliep2), matrixEllie);
        final LayeredImageView.Layer grannyLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.grannygramps), matrixGranny);
        final LayeredImageView.Layer lampLayer = layeredImageView.addLayer(reScaleBitmap(R.drawable.lamp1, 230, 250), matrixLamp);
        final LayeredImageView.Layer booLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.elliep2b), matrixBoo);

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
                        } else if (touchedImage(matrixTable, tableLayer, event)) {
                            animateShrinkEllie();
                        } else if (touchedImage(matrixLamp, lampLayer, event)) {
                            animateLamp();
                        } else if (touchedImage(matrixBook1, book1Layer, event)) {
                            animateBook1();
                        } else if (touchedImage(matrixBook2, book2Layer, event)) {
                            animateBook2();
                        } else if (touchedImage(matrixBoo, booLayer, event)) {
                            animateBoo();
                        } else if (touchedImage(matrixGranny, grannyLayer, event)) {
                            animateGranny();
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

            private void animateBoo() {
                playSound(R.raw.s_boooooo);
            }


            private void animateLamp() {
                playSound(R.raw.fx_switch);
                Drawable drawable;
                if (!lampLayer.isOn()) {
                    drawable = reScaleBitmap(R.drawable.lamp2, 230, 250);
                    drawable.setBounds(lampLayer.getDrawable().getBounds());
                    lampLayer.setDrawable(drawable);
                } else {
                    drawable = reScaleBitmap(R.drawable.lamp1, 230, 250);
                    drawable.setBounds(lampLayer.getDrawable().getBounds());
                    lampLayer.setDrawable(drawable);
                }
                lampLayer.setOn(!lampLayer.isOn());

            }


            private void animateShrinkEllie() {
                final Runnable shrink = new Runnable() {
                    @Override
                    public void run() {
                        Animation shrinkAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.scale);
                        ellieLayer.startLayerAnimation(shrinkAnim);
                    }
                };
                shrink.run();
            }


            private void animateEllie() {
                playSound(R.raw.fx_chuckle);

            }

            private void animateBook1() {
                playSound(R.raw.fxv_book2);

            }

            private void animateBook2() {
                playSound(R.raw.fxv_book3);
            }

            private void animateGranny() {
                playSound(R.raw.fx_fright);

            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

