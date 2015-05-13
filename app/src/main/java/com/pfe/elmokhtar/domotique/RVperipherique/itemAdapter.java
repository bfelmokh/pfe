package com.pfe.elmokhtar.domotique.RVperipherique;

/**
 * Created by benfraj on 30/04/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pfe.elmokhtar.domotique.R;
import com.pfe.elmokhtar.domotique.peripheriques;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
public class itemAdapter extends RecyclerView.Adapter<itemAdapter.MessagesViewHolder> {
    private LayoutInflater inflater;
    private List<com.pfe.elmokhtar.domotique.RVperipherique.item> messages;


    public itemAdapter(LayoutInflater inflater, List<com.pfe.elmokhtar.domotique.RVperipherique.item> messages) {
        this.inflater = inflater;
        this.messages = messages;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemMessage = inflater.inflate(R.layout.default_item_layout_peripherique, parent, false);
        MessagesViewHolder messagesViewHolder = new MessagesViewHolder(itemMessage);
        messagesViewHolder.view = itemMessage;
        messagesViewHolder.id = (TextView) itemMessage.findViewById(R.id.idperipherique);
        messagesViewHolder.nom = (TextView) itemMessage.findViewById(R.id.nomperipherique);
        messagesViewHolder.value = (Switch) itemMessage.findViewById(R.id.etatperi);

        return messagesViewHolder;
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        com.pfe.elmokhtar.domotique.RVperipherique.item message = messages.get(position);
        holder.id.setText(message.getId());
        holder.nom.setText(message.getNom());
        holder.value.setChecked(Boolean.valueOf(message.getValue()));
        holder.value.setText(message.getId());
        holder.value.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(buttonView.getText() +" "+ isChecked);
            }
        });
        holder.position = position;

        holder.setClickListener(new MessagesViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {
                    // View v at position pos is long-clicked.
                } else {
                    // View v at position pos is clicked.
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    System.out.println(messages.get(pos).getNom());
                    RequestParams params = new RequestParams();
                    params.put("id",messages.get(pos).getId());
                    params.put("value",messages.get(pos).getValue());
                    invokeWS(v, params);

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
        Switch value;
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
    public void invokeWS(View v,RequestParams params) {
        final View vi = v;
        final String paramss = params.toString();
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+v.getContext().getString(R.string.IP)+"/WEB-INF/peripherique/list/salon",
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onFinish(){
                        try {
                            System.out.println("here"+paramss);
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        // Extract JSON Object from JSON returned by REST WS

                        // When the JSON response has status boolean value set to true

                            /*loop*/

                        System.out.println("Change State with success!");


                    }

                    // When the response returned by REST has Http response code other than '200' such as '404', '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        // Hide Progress Dialog

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(vi.getContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(vi.getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(vi.getContext(), "Unexpected Error occcured! [Most common Error: Device might " +
                                            "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                    Toast.LENGTH_LONG).show();
                        }
                    }




                });
    }
}
