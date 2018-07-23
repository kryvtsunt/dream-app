package edu.neu.dreamapp.ui;

import android.os.Bundle;

import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseFragment;

/**
 * @author agrawroh
 * @version v1.0
 */
public class Profile extends BaseFragment {
    private static final String CLASS_TAG = "Profile";

    @Override
    public int getContentViewId() {
        return R.layout.profile_main;
    }

    @Override
    protected String getTAG() {
        return CLASS_TAG;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        /* Do Nothing */
    }
}
