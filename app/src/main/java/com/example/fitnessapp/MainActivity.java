package com.example.fitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewPropertyAnimatorListenerAdapter;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.leo.simplearcloader.SimpleArcLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.material.snackbar.Snackbar;
import java.util.concurrent.TimeUnit;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public CardView stress_module;
    TextView bpCases,bgCases,oxyCases,rlCases,hrtCases;
    SimpleArcLoader simpleArcLoader;
    TextView textResult;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    private boolean runningQOrLater =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q;
    // [START dataPointListener_variable_reference]
    // Need to hold a reference to this listener, as it's passed into the "unregister"
    // method in order to stop all sensors from sending data to this listener.


    // [END dataPointListener_variable_reference]

    // [START auth_oncreate_setup]
    private CardView cardResult;
    private RequestQueue mQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simpleArcLoader = findViewById(R.id.loader);
        stress_module = (CardView) findViewById(R.id.stress_module);

        Button btn_track = findViewById(R.id.btn_track);
        mQueue = Volley.newRequestQueue(this);

        btn_track.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
        Button fire_data = findViewById(R.id.fire_data);

    }
    private void jsonParse(){
        try {
            JSONObject obj =new JSONObject(LoadJsonFromAsset());
            JSONArray array =new JSONArray("Datasource");
            HashMap<String,String> list =new HashMap<>();
            ArrayList<HashMap<String, String>> arrayList =new ArrayList<>();
            for (int i=0;i<array.length();i++){
                JSONObject o =array.getJSONObject(i);
                String dt =o.getString("dataType");
                int v = o.getInt("value");
                list.put("dataType" , dt);
                list.put("value",String.valueOf(v));
                arrayList.add(list);
                textResult.append((CharSequence) arrayList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public  String LoadJsonFromAsset() {
        String json = null;
        try {
            InputStream in = this.getAssets().open("fitness-backend.json");
            int size = in.available();
            byte[] bbuffer = new byte[size];
            in.read(bbuffer);
            in.close();
            json = new String(bbuffer, "UTF-8");
        }

         catch(IOException e){
                e.printStackTrace();
                return null;
            }
            return json;
        }



    public void onClick(View v){
        Intent i;

        switch (v.getId()){
            case R.id.stress_module :
                i = new Intent(this,stress_activity.class);
                startActivity(i);
                break;

        }
    }

    public void process2(View view) {
        textResult = findViewById(R.id.textResult);
        FirebaseDatabase db =FirebaseDatabase.getInstance();
        DatabaseReference root =db.getReference("null");

        root.setValue(textResult.getText().toString());
        textResult.setText("");
        Toast.makeText(getApplicationContext(),"Inserted",Toast.LENGTH_LONG).show();
    }

    public void process1(View view) {
        try {
            JSONObject obj =new JSONObject(LoadJsonFromAsset());
            JSONArray array =new JSONArray("Datasource");
            HashMap<String,String> list =new HashMap<>();
            ArrayList<HashMap<String, String>> arrayList =new ArrayList<>();
            for (int i=0;i<array.length();i++){
                JSONObject o =array.getJSONObject(i);
                String dt =o.getString("dataType");
                int v = o.getInt("value");
                list.put("dataType" , dt);
                list.put("value",String.valueOf(v));
                arrayList.add(list);
                textResult.append((CharSequence) arrayList);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }    }
}

