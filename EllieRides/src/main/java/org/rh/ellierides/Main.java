package org.rh.ellierides;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.ViewGroup;

import org.rh.util.FragmentStatePagerAdapter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import static com.google.common.base.Preconditions.checkNotNull;


public class Main extends FragmentActivity
        implements MediaPlayer.OnCompletionListener,
        ResumeDialogFragment.ResumeDialogListener,
        MyViewPager.PageChangeListener,
        MyFragment.NavigationListener {


    private int[] firstLineOnPage;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private TextPagerAdapter textPagerAdapter;
    ImagePagerAdapter imagePagerAdapter;
    private MediaPlayer mediaPlayer = null;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private MyViewPager mTextViewPager;
    private MyViewPager mImageViewPager;
    private Integer pageNumberText, pageNumberImage;
    private int phraseNumber;
    private int[] imageToTextPages = new int[Constant.PAGES_IMAGE + 1];
    //    PagerTitleStrip titleStrip;
    private boolean correctImage;
    private boolean autoPlay, read2Me;

 //   Not used at the moment removed
//    private Runnable mLaunchTask = new Runnable() {
//        public void run() {
//            MediaPlayer.create(getApplicationContext(), R.raw.fxv_swipe).start();
//        }
//    };
    private final Runnable mStartReading = new Runnable() {
        public void run() {
            onClickReadToMe();
        }
    };
    private MyFragment page24Fragement = null;

    private static void saveArray(int[] value, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        SharedPreferences.Editor mEdit = sharedPrefs.edit();

        for (int i = 0; i < value.length; i++) {
            mEdit.putInt(key + i, value[i]);
        }
        mEdit.commit();
    }

    private static int[] loadArray(String key, int size) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        int[] value = new int[size];
        for (int i = 0; i < size; i++) {
            String myString = key + i;
            value[i] = sharedPrefs.getInt(myString, 0);
        }
        return value;
    }

    // Stack Overflow 14035090
//    private static Fragment getCurrentFragment(ViewPager pager, FragmentPagerAdapter adapter) {
//        try {
//            Method m = adapter.getClass().getSuperclass().getDeclaredMethod("makeFragmentName", int.class, long.class);
//            Field f = adapter.getClass().getSuperclass().getDeclaredField("mFragmentManager");
//            f.setAccessible(true);
//            FragmentManager fm = (FragmentManager) f.get(adapter);
//            m.setAccessible(true);
//            String tag = null;
//            tag = (String) m.invoke(null, pager.getId(), (long) pager.getCurrentItem());
//            return fm.findFragmentByTag(tag);
//        } catch (NoSuchMethodException e) {
//        } catch (IllegalArgumentException e) {
//        } catch (InvocationTargetException e) {
//        } catch (IllegalAccessException e) {
//        } catch (NoSuchFieldException e) {
//        }
//        return null;
//    }
    private static Fragment getCurrentTextFragment(ViewPager pager, TextPagerAdapter adapter) {
        int index = pager.getCurrentItem();
        return  adapter.getFragment(index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //  new Handler().postDelayed(mLaunchTask, 5000);

        setContentView(R.layout.main);

//        titleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
//        titleStrip.setVisibility(View.GONE);
        pageNumberText = 0;
        pageNumberImage = 0;
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        FragmentManager fragmentManager = getSupportFragmentManager();
        textPagerAdapter = new TextPagerAdapter(fragmentManager);

        // This is the Text
        mTextViewPager = (MyViewPager) findViewById(R.id.textPager);
        mTextViewPager.setAdapter(textPagerAdapter);
        mTextViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {  // for text page
                if (i > pageNumberText && read2Me) startReading();
                // if we page from  last to back to second last page change parent button back to forward
                if ((i == Constant.PAGES_TEXT - 1) && (pageNumberText == Constant.PAGES_TEXT) && (page24Fragement != null)) {
                    page24Fragement.redrawForwardButton();
                    // if we page from  last to back to second last page change parent button back to forward
                } else if ((i == Constant.PAGES_TEXT) && (page24Fragement != null)) {
                    page24Fragement.replaceForwardButton();
                }


                correctImage = false;
                pageNumberText = i;
                int pageNumberImageNew = textPage2imagePage(pageNumberText);
                if (pageNumberImageNew != pageNumberImage) {
                    correctImage = true;
                    mImageViewPager.setCurrentItem(pageNumberImageNew, true);
                } else {
                    if (mImageViewPager.getCurrentItem() != pageNumberImage) {
                        mImageViewPager.setCurrentItem(pageNumberImage, true);
                    }
                    pageFlipSound();
                }
                if (pageNumberText > 1) {
                    writeNumberToSharedPrefs(Constant.kPageNumber, pageNumberText);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        imagePagerAdapter = new ImagePagerAdapter(fragmentManager);
//
//        // Set up the ViewPager with the sections adapter.
        mImageViewPager = (MyViewPager) findViewById(R.id.imagePager);
        mImageViewPager.setAdapter(imagePagerAdapter);
        mImageViewPager.setOffscreenPageLimit(1);
//        // This is the image
        mImageViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i2) {
                Fragment fragment;
                if (i > mImageViewPager.getCurrentItem()) {
                    // get the current fragment i is the current page
                    fragment = (Fragment) imagePagerAdapter.instantiateItem(mImageViewPager, i);
                } else {
                    fragment = (Fragment) imagePagerAdapter.instantiateItem(mImageViewPager, mImageViewPager.getCurrentItem());
                }
                if (fragment instanceof MyFragment) {
                    ((MyFragment) fragment).stopAllAnimations();
                    ((MyFragment) fragment).stopAllTimers();
                }
            }

            @Override
            public void onPageSelected(int i) {  // image page

                if (i > pageNumberImage) { // page forward
                    pageNumberImage = i;
                    if (!correctImage) {
                        mTextViewPager.setCurrentItem(pageNumberText + 1, true);
                    } else {
                        pageFlipSound();
                    }
                } else if (i < pageNumberImage) {
                    pageNumberImage = i;
                    if (!correctImage) {
                        mTextViewPager.setCurrentItem(pageNumberText - 1, true);
                    } else {
                        pageFlipSound();
                    }
                }
                pageNumberImage = i;
//                imagePagerAdapter.getItem(i).isVisible();  do not remember why I had this
                correctImage = false;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        int loadPagesFlag = sharedPrefs.getInt(Constant.kFirstLineOnPage + "1", 0);
        firstLineOnPage = new int[Constant.PAGES_TEXT + 1];
        if (loadPagesFlag <= 0) {
            try {
                createFirstLineonPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            firstLineOnPage = loadArray(Constant.kFirstLineOnPage, Constant.PAGES_TEXT + 1);
        }

        loadPagesFlag = sharedPrefs.getInt(Constant.kImage2TextPage + "1", 0);
        if (loadPagesFlag <= 0) {
            imageToTextPages();
        } else {
            imageToTextPages = loadArray(Constant.kImage2TextPage, Constant.PAGES_IMAGE + 1);
        }
    }

    private void pageFlipSound() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            try {
                mediaPlayer.reset();
            } catch (Exception e) {
            }
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.fx_page_turn);
        mediaPlayer.start();
    }

    public int textPage2imagePage(int textPage) {
        if (textPage == 0) return 0;
        if (textPage > Constant.PAGES_TEXT) return 0;

        for (int imagePage = 1; imagePage <= imageToTextPages.length; imagePage++) {
            if (textPage >= imageToTextPages[imagePage - 1] && textPage <= imageToTextPages[imagePage]) {
                return imagePage;
            }
        }
        return 0;
    }

    public int imagePage2textPage(int imagePage) {
        if (imagePage == 0 || imagePage > imageToTextPages.length) {
            return 0;
        } else {
            return imageToTextPages[imagePage];
        }
    }

    void imageToTextPages() {
        int index = 0;
        for (int i = 0; index < Constant.PAGES_IMAGE + 1; i++) {
            if (ModelController.isLastTextPageForImage(i)) {
                imageToTextPages[index] = i;
                index++;
            }
        }
        saveArray(imageToTextPages, Constant.kImage2TextPage);
    }

    void createFirstLineonPage() throws Exception {
        AssetManager am = getAssets();
        Vector v = new Vector(Constant.PAGES_TEXT);
        for (int i = 0; i <= Constant.PAGES_TEXT; i++) {
            String fileName = "ellie" + i + ".txt";
            InputStream is = am.open(fileName);
            v.add(is);
        }
        Enumeration e = v.elements();
        SequenceInputStream sis = new SequenceInputStream(e);
        InputStreamReader isr = new InputStreamReader(sis);

        Scanner scanner = new Scanner(isr).useDelimiter("\\s*[~]\\s*");
        Integer lineNumber = 0;
        int count = 0;
        firstLineOnPage[0] = 0;
        while (scanner.hasNext()) {
            String line = scanner.next();
            // clever way of finding how many occurences of a character in text
            count += line.length() - line.replace("^", "").length();
            lineNumber++;
            firstLineOnPage[lineNumber] = count;

        }
        saveArray(firstLineOnPage, Constant.kFirstLineOnPage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    void onClickReadToMe() {
        phraseNumber = firstPhraseOnPage(pageNumberText);
        readPhrase(phraseNumber);
    }

    void readPhrase(int phrase) {
        TextFragement textFragement = (TextFragement) getCurrentTextFragment(mTextViewPager, textPagerAdapter);
        checkNotNull(textFragement, Constant.TAG + ":textFragmentNull");
        int uri = getResources().getIdentifier("s_p" + phrase, "raw", getPackageName());
        if (uri > 0) {
            textFragement.highlightText(phrase - firstPhraseOnPage(pageNumberText), pageNumberText);
            stopMediaPlayer();
            mediaPlayer = MediaPlayer.create(this, uri);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
        }
    }

    void stopMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.reset();
            } catch (Exception e) {

            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    Integer firstPhraseOnPage(Integer page) {
        if (page > Constant.PAGES_TEXT) {
            return 9999;
        }
        return firstLineOnPage[page];
    }

    @Override
    public void onCompletion(MediaPlayer mp) {



/*
if (scrollView == null) {
scrollView = (ScrollView) findViewById(R.id.scroll_view);
}
scrollView.smoothScrollBy(100,0);
*/
        phraseNumber++;
        if (phraseNumber < firstPhraseOnPage(pageNumberText + 1)) {
            readPhrase(phraseNumber);
        } else {
            TextFragement textFragement = (TextFragement)getCurrentTextFragment(mTextViewPager, textPagerAdapter);
            checkNotNull(textFragement, Constant.TAG + ":textFragmentNull");
            textFragement.removeHighlight();
            if (autoPlay) {
                mTextViewPager.setCurrentItem(pageNumberText + 1, true);
                readPhrase(phraseNumber);
            }
        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume reading



        resetReadPrefs();
        mTextViewPager.setCurrentItem(getPageNumber(), true);
        if (autoPlay || read2Me) {
            startReading();
        }
    }

    private void resetReadPrefs() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        autoPlay = sharedPrefs.getBoolean(Constant.kAutoPlay, false);
        read2Me = sharedPrefs.getBoolean(Constant.kReadToMe, false);
        if (autoPlay) {
            getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // Start from page 1
        resetReadPrefs();
        mImageViewPager.setCurrentItem(1, true);
        mTextViewPager.setCurrentItem(1, true);
        if (autoPlay || read2Me) {
            startReading();
        }
    }

    private void startReading() {
        new Handler().postDelayed(mStartReading, 1000);
    }

    @Override
    public void onNavigateForward() {
        mTextViewPager.setCurrentItem(pageNumberText + 1, true);
    }

    @Override
    public void onNavigateBack() {
        mTextViewPager.setCurrentItem(pageNumberText - 1, true);
    }

    @Override
    public void onRead2Me() {
        phraseNumber = firstPhraseOnPage(pageNumberText);
        readPhrase(phraseNumber);
    }

    @Override
    public void onTextPageChangeIntercept(Boolean forward) {

    }

    @Override
    public void onImagePageChangeIntercept(Boolean forward) {
        if (forward) {
            mTextViewPager.setCurrentItem(pageNumberText + 1, true);
        } else {
            mTextViewPager.setCurrentItem(pageNumberText - 1, true);
        }
    }

    public boolean isRead2Me() {
        return read2Me;
    }

    public void setRead2Me(boolean read2Me) {
        this.read2Me = read2Me;
    }

    public boolean isAutoPlay() {
        return autoPlay;
    }

    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    private void writeNumberToSharedPrefs(String key, int number) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor mEdit = sharedPrefs.edit();
        mEdit.putInt(key, number);
        mEdit.commit();
    }

    public int getPageNumber() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPrefs.getInt(Constant.kPageNumber, 0);
    }

    public TextPagerAdapter getTextPagerAdapter() {
        return textPagerAdapter;
    }

    public Integer getPageNumberText() {
        return pageNumberText;
    }

    public Integer getPageNumberImage() {
        return pageNumberImage;
    }

    @Override
    protected void onDestroy() {
        stopMediaPlayer();
        super.onDestroy();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    // using stackoverflow 8785221
    public class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ImagePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new Page0Fragement();
                    break;
                case 1:
                    fragment = new Page1Fragement();
                    break;
                case 2:
                    fragment = new Page2Fragement();
                    break;
                case 3:
                    fragment = new Page3Fragement();
                    break;
                case 4:
                    fragment = new Page4Fragement();
                    break;
                case 5:
                    fragment = new Page5Fragement();
                    break;
                case 6:
                    fragment = new Page6Fragement();
                    break;
                case 7:
                    fragment = new Page7Fragement();
                    break;
                case 8:
                    fragment = new Page8Fragement();
                    break;
                case 9:
                    fragment = new Page9Fragement();
                    break;
                case 10:
                    fragment = new Page10Fragement();
                    break;
                case 11:
                    fragment = new Page11Fragement();
                    break;
                case 12:
                    fragment = new Page12Fragement();
                    break;
                case 13:
                    fragment = new Page13Fragement();
                    break;
                case 14:
                    fragment = new Page14Fragement();
                    break;
                case 15:
                    fragment = new Page15Fragement();
                    break;
                case 16:
                    fragment = new Page16Fragement();
                    break;
                case 17:
                    fragment = new Page17Fragement();
                    break;
                case 18:
                    fragment = new Page18Fragement();
                    break;
                case 19:
                    fragment = new Page19Fragement();
                    break;
                case 20:
                    fragment = new Page20Fragement();
                    break;
                case 21:
                    fragment = new Page21Fragement();
                    break;
                case 22:
                    fragment = new Page22Fragement();
                    break;
                case 23:
                    fragment = new Page23Fragement();
                    break;
                case 24:
                    fragment = new Page24Fragement();
                    page24Fragement = (Page24Fragement) fragment;
                    break;
                default:
                    return null;
            }
            Bundle args = new Bundle();
            args.putInt(TextFragement.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return Constant.PAGES_IMAGE;
        }


    }

    public class TextPagerAdapter extends FragmentStatePagerAdapter {

        Map <Integer, Fragment> mPageReferenceMap = new HashMap<Integer,Fragment>(); 
        public Fragment getFragment(int key) {
            return mPageReferenceMap.get(key);
        }

        public TextPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new CoverFragement();
                    break;
                default:
                    fragment = new TextFragement();
            }
            mPageReferenceMap.put(position,fragment);

            Bundle args = new Bundle();
            args.putInt(TextFragement.ARG_SECTION_NUMBER, position);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return Constant.PAGES_TEXT + 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Integer pos;
            pos = position;
            return pos.toString();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            mPageReferenceMap.remove(position);
       }
    }

}
