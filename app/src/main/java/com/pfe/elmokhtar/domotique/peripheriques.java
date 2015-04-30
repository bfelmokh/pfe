package com.pfe.elmokhtar.domotique;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by benfraj on 22/04/2015.
 */
public class peripheriques extends Activity {
    RecyclerView rv ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peripheriques);
        rv=(RecyclerView) findViewById(R.id.RVperipherique);
        String nom = getIntent().getExtras().getString("group");
        setTitle(nom);
}
}
