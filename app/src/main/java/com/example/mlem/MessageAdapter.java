package com.example.mlem;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

// MessageAdapter.java
public class MessageAdapter extends BaseAdapter {

    List<Message> messages = new ArrayList<Message>();
    Context context;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    static String getUserNameInitials(String username) {
        String inits = "XX";
        if (username == null) {
            return inits;
        }
        String[] splitstr = username.split(" ");
        int len = splitstr.length;
        try {
            if (len == 1) {
                inits = splitstr[0].substring(0, 2).toUpperCase();
            } else {
                inits = (splitstr[0].substring(0, 1) + splitstr[1].substring(0, 1)).toUpperCase();
            }
        } catch (Exception e) {
            inits = "XX";
        }


        return inits;
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    // This is the backbone of the class, it handles the creation of single ListView row (chat bubble)
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Message message = messages.get(i);



        /*if (message.isBelongsToCurrentUser()) { // this message was sent by us so let's create a basic chat bubble on the right
            //convertView = messageInflater.inflate(R.layout.my_message, null);
            convertView = messageInflater.inflate(R.layout.message, null);
            holder.messageBody = (TextView) convertView.findViewById(R.id.message_text);
            holder.name = (TextView) convertView.findViewById(R.id.message_user);
            holder.timestamp = (TextView) convertView.findViewById(R.id.message_time);
            convertView.setTag(holder);
            holder.name.setText(message.getSender());
            holder.messageBody.setText(message.getText());
            holder.timestamp.setText(message.getTimestamp());
        } else {*/ // this message was sent by someone else so let's create an advanced chat bubble on the left
        //convertView = messageInflater.inflate(R.layout.their_message, null);
        convertView = messageInflater.inflate(R.layout.message, null);
        //holder.avatar = (View) convertView.findViewById(R.id.avatar);
        holder.avatarChar = convertView.findViewById(R.id.avatarText);
        holder.messageBody = convertView.findViewById(R.id.message_text);
        holder.name = convertView.findViewById(R.id.message_user);
        holder.timestamp = convertView.findViewById(R.id.message_time);
        holder.avatar = convertView.findViewById(R.id.avatar);
        convertView.setTag(holder);


        Drawable background = holder.avatar.getBackground();
        int clrset = getColor(message.getSenderName());


        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(context, clrset));
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(ContextCompat.getColor(context, clrset));
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(ContextCompat.getColor(context, clrset));
        }


        holder.name.setText(message.getSenderName());//message.getSender());
        holder.messageBody.setText(message.getText());
        holder.timestamp.setText(message.getTimestamp());
        //holder.avatar.findViewById(R.id.lilCircle).setBackgroundColor(getColor(message.getSenderName()));
        holder.avatarChar.setText(getUserNameInitials(message.getSenderName()));


        //GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
        //drawable.setColor(Color.parseColor(message.getMemberData().getColor()));
        //}

        return convertView;
    }

    int getColor(String username) {

        int colorAccent = R.color.colorAccent;
        int colorYellowLight = R.color.colorYellowLight;
        int colorGreenLight = R.color.colorGreenLight;
        int colorPurpleLight = R.color.colorPurpleLight;
        int colorDark = R.color.colorPrimaryDark;
        int colorBlack = R.color.colorBlack;

        //Final
        int color;
        char ch = ' ';
        try {
            ch = username.charAt(0);
        } catch (Exception e) {
        }


        switch (ch) {
            case 'A':
                color = colorAccent;
                break;
            case 'J':
                color = colorYellowLight;
                break;
            case 'D':
                color = colorGreenLight;
                break;
            case 'S':
                color = colorPurpleLight;
                break;
            case 'K':
                color = colorDark;
                break;
            default:
                color = colorBlack;
        }


        return color;


    }

}

class MessageViewHolder {
    //public View avatar;
    public TextView name;
    public TextView messageBody;
    public TextView timestamp;
    public TextView avatarChar;
    public View avatar;
}