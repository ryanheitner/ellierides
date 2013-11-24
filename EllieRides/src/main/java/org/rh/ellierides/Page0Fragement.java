package org.rh.ellierides;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by ryanheitner on 9/7/13.
 */
public class Page0Fragement extends Fragment {

    public Page0Fragement() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.page0, container, false);

    }


}

