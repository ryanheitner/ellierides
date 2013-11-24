package org.rh.ellierides;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.MotionEvent;
import android.widget.TextView;

class LinkTouchMovementMethod extends LinkMovementMethod {

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            assert layout != null;
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            TouchableSpan[] link = buffer.getSpans(off, off, TouchableSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onTouch(widget, event); //////// CHANGED HERE
                } else {
                    link[0].onTouch(widget, event); //////// ADDED THIS
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                }

                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }

}