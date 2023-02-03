package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.tushar.project.databinding.ActivityStudentListBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StudentListAcitvity extends AppCompatActivity  implements RecyclerViewButtonClickListener {


    public static final int QIP_STUDENT=1;
    public static final int NONQIP_STUDENT=2;
    public static final int RAC_STUDENT=3;
    public static final int COURSE_WORK=4;
    public static final int PUBLICARION=5;
    public static final int HODVIEW=3;
    public static final int VIEWDOCUMENTS=8;


    CustomDialog dialog;
    RequestQueue requestQueue;
    List<StudentModel> dataList;
    ActivityStudentListBinding binding;
    AdapterRecyclerview adapterRecyclerview;
    int val_type;
    SharedPreferences sharedPreferences;
    int viewType=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this , R.layout.activity_student_list);

        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getApplication());
        viewType=sharedPreferences.getInt("type", 1);

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
        }
        else if(val_type==7){


            makeRacStudentCall();

        }
        else if(val_type==RAC_STUDENT){


            makeRacStudentCall();

        }else if (val_type==COURSE_WORK){

            makeCourseWorkCall();

        }else if (val_type==PUBLICARION){

            makePublicationCall();

        }else if (val_type==VIEWDOCUMENTS){

            makeApiCallForViewDocuments();

        }



    }

    public void makeApiCallForViewDocuments(){



        String url=getString(R.string.domain_url)+"upload";

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
                                    Log.d("errorVolley", "data "+obj.toString());

                                    StudentModel studentModel=new StudentModel();
                                    studentModel.setType(StudentModel.COURSEWORK);
                                    studentModel.setEN(obj.optString("EN"));
                                    studentModel.setFirstName(obj.optString("name"));
                                    studentModel.setMarksheetUrl(obj.optString("marksheet"));
                                    studentModel.setRdcUrl(obj.optString("rdc"));
                                    studentModel.setPdlUrl(obj.optString("pdl"));
                                    studentModel.setThesisawa(obj.optString("thesisawa"));
                                    studentModel.setThesisub(obj.optString("thesisub"));
                                    studentModel.setSynopsis(obj.optString("synopsis"));
                                    studentModel.setTitle(obj.optString("title"));



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

    public void makePublicationCall(){


        String url=getString(R.string.domain_url)+"getallstudents?type=publication";


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
                                    Log.d("errorVolley", "data "+obj.toString());

                                    StudentModel studentModel=new StudentModel();
                                    studentModel.setType(StudentModel.COURSEWORK);
                                    studentModel.setFirstName(obj.optString("name"));
                                    studentModel.setLastName(" ");
                                    studentModel.setJournal(obj.optString("journal"));
                                    studentModel.setDop(obj.optString("dop"));
                                    studentModel.setEN(obj.optString("EN"));
                                    studentModel.setDocument(obj.optString("Document"));


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
    public void makeCourseWorkCall(){
        String url=getString(R.string.domain_url)+"getallstudents?type=coursework";


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
                                    Log.d("errorVolley", "data "+obj.toString());

                                    StudentModel studentModel=new StudentModel();
                                    studentModel.setType(StudentModel.COURSEWORK);
                                    studentModel.setFirstName(obj.optString("name"));
                                    studentModel.setLastName(" ");

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
    public void makeRacStudentCall(){

        String url=getString(R.string.domain_url)+"getallstudents?type=rac";


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
                                    studentModel.setType(StudentModel.RACMODEL);
                                    studentModel.setFullName(obj.optString("name"));
                                    studentModel.setEN(obj.optString("EN"));
                                    studentModel.setDOR(obj.optString("DOR"));
                                    studentModel.setBatch(obj.optString("Batch"));


                                    studentModel.setSuperVisor(obj.optString("SuperVisor"));
                                    studentModel.setCoSuperVisor(obj.optString("CoSupervisor"));
                                    studentModel.setDocument(obj.optString("Document"));

                                    if(viewType==3 && val_type!=7 && !obj.optString("SuperVisor").equals("null")&& !obj.optString("CoSupervisor").equals("null")){

                                        continue;

                                    }

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
                                    studentModel.setUsername(obj.optString("UserName"));
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
            Log.d("errorVolley", "username us "+studentModel.getUsername());

            startActivity(intent);

        }else if(val_type==NONQIP_STUDENT){
            Intent intent=new Intent(this , NONQIP_registration.class);
            intent.putExtra("username", studentModel.getUsername());
            startActivity(intent);

        }else if(val_type==RAC_STUDENT && viewType!=HODVIEW){

            Intent intent=new Intent(this , RACActivity.class);
            intent.putExtra("type", 3);
            intent.putExtra("en", studentModel.getEN());
            startActivity(intent);

        }else if(val_type==COURSE_WORK){


            Intent intent=new Intent(this , CourseWork.class);
            intent.putExtra("type", 4);

            intent.putExtra("en", studentModel.getEN());
            Log.d("kacb", "here "+studentModel.getEN());
            startActivity(intent);

        }else if(val_type==PUBLICARION){

            Intent intent=new Intent(this , ThesisSubmission.class);
            intent.putExtra("type", 5);
            intent.putExtra("en", studentModel.getEN());
            startActivity(intent);


        }else if (val_type==RAC_STUDENT && viewType==HODVIEW){


            Intent intent=new Intent(this , RAC_HOD.class);
            intent.putExtra("en", studentModel.getEN());
            intent.putExtra("ty", "2");
            Log.d("ianscvla", "inside this on "+studentModel.getEN());
            startActivity(intent);

        }else if (val_type==7){

            Intent intent=new Intent(this , RAC_HOD.class);
            intent.putExtra("en", studentModel.getEN());
            intent.putExtra("ty", "3");

            startActivity(intent);


        }

        else if (val_type==VIEWDOCUMENTS){

            Intent intent=new Intent(this , StudentDocumentUplaod.class);

        }

    }
}