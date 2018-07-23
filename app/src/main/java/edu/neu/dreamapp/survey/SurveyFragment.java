package edu.neu.dreamapp.survey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Arrays;

import butterknife.BindView;
import edu.neu.dreamapp.R;
import edu.neu.dreamapp.base.BaseFragment;
import edu.neu.dreamapp.model.SurveyQuestion;
import edu.neu.dreamapp.widget.CheckBoxGroup;

/**
 * @author agrawroh
 * @version v1.0
 */
public class SurveyFragment extends BaseFragment {

    @BindView(R.id.tvQuestion)
    TextView tvQuestion;

    @BindView(R.id.tvSubQuestion)
    TextView tvSubQuestion;

    @BindView(R.id.cbGroup)
    CheckBoxGroup cbGroup;

    private SurveyQuestion surveyQuestion;
    private int previousCount = 0;
    private int buttonCount = 0;

    @Override
    protected String getTAG() {
        return this.toString();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_survey;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        /* Do Nothing */
    }

    /**
     * Called when loading previous question
     *
     * @param index    Index Location
     * @param question Question Text
     */
    public void previousQuestion(final int index, SurveyQuestion question) {
        this.surveyQuestion = question;

        /* Remove all buttons in the radioGroup also interface attached */
        removeButtons();

        /* Collect the previous button count for further button identification */
        previousCount = buttonCount;

        /* Set Question */
        tvQuestion.setText(surveyQuestion.getQuestion());

        /* Set SubQuestion if any */
        if (!surveyQuestion.getSubQuestion().equals("")) {
            tvSubQuestion.setVisibility(View.VISIBLE);
            tvSubQuestion.setText(surveyQuestion.getSubQuestion());
        }

        /* Add Options */
        for (final String option : surveyQuestion.getOption()) {
            addOptionButton(index, option, surveyQuestion.getSelected());
        }
        cbGroup.onAttachedToWindow();
    }

    /**
     * Called when loading the next Question
     *
     * @param index    Index Location
     * @param question Question Text
     */
    public void nextQuestion(final int index, SurveyQuestion question) {
        this.surveyQuestion = question;

        /* Remove all buttons in the radioGroup also interface attached */
        removeButtons();

        /* Collect the previous button count for further button identification */
        previousCount = buttonCount;

        /* Set Question */
        tvQuestion.setText(surveyQuestion.getQuestion());

        /* Set SubQuestion if any */
        if (!surveyQuestion.getSubQuestion().equals("")) {
            tvSubQuestion.setVisibility(View.VISIBLE);
            tvSubQuestion.setText(surveyQuestion.getSubQuestion());
        }

        /* Add Options */
        for (final String option : surveyQuestion.getOption()) {
            addOptionButton(index, option, surveyQuestion.getSelected());
        }
        cbGroup.onAttachedToWindow();
    }

    /**
     * Helper function for adding radioButton into radioGroup
     *
     * @param option Selected Option
     */
    public void addOptionButton(final int index, String option, String selected) {
        CheckBox cb = new CheckBox(context);
        cb.setTag(buttonCount);
        if (null != selected) {
            cb.setChecked(Arrays.asList(selected.substring(1, selected.length() - 1).split(", ")).contains(String.valueOf(buttonCount)));
        }
        cb.setText(option);
        cb.setTextSize(16);
        cb.setTextColor(getResources().getColor(R.color.black));
        cb.setPadding(10, 10, 10, 10);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /* Select Item */
                selectionCallback.itemSelected(index, cbGroup.getCheckedIds());
            }
        });
        cbGroup.put(cb);

        /* Increment Button Count */
        buttonCount++;
    }

    /**
     * Helper function for removing out-of-date radioButtons
     */
    public void removeButtons() {
        /* First remove all child views in radioGroup */
        cbGroup.refresh();
        buttonCount = 0;
    }

    /**
     * Interface for communicating with SurveyActivity
     * for updating SurveyQuestion Selections
     */
    public interface SelectionCallback {
        void itemSelected(int index, String selection);
    }

    public void setSelectionCallback(SelectionCallback listener) {
        this.selectionCallback = listener;
    }

    private SelectionCallback selectionCallback;
}
