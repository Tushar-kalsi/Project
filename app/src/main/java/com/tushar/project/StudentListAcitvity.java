package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.gson.Gson;
import com.tushar.project.databinding.ActivityStudentListBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class StudentListAcitvity extends AppCompatActivity  implements RecyclerViewButtonClickListener , PastRACDialog.MoveToRacModelListener{


    public static final int QIP_STUDENT=1;
    public static final int NONQIP_STUDENT=2;
    public static final int RAC_STUDENT=3;
    public static final int COURSE_WORK=4;
    public static final int PUBLICARION=5;
    public static final int HODVIEW=3;
    public static final int VIEWDOCUMENTS=8;


    String type ="2";

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



        String url=getString(R.string.domain_url)+"upload?param=all";

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
                                    studentModel.setLastName(" ");
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

           // Intent intent=new Intent(this , RACActivity.class);
            type="3";
            makeApiCallForViewingRacOfParticularStudent(studentModel.getEN());

            //intent.putExtra("type", 3);
            //intent.putExtra("en", studentModel.getEN());
            //startActivity(intent);

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




            type="2";

            makeApiCallForViewingRacOfParticularStudent(studentModel.getEN());



        }else if (val_type==7){


            type="3";
            makeApiCallForViewingRacOfParticularStudent(studentModel.getEN());




            makeApiCallForViewingRacOfParticularStudent(studentModel.getEN());


        }

        else if (val_type==VIEWDOCUMENTS){

            Intent intent=new Intent(this , StudentDocumentUplaod.class);

            intent.putExtra("type", 2);
            intent.putExtra("en", studentModel.getEN());

            startActivity(intent);

        }

    }

    private void makeApiCallForViewingRacOfParticularStudent(String en) {

        String url =getString(R.string.domain_url)+"rac?en="+en;
        Log.d("errorVolley", url);
        dialog.startDialog();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject myJsonObject = new JSONObject(response);

                            boolean success= myJsonObject.optBoolean("success");
                            JSONArray data =myJsonObject.optJSONArray("results1");

                            if(success){

                                List<RacDataModel> dataModelList=new ArrayList<>();


                                for(int i=0;i<data.length();i++){
                                    RacDataModel racDataModel=new RacDataModel();


                                    JSONObject jsonObject=data.getJSONObject(i);

                                    String name= (jsonObject.optString("name"));
                                    String enrollment_number=(jsonObject.optString("EN"));
                                    String dorDate = (jsonObject.optString("DOR"));
                                    String batch=(jsonObject.optString("Batch"));
                                    String supervisor=jsonObject.optString("SuperVisor");
                                    String coSuperVisor=jsonObject.optString("CoSupervisor");
                                    String documentLink=jsonObject.optString("Document");
                                    int hodDoc=jsonObject.optInt("HodDocumentInserted");
                                    String hodDocUrl=jsonObject.optString("HODDocument");
                                    String departmentName =jsonObject.optString("DepartmentName");

                                    racDataModel.setBatch(batch);
                                    racDataModel.setName(name);
                                    racDataModel.setEnrollment_number(enrollment_number);
                                    racDataModel.setDorDate(dorDate);
                                    racDataModel.setSupervisor(supervisor);
                                    racDataModel.setCoSuperVisor(coSuperVisor);
                                    racDataModel.setDocumentLink(documentLink);
                                    racDataModel.setHodDoc(hodDoc);
                                    racDataModel.setHodDocUrl(hodDocUrl);
                                    racDataModel.setDepartmentName(departmentName);
                                    dataModelList.add(racDataModel);

                                }


                                JSONObject jsonObject= (JSONObject) data.getJSONObject(data.length()-1);
                                String dorLast= jsonObject.optString("DOR");

                                Log.d("errorWq", "insisder here " );
                                String nextDate= calculcateNextRacDate(dorLast);



                               setDialog(dataModelList, nextDate);





                            }

                        }
                        catch (Exception e){

                            Log.d("errorVolley", "Inside exeption "+e.toString());
                        }

                        dialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentListAcitvity.this , "There is some error ", Toast.LENGTH_LONG).show();

                dialog.endDialog();


            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);


    }

    private String calculcateNextRacDate(String dorLast) {



        String dateView="";


        try{

            Log.d("errorWq", "date is"+dorLast);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Instant s = Instant.parse(dorLast);
                ZoneId.of("Asia/Kolkata");
                LocalDateTime l = LocalDateTime.ofInstant(s, ZoneId.of("Asia/Kolkata")).plusMonths(6);

                String date =l.getDayOfMonth()+"/"+l.getMonthValue()+"/"+l.getYear();
                Log.d("errorWq", "date is"+date);
               dateView= date;




            }







        }catch (Exception e){

            Log.d("errorWq", "excetion found" +e.toString());


        }

        return dateView;


    }



    private void setDialog(List<RacDataModel> dataModelList, String nextDate) {

        PastRACDialog pastRACDialog=new PastRACDialog(StudentListAcitvity.this , "Loading ", this , dataModelList, false );

        pastRACDialog.startDialog();


        pastRACDialog.setNextRacDate(nextDate);


    }

    @Override
    public void moveToRacModel(RacDataModel racDataModel) {


        if (viewType!=HODVIEW){

            Intent intent=new Intent(this , RACActivity.class);
            String json=new Gson().toJson(racDataModel);
            intent.putExtra(RACActivity.KEY_RAC_DATA,json );

            intent.putExtra("type", type);
            intent.putExtra("en", racDataModel.enrollment_number);
            startActivity(intent);


        }

        else{


            Intent intent=new Intent(this , RAC_HOD.class);
            intent.putExtra("en", racDataModel.enrollment_number);
            intent.putExtra("ty", type);

            intent.putExtra(RAC_HOD.KEY_DATA, new Gson().toJson(racDataModel));

            startActivity(intent);

        }


    }
}