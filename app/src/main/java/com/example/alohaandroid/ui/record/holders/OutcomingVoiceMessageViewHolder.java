package com.example.alohaandroid.ui.record.holders;

import android.view.View;
import android.widget.TextView;

import com.example.alohaandroid.R;
import com.example.alohaandroid.ui.ae_chat.MessageHolders1;
import com.example.alohaandroid.ui.ae_chat.model.Message;
import com.example.alohaandroid.utils.messagekit.DateFormatter;

/*
 * Created by troy379 on 05.04.17.
 */
public class OutcomingVoiceMessageViewHolder
        extends MessageHolders1.OutcomingTextMessageViewHolder<Message> {

    private TextView tvDuration;
    private TextView tvTime;

    public OutcomingVoiceMessageViewHolder(View itemView, Object payload) {
        super(itemView, payload);
        tvDuration = itemView.findViewById(R.id.duration);
        tvTime = itemView.findViewById(R.id.time);
    }

    @Override
    public void onBind(Message message) {
        super.onBind(message);
        convertLongtoTime(message.getVoice().getDuration());
        tvTime.setText(DateFormatter.format(message.getCreatedAt(), DateFormatter.Template.TIME));
    }

  private void convertLongtoTime(long a){
        int h   = (int)(a /3600000);
        int m = (int)(a - h*3600000)/60000;
        int s= (int)(a - h*3600000- m*60000)/1000 ;
        String t = (h < 10 ? "0"+h: h)+":"+(m < 10 ? "0"+m: m)+":"+ (s < 10 ? "0"+s: s);
        tvDuration.setText(t);
    }

}
