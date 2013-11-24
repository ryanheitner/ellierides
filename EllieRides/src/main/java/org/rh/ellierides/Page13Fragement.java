package org.rh.ellierides;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.Random;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page13Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    final int ellieX = 170;
    final int ellieY = 177;
    final int dadX = 280;
    final int dadY = 43;
    final int bikeX = 250;
    final int bikeY = 131;


    public Page13Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page13);

        final Matrix matrixEllie = new Matrix();
        final Matrix matrixDad = new Matrix();
        final Matrix matrixBike = new Matrix();
        final Matrix matrixHelmet = new Matrix();


        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixDad.preTranslate(dadX*_p,_p* dadY);
        matrixBike.preTranslate(bikeX*_p,_p* bikeY);
        matrixHelmet.preTranslate(40*_p,_p* 511);

        final LayeredImageView.Layer bikeLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.bike13), matrixBike);

        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dad13);
        final AnimationDrawable animationDrawable = (AnimationDrawable) res.getDrawable(R.drawable.anim_spanner);
        assert animationDrawable != null;
        animationDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer dadLayer = layeredImageView.addLayer(animationDrawable, matrixDad);


        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie13), matrixEllie);
        final LayeredImageView.Layer helmetLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.helmet130), matrixHelmet);


        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImage(matrixHelmet, helmetLayer, event)) {
                            animateHelmet();
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

            private void animateHelmet() {
                Random r = new Random();
                int x = r.nextInt(5);
                String fileName = "helmet13" + x;
                int drawableResourceId = res.getIdentifier(fileName, "drawable", MyApplication.getAppContext().getPackageName());
                Drawable drawable = res.getDrawable(drawableResourceId);
                helmetLayer.setDrawable(drawable);
            }


            private void animateDad() {
                playSound(R.raw.fxv_does_that_hurt);
                animationDrawable.stop();
                animationDrawable.start();
            }

            private void animateEllie() {
                playSound(R.raw.fxv_not_at_all);
            }


        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

