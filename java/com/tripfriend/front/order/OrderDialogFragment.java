package com.tripfriend.front.order;

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
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OrderDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String tag;
    private OnCompleteODFListener completeListener;
    final Calendar c = Calendar.getInstance();

    public interface OnCompleteODFListener {
        void onLocationPicked(int location);
        void onLanguagePicked(int language);
        void onTimespanPicked(int timespan);
        void onDatePicked(int year, int monthOfYear, int dayOfMonth);
        void onTimePicked(int hour, int minute);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        List<String> list;
        CharSequence[] cs = new CharSequence[0];
        int position;

        switch (tag) {
            case "datePicker":
                return createDatePickerDialog();
            case "timePicker":
                return createTimePickerDialog();
            case "locationPicker":
                list = (List<String>) bundle.get("locations");
                position = (int) bundle.get("locations_selected");
                if( list != null ) {
                    cs = list.toArray(new CharSequence[list.size()]);
                }

                return createPickerDialog("Select Location", cs, position, 0);
            case "languagePicker":
                list = (List<String>) bundle.get("languages");
                position = (int) bundle.get("languages_selected");
                if( list != null ) {
                    cs = list.toArray(new CharSequence[list.size()]);
                }

                return createPickerDialog("Select Language", cs, position, 1);
            case "timespanPicker":
                list = (List<String>) bundle.get("timespans");
                position = (int) bundle.get("timespans_selected");
                if( list != null ) {
                    cs = list.toArray(new CharSequence[list.size()]);
                }

                return createPickerDialog("Select Location", cs, position, 2);
            default:
                return super.onCreateDialog(savedInstanceState);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            completeListener = (OnCompleteODFListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteODFListener.");
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        this.tag = tag;
        super.show(manager, tag);
    }

    public Dialog createDatePickerDialog() {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

        return datePickerDialog;
    }

    public Dialog createTimePickerDialog() {
        int hour = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), TimePickerDialog.THEME_DEVICE_DEFAULT_DARK , this, hour, minute, true);
        return timePickerDialog;
    }

    public Dialog createPickerDialog(String title, CharSequence[] cs, int position, final int type) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(title);
        dialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setSingleChoiceItems(cs, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(completeListener != null) {
                    switch (type) {
                        case 0:
                            completeListener.onLocationPicked(which);
                            break;
                        case 1:
                            completeListener.onLanguagePicked(which);
                            break;
                        case 2:
                            completeListener.onTimespanPicked(which);
                            break;
                        default:
                            break;
                    }

                }
                dialog.dismiss();
            }
        });
        return dialog.create();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if(completeListener != null) {
            completeListener.onDatePicked(year, monthOfYear, dayOfMonth);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(completeListener != null) {
            completeListener.onTimePicked(hourOfDay, minute);
        }
    }
}
