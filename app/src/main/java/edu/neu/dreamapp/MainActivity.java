package edu.neu.dreamapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import edu.neu.dreamapp.base.BaseActivity;
import edu.neu.dreamapp.ui.Dashboard;
import edu.neu.dreamapp.ui.Profile;
import edu.neu.dreamapp.ui.Reports;
import edu.neu.dreamapp.ui.Survey;

public class MainActivity extends BaseActivity {
    @BindView(R.id.frameContent)
    FrameLayout frameContent;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.bottomNavigationBar)
    BottomNavigationBar bottomNavigationBar;

    private List<Fragment> fragmentContent;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment currentContent = new Fragment();
    private long mExitTime = 0;

    @Override
    protected String getTAG() {
        return "MainActivity";
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("DREAM_APP_CXT", Context.MODE_PRIVATE);
        SharedPreferences.Editor scoreEditor = prefs.edit();
        scoreEditor.putStringSet("SR_RESP_SET", null);
        scoreEditor.commit();
        */
    }

    @Override
    protected void initResAndListener() {
        /* Initialize Fragment */
        initFragment();

        /* Bottom Navigation Bar */
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.dash_icon_board, getResources().getString(R.string.dashboard)))
                .addItem(new BottomNavigationItem(R.drawable.dash_icon_news, getResources().getString(R.string.survey)))
                .addItem(new BottomNavigationItem(R.drawable.dash_icon_chat, getResources().getString(R.string.reports)))
                .addItem(new BottomNavigationItem(R.drawable.dash_icon_user, getResources().getString(R.string.profile)))
                .initialise();

        /* Add Tab Selected Listener */
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0: // Dashboard
                        switchContent(currentContent, fragmentContent.get(position));
                        tvTitle.setText(getString(R.string.dashboard));
                        break;
                    case 1: // Survey
                        switchContent(currentContent, fragmentContent.get(position));
                        tvTitle.setText(getResources().getString(R.string.survey));
                        break;
                    case 2: // Reports
                        switchContent(currentContent, fragmentContent.get(position));
                        tvTitle.setText(getString(R.string.reports));
                        break;
                    case 3: // Profile
                        switchContent(currentContent, fragmentContent.get(position));
                        tvTitle.setText(getString(R.string.profile));
                        break;
                }
            }

            @Override
            public void onTabUnselected(int position) {
                /* Do Nothing */
            }

            @Override
            public void onTabReselected(int position) {
                /* Do Nothing */
            }
        });
    }

    /**
     * Initialize Fragments
     */
    private void initFragment() {
        /* Initialize New Fragment Instances */
        Dashboard dashboardFragment = new Dashboard();
        Survey surveyFragment = new Survey();
        Reports reportsFragment = new Reports();
        Profile profileFragment = new Profile();

        /* Create Fragment Content */
        fragmentContent = new ArrayList<>();
        fragmentContent.add(dashboardFragment);
        fragmentContent.add(surveyFragment);
        fragmentContent.add(reportsFragment);
        fragmentContent.add(profileFragment);

        /* Create Fragment Manager */
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameContent, fragmentContent.get(0));
        currentContent = fragmentContent.get(0);
        tvTitle.setText(getString(R.string.dashboard));
        fragmentTransaction.commit();
    }

    /**
     * Switch Fragment
     *
     * @param from From
     * @param to   To
     */
    public void switchContent(Fragment from, Fragment to) {
        if (currentContent != to) {
            currentContent = to;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(from).add(R.id.frameContent, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
        currentContent = to;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fragmentManager = null;
        fragmentTransaction = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(mContext, getResources().getString(R.string.exit_toast), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
