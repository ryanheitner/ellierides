package org.rh.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import org.rh.ellierides.Constant;
import org.rh.ellierides.R;

public class FontFitTextView extends TextView {


    public FontFitTextView(Context context) {
        super(context);
        initialise();
    }

    public FontFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        mTestPaint = new Paint();
        // max size defaults to the initially specified text size unless it is
        // too small
    }

    /*
     * Re size the font so the specified text fits in the text box assuming the
     * text box is the specified width.
     */
    private void refitText(String text, int textWidth, int textHeight) {
        if (textWidth <= 0 || textHeight <= 0)
            return;
        final int targetWidth = textWidth - getCompoundPaddingLeft()
                - getCompoundPaddingRight();
        final int targetHeight = textHeight - getCompoundPaddingBottom()
                - getCompoundPaddingTop();
        // this.setHeight(targetHeight/2);
        float hi = 100;
        float lo = 2;
        final float threshold = 0.5f; // How close we have to be

        mTestPaint.set(this.getPaint());

        while (hi - lo > threshold) {
            float size = (hi + lo) / 2;
            mTestPaint.setTextSize(size);
            Rect rect = new Rect();
            mTestPaint.getTextBounds(text, 0, text.length(), rect);
//			Log.e("Size: " + size);
//			Log.e("Rect width:" + rect.width() + ". Target width:"
//					+ targetWidth);
//			Log.e("Rect height:" + rect.height() + ". Target height:"
//					+ targetHeight);
//			if ((Math.abs(rect.width()) == targetWidth
//					&& Math.abs(rect.height()) <= targetHeight)
//					|| (Math.abs(rect.width()) <= targetWidth && Math.abs(rect
//							.height()) == targetHeight)) {
//				break;
//			}
            if (Math.abs(rect.width()) >= targetWidth
                    || Math.abs(rect.height()) >= targetHeight) {
                hi = size; // too big
            } else {
                lo = size; // too small
            }
        }
        // Use lo so that we undershoot rather than overshoot

        this.setTextSize(TypedValue.COMPLEX_UNIT_PX, lo);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        // int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        refitText(this.getText().toString(), getWidth(), getHeight());
        this.setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onTextChanged(final CharSequence text, final int start,
                                 final int before, final int after) {
        refitText(text.toString(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            refitText(this.getText().toString(), w, h);
        }
    }

    // Attributes
    private Paint mTestPaint;
}