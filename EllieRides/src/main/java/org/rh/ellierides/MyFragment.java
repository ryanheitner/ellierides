package org.rh.ellierides;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import org.rh.util.TextDrawable;


/**
 * Created by ryanheitner on 9/7/13.
 */
public abstract class MyFragment extends Fragment implements MediaPlayer.OnCompletionListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    float imageHeightProportion, imageWidthProportion;
    private NavigationListener navigationListener;
    LayeredImageView layeredImageView;
    private int imageScreenWidth;
    private int imageScreenHeight;
    private Integer pageNumber;
    private MediaPlayer mediaPlayer = null;
    Resources res;
    private LayeredImageView.Layer read2MeButtonLayer;
    private LayeredImageView.Layer backButtonLayer;
    private LayeredImageView.Layer forwardButtonLayer;
    private Matrix matrixRead2MeButton = new Matrix();
    private Matrix matrixForwardButton = new Matrix();
    private Matrix matrixBackButton = new Matrix();

    float _p = 1.0f;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layeredImageView = new LayeredImageView(MyApplication.getAppContext());
        res = layeredImageView.getResources();
        Rect myRect = new Rect();
        layeredImageView.getWindowVisibleDisplayFrame(myRect);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Resources myRes = getResources();
        int weightText = myRes.getInteger(R.integer.weight_text);
        int weightImage = myRes.getInteger(R.integer.weight_image);
        imageScreenWidth = (int) ((float) metrics.widthPixels * weightImage / (weightText + weightImage));     // 1280  - 984
        imageScreenHeight = (int) ((float) metrics.heightPixels);                                              // 736
        // this corresponds to the sw520 folder 
        if (Math.min(imageScreenWidth,imageScreenHeight) < 520) _p = 0.5f;
        
        pageNumber = (getArguments().getInt(ARG_SECTION_NUMBER));
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            removeNavigationButtons();
        }
    }

    void getImageProportions() {
        //noinspection ConstantConditions
        int imageWidth = layeredImageView.getDrawable().getIntrinsicWidth();  // 1024
        int imageHeight = layeredImageView.getDrawable().getIntrinsicHeight();  // 768
        imageWidthProportion = (float) imageWidth / (float) imageScreenWidth;
        imageHeightProportion = (float) imageHeight / (float) imageScreenHeight;
    }

    void stopAllAnimations() {
        if (layeredImageView != null) {
            layeredImageView.stopAllAnimations();
        }
    }

    void setLayeredViewImage(int resID) {
        layeredImageView.setImageResource(resID);
        this.getImageProportions();
    }

    void playSound(String fileName) {
        int resID = res.getIdentifier(fileName, "raw", MyApplication.getAppContext().getPackageName());
        this.playSound(resID);
    }

    void playSound(int resID) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getActivity(), resID);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (mp != null) {
            try {
                mp.reset();
            } catch (Exception e) {

            }
            mp.release();
        }
    }

    void stopMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
            } catch (Exception e) {

            }
            mediaPlayer.release();
        }
    }

    BitmapDrawable reScaleBitmap(int resourceID, int width, int height) {
        Bitmap temp = BitmapFactory.decodeResource(getResources(), resourceID);
        return new BitmapDrawable(res, Bitmap.createScaledBitmap(temp, (int)(width*_p),(int) (height*_p), true));
    }

    public void stopAllTimers() {
    }

    void addNavigationButtons() {
        // Page number
        TextDrawable drawable = new TextDrawable(MyApplication.getAppContext());
        drawable.setText("" + (pageNumber - 1));
        drawable.setTextColor(Color.BLACK);
        Typeface typeface = Typeface.SERIF;
        drawable.setTypeface(typeface, 2);
        final int textSize = 20;
        drawable.setTextSize(textSize);
        drawable.setBounds(0, 0, 1, 1);
        final Matrix matrixPageNumber = new Matrix();
        //noinspection ConstantConditions,ConstantConditions
        int imageWidth = layeredImageView.getDrawable().getIntrinsicWidth();  // 1024
        int imageHeight = layeredImageView.getDrawable().getIntrinsicHeight();  // 768
        matrixPageNumber.preTranslate(imageWidth / 2, imageHeight - (textSize * 2)); // pixels to offset
        layeredImageView.addLayer(drawable, matrixPageNumber);

        Main myActivity = (Main) getActivity();
        if (!myActivity.isAutoPlay() && !myActivity.isRead2Me()) {
            matrixRead2MeButton.preTranslate(14*_p,_p* 10); // pixels to offset
            read2MeButtonLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.read_to_me_small), matrixRead2MeButton);
        }
        matrixForwardButton.preTranslate(925*_p,_p* 668); // pixels to offset
        if (!myActivity.isAutoPlay()) {
            matrixBackButton.preTranslate(14*_p,_p* 668); // pixels to offset
            backButtonLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.back_button), matrixBackButton);
            forwardButtonLayer = layeredImageView.addLayer(res.getDrawable(R.drawable.forward_button), matrixForwardButton);
        }
    }

    void removeNavigationButtons() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPrefs.getBoolean(Constant.kAutoPlay, false) || sharedPrefs.getBoolean(Constant.kReadToMe, false)) {
            if (read2MeButtonLayer != null) {
                layeredImageView.removeLayer(read2MeButtonLayer);
                matrixRead2MeButton = null;
            }
        }
        if (sharedPrefs.getBoolean(Constant.kAutoPlay, false)) {
            if (backButtonLayer != null) {
                layeredImageView.removeLayer(backButtonLayer);
                matrixBackButton = null;
            }
            removeForwardButton();
        }
    }

    void removeForwardButton() {
        if (forwardButtonLayer != null) {
            layeredImageView.removeLayer(forwardButtonLayer);
            matrixForwardButton = null;
        }
    }
    public void replaceForwardButton() {
        if (forwardButtonLayer != null) {
            forwardButtonLayer.setDrawable(res.getDrawable(R.drawable.parent_button_small));
            redraw();
        }
    }
    public void redrawForwardButton() {
        if (forwardButtonLayer != null) {
            forwardButtonLayer.setDrawable(res.getDrawable(R.drawable.forward_button));
            redraw();
        }
    }
    void redraw(){
        layeredImageView.invalidate();
    }

    Boolean navigate(MotionEvent event) {
        Main myActivity = (Main) getActivity();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getBackButtonLayer() != null && !myActivity.isAutoPlay()
                    && (touchedImageBigger(getMatrixBackButton(), getBackButtonLayer(), event, 20))) {
                navigationListener.onNavigateBack();
                return true;
            } else if (getForwardButtonLayer() != null && !myActivity.isAutoPlay()
                    && !(myActivity.getPageNumberText() == Constant.PAGES_TEXT)
                    && (touchedImageBigger(getMatrixForwardButton(), getForwardButtonLayer(), event, 20))) {
                navigationListener.onNavigateForward();
                return true;
            } else if (getRead2MeButtonLayer() != null && !myActivity.isAutoPlay() && !myActivity.isRead2Me()
                    && (touchedImageBigger(getMatrixRead2MeButton(), getRead2MeButtonLayer(), event,20))) {
                navigationListener.onRead2Me();
                return true;
            }
        }
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            navigationListener = (NavigationListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement ResumeDialogListener");
        }
    }

    LayeredImageView.Layer getBackButtonLayer() {
        return backButtonLayer;
    }

    public void setBackButtonLayer(LayeredImageView.Layer backButtonLayer) {
        this.backButtonLayer = backButtonLayer;
    }

    LayeredImageView.Layer getForwardButtonLayer() {
        return forwardButtonLayer;
    }

    LayeredImageView.Layer getRead2MeButtonLayer() {
        return read2MeButtonLayer;
    }

    public void setRead2MeButtonLayer(LayeredImageView.Layer read2MeButtonLayer) {
        this.read2MeButtonLayer = read2MeButtonLayer;
    }

    Matrix getMatrixForwardButton() {
        return matrixForwardButton;
    }

    Matrix getMatrixBackButton() {
        return matrixBackButton;
    }

    public void setMatrixBackButton(Matrix matrixBackButton) {
        this.matrixBackButton = matrixBackButton;
    }

    Matrix getMatrixRead2MeButton() {
        return matrixRead2MeButton;
    }

    public void setMatrixRead2MeButton(Matrix matrixRead2MeButton) {
        this.matrixRead2MeButton = matrixRead2MeButton;
    }

    public LayeredImageView getLayeredImageView() {
        return layeredImageView;
    }

    public int getImageScreenHeight() {
        return imageScreenHeight;
    }

    public int getImageScreenWidth() {
        return imageScreenWidth;
    }

    boolean touchedImage(Matrix matrix, LayeredImageView.Layer layer, MotionEvent event) {
        final float xTouch = event.getX();
        final float yTouch = event.getY();
// Used to get the values of a Matrix
        final float[] values = new float[9];
        matrix.getValues(values);
        final float imageHeight = layer.getDrawable().getBounds().height() / imageWidthProportion;
        final float imageWidth = layer.getDrawable().getBounds().width() / imageHeightProportion;
        final float width = values[Matrix.MSCALE_X] * imageWidth / imageWidthProportion;
        final float height = values[Matrix.MSCALE_Y] * imageHeight / imageHeightProportion;

        // This is the width of the touched image

        final float originX = values[Matrix.MTRANS_X] / imageWidthProportion;
        final float originY = values[Matrix.MTRANS_Y] / imageHeightProportion;

        if ((xTouch >= originX && xTouch <= originX + width) &&
                (yTouch >= originY && yTouch <= originY + height)) {

            float baseHeight = layer.getDrawable().getIntrinsicHeight() / imageWidthProportion;
            float baseWidth = layer.getDrawable().getIntrinsicWidth() / imageHeightProportion;
            int pixX = (int) ((((xTouch - originX)) * baseWidth / imageWidth) * imageWidthProportion);
            int pixY = (int) ((((yTouch - originY)) * baseHeight / imageHeight) * imageHeightProportion);

            Bitmap bitmap;
            Drawable drawable = layer.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                bitmap = ((BitmapDrawable) ((AnimationDrawable) drawable).getFrame(0)).getBitmap();
            } else {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            }

            try {
                //noinspection RedundantIfStatement
                if (bitmap.getPixel(pixX, pixY) != 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public boolean touchedImageLite(Matrix matrix, LayeredImageView.Layer layer, MotionEvent event) {
        final float xTouch = event.getX();
        final float yTouch = event.getY();
// Used to get the values of a Matrix
        final float[] values = new float[9];
        matrix.getValues(values);
        final float imageHeight = layer.getDrawable().getBounds().height() / imageWidthProportion;
        final float imageWidth = layer.getDrawable().getBounds().width() / imageHeightProportion;
        final float width = values[Matrix.MSCALE_X] * imageWidth / imageWidthProportion;
        final float height = values[Matrix.MSCALE_Y] * imageHeight / imageHeightProportion;

        // This is the width of the touched image

        final float originX = values[Matrix.MTRANS_X] / imageWidthProportion;
        final float originY = values[Matrix.MTRANS_Y] / imageHeightProportion;

        //noinspection RedundantIfStatement,RedundantIfStatement
        if ((xTouch >= originX) && (xTouch <= (originX + width)) &&
                (yTouch >= originY) && (yTouch <= (originY + height))) {
            return true;
        }
        return false;
    }

    boolean touchedImageBigger(Matrix matrix, LayeredImageView.Layer layer, MotionEvent event, int percentIncrease) {
        final float xTouch = event.getX();
        final float yTouch = event.getY();
// Used to get the values of a Matrix
        final float[] values = new float[9];
        matrix.getValues(values);

        final float originX = values[Matrix.MTRANS_X] / imageWidthProportion;
        final float originY = values[Matrix.MTRANS_Y] / imageHeightProportion;

        final float imageHeight = layer.getDrawable().getBounds().height() / imageWidthProportion;
        final float imageWidth = layer.getDrawable().getBounds().width() / imageHeightProportion;

        final float width = values[Matrix.MSCALE_X] * imageWidth / imageWidthProportion;
        final float height = values[Matrix.MSCALE_Y] * imageHeight / imageHeightProportion;

        final float halfMultiplier = ((percentIncrease / 2.0f) / 100.0f) + 1.0f;

        final float scaledWidth = width * halfMultiplier;
        final float scaledHeight = height * halfMultiplier;
        final float scaledOriginX = originX - (scaledWidth - width);
        final float scaledOriginY = originY - (scaledHeight - height);

        //noinspection RedundantIfStatement,RedundantIfStatement
        if ((xTouch >= scaledOriginX) && (xTouch <= (scaledOriginX + scaledWidth)) &&
                (yTouch >= scaledOriginY) && (yTouch <= (scaledOriginY + scaledHeight))) {
            return true;
        }
        return false;
    }


    public interface NavigationListener {
        public void onNavigateForward();

        public void onNavigateBack();

        public void onRead2Me();
    }
}


