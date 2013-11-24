package org.rh.ellierides;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import junit.framework.Assert;

import org.rh.util.CountDownTimer;

import java.util.Random;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page17Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {

    private final int ellieX = 20;
    private final int ellieY = 230;
    private final int dadX = 340;
    private final int dadY = 40;
    private CountDownTimer countDownTimer;


    public Page17Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page17);


        final Matrix matrixButterfly1 = new Matrix();

        final Matrix matrixTree = new Matrix();
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixDad = new Matrix();


        // use the translations from IPAD XCode
        matrixEllie.preTranslate(ellieX*_p,_p* ellieY);
        matrixDad.preTranslate(dadX*_p,_p* dadY);
        matrixButterfly1.preTranslate(272*_p,_p* 220);
        matrixTree.preTranslate(620*_p,_p* 0);

        final LayeredImageView.Layer butterfly1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.butterfly14), matrixButterfly1);
        final LayeredImageView.Layer treeLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.tree), matrixTree);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dad17a);
        final AnimationDrawable animationDrawableDad = (AnimationDrawable) res.getDrawable(R.drawable.anim_dad17);
        assert animationDrawableDad != null;
        animationDrawableDad.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer dadLayer = layeredImageView.addLayer(animationDrawableDad, matrixDad);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ellie17a);
        final AnimationDrawable animationDrawableEllie = (AnimationDrawable) res.getDrawable(R.drawable.anim_ellie17);
        assert animationDrawableEllie != null;
        animationDrawableEllie.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(animationDrawableEllie, matrixEllie);


        super.addNavigationButtons();

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImageBigger(matrixButterfly1, butterfly1Layer, event, 25)) {
                            animateButterfly1();
                        } else if (touchedImage(matrixTree, treeLayer, event)) {
                            animateTree(event);
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

            private void animateTree(MotionEvent event) {
                final float xTouch = event.getX();
                final float yTouch = event.getY();


                Drawable drawable;
                Random r = new Random();
                int x = r.nextInt(2);
                String fileName = "apple" + x;
                int drawableResourceId = res.getIdentifier(fileName, "drawable", MyApplication.getAppContext().getPackageName());
                drawable = res.getDrawable(drawableResourceId);
                Assert.assertNotNull("Must no be null", drawable);

                drawable.setBounds(0, 0, (drawable.getIntrinsicWidth()), (drawable.getIntrinsicHeight()));

                final float[] values = new float[9];
                matrixTree.getValues(values);
                float paintingX = values[Matrix.MTRANS_X];
                float paintingY = values[Matrix.MTRANS_Y];


                final Matrix matrixAnimal = new Matrix();
                float originX = (xTouch - (0.5f * drawable.getIntrinsicWidth())) * imageWidthProportion;
                float originY = (yTouch - (0.5f * drawable.getIntrinsicHeight())) * imageHeightProportion;


                float maxX = (paintingX + treeLayer.getDrawable().getIntrinsicWidth() - (0.5f * drawable.getIntrinsicWidth())) ;
                float maxY = (paintingY + treeLayer.getDrawable().getIntrinsicHeight() - (0.5f * drawable.getIntrinsicHeight()));
                originX = Math.min(originX, maxX);
                originY = Math.min(originY, maxY);
                matrixAnimal.preTranslate(originX, originY);
                layeredImageView.addLayer(drawable, matrixAnimal);
            }


            private void animateDad() {
                this.animateEllie();

            }

            private void animateEllie() {


                animationDrawableDad.stop();
                animationDrawableDad.start();


                animationDrawableEllie.stop();
                animationDrawableEllie.start();
                new Handler().postDelayed(stopEllieAnim, 6400);


                TranslateAnimation translateAnimation = new TranslateAnimation(0, ellieLayer.getDrawable().getIntrinsicWidth() * 1.1f, 0, ellieLayer.getDrawable().getIntrinsicHeight() / 5.5f);
                translateAnimation.setDuration(6400);
                translateAnimation.setInterpolator(getActivity(), android.R.anim.accelerate_decelerate_interpolator);
                ellieLayer.setFillAfter(true);
                ellieLayer.startLayerAnimation(translateAnimation);

            }

            private Runnable stopEllieAnim = new Runnable() {
                public void run() {
                    animationDrawableDad.stop();
                    animationDrawableEllie.stop();
                }
            };

            private void animateButterfly1() {
                countDownTimer = new CountDownTimer(30000, 40) {
                    final int width = butterfly1Layer.getDrawable().getIntrinsicWidth();
                    Random r = new Random();
                    public void onTick(long millisUntilFinished) {
                        butterfly1Layer.setXoffsetCumulative(r.nextInt(width/3)  - width/6 );
                        butterfly1Layer.setYoffsetCumulative(r.nextInt(width/3)  - (width*9/60) );
                    }
                    public void onFinish() {
                    }
                }.start();
            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }

    @Override
    public void stopAllTimers() {
        if (countDownTimer != null)
            countDownTimer.cancel();
    }


}

