package com.pfe.elmokhtar.domotique.statistiques;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pfe.elmokhtar.domotique.R;
import com.pfe.elmokhtar.domotique.RVperipherique.item;
import com.pfe.elmokhtar.domotique.RVperipherique.itemAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benfraj on 23/05/2015.
 */
public class statistiquesFragment extends Fragment {
    private LinearLayout mainLayout;
    private PieChart mchart;
    public int[] yData;
    public String[] xData;
    List<String> list = new ArrayList<String>();

    SwipeRefreshLayout mSwipeRefresh;
    PieData data;
    Spinner spinner;
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle args) {
        View view = inflater.inflate(R.layout.statistiques_fragment, container, false);
        mainLayout = (LinearLayout) view.findViewById(R.id.mainLayout);
        spinner = (Spinner) view.findViewById(R.id.list_piece);
        mSwipeRefresh=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Here i do some job
                invokeWSpiece();
                invokeWS(String.valueOf(spinner.getSelectedItem()));
                mSwipeRefresh.setRefreshing(false);
            }
        });
        mchart = (PieChart) view.findViewById(R.id.chart);

        mchart.setUsePercentValues(true);
        mchart.setDescription("Statistique d'utilisation");
        mchart.setDrawHoleEnabled(true);
        mchart.setHoleColorTransparent(true);
        mchart.setHoleRadius(30);
        mchart.setTransparentCircleRadius(10);
        mchart.setRotationAngle(0);
        mchart.setRotationEnabled(true);
        mchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                if (e==null)
                    return;
                Toast.makeText(getActivity(),xData[e.getXIndex()]+" = "+String.valueOf(e.getVal())+" fois utilisés",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
        invokeWSpiece();
        invokeWS("tout les peripheriques");

        Legend l = mchart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);


        return view;
    }
    private void addData(){
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for (int i=0;i<yData.length;i++){
            yVals.add(new Entry(yData[i],i));
        }
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i=0;i<xData.length;i++){
            xVals.add(xData[i]);
        }
        PieDataSet dataSet = new PieDataSet(yVals,"Liste des Peripheriques");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<Integer>();


        for (int o:ColorTemplate.JOYFUL_COLORS)
            colors.add(o);
        for (int o:ColorTemplate.VORDIPLOM_COLORS)
            colors.add(o);

        for (int o:ColorTemplate.PASTEL_COLORS)
            colors.add(o);
        for (int o:ColorTemplate.COLORFUL_COLORS)
            colors.add(o);
        for (int o:ColorTemplate.LIBERTY_COLORS)
            colors.add(o);
    colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);
        data = new PieData(xVals,dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(R.color.noir);
        mchart.setData(data);
        mchart.highlightValues(null);
        mchart.invalidate();

    }

    public void invokeWS(String piece) {
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+getString(R.string.IP)+"/WEB-INF/statistique/list/"+piece,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onFinish(){
                        addData();
                    }
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        try {
                            // Extract JSON Object from JSON returned by REST WS
                            JSONObject obj = new JSONObject(response);
                            // When the JSON response has status boolean value set to true
                            JSONArray array= obj.getJSONArray("statistique");

                            yData = new int[array.length()];
                            xData = new String[array.length()];
                            for (int i=0; i<array.length(); i++) {
                                JSONObject statistique = array.getJSONObject(i);
                                xData[i]=statistique.getString("nom");
                                yData[i]=statistique.getInt("occurence");
                                System.out.println(statistique.getInt("occurence"));
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

    public void invokeWSpiece() {
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://"+getString(R.string.IP)+"/WEB-INF/piece/list",
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onFinish(){
                        try {

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getView().getContext(),android.R.layout.simple_spinner_dropdown_item, list);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(dataAdapter);
                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    invokeWS(String.valueOf(spinner.getSelectedItem()));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
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
                            JSONArray array= obj.getJSONArray("piece");
                            /*loop*/
                            list= new ArrayList<String>();
                            list.add("tout les peripheriques");
                            for (int i=0; i<array.length(); i++) {
                                JSONObject group = array.getJSONObject(i);
                                list.add(group.getString("libelle"));

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
