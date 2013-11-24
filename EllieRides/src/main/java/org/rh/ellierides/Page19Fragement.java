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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import junit.framework.Assert;

import java.util.Random;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page19Fragement extends MyFragment implements MediaPlayer.OnCompletionListener, View.OnTouchListener {
    private final Matrix matrixEllie = new Matrix();
    private final Matrix matrixDad = new Matrix();
    private final Matrix matrixArm = new Matrix();
    private final Matrix matrixTree = new Matrix();
    private final Matrix matrixDove1 = new Matrix();
    private final Matrix matrixDove2 = new Matrix();
    private final Matrix matrixDove3 = new Matrix();
    private final Matrix matrixDove4 = new Matrix();
    private LayeredImageView.Layer dove1Layer;
    private LayeredImageView.Layer dove2Layer;
    private LayeredImageView.Layer dove3Layer;
    private LayeredImageView.Layer dove4Layer;
    private LayeredImageView.Layer dadLayer;
    private LayeredImageView.Layer ellieLayer;
    private LayeredImageView.Layer treeLayer;
    private LayeredImageView.Layer dadArmLayer;
    private AnimationDrawable adDove1;
    private AnimationDrawable adDove2;
    private AnimationDrawable adDove3;
    private AnimationDrawable adDove4;
    private Random r = new Random();
    private final int imageNumber = r.nextInt(4);

    public Page19Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setLayeredViewImage(R.drawable.page19);

        // use the translations from IPAD XCode
        matrixEllie.preTranslate(300*_p,_p* 133);
        matrixDad.preTranslate(480*_p,_p* 429);
        matrixArm.preTranslate(510*_p,_p* 470);
        matrixTree.preTranslate(0*_p,_p* 145);
        matrixDove1.preTranslate(108*_p,_p* 54);
        matrixDove2.preTranslate(71*_p,_p* 140);
        matrixDove3.preTranslate(241*_p,_p* 111);
        matrixDove4.preTranslate(200*_p,_p* 210);
        Bitmap bitmap;

        treeLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.tree19), matrixTree);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dove19c);
        adDove1 = (AnimationDrawable) res.getDrawable(R.drawable.anim_dove1);
        adDove1.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        dove1Layer = layeredImageView.addLayer(adDove1, matrixDove1);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dove19a);
        adDove2 = (AnimationDrawable) res.getDrawable(R.drawable.anim_dove2);
        adDove2.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        dove2Layer = layeredImageView.addLayer(adDove2, matrixDove2);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dove19a);
        adDove3 = (AnimationDrawable) res.getDrawable(R.drawable.anim_dove3);
        adDove3.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        dove3Layer = layeredImageView.addLayer(adDove3, matrixDove3);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dove19a);
        adDove4 = (AnimationDrawable) res.getDrawable(R.drawable.anim_dove2);
        adDove4.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        dove4Layer = layeredImageView.addLayer(adDove4, matrixDove4);

        dadLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.dad19b), matrixDad);
        ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie19a), matrixEllie);
        dadArmLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.dad19barm), matrixArm);

        super.addNavigationButtons();

        layeredImageView.setOnTouchListener(this);
        return layeredImageView;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (navigate(event)) return true;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (touchedImage(matrixDad, dadLayer, event)) {
                    animateDad();
                } else if (touchedImage(matrixEllie, ellieLayer, event)) {
                    animateEllie();
                } else if (touchedImage(matrixTree, treeLayer, event)) {
                    animateTree(event);
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

        String fileName = null;

        int rand = new Random().nextInt(21);
        switch (rand) {
            case 10:
                fileName = "squirrel";
                break;
            case 11:
                fileName = "squirrel2";
                break;
            default:
                switch (imageNumber) {
                    case 0:
                        fileName = "pear";
                        break;
                    case 1:
                        fileName = "cherry";
                        break;
                    case 2:
                        fileName = "berry";
                        break;
                    case 3:
                        fileName = "orange";
                        break;

                    default:
                        break;
                }
        }

        Drawable drawable;
        int drawableResourceId = res.getIdentifier(fileName, "drawable", MyApplication.getAppContext().getPackageName());
        drawable = res.getDrawable(drawableResourceId);
        Assert.assertNotNull("Must no be null", drawable);

        drawable.setBounds(0, 0, (drawable.getIntrinsicWidth()),(drawable.getIntrinsicHeight()));

        final float[] values = new float[9];
        matrixTree.getValues(values);
        float paintingX = values[Matrix.MTRANS_X];
        float paintingY = values[Matrix.MTRANS_Y];


        final Matrix matrixAnimal = new Matrix();
        float originX = (xTouch - (0.5f * drawable.getIntrinsicWidth())) * imageWidthProportion;
        float originY = (yTouch - (0.5f * drawable.getIntrinsicHeight())) * imageHeightProportion;

        float maxX = (paintingX + treeLayer.getDrawable().getIntrinsicWidth() - (0.5f * drawable.getIntrinsicWidth()));
        float maxY = (paintingY + treeLayer.getDrawable().getIntrinsicHeight() - (0.5f * drawable.getIntrinsicHeight()));
        originX = Math.min(originX, maxX);
        originY = Math.min(originY, maxY);
        matrixAnimal.preTranslate(originX, originY);
        layeredImageView.addLayer(1, drawable, matrixAnimal);
    }

    private void animateDad() {
        animateEllie();
    }

    private void animateDoves() {
        animateDove1();
        animateDove2();
        animateDove3();
        animateDove4();

    }

    private void animateDove(AnimationDrawable ad, LayeredImageView.Layer layer) {
        ad.stop();
        ad.start();

        TranslateAnimation translateAnimation = new TranslateAnimation(0, getImageScreenWidth(), 0, 0);
        translateAnimation.setDuration(4000);
        layer.setFillAfter(true);
        layer.startLayerAnimation(translateAnimation);
    }

    private void animateDove1() {
        animateDove(adDove1, dove1Layer);
    }

    private void animateDove2() {
        animateDove(adDove2, dove2Layer);
    }

    private void animateDove3() {
        animateDove(adDove3, dove3Layer);
    }

    private void animateDove4() {
        animateDove(adDove4, dove4Layer);
    }

    private void animateEllie() {
        animateDoves();
        playSound(R.raw.fx_laugh2);
        float x = ellieLayer.getDrawable().getIntrinsicWidth() / 10.0f;
        float y = ellieLayer.getDrawable().getIntrinsicHeight() / 1.5f;
        if (ellieLayer.isOn()) {
            x *= -1.0f;
            y *= -1.0f;
        }
        final float xf = x;
        final float yf = y;
        TranslateAnimation translateAnimation = new TranslateAnimation(0, x, 0, y);
        translateAnimation.setDuration(1000);
        translateAnimation.setRepeatCount(1);
        translateAnimation.setRepeatMode(Animation.REVERSE);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (!ellieLayer.isOn()) {
//                            ellieLayer.setFillAfter(true);
                    TranslateAnimation translateAnimation = new TranslateAnimation(0, xf, 0, yf);
                    translateAnimation.setDuration(1000);
                    translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            final float[] values = new float[9];
                            matrixEllie.getValues(values);
                            values[Matrix.MTRANS_X] = 341*_p;
                            values[Matrix.MTRANS_Y] = 300*_p;
                            matrixEllie.setValues(values);
                            layeredImageView.invalidate();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    ellieLayer.startLayerAnimation(translateAnimation);
                }
                ellieLayer.setOn(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ellieLayer.startLayerAnimation(translateAnimation);

        final Runnable shake = new Runnable() {
            @Override
            public void run() {
                Animation shakeAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.shake4);
                dadLayer.startLayerAnimation(shakeAnim);
                dadArmLayer.startLayerAnimation(shakeAnim);
            }
        };
        shake.run();
    }
}

