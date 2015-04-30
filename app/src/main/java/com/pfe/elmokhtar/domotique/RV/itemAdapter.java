package com.pfe.elmokhtar.domotique.RV;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pfe.elmokhtar.domotique.Home;
import com.pfe.elmokhtar.domotique.R;
import com.pfe.elmokhtar.domotique.peripheriques;

import java.util.List;

/**
 * Created by benfraj on 25/04/2015.
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
        View itemMessage = inflater.inflate(R.layout.simple_item_layout_group, parent, false);
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

        holder.setClickListener(new MessagesViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {
                    // View v at position pos is long-clicked.
                } else {
                    // View v at position pos is clicked.
                    Intent i = new Intent(v.getContext(),peripheriques.class);
                    v.getContext().startActivity(i);
                    System.out.println(messages.get(pos).getNom());

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public static class MessagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        View view;
        TextView id,nom;
        int position;
        private ClickListener clickListener;
        public MessagesViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        /* Interface for handling clicks - both normal and long ones. */
        public interface ClickListener {

            /**
             * Called when the view is clicked.
             *
             * @param v view that is clicked
             * @param position of the clicked item
             * @param isLongClick true if long click, false otherwise
             */
            public void onClick(View v, int position, boolean isLongClick);

        }
        /* Setter for listener. */
        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {

            // If not long clicked, pass last variable as false.
            clickListener.onClick(v, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {

            // If long clicked, passed last variable as true.
            clickListener.onClick(v, getPosition(), true);
            return true;
        }
    }

}
