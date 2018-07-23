package edu.neu.dreamapp.survey;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseActivity;

/**
 * @author agrawroh
 * @version v1.0
 */
public class SurveyStartActivity extends BaseActivity {
    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.im1)
    ImageView im1;

    @BindView(R.id.im2)
    ImageView im2;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_survey_start;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    /**
     * Initialize Views
     */
    @Override
    protected void initResAndListener() {
        ivBack.setVisibility(View.INVISIBLE);
        tvTitle.setText("Start Survey");

        /* Vouch Step */
        Bundle b = getIntent().getExtras();
        if (null != b) {
            int state = b.getInt("S");
            if (1 == state) {
                im1.setImageResource(R.drawable.ico1b);
            }
        }

        /* Listeners */
        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SurveyActivity.class);
                i.putExtra("S", 1);
                startActivity(i);
                finish();
            }
        });

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SurveyActivity.class);
                i.putExtra("S", 2);
                startActivity(i);
                finish();
            }
        });
    }
}
