package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
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


        int type=sharedPreferences.getInt("type", 1) ;

        if(type==2){
            customDialog=new CustomDialog(this , "Loading, Please wait .... ");

            Intent intent=getIntent();

            String en=intent.getStringExtra("en");

            getPublicationDetails(en);

            binding.titletext.setText("View Publication");
            binding.button4.setText("Back");
            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    finish();

                }
            });

        }else{
            String name=sharedPreferences.getString("name", " ");
            binding.studentNmaeInput.setText(name);
            enrollment_number=sharedPreferences.getString("enrollment_number"," ");
            getPublicationDetails(enrollment_number);
        }




    }

    public void getPublicationDetails(String enrollment_number){

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
                            String document ;

                            if(success){

                                if(data!=null ){
                                    binding.titleNameInput.setText(data.optString("title"));
                                    binding.enrollmentInout.setText (data.optString("EN"));
                                    binding.journalInput.setText (data.optString("journal"));
                                    binding.dateofpublicatoinInput.setText(data.optString("dop"));
                                    binding.typeSpinner.setText( data.optString("type"));
                                    binding.enrollmentInout.setText(data.optString("en"));
                                    binding.studentNmaeInput.setText(data.optString("name"));
                                   document= data.optString("doc");

                                   if(!document.equals("null") && document.length()>0){

                                       binding.uploadButton.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {

                                               Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(document));
                                               startActivity(browserIntent);


                                           }
                                       });
                                   }

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