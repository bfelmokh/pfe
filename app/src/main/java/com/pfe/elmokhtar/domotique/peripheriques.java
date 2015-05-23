package com.pfe.elmokhtar.domotique;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pfe.elmokhtar.domotique.RVperipherique.item;
import com.pfe.elmokhtar.domotique.RVperipherique.itemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by benfraj on 22/04/2015.
 */
public class peripheriques extends Activity implements View.OnTouchListener {
    RecyclerView rv ;
    ArrayList<item> lil = new ArrayList<item>();
    String nom;
    String user;
    SharedPreferences sp;
    SwipeRefreshLayout mSwipeRefresh;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peripheriques);
        View v1 = (View)findViewById(R.id.pv);
        rv=(RecyclerView) findViewById(R.id.RVperipherique);
        rv.setOnTouchListener(this);
        nom = getIntent().getExtras().getString("group");
        setTitle(nom);
        rv.setLayoutManager(new LinearLayoutManager(this));
        sp = getSharedPreferences("login",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sp.edit();
        user = sp.getString("pseudo","");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        invokeWS();
        mSwipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Here i do some job
                invokeWS();
                mSwipeRefresh.setRefreshing(false);
            }
        });
}
    public void invokeWS() {
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+getString(R.string.IP)+"/WEB-INF/peripherique/list/"+nom,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onFinish(){
                        try {

                            if(!lil.isEmpty()){
                                //ArrayAdapter<String> adapter;
                                //adapter = new ArrayAdapter<String>(DetailFragment.this.getActivity(), android.R.layout.simple_list_item_1, lil);
                                //list.setAdapter(adapter);
                                itemAdapter msgAdapter = new itemAdapter(getLayoutInflater(), lil,user);
                                rv.setAdapter(msgAdapter);
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
                            JSONArray array= obj.getJSONArray("peripherique");

                            lil.clear();
                            for (int i=0; i<array.length(); i++) {
                                JSONObject peripherique = array.getJSONObject(i);
                                lil.add(i, new item(peripherique.getString("id"), peripherique.getString("nom"),peripherique.getString("etat")));

                            }



                        } catch (JSONException e) {

                            Toast.makeText(getApplicationContext(), "Error Occured while parsing [Check your Server]", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }

                    // When the response returned by REST has Http response code other than '200' such as '404', '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        // Hide Progress Dialog

                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might " +
                                            "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                    Toast.LENGTH_LONG).show();
                        }
                    }




                });
    }
    // onTouchEvent () method gets called when User performs any touch event on screen
    // Method to handle touch event like left to right swap and right to left swap
    public boolean onTouchEvent(MotionEvent touchevent)
    {
        switch (touchevent.getAction())
        {
            // when user first touches the screen we get x and y coordinate
            case MotionEvent.ACTION_DOWN:

                break;

            case MotionEvent.ACTION_MOVE:

                break;

            case MotionEvent.ACTION_UP:
                //invokeWS();
                break;

        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        onTouchEvent(event);
        return false;
    }
}
