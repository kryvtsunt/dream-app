package edu.neu.dreamapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author agrawroh
 * @version v1.0
 */
public class CheckBoxGroup extends GridLayout {
    private List<CheckBox> checkboxes = new ArrayList<>();

    public CheckBoxGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void put(CheckBox checkBox) {
        checkboxes.add(checkBox);
        invalidate();
        requestLayout();
    }

    public void refresh() {
        checkboxes = new ArrayList<>();
        removeAllViews();
        invalidate();
        requestLayout();
    }

    public void remove(Integer id) {
        // TODO: Remove items from ArrayList
    }

    public List<?> getCheckboxesChecked() {

        List<CheckBox> checkeds = new ArrayList<>();
        for (CheckBox c : checkboxes) {
            if (c.isChecked())
                checkeds.add(c);
        }

        return checkeds;
    }

    public String getCheckedIds() {
        List<String> checkIds = new ArrayList<>();
        for (final CheckBox c : checkboxes) {
            if (c.isChecked()) {
                checkIds.add(c.getTag().toString());
            }
        }
        return checkIds.toString();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        removeAllViews();
        for (final CheckBox c : checkboxes) {
            addView(c);
        }

        invalidate();
        requestLayout();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }
}
