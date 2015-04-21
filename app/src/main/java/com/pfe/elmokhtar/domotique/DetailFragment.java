package com.pfe.elmokhtar.domotique;

/**
 * Created by elmokhtar on 3/11/15.
 */

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class DetailFragment extends Fragment {
    ListView list;
ArrayList<String>lil = new ArrayList<String>();
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.detailfragment, container, false);
        //TextView text= (TextView) view.findViewById(R.id.detail);

        list = (ListView) view.findViewById(R.id.list);
        //Bundle test = getArguments();
        //String menu = getArguments().getString("Menu");

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        invokeWS();

    //text.setText("test");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id){
                System.out.println(list.getItemAtPosition(position).toString());

            }



        });
        return view;
    }

    public void invokeWS() {
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.0.100:8080/WEB-INF/groups/list",
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onFinish(){
                        try {
                            System.out.println("here");
                            if(!lil.isEmpty()){
                                ArrayAdapter<String> adapter;
                                adapter = new ArrayAdapter<String>(DetailFragment.this.getActivity(), android.R.layout.simple_list_item_1, lil);
                                list.setAdapter(adapter);
                                System.out.println("done!");}
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
                            JSONArray array= obj.getJSONArray("group");
                            /*loop*/
                            for (int i=0; i<array.length(); i++) {
                                JSONObject group = array.getJSONObject(i);
                                System.out.println(group.getString("nom"));
                                lil.add(i, group.getString("nom"));

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
}
