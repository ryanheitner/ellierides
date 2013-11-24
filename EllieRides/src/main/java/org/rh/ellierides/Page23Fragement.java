package org.rh.ellierides;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page23Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {


    public Page23Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page23);


        final Matrix matrixEllie = new Matrix();
        final Matrix matrixMomDad = new Matrix();


        // use the translations from IPAD XCode
        matrixEllie.preTranslate(550*_p,_p* 325);
        matrixMomDad.preTranslate(50*_p,_p* 71);


        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie23), matrixEllie);
        final LayeredImageView.Layer momDadLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.momdad23), matrixMomDad);

        final AnimationDrawable adEllie = (AnimationDrawable) res.getDrawable(R.drawable.anim_ellie23);


        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImage(matrixMomDad, momDadLayer, event)) {
                            animateMomDad();
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


            private void animateMomDad() {
                playSound(R.raw.fxv_yeah);
            }

            private void animateEllie() {
                if (ellieLayer.isOn()) return;
                ellieLayer.setOn(true);
                ellieLayer.setAlpha(0);
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ellie23b);
                assert adEllie != null;
                adEllie.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                layeredImageView.addLayer(adEllie, matrixEllie);

                adEllie.stop();
                adEllie.start();
                playSound(R.raw.fxv_mom_watch);
                new Handler().postDelayed(stopEllieAnim, 3900);

            }

            private Runnable stopEllieAnim = new Runnable() {
                public void run() {
                    if (adEllie != null) {
                        adEllie.stop();
                    }
                    ellieLayer.setOn(false);
                }
            };


        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

