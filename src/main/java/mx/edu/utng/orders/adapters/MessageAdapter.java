package mx.edu.utng.orders.adapters;

import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.edu.utng.orders.R;
import mx.edu.utng.orders.model.Messages;
import mx.edu.utng.orders.sqlite.OrderContract;

/**
 * Created by jntreyes on 23/02/17.
 */

public class MessagesAdapter extends RecyclerView.Adapter <MessagesAdapter.MessageViewHolder>
        implements View.OnClickListener {

    List<Messages> messages;
    View.OnClickListener listener;
    //Constructor
    public MessagesAdapter(List<Messages> messages){
        this.messages=messages;
    }
    //getter and setter de listener
    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MessagesAdapter.MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_layout,parent,false);
        MessageViewHolder holder=new MessageViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }



    @Override
    public void onBindViewHolder(MessagesAdapter.MessageViewHolder holder, int position) {
        holder.tvMessage.setText(messages.get(position).getMessage());
        holder.tvIp.setText(messages.get(position).getIp());
        holder.iv_message.setImageResource(R.mipmap.ic_launcher);
        holder.setListener(this);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }

    }

    public static class MessageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cvMessage;
        TextView tvMessage;
        TextView tvIp;
        ImageView iv_message;
        ImageButton btEditMessage;
        ImageButton btDeleteMessage;
        View.OnClickListener listener;

        public MessageViewHolder(View itemView) {
            super(itemView);
            cvMessage=(CardView)itemView.findViewById(R.id.cv_message);
            iv_message=(ImageView)itemView.findViewById(R.id.iv_message);
            tvMessage=(TextView)itemView.findViewById(R.id.tv_message);
            tvIp=(TextView)itemView.findViewById(R.id.tv_message_ip);
            btEditMessage=(ImageButton) itemView.findViewById(R.id.bt_edit_message);
            btDeleteMessage=(ImageButton) itemView.findViewById(R.id.bt_delete_message);
            btEditMessage.setOnClickListener(this);
            btDeleteMessage.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (listener!=null){
                listener.onClick(v);
            }
        }

        public void setListener(View.OnClickListener listener){
            this.listener=listener;

        }
    }

}
