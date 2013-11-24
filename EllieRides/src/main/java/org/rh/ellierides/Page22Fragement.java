
package org.rh.ellierides;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
public class Page22Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {


    public Page22Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page22);
        final Matrix matrixDadEllie = new Matrix();
        final Matrix matrixMom = new Matrix();
        final Matrix matrixLight1 = new Matrix();
        final Matrix matrixLight2 = new Matrix();
        final Matrix matrixLight3 = new Matrix();


        // use the translations from IPAD XCode
        matrixDadEllie.preTranslate(400*_p,_p* 70);
        matrixMom.preTranslate(57*_p,_p* 236);
        matrixLight1.preTranslate(350*_p,_p* -50);
        matrixLight2.preTranslate(160*_p,_p* 140);
        matrixLight3.preTranslate(295*_p,_p* 320);

        final LayeredImageView.Layer Light1aLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.light22a), matrixLight1);
        final LayeredImageView.Layer Light1bLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.light22c), matrixLight1);
        Light1aLayer.setOn(true);
        Light1bLayer.setAlpha(0);
        final LayeredImageView.Layer Light2aLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.light22b), matrixLight2);
        final LayeredImageView.Layer Light2bLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.light22d), matrixLight2);
        Light2aLayer.setOn(true);
        Light2bLayer.setAlpha(0);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.light22b);
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)(75*_p),(int)(130*_p), true);
        Drawable d = new BitmapDrawable(res, bitmap);
        final LayeredImageView.Layer Light3aLayer = layeredImageView.addLayer(d, matrixLight3);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.light22d);
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)(75*_p),(int)(130*_p), true);
        d = new BitmapDrawable(res, bitmap);
        final LayeredImageView.Layer Light3bLayer = layeredImageView.addLayer(d, matrixLight3);
        Light3aLayer.setOn(true);
        Light3bLayer.setAlpha(0);


        final LayeredImageView.Layer dadEllieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.dadellie22), matrixDadEllie);
        final LayeredImageView.Layer momLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.mom22), matrixMom);


        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImage(matrixDadEllie, dadEllieLayer, event)) {
                            animateDadEllie();
                        } else if (touchedImage(matrixMom, momLayer, event)) {
                            animateMom();
                        } else if (touchedImage(matrixLight1, Light1aLayer, event)) {
                            animateLight1();
                        } else if (touchedImage(matrixLight2, Light2aLayer, event)) {
                            animateLight2();
                        } else if (touchedImage(matrixLight3, Light3aLayer, event)) {
                            animateLight3();
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


            private void animateDadEllie() {
                playSound(R.raw.fxv_show_mom);
            }

            private void animateMom() {
                playSound(R.raw.fxv_dinner);
            }

            private void animateLight1() {
                animateLight(Light1aLayer, Light1bLayer);
            }

            private void animateLight2() {
                animateLight(Light2aLayer, Light2bLayer);
            }

            private void animateLight3() {
                animateLight(Light3aLayer, Light3bLayer);
            }


            private void animateLight(LayeredImageView.Layer lightALayer, LayeredImageView.Layer lightBLayer) {
                if (lightALayer.isOn()) {
                    lightALayer.setAlpha(0);
                    lightBLayer.setAlpha(255);
                } else {
                    lightALayer.setAlpha(255);
                    lightBLayer.setAlpha(0);
                }
                lightALayer.setOn(!lightALayer.isOn());
            }


        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

