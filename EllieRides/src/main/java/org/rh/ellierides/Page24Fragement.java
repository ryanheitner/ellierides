package org.rh.ellierides;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import org.rh.util.TextDrawable;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page24Fragement extends MyFragment implements MediaPlayer.OnCompletionListener {


    public Page24Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setLayeredViewImage(R.drawable.page24);
        final Matrix matrixEllie = new Matrix();
        final Matrix matrixMom = new Matrix();
        final Matrix matrixSammy = new Matrix();
        final Matrix matrixTable = new Matrix();
        final Matrix matrixFork = new Matrix();
        final Matrix matrixSandwich = new Matrix();
        final Matrix matrixPlate = new Matrix();
        final Matrix matrixMilk = new Matrix();
        final Matrix matrixText = new Matrix();


        // use the translations from IPAD XCode
        matrixMom.preTranslate(561*_p,_p* 56);
        matrixTable.preTranslate(0*_p,_p* 607);
        matrixSammy.preTranslate(410*_p,_p* 150);
        matrixEllie.preTranslate(124*_p,_p* 259);
        matrixSandwich.preTranslate(208*_p,_p* 632);
        matrixPlate.preTranslate(194*_p,_p* 645);
        matrixMilk.preTranslate(332*_p,_p* 580);
        matrixFork.preTranslate(261*_p,_p* 552);
        matrixText.preTranslate(450*_p,_p* 433);

        final LayeredImageView.Layer momLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.mom24), matrixMom);
        layeredImageView.addLayer(res.getDrawable(R.drawable.table24), matrixTable);
        final LayeredImageView.Layer sammyLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.sammy24), matrixSammy);

        final LayeredImageView.Layer ellie1Layer = layeredImageView.addLayer(res.getDrawable(R.drawable.ellie24c), matrixEllie);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ellie24a);
        final AnimationDrawable adEllie = (AnimationDrawable) res.getDrawable(R.drawable.anim_ellie24);
        assert adEllie != null;
        adEllie.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final LayeredImageView.Layer ellie2Layer = layeredImageView.addLayer(adEllie, matrixEllie);


        ellie2Layer.setAlpha(0);
        ellie1Layer.setAlpha(255);

        final LayeredImageView.Layer plateLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.plate24), matrixPlate);
        final LayeredImageView.Layer milkFullLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.milk24f), matrixMilk);
        final LayeredImageView.Layer milkEmptyLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.milk24e), matrixMilk);
        milkEmptyLayer.setAlpha(0);

        final LayeredImageView.Layer sandwichLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.sandwich24), matrixSandwich);
        final LayeredImageView.Layer forkLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.fork24), matrixFork);

        TextDrawable textDrawable = new TextDrawable(MyApplication.getAppContext());
        textDrawable.setText(Constant.kTheEnd);
        textDrawable.setTextColor(Color.BLACK);
        // Typeface typeface = Typeface.SERIF;
        Typeface myTypeface = Typeface.createFromAsset(res.getAssets(), "mysteryquest_regular.ttf");

//        Typeface tf = Typeface.create("Helvetica",Typeface.SERIF);
//        Paint paint = new Paint();
//        paint.setTypeface(tf);
//        canvas.drawText("Sample text in bold Helvetica",0,0,paint);


        textDrawable.setTypeface(myTypeface, 2);
        textDrawable.setTextSize(56);
        textDrawable.setBounds(0, 0, 270, 172);
        layeredImageView.addLayer(textDrawable, matrixText);


        super.addNavigationButtons();
        // If we are on the last page do not display the forward button
        Main myActivity = (Main)getActivity();
        if (myActivity.getPageNumberText() == Constant.PAGES_TEXT) {
            super.replaceForwardButton();
        }

        View.OnTouchListener l = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (navigate(event)) return true;
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchedImage(matrixMom, momLayer, event)) {
                            animateMom();
                        } else if (touchedImageBigger(matrixFork, forkLayer, event, 100)) {
                            animateFork();
                        } else if (touchedImageLite(matrixMilk, milkFullLayer, event)) {
                            animateMilk();
                        } else if ((touchedImageBigger(matrixSandwich, sandwichLayer, event, 40) || (touchedImageLite(matrixPlate, plateLayer, event)))) {
                            animateSandwich();
                        } else if (touchedImageLite(matrixSammy, sammyLayer, event)) {
                            animateSammy();
                        } else if ((touchedImage(matrixEllie, ellie1Layer, event) || (touchedImage(matrixEllie, ellie2Layer, event)))) {
                            animateEllie();
                            // this is the parent button
                        } else if (getForwardButtonLayer() != null && (touchedImageLite(getMatrixForwardButton(), getForwardButtonLayer(), event))) {
                            Intent intent = new Intent(getActivity(),Parent.class);
                            startActivity(intent);
                            break;
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

            private void animateFork() {

                playSound(R.raw.fx_fork);
                forkLayer.setOn(true);
                forkLayer.setFillAfter(true);
                if (forkLayer.isOn()) {
                    final Runnable fork = new Runnable() {
                        @Override
                        public void run() {
                            Animation forkAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.fork);
                            forkLayer.startLayerAnimation(forkAnim);
                        }
                    };
                    fork.run();

                } else {
                    final Runnable fork = new Runnable() {
                        @Override
                        public void run() {
                            TranslateAnimation translateAnimation = new TranslateAnimation(0, forkLayer.getIntrinsicWidth(), 0, forkLayer.getIntrinsicHeight() / 2.0f);
                            translateAnimation.setDuration(200);
                            translateAnimation.setInterpolator(getActivity(), android.R.anim.accelerate_interpolator);
                            forkLayer.startLayerAnimation(translateAnimation);
                        }
                    };
                    fork.run();
                }
            }

            private void animateSandwich() {
                if (sandwichLayer.isOn()) return;
                if (forkLayer.isOn()) this.animateFork();
                sandwichLayer.setOn(true);
                ellie1Layer.setAlpha(0);
                sandwichLayer.setAlpha(0);
                ellie2Layer.setAlpha(255);
                adEllie.start();
                playSound(R.raw.fx_eat);
                new Handler().postDelayed(stopEllieAnim, 3500);

            }

            private Runnable stopEllieAnim = new Runnable() {
                public void run() {
                    adEllie.stop();
                    ellie1Layer.setOn(false);
                }
            };


            private void animateSammy() {
                playSound(R.raw.fx_bell);
            }


            private void animateMilk() {
                if (!milkFullLayer.isOn()) {
                    milkFullLayer.setAlpha(0);
                    milkEmptyLayer.setAlpha(255);
                    playSound(R.raw.fx_slurp);
                } else {
                    milkFullLayer.setAlpha(255);
                    milkEmptyLayer.setAlpha(0);
                }
                milkFullLayer.setOn(!milkFullLayer.isOn());

            }

            private void animateEllie() {
                playSound(R.raw.fxv_ride_my_bike);
            }

            private void animateMom() {
                playSound(R.raw.fxv_after_lunch);
            }
        };
        layeredImageView.setOnTouchListener(l);
        return layeredImageView;
    }

}

