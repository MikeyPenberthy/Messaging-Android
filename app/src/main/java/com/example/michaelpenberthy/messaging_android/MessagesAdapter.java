package com.example.michaelpenberthy.messaging_android;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelpenberthy on 9/9/16.
 */
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private Context context;
    private List<Message> messages = new ArrayList<>();
    private String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    public MessagesAdapter(@NonNull Context context, @NonNull List<Message> messages) {
        this.context = context;
        this.messages = messages;

    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_text_message, null);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        Message message = messages.get(position);

        viewHolder.messageEditText.setText(messages.get(position).getText());

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout
                .LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);

        if (userId.equals(message.getSenderId())) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            viewHolder.messageContainerLayout.setLayoutParams(layoutParams);

            String imageUrl = "https://s-media-cache-ak0.pinimg.com/originals/bb/60/9c/bb609c45d1abd73d815ae14938720a9b.jpg";
            viewHolder.userImageViewContainerLayout.setVisibility(View.VISIBLE);
            viewHolder.matchImageViewContainerLayout.setVisibility(View.GONE);
            viewHolder.userImageView.setImageUrl(imageUrl);

            viewHolder.messageEditText.setBackgroundResource(R.drawable.send_bubble);
            viewHolder.messageEditText.setBackgroundTintList(ColorStateList
                    .valueOf(ContextCompat.getColor(context, R.color.colorBlueChatBubble)));
            viewHolder.messageEditText.setGravity(Gravity.CENTER);

        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            viewHolder.messageContainerLayout.setLayoutParams(layoutParams);

            String imageUrl = "https://s-media-cache-ak0.pinimg.com/564x/14/05/a3/1405a36471d49387c99563865b09392a.jpg";
            viewHolder.userImageViewContainerLayout.setVisibility(View.GONE);
            viewHolder.matchImageViewContainerLayout.setVisibility(View.VISIBLE);
            viewHolder.matchImageView.setImageUrl(imageUrl);

            viewHolder.messageEditText.setBackgroundResource(R.drawable.recieved_bubble);
            viewHolder.messageEditText.setBackgroundTintList(ColorStateList
                    .valueOf(ContextCompat.getColor(context, R.color.colorText)));
            viewHolder.messageEditText.setGravity(Gravity.CENTER);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout messageContainerLayout;
        public RelativeLayout matchImageViewContainerLayout;
        public RelativeLayout userImageViewContainerLayout;
        public SmartImageView matchImageView;
        public SmartImageView userImageView;
        public TextView messageEditText;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            messageContainerLayout = (LinearLayout) itemLayoutView
                    .findViewById(R.id.messageContainerLayout);
            matchImageViewContainerLayout = (RelativeLayout) itemLayoutView
                    .findViewById(R.id.matchImageViewContainerLayout);
            userImageViewContainerLayout = (RelativeLayout) itemLayoutView
                    .findViewById(R.id.userImageViewContainerLayout);
            matchImageView = (SmartImageView) itemLayoutView.findViewById(R.id.matchImageView);
            userImageView = (SmartImageView) itemLayoutView.findViewById(R.id.userImageView);
            messageEditText = (TextView) itemLayoutView.findViewById(R.id.messageEditText);
            messageEditText.setTextColor(Color.WHITE);
        }
    }
}
