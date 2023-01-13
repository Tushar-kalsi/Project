package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tushar.project.databinding.ActivityStudentListBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentListAcitvity extends AppCompatActivity  implements RecyclerViewButtonClickListener{


    public static final int QIP_STUDENT=1;
    public static final int NONQIP_STUDENT=2;
    public static final int RAC_STUDENT=3;

    CustomDialog dialog;
    RequestQueue requestQueue;
    List<StudentModel> dataList;
    ActivityStudentListBinding binding;
    AdapterRecyclerview adapterRecyclerview;
    int val_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this , R.layout.activity_student_list);

        requestQueue= Volley.newRequestQueue(this);
        dataList=new ArrayList<>();

        dialog=new CustomDialog(this , "Loading , Please wait .......");

        Intent intent=getIntent();
        val_type= intent.getIntExtra("student", 1);

        adapterRecyclerview=new AdapterRecyclerview(dataList,this );
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapterRecyclerview);



        if(val_type==QIP_STUDENT){

            makeQIPApiCall(QIP_STUDENT);

        }else if(val_type==NONQIP_STUDENT){

            makeQIPApiCall(NONQIP_STUDENT);
        }else if(val_type==RAC_STUDENT){


            makeRacStudentCall();

        }



    }
    public void makeRacStudentCall(){


    }

    public void makeQIPApiCall(int type){

        String url=getString(R.string.domain_url)+"getallstudents?type=";
        if(type==QIP_STUDENT){

            url+="qip";

        }else{
            url+="nonqip";
        }


        dialog.startDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject myJsonObject = new JSONObject(response);

                            boolean success= myJsonObject.optBoolean("success");
                            JSONArray  data =myJsonObject.optJSONArray("results1");

                            if(success && data!=null){

                                for(int i =0;i< data.length();i++){

                                    JSONObject obj=data.getJSONObject(i) ;

                                    StudentModel studentModel=new StudentModel();
                                    studentModel.setType(type);

                                    studentModel.setId(obj.optInt("Id"));
                                    studentModel.setAddress(obj.optString("Address"));
                                    studentModel.setDOB(obj.optString("DOB"));
                                    studentModel.setFatherName(obj.optString("FatherName"));
                                    studentModel.setDepartmentNumber(obj.optString("DeptNumber"));
                                    studentModel.setFirstName(obj.optString("FirstName"));
                                    studentModel.setLastName(obj.optString("LastName"));
                                    studentModel.setLastName(obj.optString("UserName"));
                                    studentModel.setEN(obj.optString("EN"));

                                    dataList.add(studentModel);


                                }

                                adapterRecyclerview.notifyDataSetChanged();





                            }

                        }
                        catch (Exception e){


                        }

                        dialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorVolley", error.toString().trim());
                Toast.makeText(StudentListAcitvity.this , "There is some error ", Toast.LENGTH_LONG).show();

                dialog.endDialog();


            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);


    }


    @Override
    public void onClick(StudentModel studentModel) {

        if(val_type==QIP_STUDENT){
            Intent intent=new Intent(this , QIP_registration.class);
            intent.putExtra("username", studentModel.getUsername());
            startActivity(intent);

        }else{
            Intent intent=new Intent(this , NONQIP_registration.class);
            intent.putExtra("username", studentModel.getUsername());
            startActivity(intent);

        }

    }
}