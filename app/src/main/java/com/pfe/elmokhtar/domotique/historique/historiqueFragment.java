package com.pfe.elmokhtar.domotique.historique;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pfe.elmokhtar.domotique.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by benfraj on 16/05/2015.
 */
public class historiqueFragment extends Fragment {
    ListView list;
    RecyclerView msgView;
    ArrayList<item> lil = new ArrayList<item>();
    SwipeRefreshLayout mSwipeRefresh;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.historique_fragment, container, false);
        msgView = (RecyclerView) view.findViewById(R.id.RVhistorique);
        msgView.setLayoutManager(new LinearLayoutManager(getActivity()));
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        invokeWS();
        //msgView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        /*refresh test*/
        mSwipeRefresh=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Here i do some job
                invokeWS();
                mSwipeRefresh.setRefreshing(false);
            }
        });

        msgView.setItemAnimator(new DefaultItemAnimator());
        return view;
    }
    public void invokeWS() {
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+getString(R.string.IP)+"/WEB-INF/historique/list",
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onFinish(){
                        try {
                            if(!lil.isEmpty()){

                                itemAdapter msgAdapter = new itemAdapter(getActivity().getLayoutInflater(), lil);
                                msgView.setAdapter(msgAdapter);
                                }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject obj = new JSONObject(response);
                            // When the JSON response has status boolean value set to true
                            JSONArray array= obj.getJSONArray("historique");
                            /*loop*/
                            lil.clear();
                            for (int i=0; i<array.length(); i++) {
                                JSONObject group = array.getJSONObject(i);
                                lil.add(i, new item(i+"", convertir(group.getString("piece"), group.getString("nom"), group.getInt("etat")),group.getString("date"),group.getString("pseudo")));

                            }



                        } catch (JSONException e) {

                            Toast.makeText(getActivity().getApplicationContext(), "Error Occured while parsing [Check your Server]", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }

                    // When the response returned by REST has Http response code other than '200' such as '404', '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        // Hide Progress Dialog

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getActivity().getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(getActivity().getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might " +
                                            "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                    Toast.LENGTH_LONG).show();
                        }
                    }




                });
    }

    private String convertir(String piece, String nom, int etat) {
        String etatt = "eteint";
        if(etat==1){
            etatt="allumé";
        }

        return "le peripherique "+nom.toUpperCase()+" du "+piece+" a été "+etatt+".";
    }
}
