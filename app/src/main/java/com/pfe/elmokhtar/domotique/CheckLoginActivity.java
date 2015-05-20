package com.pfe.elmokhtar.domotique ;

/**
 * Created by elmokhtar on 3/12/15.
 */
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CheckLoginActivity extends Activity {
    SharedPreferences sp;
    private EditText pseudo;
    private EditText mdp;
    private TextView textView;
    private ImageView imgView;
    private Intent i;
    // Progress Dialog Object
    ProgressDialog prgDialog;
    RequestParams params = new RequestParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pseudo = (EditText) findViewById(R.id.pseudo);
        mdp = (EditText) findViewById(R.id.password);
        textView = (TextView) findViewById(R.id.tv_result);
        imgView = (ImageView) findViewById(R.id.progressBar1);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }
        i = new Intent(this , Home.class);
        prgDialog = new ProgressDialog(this);
        // Set Progress Dialog Text
        prgDialog.setMessage("Please wait...");
        // Set Cancelable as False
        prgDialog.setCancelable(false);

    }

    public void checkbtn(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(pseudo.getWindowToken(), 0);
        // When Username is greater than or equal to 5 characters
        if (Utility.isNotNull(pseudo.getText().toString()) && pseudo.getText().toString().length() >= 5) {
            // Set Username typed by user in Request Params object
            params.put("pseudo", pseudo.getText().toString());
            params.put("mdp", mdp.getText().toString());
            // Invoke REST Web service to check if username typed is available or not
            invokeWS(params);
        }
        // When Username is not greater than or equal to 5 characters
        else {
            imgView.setBackgroundResource(R.drawable.ic_action_warning);
            textView.setText("Username must be minimum 5 characters");
        }
    }

    public void invokeWS(RequestParams params) {
        // Show Progress Dialog
        prgDialog.show();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+getString(R.string.IP)+"/WEB-INF/login/docheck",params,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject obj = new JSONObject(response);
                            // When the JSON response has status boolean value set to true
                            if (obj.getBoolean("status")) {
                                // Set 'Thumb up' Image as status image
                                imgView.setBackgroundResource(R.drawable.ic_action_good);
                                // Set Status message
                                //local storage with Shared preferences
                                sp = getSharedPreferences("login",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("pseudo",pseudo.getText().toString());
                                editor.commit();



                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);


                                Toast.makeText(getApplicationContext(),"Bienvenue !", Toast.LENGTH_LONG).show();
                            }
                            // Else display error message set in JSON response
                            else {
                                imgView.setBackgroundResource(R.drawable.ic_action_warning);
                                textView.setText(obj.getString("error_msg"));
                            }
                        } catch (JSONException e) {

                            Toast.makeText(getApplicationContext(),"Error Occured while parsing [Server's JSON response might be invalid]!",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                    // When the response returned by REST has Http response code other than '200' such as '404', '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        // Hide Progress Dialog
                        prgDialog.hide();
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "Requested resource not found",Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end",Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(getApplicationContext(),    "Unexpected Error occcured! [Most common Error: Device might " +
                                            "not be connected to Internet or remote server is not up and running], check for other errors as well",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}