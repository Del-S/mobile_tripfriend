package com.tripfriend.front.order;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tripfriend.R;
import com.tripfriend.front.Friend;

import java.util.List;

public class PickFriendAdapter extends ArrayAdapter<Friend>{

    public PickFriendAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = getContext();
        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.item_friend, null);
        }
        Friend friend = getItem(position);

        ImageView imgFriend = (ImageView) convertView.findViewById(R.id.item_friend_image);
        TextView txtName = (TextView) convertView.findViewById(R.id.item_friend_name);
        Bitmap bmp = BitmapFactory.decodeFile( context.getFilesDir() + "/" + friend.getImage() );
        imgFriend.setImageBitmap(bmp);
        txtName.setText(friend.getName());

        LinearLayout languagesLayout = (LinearLayout) convertView.findViewById(R.id.item_friend_languages);
        List<String> languages = friend.getLanguages();
        for(int i = 0; languages.size() > i; i++ ) {
            ImageView imgLanguage = new ImageView(context);
            Bitmap bmpLang = BitmapFactory.decodeFile( context.getFilesDir() + "/" + languages.get(i) );
            imgLanguage.setImageBitmap(bmpLang);

            languagesLayout.addView(imgLanguage);
        }

        return convertView;
    }
}
