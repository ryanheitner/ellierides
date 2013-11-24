package org.rh.ellierides;


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
public class Page7Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    private final int ellieX = 160;
    private final int ellieY = 230;
    private final int momX = 360;
    private final int momY = 40;
    private final int bikeX = 85;
    private final int bikeY = 300;

    public Page7Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page7);


        final Matrix matrixEllie = new Matrix();
        final Matrix matrixMom = new Matrix();
        final Matrix matrixBike = new Matrix();
        final Matrix matrixStar1 = new Matrix();
        final Matrix matrixStar2 = new Matrix();
        final Matrix matrixStar3 = new Matrix();
        final Matrix matrixStar4 = new Matrix();


        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixMom.preTranslate(momX*_p,_p* momY);
        matrixBike.preTranslate(bikeX*_p,_p* bikeY);
        matrixStar1.preTranslate(467*_p,_p* 194);
        matrixStar3.preTranslate(462*_p,_p* 189);
        matrixStar2.preTranslate(428*_p,_p* 221);
        matrixStar4.preTranslate(423*_p,_p* 216);


        layeredImageView.addLayer(res.getDrawable(R.drawable.bikep7), matrixBike);
        final LayeredImageView.Layer mom1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.momp7), matrixMom);
        final LayeredImageView.Layer mom2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.momp7b), matrixMom);
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie7), matrixEllie);
        final LayeredImageView.Layer star1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.star1), matrixStar1);
        final LayeredImageView.Layer star3Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.star1), matrixStar3);
        final LayeredImageView.Layer star2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.star3), matrixStar2);
        final LayeredImageView.Layer star4Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.star3), matrixStar4);
        mom2Layer.setAlpha(0);
        star1Layer.setAlpha(0);
        star2Layer.setAlpha(0);
        star3Layer.setAlpha(0);
        star4Layer.setAlpha(0);


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
                        } else if ((touchedImage(matrixMom, mom2Layer, event) || (touchedImage(matrixMom, mom2Layer, event)))) {
                            animateMom();
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
                if (!mom1Layer.isOn()) {
                    mom1Layer.setAlpha(0);
                    mom2Layer.setAlpha(255);
                    star1Layer.setAlpha(255);
                    star2Layer.setAlpha(255);
                    star3Layer.setAlpha(255);
                    star4Layer.setAlpha(255);
                    final Runnable spin = new Runnable() {
                        @Override
                        public void run() {
                            Animation spinAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_stars);
                            star1Layer.startLayerAnimation(spinAnim);
                            star2Layer.startLayerAnimation(spinAnim);
                            star3Layer.startLayerAnimation(spinAnim);
                            star4Layer.startLayerAnimation(spinAnim);
                        }
                    };
                    spin.run();
                } else {
                    mom1Layer.setAlpha(255);
                    mom2Layer.setAlpha(0);
                    star1Layer.setAlpha(0);
                    star2Layer.setAlpha(0);
                    star3Layer.setAlpha(0);
                    star4Layer.setAlpha(0);
                }
                mom1Layer.setOn(!mom1Layer.isOn());
            }

            private void animateEllie() {
                playSound(R.raw.fxv_sad);
            }


        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

