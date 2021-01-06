package com.kibzdev.globalfarm.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kibzdev.globalfarm.R;
import com.kibzdev.globalfarm.models.response.ChatResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NewGroupChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = NewGroupChatAdapter.class.getSimpleName();
    private int SELF = 100;
    private int OTHER = 300;

    private Context mContext;
    private List<ChatResponse.ChatMessages> chateList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar, imgAbbrev;
        TextView name, txtviewDate, messageBody;


        public ViewHolder(View view) {
            super(view);
            name = itemView.findViewById(R.id.name);
            txtviewDate = itemView.findViewById(R.id.date);
            messageBody = itemView.findViewById(R.id.message_body);


        }
    }


    public NewGroupChatAdapter(Context mContext, List<ChatResponse.ChatMessages> messageArrayList) {
        this.mContext = mContext;
        this.chateList = messageArrayList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == SELF) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_messages, parent, false);
        } else if (viewType == OTHER) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.their_messages, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.their_messages, parent, false);
        }

        return new NewGroupChatAdapter.ViewHolder(itemView);
    }


    @Override
    public int getItemViewType(int position) {
        ChatResponse.ChatMessages message = chateList.get(position);

        String from = "";

        if (!TextUtils.isEmpty(message.getFrom())) {
            from = message.getFrom();
        }


        if (from.equalsIgnoreCase("self")) {
            return SELF;
        } else if (!message.getFrom().equalsIgnoreCase("other")) {
            return OTHER;
        }

        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        ChatResponse.ChatMessages message = chateList.get(position);
        ((ViewHolder) holder).messageBody.setText(message.getMessage());
        ((ViewHolder) holder).txtviewDate.setText(getDate(message.getDate()));


        if (!TextUtils.isEmpty(message.getUser().getName())) {
            ((ViewHolder) holder).name.setText(message.getUser().getName());
        }

    }

    @Override
    public int getItemCount() {
        return chateList.size();
    }


    public String getDate(long milliseconds) {

        DateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);


        return formatter.format(calendar.getTime());

    }

}

