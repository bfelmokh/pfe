package com.pfe.elmokhtar.domotique;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by elmokhtar on 3/10/15.
 */
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pfe.elmokhtar.domotique.historique.historiqueFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends Activity {
    String[] menu;
    ArrayList<String> li= new ArrayList<String>();
    String temp;
    DrawerLayout dLayout;
    ListView dList;
    ArrayAdapter<String> adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        menu = new String[]{"Pieces", "Consommation", "Historiques", "Scénarios", "Paramétres", "Se déconnecter"};
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        dLayout.openDrawer(this.dList);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(true); // remove the icon
        }
        dList.setAdapter(adapter);
        dList.getContext().setTheme(android.R.style.Theme_Material_Light);
        dList.setSelector(android.R.color.holo_blue_light);
        dList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                dLayout.closeDrawers();

                FragmentManager fragmentManager = getFragmentManager();

                if (position == 0) {
                /*
                Pieces
                 */
                    Fragment detail = new DetailFragment();
                    Bundle args = new Bundle();
                    args.putString("Menu", menu[position]);

                    fragmentManager.beginTransaction().replace(R.id.content_frame, detail).commit();
                //    args.putStringArrayList("lp", li);
                 //   detail.setArguments(args);
                  //  if(li.isEmpty())
                   //     System.out.println("null Catched !");
                }
                    /*
                Pieces
                 */
                if(position == 2){

                    Fragment historique= new historiqueFragment();
                    Bundle args = new Bundle();
                    args.putString("Menu",menu[position]);
                    fragmentManager.beginTransaction().replace(R.id.content_frame,historique).commit();
                }

                if (position == 5) {

                    Intent intent = new Intent(Home.this, CheckLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }


            }

        });
    }


}