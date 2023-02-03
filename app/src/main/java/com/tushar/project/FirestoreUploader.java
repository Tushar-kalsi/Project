package com.tushar.project;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirestoreUploader {

    StorageReference storageReference;

    public FirestoreUploader(StorageReference storageReference){

        this.storageReference=storageReference;

    }

    public Task uploadFile(Uri uri, String enrollmentNumber, String type){

        StorageReference riversRef = storageReference.child("uploads/"+type+"/"+enrollmentNumber+".pdf");

       return  riversRef.putFile(uri).continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull Task task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        });


    }

}
