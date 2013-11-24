package org.rh.ellierides;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by ryanheitner on 9/6/13.
 */
@SuppressWarnings("ConstantConditions")
class ModelController {

    private final Context context;

    public ModelController(Context context) {
        this.context = context;
    }

    public static String getTextForPage(Integer pageNumber) {
        InputStream inputStream = null;
        AssetManager am = MyApplication.getAppContext().getAssets();
        String fileName = "ellie" + pageNumber.toString() + ".txt";

        try {
            inputStream = am.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String stringFromStream = null;
        try {
            stringFromStream = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
        } catch (IOException e) {
        }
        return stringFromStream;

    }

    public static ArrayList<String> getPhrasesForPage(Integer pageNumber) {
        String text = ModelController.getTextForPage(pageNumber);
        String charsToRemove = "'" + Constant.kPageStartMarker + Constant.kPageEndMarker;
        text = CharMatcher.anyOf(charsToRemove).removeFrom(text);

        final Iterable<String> result = Splitter.on(Constant.kPhraseMarker.toString())
                .trimResults()
                .omitEmptyStrings()
                .split(text);
        return Lists.newArrayList(result);
    }

    // the text page is the last one for the image
    public static boolean isLastTextPageForImage(Integer pageNumber) {
        InputStream inputStream = null;
        AssetManager am = MyApplication.getAppContext().getAssets();
        String fileName = "ellie" + pageNumber.toString() + ".txt";

        try {
            inputStream = am.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String stringFromStream = null;
        try {
            stringFromStream = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
        } catch (IOException e) {
        }
        assert stringFromStream != null;
        return stringFromStream.contains(Constant.kPageEndMarker);
    }

    public static boolean isFirstTextPageForImage(Integer pageNumber) {
        if (pageNumber == 0) return true;
        Integer pageBefore = pageNumber - 1;
        InputStream inputStream = null;
        AssetManager am = MyApplication.getAppContext().getAssets();
        String fileName = "ellie" + pageBefore.toString() + ".txt";
        try {
            inputStream = am.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String stringFromStream = null;
        try {
            stringFromStream = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
        } catch (IOException e) {
        }
        assert stringFromStream != null;
        return stringFromStream.contains(Constant.kPageEndMarker);
//        InputStream inputStream = null;
//        AssetManager am = MyApplication.getAppContext().getAssets();
//        String fileName = "ellie" + pageNumber.toString() + ".txt";
//        try {
//            inputStream = am.open(fileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String stringFromStream = null;
//        try {
//            stringFromStream = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
//        } catch (IOException e) {
//        }
//        return stringFromStream.contains(Constant.kPageStartMarker);
    }

    public String getTextForPagePhrase(Integer pageNumber, Integer phraseNumber) {
        InputStream inputStream = null;
        AssetManager am = MyApplication.getAppContext().getAssets();
        String fileName = "ellie" + pageNumber.toString() + ".txt";

        try {
            inputStream = am.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String stringFromStream = null;
        try {
            stringFromStream = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final String[] strings = Iterables.toArray(Splitter.on("~").trimResults().omitEmptyStrings().split(stringFromStream), String.class);
        phraseNumber = Math.min(phraseNumber, strings.length - 1);
        return strings[phraseNumber];
    }
}
