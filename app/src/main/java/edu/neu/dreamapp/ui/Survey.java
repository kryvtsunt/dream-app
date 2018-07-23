package edu.neu.dreamapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseFragment;
import edu.neu.dreamapp.survey.SurveyStartActivity;

/**
 * @author agrawroh
 * @version v1.0
 */
public class Survey extends BaseFragment {
    private static final String CLASS_TAG = "Survey";

    @BindView(R.id.btnTakeSurvey)
    Button btnTakeSurvey;

    @Override
    public int getContentViewId() {
        return R.layout.survey_main;
    }

    @Override
    protected String getTAG() {
        return CLASS_TAG;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        /* Bind Take Survey */
        btnTakeSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getApplicationContext(), SurveyStartActivity.class));
            }
        });
    }
}
