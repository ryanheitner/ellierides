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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page21Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {


    public Page21Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page21);


        final Matrix matrixEllie = new Matrix();
        final Matrix matrixDad = new Matrix();
        final Matrix matrixTree = new Matrix();
        final Matrix matrixBike = new Matrix();


        // use the translations from IPAD XCode
        matrixEllie.preTranslate(373*_p,_p* 380);
        matrixDad.preTranslate(153*_p,_p* 104);
        matrixBike.preTranslate(642*_p,_p* 500);
        matrixTree.preTranslate(373*_p,_p* -342);

        final LayeredImageView.Layer tree1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.tree21), matrixTree);
        final LayeredImageView.Layer tree2Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.tree21b), matrixTree);
        tree2Layer.setAlpha(0);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bike21a);
        final AnimationDrawable adBike = (AnimationDrawable) res.getDrawable(R.drawable.anim_bike21);
        assert adBike != null;
        adBike.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer bikeLayer = layeredImageView.addLayer(adBike, matrixBike);

        final LayeredImageView.Layer dadLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.dad21), matrixDad);
        final LayeredImageView.Layer ellieLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie21), matrixEllie);


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
                        } else if (touchedImage(matrixDad, dadLayer, event)) {
                            animateDad();
                        } else if (touchedImage(matrixBike, bikeLayer, event)) {
                            animateBike();
                        } else if (touchedImage(matrixTree, tree1Layer, event)) {
                            animateTree();
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

            private void animateBike() {
                playSound(R.raw.fx_wheel_spin);
                adBike.stop();
                adBike.start();
                new Handler().postDelayed(stopSpin, 5000);

            }

            private Runnable stopSpin = new Runnable() {
                public void run() {
                    adBike.stop();
                }
            };

            private void animateTree() {
                if (tree1Layer.isOn())
                    return;

                tree1Layer.setAlpha(0);
                tree2Layer.setAlpha(255);
                tree1Layer.setOn(true);


                TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, 0, -tree1Layer.getDrawable().getIntrinsicHeight());
                translateAnimation.setDuration(750);
                translateAnimation.setRepeatCount(1);
                translateAnimation.setRepeatMode(TranslateAnimation.REVERSE);
                translateAnimation.setInterpolator(getActivity(), android.R.anim.accelerate_decelerate_interpolator);
                Animation.AnimationListener animationListener = new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        playSound(R.raw.fx_boing);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        tree1Layer.setOn(false);
                        playSound(R.raw.fx_boing);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                };
                translateAnimation.setAnimationListener(animationListener);
                tree2Layer.startLayerAnimation(translateAnimation);
            }


            private void animateDad() {
                playSound(R.raw.fxv_not_to_worry);
            }

            private void animateEllie() {
                playSound(R.raw.fx_ow);
            }


        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }


}

