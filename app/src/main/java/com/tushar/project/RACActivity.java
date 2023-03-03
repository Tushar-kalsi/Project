package com.tushar.project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.tushar.project.databinding.ActivityRacactivityBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RACActivity extends AppCompatActivity  {
    public static final int STUDENT_VIEW=1;
    public static final int TEACHER_VIEW=2;
    public static final int HOD_VIEW=3;

    public static final String KEY_RAC_STUDENT_FORM="rac_data";

    private static final int PICK_FILE = 1;
    PdfRenderer pdfRenderer;
    ActivityRacactivityBinding binding;
    RequestQueue requestQueue ;
    Uri bitmap;
    boolean imageSelected=false;
    SharedPreferences preferences;
    StorageReference storageRef;
    CustomDialog customDialog;
    int view=STUDENT_VIEW;
    String date;
    final Calendar myCalendar= Calendar.getInstance();
    String dateSelected="";

    public static final String KEY_RAC_DATA="data_rac";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this , R.layout.activity_racactivity);
        customDialog=new CustomDialog(this , "Loading ");
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://project-44332.appspot.com");
        storageRef = storage.getReference();

        requestQueue= Volley.newRequestQueue(this);
        preferences= PreferenceManager.getDefaultSharedPreferences(getApplication());



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        date = sdf.format(c.getTime());

        view=preferences.getInt("type", STUDENT_VIEW);


        if(view==TEACHER_VIEW) {
            customDialog = new CustomDialog(this, "Loading Please wait ....");
            Intent intent = getIntent();

            String gsonData=intent.getStringExtra(KEY_RAC_DATA);
            Gson gson = new Gson();

            RacDataModel user = gson.fromJson(gsonData, RacDataModel.class);
            binding.enrollmentNumberInput.setText(user.enrollment_number);

            binding.titletext.setText("RAC Information");
            binding.enrollmentNumberInput.setEnabled(false);
            binding.uploadButton.setText("View Document");
            binding.dateOfRegistration.setEnabled(false);
            binding.batchInput.setEnabled(false);
            binding.button4.setText("Back");
            binding.button4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });


            binding.nameInput.setText(user.name);
            binding.enrollmentNumberInput.setText(user.enrollment_number);
            binding.dorinput.setText(user.dorDate);
            binding.batchInput.setText(user.batch);
            String supervisor=user.supervisor;
            String coSuperVisor=user.coSuperVisor;
            String documentLink=user.documentLink;
            int hodDoc=user.hodDoc;
            String hodDocUrl=user.hodDocUrl;
            String departmentName =user.departmentName;

            if( hodDoc==1){

                binding.documentUploadedByHod.setVisibility(View.VISIBLE);

                binding.documentUploadedByHod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hodDocUrl));
                            startActivity(browserIntent);
                        }catch (Exception e){

                        }
                    }
                });
            }

            binding.dorinput.setText(user.dorDate);


            if (!supervisor.equals("null"))
                binding.superVisorTextView.setText(supervisor);
            if (!coSuperVisor.equals("null"))
                binding.coSuperVisorSpinnerTextview.setText(coSuperVisor);

            if(!departmentName.equals("null")){
                binding.departmentSpinnerTextView.setText(departmentName);

            }


            binding.uploadButton.setText("View Document");

            binding.uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    try{


                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentLink));
                        startActivity(browserIntent);

                    }
                    catch (Exception e){

                        Log.d("errorVolley", "clicked "+documentLink);

                    }
                }
            });

          //  makeRacCall(enNumnber);


        }
        else if ( view==HOD_VIEW){


            customDialog=new CustomDialog(this, "Uploading Image ...." );
            SetDate setDate=new SetDate(binding.dorinput , this);
            String name =preferences.getString("name", "");

        binding.nameInput.setText(name);
        String enrollment_number =preferences.getString("enrollment_number","");





        binding.enrollmentNumberInput.setText(enrollment_number);
        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                accessPdf();

            }
        });


        binding.button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(binding.enrollmentNumberInput.getText()==null || binding.enrollmentNumberInput.getText().toString().trim().length()==0){

                    binding.enrollmentLayout.setError("Invalid Enrollment Number");
                }else{

                    if(binding.dorinput.getText()==null || binding.dorinput.getText().toString().trim().length()==0){
                        binding.dorlayout.setError("Invalid DOR ");

                    }
                    else{

                      if(binding.batchInput.getText()==null || binding.batchInput.getText().toString().trim().length()==0){
                          binding.batchLayout.setError("Invalid batch");
                      }else{

                          if(bitmap==null ){
                              binding.uploadButton.setError("Upload Image");

                          }else{
                              String batch=binding.batchInput.getText().toString().trim();
                              String enrollmentNumber =binding.enrollmentNumberInput.getText().toString().trim();


                              if(dateSelected!=null && dateSelected.length()>0)
                                uploadRac(batch, enrollmentNumber, dateSelected);
                              else{
                                  binding.dorlayout.setError("Please select date");
                              }


                          }

                      }
                    }
                }
            }
        });

            if(view==HOD_VIEW){
                binding.enrollmentNumberInput.setEnabled(true );
                binding.enrollmentNumberInput.setText("");
                binding.nameInput.setText("");
                binding.nameInput.setEnabled(true);

            }



        }



        else if(view==STUDENT_VIEW){

            Intent intent=getIntent();

            String gsonData=intent.getStringExtra(KEY_RAC_STUDENT_FORM);
            Gson gson = new Gson();

            RacDataModel user = gson.fromJson(gsonData, RacDataModel.class);



            DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH,month);
                    myCalendar.set(Calendar.DAY_OF_MONTH,day);

                    if(month<=9){
                        dateSelected=year+"/0"+month+"/"+day;
                    }
                    if(day<=9){
                        dateSelected=year+"/0"+month+"/0"+day;
                    }
                    if(month>9 || day>9) {
                        dateSelected = year + "/" + month + "/" + day;
                    }


                    binding.dorinput.setText(dateSelected);

                }
            };




            String name =preferences.getString("name", "");
            binding.nameInput.setText(name);
            String enrollment_number =preferences.getString("enrollment_number","");


            binding.enrollmentNumberInput.setText(enrollment_number);

            if(user!=null){


            binding.nameInput.setText(user.name);
            binding.enrollmentNumberInput.setText(user.enrollment_number);
            binding.dorinput.setText(user.dorDate);
            binding.batchInput.setText(user.batch);
            String supervisor=user.supervisor;
            String coSuperVisor=user.coSuperVisor;
            String documentLink=user.documentLink;
            int hodDoc=user.hodDoc;
            String hodDocUrl=user.hodDocUrl;
            String departmentName =user.departmentName;

            if( hodDoc==1){

                binding.documentUploadedByHod.setVisibility(View.VISIBLE);

                binding.documentUploadedByHod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hodDocUrl));
                            startActivity(browserIntent);
                        }catch (Exception e){

                        }
                    }
                });
            }

            binding.dorinput.setText(user.dorDate);


                if (!supervisor.equals("null"))
                    binding.superVisorTextView.setText(supervisor);
                if (!coSuperVisor.equals("null"))
                    binding.coSuperVisorSpinnerTextview.setText(coSuperVisor);

                if(!departmentName.equals("null")){
                    binding.departmentSpinnerTextView.setText(departmentName);

                }


            binding.uploadButton.setText("View Document");

            binding.uploadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    try{


                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentLink));
                        startActivity(browserIntent);

                    }
                    catch (Exception e){

                        Log.d("errorVolley", "clicked "+documentLink);
                    }
                }
            });




        }else{

                binding.dorlayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new DatePickerDialog(RACActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                    }
                });

                binding.dorinput.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Log.d("errorWq", "clicked");

                        new DatePickerDialog(RACActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                    }
                });

                binding.uploadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        accessPdf();

                    }
                });

                binding.button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if(binding.enrollmentNumberInput.getText()==null || binding.enrollmentNumberInput.getText().toString().trim().length()==0){

                            binding.enrollmentLayout.setError("Invalid Enrollment Number");
                        }else{

                            if(binding.dorinput.getText()==null || binding.dorinput.getText().toString().trim().length()==0){
                                binding.dorlayout.setError("Invalid DOR ");

                            }
                            else{

                                if(binding.batchInput.getText()==null || binding.batchInput.getText().toString().trim().length()==0){
                                    binding.batchLayout.setError("Invalid batch");
                                }else{

                                    if(bitmap==null ){
                                        binding.uploadButton.setError("Upload Image");

                                    }else{
                                        String batch=binding.batchInput.getText().toString().trim();
                                        String enrollmentNumber =binding.enrollmentNumberInput.getText().toString().trim();


                                        if(dateSelected!=null && dateSelected.length()>0)
                                            uploadRac(batch, enrollmentNumber, dateSelected);
                                        else{
                                            binding.dorlayout.setError("Please select date");
                                        }


                                    }

                                }
                            }
                        }
                    }
                });


            }

        }



    }


    private void uploadRac(String batch, String enrollmentNumber , String DOR) {

        customDialog.startDialog();

//        Uri file = Uri.fromFile(new File(bitmap.toString()));

        StorageReference riversRef = storageRef.child("racUpload/"+enrollmentNumber+".pdf");


        riversRef.putFile(bitmap).continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    // After uploading is done it progress
                    // dialog box will be dismissed

                    Uri uri = task.getResult();
                    String myurl;
                    myurl = uri.toString();

                    uploadRACToServer(myurl , batch, enrollmentNumber, DOR );
                } else {
                    customDialog.endDialog();

                    Toast.makeText(RACActivity.this, "UploadedFailed ", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    public void uploadRACToServer(String downloadUrl, String batch, String enrollmentNumber, String DOR){



        String URL=getString(R.string.domain_url)+"rac ";
        try {

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("DOR", dateSelected);
            jsonBody.put("enrollment_number", enrollmentNumber);
            jsonBody.put("batch", batch);
            jsonBody.put("document", downloadUrl);
            jsonBody.put("date", date);




            final String requestBody = jsonBody.toString();
            Log.d("request", requestBody+" form saving");


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    customDialog.endDialog();

                    try {
                        JSONObject myJsonObject = new JSONObject(response);

                        boolean success= myJsonObject.optBoolean("success");
                        String message =myJsonObject.optString("message ");
                        if(success){
                            Toast.makeText(RACActivity.this , "Success: "+message, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RACActivity.this, home.class) );
                            finish();
                        }else{
                            Toast.makeText(RACActivity.this , "Error", Toast.LENGTH_LONG).show();

                        }

                    }
                    catch (Exception e){


                    }





                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.endDialog();


                    Toast.makeText(RACActivity.this , "Error:"+error.toString(), Toast.LENGTH_LONG).show();


                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> params = new HashMap<>();

                    return params;
                }
                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);

                        Log.d("response code",responseString);
                        // can get more details such as response.headers
                    }
                    return super.parseNetworkResponse(response);
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                    2,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }





    }

    private void makeRacCall(String enNumnber) {

        String url =getString(R.string.domain_url)+"rac?en="+enNumnber;
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
                            JSONArray data =myJsonObject.optJSONArray("results1");

                            if(success){


                                for(int i=0;i<data.length();i++){

                                    JSONObject jsonObject=data.getJSONObject(i);

                                    binding.nameInput.setText(jsonObject.optString("name"));
                                    binding.enrollmentNumberInput.setText(jsonObject.optString("EN"));
                                    binding.dorinput.setText(jsonObject.optString("DOR"));
                                    binding.batchInput.setText(jsonObject.optString("Batch"));
                                    String supervisor=jsonObject.optString("SuperVisor");
                                    String coSuperVisor=jsonObject.optString("CoSupervisor");
                                    String documentLink=jsonObject.optString("Document");
                                    int hodDoc=jsonObject.optInt("HodDocumentInserted");
                                    String hodDocUrl=jsonObject.optString("HODDocument");
                                    String departmentName =jsonObject.optString("DepartmentName");

                                    if(view==STUDENT_VIEW && hodDoc==1){

                                        binding.documentUploadedByHod.setVisibility(View.VISIBLE);

                                        binding.documentUploadedByHod.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                try {
                                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(hodDocUrl));
                                                    startActivity(browserIntent);
                                                }catch (Exception e){

                                                }
                                            }
                                        });
                                    }

                                    binding.dorinput.setText(jsonObject.optString("DOR"));

                                    if(view==TEACHER_VIEW || view==STUDENT_VIEW) {
                                        if (!supervisor.equals("null"))
                                            binding.superVisorTextView.setText(supervisor);
                                        if (!coSuperVisor.equals("null"))
                                            binding.coSuperVisorSpinnerTextview.setText(coSuperVisor);

                                        if(!departmentName.equals("null")){
                                            binding.departmentSpinnerTextView.setText(departmentName);

                                        }

                                    }else{

                                        binding.button4.setVisibility(View.VISIBLE);

                                    }
                                    binding.uploadButton.setText("View Document");

                                    binding.uploadButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.d("errorVolley", "clicked "+documentLink);

                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(documentLink));
                                            startActivity(browserIntent);

                                        }
                                    });





                                }


                            }

                        }
                        catch (Exception e){

                            Log.d("errorVolley", "Inside exeption "+e.toString());
                        }

                        customDialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RACActivity.this , "There is some error ", Toast.LENGTH_LONG).show();

                customDialog.endDialog();


            }
        });

// Add the request to the RequestQueue.
        requestQueue.add(stringRequest);
    }

    public void accessPdf(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_FILE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE && resultCode == RESULT_OK){
            if (data != null){
                Uri uri = data.getData();
                Log.d("errorVolley", "uri data "+uri.toString());
                bitmap=uri;

                try {
                    ParcelFileDescriptor parcelFileDescriptor = getContentResolver()
                            .openFileDescriptor(uri, "r");
                    pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                    int total_pages = pdfRenderer.getPageCount();
                    int display_page = 0;
                    _display(display_page);
                } catch (FileNotFoundException fnfe){

                } catch (IOException e){

                }
            }
        }

    }
    private void _display(int _n) {
        if (pdfRenderer != null) {
            PdfRenderer.Page page = pdfRenderer.openPage(_n);
            Bitmap mBitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
            page.render(mBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            // imageview1.setImageBitmap(mBitmap);
            page.close();

            //textview1.setText((_n + 1) + "/" + total_pages);
        }
    }



}