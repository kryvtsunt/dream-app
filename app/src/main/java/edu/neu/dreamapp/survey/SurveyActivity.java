package edu.neu.dreamapp.survey;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import edu.neu.dreamapp.MainActivity;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseActivity;
import edu.neu.dreamapp.model.SurveyQuestion;
import edu.neu.dreamapp.widget.CustomProgressBar;

/**
 * @author agrawroh
 * @version v1.0
 */
public class SurveyActivity extends BaseActivity implements SurveyFragment.SelectionCallback {

    @BindView(R.id.ivBack)
    ImageView ivBack;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.horizontalProgress)
    CustomProgressBar horizontalProgress;

    @BindView(R.id.btnPrevious)
    Button btnPrevious;

    @BindView(R.id.btnNext)
    Button btnNext;

    private List<SurveyQuestion> surveyQuestions;
    private int progress;
    private SurveyFragment surveyFragment;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_survey;
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
        ivBack.setVisibility(View.VISIBLE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initEndDialog();
            }
        });

        /* Get Info */
        Bundle b = getIntent().getExtras();
        int step = b.getInt("S");
        if (1 == step) {
            tvTitle.setText("Pre-Evaluation Survey");
        } else {
            tvTitle.setText("Post-Evaluation Survey");
        }

        /* Grab Fragment */
        surveyFragment = (SurveyFragment) getSupportFragmentManager().findFragmentById(R.id.surveyFragment);
        surveyFragment.setSelectionCallback(this);

        btnNext.setEnabled(true);

        /* Next Question Button */
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (progress == surveyQuestions.size() - 1) {
                    pushAnswersOnFirebase();
                } else {
                    progress++;
                    surveyFragment.nextQuestion(progress, surveyQuestions.get(progress));
                    horizontalProgress.setProgress((int) (((double) progress / (double) surveyQuestions.size()) * 100));

                /* Enable previous button since there is always a question to go back to */
                    btnPrevious.setEnabled(true);
                }
                btnNext.setEnabled(isChecked(progress));
            }
        });

        /* Previous Question Button */
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress--;
                surveyFragment.previousQuestion(progress, surveyQuestions.get(progress));
                horizontalProgress.setProgress((int) (((double) progress / (double) surveyQuestions.size()) * 100));

                /* Disable previous button if progress is pushed back to 0 */
                if (progress == 0) {
                    btnPrevious.setEnabled(false);
                }
                btnNext.setEnabled(isChecked(progress));
            }
        });

        initQuestion();
    }

    /**
     * Initializing Survey Questions (Pre-entered)
     */
    private void initQuestion() {
        surveyQuestions = new ArrayList<>();
        /* Attendance */
        surveyQuestions.add(new SurveyQuestion("Attendance", "",
                Arrays.asList("Adalquiris", "Jhon Olivo", "Elvis Manuel", "Elianny", "Adelyn Sanchez", "Samuel Arturo")));

        /* Questions */
        surveyQuestions.add(new SurveyQuestion("1. Tener relaciones sexuales con una persona mayor que t˙ (5 aÒos o·s) te pone en peligro de contagiarse del VIH.\n\n" +
                "1. Have sex with a person older than you (5 years or\n" +
                "more) puts you in danger of getting HIV.", "",
                Arrays.asList("Excellent", "Good", "Fair", "Poor")));

        surveyQuestions.add(new SurveyQuestion("2. Los condones funcionan para prevenir el VIH/SIDA.\n\n" +
                "2. Condoms work to prevent HIV / AIDS.", "",
                Arrays.asList("I am not physically active", "less than 75 minutes", "75 - 149 minutes", "150 - 300 minutes", "more than 300 minutes")));

        surveyQuestions.add(new SurveyQuestion("3. Una persona que se ve sana puede tener el VIH.\n\n" +
                "3. A person who looks healthy can have HIV.", "",
                Arrays.asList("I do not stretch often, if ever", "1 day/week", "2 days/week", "3 or more days/week")));

        surveyQuestions.add(new SurveyQuestion("4. Puedes saber si tienes VIH sin hacer una prueba de VIH.\n\n" +
                "4. You can know if you have HIV without an HIV test.", "",
                Arrays.asList("I do not typically weight-lift or resistance train", "1 day/week", "2 days/week", "3 or more days/week")));

        surveyQuestions.add(new SurveyQuestion("5. Tener varias parejas al mismo tiempo aumenta el peligro de contagiarse del VIH y otras infecciones.\n\n" +
                "5. Having several partners at the same time increases the danger of get HIV and other infections.", "",
                Arrays.asList("I do not typically weight-lift or resistance train", "1 day/week", "2 days/week", "3 or more days/week")));

        /* Set current Progress to 0 */
        progress = 0;

        /* Call nextQuestion on SurveyFragment to load out the first question */
        surveyFragment.nextQuestion(progress, surveyQuestions.get(progress));

        /* Set current Progressbar to show 0 */
        horizontalProgress.setProgress(0);

        /* Disable previous button since it is the first question */
        btnPrevious.setEnabled(false);
    }

    /**
     * Overwrites the interface and creates callback from SurveyFragment
     *
     * @param index     Index Location
     * @param selection Selection
     */
    @Override
    public void itemSelected(int index, String selection) {
        surveyQuestions.get(index).setSelected(selection);
        if (0 == index) {
            List<String> options = new ArrayList<>();
            int valPresent = 0;
            for (final String name : surveyQuestions.get(0).getOption()) {
                if (Arrays.asList(selection.substring(1, selection.length() - 1).split(", ")).contains(String.valueOf(valPresent))) {
                    options.add(name + " : True? Cierto?");
                }
                ++valPresent;
            }
            for (int i = 1; i < surveyQuestions.size(); i++) {
                surveyQuestions.get(i).setOption(options);
            }
        }
        btnNext.setEnabled(true);
    }

    /**
     * Push Answers
     */
    public void pushAnswersOnFirebase() {
        /* Get Info */
        String key = "";
        Bundle b = getIntent().getExtras();
        int step = b.getInt("S");
        if (1 == step) {
            key = "SR_RESP_SET_PRE";
        } else {
            key = "SR_RESP_SET_POST";
        }

        /* Get Shared Preferences */
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("DREAM_APP_CXT", Context.MODE_PRIVATE);
        SharedPreferences.Editor scoreEditor = prefs.edit();

        /* Get Responses */
        Set<String> set = prefs.getStringSet(key, new HashSet<String>());
        StringBuilder builder = new StringBuilder();
        builder.append(new Date()).append(";");
        builder.append(surveyQuestions.get(0).getOption()).append(";");
        builder.append(surveyQuestions.get(0).getSelected()).append(";");
        for (int i = 1; i < surveyQuestions.size(); i++) {
            builder.append(surveyQuestions.get(i).getSelected()).append(";");
        }
        set.add(builder.toString());
        scoreEditor.putStringSet(key, set);
        scoreEditor.commit();

        /* Finish */
        if (1 == step) {
            Intent i = new Intent(getApplicationContext(), SurveyStartActivity.class);
            i.putExtra("S", 1);
            startActivity(i);
        }
        finish();
    }

    /**
     * return if any of the options are selected
     *
     * @return Return Value
     */
    public boolean isChecked(int position) {
        return null != surveyQuestions.get(position).getSelected();
    }

    /**
     * Survey entry confirmation dialog initialization
     */
    @SuppressWarnings("all")
    private void initEndDialog() {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_popup, null);
        final AlertDialog dialog = new AlertDialog.Builder(mContext).setView(layout).show();
        TextView tvContent = (TextView) layout.findViewById(R.id.tvContent);
        tvContent.setText(getResources().getString(R.string.end_survey));
        TextView tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView tv_confirm = (TextView) layout.findViewById(R.id.tv_confirm);

        /* if confirm, launch MainActivity */
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, MainActivity.class);
                ((Activity) mContext).startActivity(intent);

                finish();
                overridePendingTransition(R.anim.fade_in_500, R.anim.fade_out_500);
            }
        });
    }
}
