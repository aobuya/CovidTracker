package com.example.covidtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    TextView confirmed,recovered,deaths,last_checked;
    Double confirmeds;
    Double recovers;
    Double d_count;
    String dates;
    Toolbar toolbar;
    EditText editText;
    ShimmerFrameLayout shimmerFrameLayouts;
    ProgressBar progressBar;
    String load = "loading";
    CoordinatorLayout linearLayout;


    FloatingActionButton search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        confirmed = findViewById(R.id.confirmed);
        recovered= findViewById(R.id.rec);
        deaths = findViewById(R.id.d_count);
        //last_checked = findViewById(R.id.dates);
        toolbar = findViewById(R.id.toolbar);
        search= findViewById(R.id.search_icon);
        editText = findViewById(R.id.edittext);
        linearLayout = findViewById(R.id.coordinator);
        last_checked = findViewById(R.id.time_stamp);



        progressBar =findViewById(R.id.progress_circular);
        setSupportActionBar(toolbar);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = editText.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);
                confirmed.setText(load);
                recovered.setText(load);
                deaths.setText(load);


                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("https://covid-19-coronavirus-statistics.p.rapidapi.com/v1/total?country="+city)
                        .get()
                        .addHeader("x-rapidapi-host", "covid-19-coronavirus-statistics.p.rapidapi.com")
                        .addHeader("x-rapidapi-key", "b58e87fd4amshdcaa34606dbcc25p11150cjsn14365c24c008")
                        .build();
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll()
                        .build();
                StrictMode.setThreadPolicy(policy);

                try {
                    Response response = client.newCall(request).execute();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Snackbar.make(linearLayout, "Something is not  right", Snackbar.LENGTH_LONG).
                                    setAction("Dismiss", null).show();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            final String json = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                JSONObject data = jsonObject.getJSONObject("data");
                                confirmeds = data.getDouble("confirmed");
                                //recovered
                                recovers = data.getDouble("recovered");
                                //deaths
                                d_count = data.getDouble("deaths");
                                //time
                                dates = data.getString("lastReported");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    confirmed.setText(String.valueOf(confirmeds));
                                    recovered.setText(String.valueOf(recovers));
                                    deaths.setText(String.valueOf(d_count));
                                    last_checked.setText(String.valueOf(dates));
                                    progressBar.setVisibility(View.GONE);

                                }
                            });


                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }

}
