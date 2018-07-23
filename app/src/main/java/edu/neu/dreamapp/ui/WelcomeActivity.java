package edu.neu.dreamapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import butterknife.BindView;
import edu.neu.dreamapp.MainActivity;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseActivity;

/**
 * @author Sh1nJi
 * @version v1.0
 */
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.tvCountDown)
    TextView tvCountDown;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected String getTAG() {
        return this.toString();
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, WelcomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CountDownTimer timer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int countDown = (int) millisUntilFinished / 1000;
                if (millisUntilFinished < 1000) {
                    countDown = 1;
                }
                tvCountDown.setText(String.valueOf(countDown));
            }

            @Override
            public void onFinish() {
                tvCountDown.setText("0");
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                overridePendingTransition(R.anim.fade_in_500, R.anim.fade_out_500);
            }
        };

        timer.start();
    }

    @Override
    protected void initResAndListener() {
        /* Do Nothing */
    }
}
