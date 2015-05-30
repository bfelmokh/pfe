package com.pfe.elmokhtar.domotique.Scenario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pfe.elmokhtar.domotique.R;
import com.pfe.elmokhtar.domotique.peripheriques;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by benfraj on 24/05/2015.
 */
public class itemAdapter extends RecyclerView.Adapter<itemAdapter.MessagesViewHolder> {
    private LayoutInflater inflater;
    private List<item> messages;
    private View itemMessage;
    private String id;
    String s=new String();
    public itemAdapter(LayoutInflater inflater, List<item> messages) {
        this.inflater = inflater;
        this.messages = messages;
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemMessage = inflater.inflate(R.layout.simple_item_layout_histo, parent, false);
        MessagesViewHolder messagesViewHolder = new MessagesViewHolder(itemMessage);
        messagesViewHolder.view = itemMessage;
        messagesViewHolder.libelle = (TextView) itemMessage.findViewById(R.id.histo);
        messagesViewHolder.id = (TextView) itemMessage.findViewById(R.id.histodate);
        messagesViewHolder.id.setVisibility(View.INVISIBLE);
        messagesViewHolder.peri = (TextView) itemMessage.findViewById(R.id.histouser);
        return messagesViewHolder;
    }

    @Override
    public void onBindViewHolder(final MessagesViewHolder holder, int position)  {
        final item message = messages.get(position);
        holder.id.setText(message.getId());
        holder.libelle.setText(message.getLibelle());
        holder.peri.setText(message.getPeri());
        holder.position = position;

        holder.setClickListener(new MessagesViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {

                    applique(messages.get(pos).getId());

                } else {
                    // View v at position pos is clicked.
                    id = messages.get(pos).getId();
                    if(s.isEmpty()){
                    s=invokeWS(holder);
                    }
                    else
                    {
                        s =new String();
                        holder.peri.setText("");
                    }


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public static class MessagesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener {
        View view;
        TextView id,peri,libelle;
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
    public String invokeWS(final MessagesViewHolder holder) {
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        final MessagesViewHolder holde;
        holde= holder;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://" + itemMessage.getContext().getString(R.string.IP) + "/WEB-INF/scenario/getperi/"+id,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onFinish() {
                        holde.peri.setText(s);
                        try {
                            System.out.println(s);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject obj = new JSONObject(response);
                            // When the JSON response has status boolean value set to true
                            JSONArray array = obj.getJSONArray("scenario");
                            /*loop*/
                            s = new String();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject scenarioG = array.getJSONObject(i);
                               s=s+("-"+scenarioG.getString("nom") + " du " + scenarioG.getString("libelle") +" à "+convertir(scenarioG.getInt("etat")) +" \n");
                            }


                        } catch (JSONException e) {

                            Toast.makeText(itemMessage.getContext(), "Error Occured while parsing [Check your Server]", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }

                    // When the response returned by REST has Http response code other than '200' such as '404', '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        // Hide Progress Dialog

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(itemMessage.getContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(itemMessage.getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(itemMessage.getContext(), "Unexpected Error occcured! [Most common Error: Device might " +
                                            "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                    Toast.LENGTH_LONG).show();
                        }
                    }


                });
        return s;
    }

    public String applique(String s) {
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        SharedPreferences sp;
        sp = itemMessage.getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        String user = sp.getString("pseudo","");
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://" + itemMessage.getContext().getString(R.string.IP) + "/WEB-INF/scenario/appliquer/"+user+"/"+id,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onFinish() {

                    }

                    public void onSuccess(String response) {
                        // Hide Progress Dialog

                        Toast.makeText(itemMessage.getContext(), response , Toast.LENGTH_LONG).show();
                    }

                    // When the response returned by REST has Http response code other than '200' such as '404', '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        // Hide Progress Dialog

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(itemMessage.getContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(itemMessage.getContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(itemMessage.getContext(), "Unexpected Error occcured! [Most common Error: Device might " +
                                            "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                    Toast.LENGTH_LONG).show();
                        }
                    }


                });
        return s;
    }
    private String convertir(int etat) {
        String etatt = "eteindre";
        if(etat==1){
            etatt="allumer";
        }

        return etatt;
    }
}
