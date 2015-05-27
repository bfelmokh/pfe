package com.pfe.elmokhtar.domotique;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by elmokhtar on 3/10/15.
 */
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
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
import com.pfe.elmokhtar.domotique.Scenario.ScenarioFragment;
import com.pfe.elmokhtar.domotique.historique.historiqueFragment;
import com.pfe.elmokhtar.domotique.statistiques.statistiquesFragment;

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
    ActionBarDrawerToggle mDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        menu = new String[]{"Pièces", "Statistiques", "Historiques", "Scénarios", "Paramétres", "Se déconnecter"};
        dLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        dList = (ListView) findViewById(R.id.left_drawer);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menu);
        dLayout.openDrawer(this.dList);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                dLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.hello_world,  /* "open drawer" description */
                R.string.hello_world  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle("Title");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getActionBar().setTitle("Title");
            }
        };

        /*ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(true); // remove the icon
        }
*/       dLayout.setDrawerListener(mDrawerToggle);


        dList.setAdapter(adapter);
        dList.getContext().setTheme(android.R.style.Theme_Material_Light);
        dList.setSelector(android.R.color.holo_blue_light);
        dList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
                dLayout.closeDrawers();

                FragmentManager fragmentManager = getFragmentManager();
                setTitle(menu[position]);
                if (position == 0) {
                /*
                Pieces
                 */
                    Fragment detail = new DetailFragment();
                    Bundle args = new Bundle();
                    args.putString("Menu", menu[position]);

                    fragmentManager.beginTransaction().replace(R.id.content_frame, detail).commit();

                }
                    /*
                Pieces
                 */
                if(position == 1){

                    Fragment statistiques= new statistiquesFragment();
                    Bundle args = new Bundle();
                    args.putString("Menu",menu[position]);
                    fragmentManager.beginTransaction().replace(R.id.content_frame,statistiques).commit();
                }
                if(position == 2){

                    Fragment historique= new historiqueFragment();
                    Bundle args = new Bundle();
                    args.putString("Menu",menu[position]);
                    fragmentManager.beginTransaction().replace(R.id.content_frame,historique).commit();
                }
                if(position == 3){

                    Fragment scenario= new ScenarioFragment();
                    Bundle args = new Bundle();
                    args.putString("Menu",menu[position]);
                    fragmentManager.beginTransaction().replace(R.id.content_frame,scenario).commit();
                }
                if (position == 5) {

                    Intent intent = new Intent(Home.this, CheckLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }


            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {

    }
}