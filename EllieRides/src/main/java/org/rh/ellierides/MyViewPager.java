package org.rh.ellierides;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;


/**
 * Created by ryanheitner on 9/22/13.
 */
public class MyViewPager extends ViewPager {

    private int viewpagerType;
    private GestureDetectorCompat mDetector;
    private Context context;

    public interface PageChangeListener {
        public void onTextPageChangeIntercept(Boolean forward);

        public void onImagePageChangeIntercept(Boolean forward);
    }

    private PageChangeListener pageChangeListener;

    private boolean isPagingEnabled;

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.isPagingEnabled = false;
        setMyScroller();

        TypedArray a = null;
        try {
            //noinspection ConstantConditions
            a = getContext().obtainStyledAttributes(attrs, R.styleable.ViewPagerType);
            assert a != null;
            viewpagerType = a.getInt(R.styleable.ViewPagerType_view_pager_type, 0);
        } finally {
            if (a != null) {
                a.recycle(); // ensure this is always called
            }
        }
        mDetector = new GestureDetectorCompat(this.context, new MyGestureListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isPagingEnabled) {
            return false;
        }


        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
//       // if (!super.onTouchEvent(event)) return false;

    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {

        if (this.isPagingEnabled) {
            return super.onInterceptTouchEvent(event);
        }
        this.mDetector.onTouchEvent(event);
        return false;
    }


    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the pageChangeListener so we can send events to the host
            pageChangeListener = (PageChangeListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement PageChangeListener");
        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onDown(MotionEvent event) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
            if (sharedPrefs.getBoolean(Constant.kAutoPlay,false)) {
                return false;
            }
            boolean pageForward;
            if(event1.getX() - event2.getX() > SWIPE_MIN_DISTANCE &&
                    Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //From Right to Left
                 pageForward = true;

            }  else if (event2.getX() - event1.getX() > SWIPE_MIN_DISTANCE &&
                    Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //From Left to Right
                 pageForward = false;

            } else {
                return false;
            }
            if (viewpagerType == context.getResources().getInteger(R.integer.image_viewpager)) {
                pageChangeListener.onImagePageChangeIntercept(pageForward);
            } else {
                pageChangeListener.onTextPageChangeIntercept(pageForward);
            }
            return true;
        }
    }

    private void setMyScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 1000 /*1 secs*/);
        }
    }
}