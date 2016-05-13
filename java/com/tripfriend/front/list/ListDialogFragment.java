package com.tripfriend.front.list;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.HashMap;

public class ListDialogFragment extends DialogFragment {

    private String tag;
    private OnCompleteTListener completeListener;
    EditText et;

    public interface OnCompleteTListener {
        void onEmailSet(String email);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        switch (tag) {
            case "emailSet":
                String title = (String) bundle.get("titleDialog");
                String heading = (String) bundle.get("headingDialog");
                String hint = (String) bundle.get("hintEditTextDialog");
                return createEditTextDialog(title, heading, hint, 0);
            default:
                return super.onCreateDialog(savedInstanceState);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            completeListener = (OnCompleteTListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteTListener.");
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        this.tag = tag;
        super.show(manager, tag);
    }

    public Dialog createEditTextDialog(String title, String heading, String hint, final int type) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(title);
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        TextView twHeading = new TextView(getActivity());
        twHeading.setText(heading);

        et = new EditText(getActivity());
        et.setHint(hint);

        RelativeLayout rl = new RelativeLayout(getActivity());
        rl.addView(twHeading);
        rl.addView(et);

        dialog.setView(rl);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = et.getText().toString();
                if (!text.equals("")) {
                    if(completeListener != null) {
                        completeListener.onEmailSet(text);
                    }
                    dialog.dismiss();
                } else {
                    et.setError("Please enter required value.");
                }
            }
        });
        return dialog.create();
    }
}
