package com.hassan.masla7ty.chat;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hassan.masla7ty.R;


/**
 * Created by Vivek on 01-Aug-16.
 */
public class ChatMessageViewHolder extends RecyclerView.ViewHolder {

    TextView sender, msg;
    public ChatMessageViewHolder(View itemView) {
        super(itemView);
        sender = (TextView) itemView.findViewById(R.id.lbl1);
        msg = (TextView) itemView.findViewById(R.id.lbl2);
    }
}
