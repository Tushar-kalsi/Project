package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tushar.project.databinding.ActivityViewPublicationBinding;

import org.json.JSONObject;

import java.util.Optional;

public class ViewPublication extends AppCompatActivity {
    ActivityViewPublicationBinding binding;
    SharedPreferences sharedPreferences;
    RequestQueue requestQueue;
    CustomDialog customDialog;
    String enrollment_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this , R.layout.activity_view_publication);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplication());
        requestQueue= Volley.newRequestQueue(getApplication());
        customDialog= new CustomDialog(this , "Fetching record ......");
        enrollment_number=sharedPreferences.getString("enrollment_number"," ");

        getPublicationDetails();



    }

    public void getPublicationDetails(){

        String url =getString(R.string.domain_url)+"publication?en="+enrollment_number;
        Log.d("errorVolley", url);
        customDialog.startDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject myJsonObject = new JSONObject(response);

                            boolean success= myJsonObject.optBoolean("success");
                            JSONObject data =myJsonObject.optJSONObject("message");

                            if(success){

                                if(data!=null ){
                                    binding.titleNameInput.setText(data.optString("title"));
                                    binding.enrollmentInout.setText (data.optString("EN"));
                                    binding.journalInput.setText (data.optString("journal"));
                                    binding.dateofpublicatoinInput.setText(data.optString("dop"));
                                    binding.typeSpinner.setPrompt( data.optString("type"));
                                    data.optString("doc");

                                }
                            }

                        }
                        catch (Exception e){


                        }

                        customDialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewPublication.this , "There is some error ", Toast.LENGTH_LONG).show();

                customDialog.endDialog();


            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);

    }
}