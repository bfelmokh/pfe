package com.pfe.elmokhtar.domotique.Scenario;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pfe.elmokhtar.domotique.R;

import java.util.List;

/**
 * Created by benfraj on 24/05/2015.
 */
public class itemAdapter extends RecyclerView.Adapter<itemAdapter.MessagesViewHolder> {
    private LayoutInflater inflater;
    private List<item> messages;


    public itemAdapter(LayoutInflater inflater, List<item> messages) {
        this.inflater = inflater;
        this.messages = messages;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemMessage = inflater.inflate(R.layout.simple_item_layout_histo, parent, false);
        MessagesViewHolder messagesViewHolder = new MessagesViewHolder(itemMessage);
        messagesViewHolder.view = itemMessage;
        messagesViewHolder.id = (TextView) itemMessage.findViewById(R.id.histo);
        messagesViewHolder.libelle = (TextView) itemMessage.findViewById(R.id.histodate);
        messagesViewHolder.peri = (TextView) itemMessage.findViewById(R.id.histouser);
        return messagesViewHolder;
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        item message = messages.get(position);
        holder.id.setText(message.getId());
        holder.libelle.setText(message.getLibelle());
        holder.peri.setText(message.getPeri());
        holder.position = position;

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public static class MessagesViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView id,peri,libelle;
        int position;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
        }
        /* Interface for handling clicks - both normal and long ones. */

    }

}
