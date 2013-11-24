package org.rh.ellierides;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.common.base.CharMatcher;

import org.rh.util.AutoSizeFontTextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ryanheitner on 9/7/13.
 */
public class TextFragement extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private MediaPlayer mp = null;
    private ArrayList<String> myPhrases = new ArrayList<String>();
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private SpannableString spannableString;
    private AutoSizeFontTextView tv;

    public TextFragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView;
        rootView = inflater.inflate(R.layout.fragment_text, container, false);
        assert rootView != null;
        tv = (AutoSizeFontTextView) rootView.findViewById(R.id.story_text);
        if (container.getHeight() == 0 && container.getWidth() == 0) {
            DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
            int height = metrics.heightPixels;
            int width = (metrics.widthPixels * 24 / 24 + 80) - 16;
        } else {
            int height = container.getHeight();
            int width = container.getWidth() - 16;
        }
        splitWords();

        return rootView;
    }

    private void splitWords() {

        Integer pageNumber = (getArguments().getInt(ARG_SECTION_NUMBER));
        ModelController modelController = new ModelController(getActivity());


        String regex = "\\w+";
        Pattern pattern = Pattern.compile(regex);
        String text = modelController.getTextForPage(pageNumber);
        String charsToRemove = "'" + Constant.kPhraseMarker + Constant.kPageStartMarker + Constant.kPageEndMarker;

        text = CharMatcher.anyOf(charsToRemove).removeFrom(text);

/*
text = text.replaceAll("'","");
text = text.replaceAll("\\^","");
text = text.replaceAll("\\|","");
*/
        spannableString = new SpannableString(text);
        Matcher matcher;
        matcher = pattern.matcher(spannableString);
        while (matcher.find()) {
            final int begin = matcher.start();
            final int end = matcher.end();
            TouchableSpan touchableSpan = new TouchableSpan() {
                @Override
                public void updateDrawState(TextPaint ds) {
                    ds.setUnderlineText(false);
                    ds.setAntiAlias(true);
                }

                @Override
                public boolean onTouch(View widget, MotionEvent m) {
                    int action = m.getAction();

                    if (action == MotionEvent.ACTION_DOWN) {

                        String word = spannableString.subSequence(begin, end)
                                .toString();
                        spannableString.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), 0,
                                spannableString.length(), 0);
                        spannableString.setSpan(new BackgroundColorSpan(Color.CYAN), begin,
                                end, 0);
//                    tv.setMovementMethod(new LinkTouchMovementMethod());
                        tv.setText(spannableString, TextView.BufferType.SPANNABLE);


                        if (mp != null) {
                            try {
                                mp.reset();
                            } catch (Exception e) {
                            }
                            mp.release();
                            mp = null;
                        }
                        int res = 0;

                        String sound_file = "s_" + word.toLowerCase();

                        try {
                            res = getActivity().getResources()
                                    .getIdentifier(sound_file, "raw",
                                            getActivity().getPackageName());
                        } catch (Exception e) {
                        }
                        if (res > 0) {
                            try {
                                mp = MediaPlayer.create(getActivity(), res);
                                mp.start();
                            } catch (IllegalStateException e) {

                            } catch (Exception e) {

                            }
                        }
                    }
                    return true;

                }
            };
            spannableString.setSpan(touchableSpan, begin, end, 0);
        }


        tv.setMovementMethod(new LinkTouchMovementMethod());    // This is my method
        tv.setText(spannableString, TextView.BufferType.SPANNABLE);
    }

    @SuppressWarnings("ConstantConditions")
    public void highlightText(int phrase, int page) {
        if (myPhrases.size() == 0)
            myPhrases = ModelController.getPhrasesForPage(page);
        if (phrase >= myPhrases.size()) return;


        String text = tv.getText().toString();

        String matchString = myPhrases.get(phrase);
        int start = text.indexOf(matchString);
        int end = start + matchString.length();

        SpannableString sString = new SpannableString(text);
        sString.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), 0,
                sString.length(), 0);
        sString.setSpan(new BackgroundColorSpan(Color.CYAN), start,
                end, 0);
        tv.setText(sString, TextView.BufferType.SPANNABLE);
    }
    public void removeHighlight(){
        SpannableString sString;
        try {
            sString = new SpannableString(tv.getText());
        } catch (Exception e) {
            return;
        }
        sString.setSpan(new BackgroundColorSpan(Color.TRANSPARENT), 0,
                sString.length(), 0);
        tv.setText(sString, TextView.BufferType.SPANNABLE);
        splitWords();

    }
}

