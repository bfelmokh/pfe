package com.pfe.elmokhtar.domotique;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by benfraj on 22/04/2015.
 */
public class peripheriques extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peripheriques);
        Switch s = (Switch) findViewById(R.id.etat);
        TextView tv = (TextView) findViewById(R.id.nom);
        String nom = getIntent().getExtras().getString("group");
        tv.setText(nom);
}
}
