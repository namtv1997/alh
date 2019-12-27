package com.example.alohaandroid.ui.ae_chat.messages;

import android.view.View;

import com.example.alohaandroid.ui.ae_chat.MessageHolders1;
import com.example.alohaandroid.ui.ae_chat.model.Message;

public class CustomOutcomingTextMessageViewHolder
        extends MessageHolders1.OutcomingTextMessageViewHolder<Message> {

    public CustomOutcomingTextMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);

        time.setText(message.getStatus() + " " + time.getText());
    }
}
