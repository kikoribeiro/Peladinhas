package com.example.peladinhas;

import com.example.peladinhas.Classes.Message;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<Message> {

    private List<Message> messages = new ArrayList<>();

    public MessageAdapter(Context context) {
        super(context, 0);
    }

    public void addMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_message, parent, false);
        }

        Message message = getItem(position);

        TextView senderTextView = convertView.findViewById(R.id.senderTextView);
        TextView contentTextView = convertView.findViewById(R.id.contentTextView);
        TextView timestampTextView = convertView.findViewById(R.id.timestampTextView);

        senderTextView.setText(message.getSender());
        contentTextView.setText(message.getContent());

        // Convert timestamp to a readable date format
        Date date = message.getTimestamp();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        timestampTextView.setText(dateFormat.format(date));


        return convertView;
    }
}
