package com.tripfriend.front.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tripfriend.R;
import com.tripfriend.configuration.Configuration;
import com.tripfriend.front.Friend;
import com.tripfriend.front.Schedule;

import java.text.SimpleDateFormat;
import java.util.List;

public class ListScheduleAdapter extends ArrayAdapter<Schedule> {

    public ListScheduleAdapter(Context context, int resource, List<Schedule> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = getContext();
        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_schedule, null);
        }
        Schedule schedule = getItem(position);
        Configuration config = Configuration.getInstance();
        Friend friend = config.getFriendByID(schedule.getId_friend());

        ImageView imageViewFriend = (ImageView) convertView.findViewById(R.id.item_schedule_friend);
        TextView textViewLocation = (TextView) convertView.findViewById(R.id.item_schedule_location);
        TextView textViewLanguage = (TextView) convertView.findViewById(R.id.item_schedule_language);
        TextView dateTimeViewLocation = (TextView) convertView.findViewById(R.id.item_schedule_date_time);
        TextView pickupLocViewLocation = (TextView) convertView.findViewById(R.id.item_schedule_pickup_location);

        Bitmap bmp = BitmapFactory.decodeFile(context.getFilesDir() + "/" + friend.getImage());
        imageViewFriend.setImageBitmap(bmp);

        String textLocation = context.getString(R.string.list_schedule_location);
        textLocation += " " + config.getLocations().get( schedule.getLocation() );
        textViewLocation.setText(textLocation);

        String textLanguage = context.getString(R.string.list_schedule_language);
        textLanguage += " " + config.getLanguages().get( schedule.getLanguage() );
        textViewLanguage.setText(textLanguage);

        // TODO: change format by mobile settings
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        String textDateTime = context.getString(R.string.list_schedule_date_time);
        textDateTime += " " + dateFormat.format(schedule.getCalendar_start().getTime());
        dateTimeViewLocation.setText(textDateTime);

        String textPickupLocation = context.getString(R.string.list_schedule_pickup_location);
        textPickupLocation += " " + schedule.getPickup_location();
        pickupLocViewLocation.setText(textPickupLocation);

        return convertView;
    }
}
