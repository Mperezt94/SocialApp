package com.example.gerard.socialapp.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.gerard.socialapp.R;

public class MessageViewHolder extends RecyclerView.ViewHolder {


    public TextView author;
    public TextView content;


    public MessageViewHolder(View itemView) {
        super(itemView);

        author = itemView.findViewById(R.id.author);
        content = itemView.findViewById(R.id.content);

    }
}
