package com.tushar.project;



import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.tushar.project.databinding.ActivityRachodverifyBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HOD_racVerifyActivity extends AppCompatActivity implements HodClickListener{

    public static final int DENY_COMMOND=1;
    public static final int APPROVE_COMMOND=2;
    public static final int DOCUMENT_COMMOND=3;

    ActivityRachodverifyBinding binding;
    RequestQueue requestQueue;
    int viewType;
    SharedPreferences sharedPreferences;
    List<StudentModel> dataList;
    CustomDialog dialog;
    HOD_DOC_verifyAdapter adapterRecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this , R.layout.activity_rachodverify);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplication());
        viewType=sharedPreferences.getInt("type", 1);

        requestQueue= Volley.newRequestQueue(this);
        dataList=new ArrayList<StudentModel>();

        dialog=new CustomDialog(this , "Loading , Please wait .......");

        Intent intent=getIntent();


        adapterRecyclerview=new HOD_DOC_verifyAdapter(dataList,this );
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapterRecyclerview);


        makeRacStudentCall();


    }

    public void makeRacStudentCall() {

        String url = getString(R.string.domain_url) + "getallstudents?type=rac";


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
                                    StudentModel studentModel = new StudentModel();
                                    studentModel.setType(StudentModel.RACMODEL);
                                    studentModel.setFullName(obj.optString("name"));
                                    studentModel.setEN(obj.optString("EN"));
                                    studentModel.setDOR(obj.optString("DOR"));
                                    studentModel.setBatch(obj.optString("Batch"));
                                    studentModel.setSuperVisor(obj.optString("SuperVisor"));
                                    studentModel.setCoSuperVisor(obj.optString("CoSupervisor"));
                                    studentModel.setDocument(obj.optString("Document"));


                                    dataList.add(studentModel);


                                }

                                adapterRecyclerview.notifyDataSetChanged();


                            }

                        } catch (Exception e) {


                        }

                        dialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorVolley", error.toString().trim());
                Toast.makeText(HOD_racVerifyActivity.this, "There is some error ", Toast.LENGTH_LONG).show();

                dialog.endDialog();


            }
        });

        requestQueue.add(stringRequest);


    }
    @Override
    public void clicked ( int type, StudentModel studentModel){

        if(type==DOCUMENT_COMMOND){
            Log.d("kacb", "clicked ");
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(studentModel.getDocument()));
            startActivity(browserIntent);

        }else if (type==DENY_COMMOND){

            finish();

            
        }else{

            finish();

        }
    }

}