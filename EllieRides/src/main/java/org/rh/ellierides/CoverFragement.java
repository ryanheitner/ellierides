package org.rh.ellierides;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class CoverFragement extends Fragment implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {


    private TextView page;
    private Integer pageNumber;

    public CoverFragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cover, container, false);
        assert v != null;
        ImageButton autoPlayButton = (ImageButton) v.findViewById(R.id.autoPlayButton);
        ImageButton parentButton = (ImageButton) v.findViewById(R.id.parentButton);
        ImageButton readByMyselfButton = (ImageButton) v.findViewById(R.id.readByMyselfButton);
        ImageButton readToMeButton = (ImageButton) v.findViewById(R.id.readToMeButton);
        page = (TextView) v.findViewById(R.id.page);
        SeekBar seekBar = (SeekBar)v.findViewById(R.id.seekBar);
        Main myActivity = (Main)getActivity();
        pageNumber =  myActivity.textPage2imagePage(myActivity.getPageNumber());
        seekBar.setProgress(pageNumber);
        page.setText(pageNumber.toString());
        seekBar.setMax(myActivity.imagePagerAdapter.getCount() - 1);
        seekBar.setOnSeekBarChangeListener(this);
        autoPlayButton.setOnClickListener(this);
        parentButton.setOnClickListener(this);
        readByMyselfButton.setOnClickListener(this);
        readToMeButton.setOnClickListener(this);
        return v;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        pageNumber = progress;
       page.setText(pageNumber.toString());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        SharedPreferences.Editor mEdit = sharedPrefs.edit();
        Main myActivity = (Main)getActivity();
        mEdit.putInt(Constant.kPageNumber,myActivity.imagePage2textPage(pageNumber));
        mEdit.commit();
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor mEdit = sharedPrefs.edit();
        switch (v.getId()) {
            case R.id.autoPlayButton:
                mEdit.putBoolean(Constant.kAutoPlay, true);
                mEdit.putBoolean(Constant.kReadByMyself, false);
                mEdit.putBoolean(Constant.kReadToMe, false);
                break;
            case R.id.parentButton:
                Intent intent = new Intent(getActivity(),Parent.class);
                startActivity(intent);
                break;
            case R.id.readToMeButton:
                mEdit.putBoolean(Constant.kReadToMe, true);
                mEdit.putBoolean(Constant.kAutoPlay, false);
                mEdit.putBoolean(Constant.kReadByMyself, false);
                break;
            case R.id.readByMyselfButton:
                mEdit.putBoolean(Constant.kReadByMyself, true);
                mEdit.putBoolean(Constant.kAutoPlay, false);
                mEdit.putBoolean(Constant.kReadToMe, false);
                break;
            default:
                break;
        }
        mEdit.apply();
        switch (v.getId()) {
            case R.id.autoPlayButton:
            case R.id.readToMeButton:
            case R.id.readByMyselfButton:
                int pageNumber = ((Main) getActivity()).getPageNumber();
                if (pageNumber > 1) {
                    resumeDialog();
                } else {
                    ((Main) getActivity()).onDialogNegativeClick(null);
                }
            default:
                break;
        }


    }

    private void resumeDialog() {
        ResumeDialogFragment resumeDialogFragment = new ResumeDialogFragment();
        resumeDialogFragment.show(getFragmentManager(), "resume");
    }


}

