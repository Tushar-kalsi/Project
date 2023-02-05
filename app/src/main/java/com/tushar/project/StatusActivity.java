package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tushar.project.databinding.ActivityStatusBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatusActivity extends AppCompatActivity {

    List<StatusModel> dataList;
    StatusRecyclerViewAdapter adapter;
    ActivityStatusBinding binding;
    CustomDialog dialog;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this , R.layout.activity_status);
        dialog=new CustomDialog(this , "Fetching data ........");
        dataList=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(this);
        adapter=new StatusRecyclerViewAdapter(dataList, this);

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        makeApiCall("qw");


    }
    public void makeApiCall(String en ){

        String url = getString(R.string.domain_url) + "rac/approve/status";



        dialog.startDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject myJsonObject = new JSONObject(response);

                            boolean success = myJsonObject.optBoolean("success");
                            JSONArray data = myJsonObject.optJSONArray("results1");

                            if (success && data != null) {

                                for (int i = 0; i < data.length(); i++) {

                                    JSONObject obj = data.getJSONObject(i);
                                    StatusModel statusModel=new StatusModel();

                                    statusModel.setCoursework(obj.optInt("coursework"));
                                    statusModel.setPublication(obj.optInt("publication"));
                                    statusModel.setRac(obj.optInt("rac"));
                                    statusModel.setEnrollment_number(obj.optString("EN"));
                                    statusModel.setName(obj.optString("name"));
                                    statusModel.setRdc(obj.optInt("rdc"));
                                    statusModel.setMarksheet(obj.optInt("marksheet"));
                                    statusModel.setPredefenceLetter(obj.optInt("pdl"));
                                    statusModel.setThesisSubmission(obj.optInt("thesissub"));
                                    statusModel.setThesisAwarded(obj.optInt("thesisawa"));
                                    statusModel.setSynopsis(obj.optInt("synopsis"));
                                    statusModel.setTitle(obj.optString("title"));


                                    dataList.add(statusModel);


                                }

                                adapter.notifyDataSetChanged();


                            }

                        } catch (Exception e) {


                        }

                        dialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorVolley", error.toString().trim());
                Toast.makeText(StatusActivity.this, "There is some error ", Toast.LENGTH_LONG).show();

                dialog.endDialog();


            }
        });

        requestQueue.add(stringRequest);

    }
}