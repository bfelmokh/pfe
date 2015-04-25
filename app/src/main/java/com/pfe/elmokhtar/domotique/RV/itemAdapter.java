package com.pfe.elmokhtar.domotique.RV;

import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pfe.elmokhtar.domotique.R;

import java.util.List;

/**
 * Created by benfraj on 25/04/2015.
 */
public class itemAdapter extends RecyclerView.Adapter<itemAdapter.MessagesViewHolder> implements View.OnClickListener {
    private LayoutInflater inflater;
    private List<item> messages;


    public itemAdapter(LayoutInflater inflater, List<item> messages) {
        this.inflater = inflater;
        this.messages = messages;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemMessage = inflater.inflate(R.layout.item_layout, parent, false);
        MessagesViewHolder messagesViewHolder = new MessagesViewHolder(itemMessage);
        messagesViewHolder.view = itemMessage;
        messagesViewHolder.id = (TextView) itemMessage.findViewById(R.id.idgroup);
        messagesViewHolder.nom = (TextView) itemMessage.findViewById(R.id.nomgroup);

        return messagesViewHolder;
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        item message = messages.get(position);
        holder.id.setText(message.getId());
        holder.nom.setText(message.getNom());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public void onClick(View v) {
    System.out.println("");
    }

    public static class MessagesViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView id,nom;
        int position;

        public MessagesViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
        }
    }
}
