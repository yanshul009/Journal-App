package com.example.self;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.Date;
import java.util.Objects;

import model.Journal;
import util.JournalApi;

public class Postjournal extends AppCompatActivity implements View.OnClickListener {

    private Button savebtn;
    private EditText title;
    private EditText thought;
    private ProgressBar bar;
    private ImageView imageView;
    private TextView curruser;
    private ImageView backimg;

    private String curruserid;
    private String currusername;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;


    private FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference=firebaseFirestore.collection("Journal");
    private int Gallery_Code=1;
    private Uri Uriimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postjournal);
        firebaseAuth=FirebaseAuth.getInstance();
        bar=findViewById(R.id.post_progess);
        curruser=findViewById(R.id.post_name);
        storageReference= FirebaseStorage.getInstance().getReference();
        title=findViewById(R.id.title);
        thought=findViewById(R.id.thought);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);

        backimg=findViewById(R.id.img);
        imageView=findViewById(R.id.post_camera);
        savebtn=findViewById(R.id.postbtn);

        bar.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        imageView.setOnClickListener(this);
        savebtn.setOnClickListener(this);

        if(JournalApi.getInstance()!=null)
        {

            JournalApi journalApi=JournalApi.getInstance();
            curruserid=journalApi.getUserid();
            currusername=journalApi.getUsername();
//            Toast.makeText(Postjournal.this,""+curruserid,Toast.LENGTH_LONG).show();
            curruser.setText(currusername);
        }

        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser=firebaseAuth.getCurrentUser();
                if(firebaseUser!=null)
                {

                }
                else
                {

                }
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.post_camera:
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,Gallery_Code);
                break;

            case R.id.postbtn:
                savejournal();
                break;
        }
    }

    private void savejournal() {
    final String title1=title.getText().toString().trim();
    final String thought1=thought.getText().toString().trim();
    bar.setVisibility(View.VISIBLE);
    if(!TextUtils.isEmpty(title1) && !TextUtils.isEmpty(thought1) && Uriimg!=null)
    {
        final StorageReference filepath=storageReference.child("journal_images").child("myimg"+ Timestamp.now().getSeconds());
        filepath.putFile(Uriimg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url=uri.toString();
                        Journal journal=new Journal();
                        journal.setImgurl(url);
                        journal.setTitle(title1);
                        journal.setThought(thought1);
                        journal.setUserid(curruserid);
                        journal.setUsername(currusername);
                        journal.setTimestamp(new Timestamp(new Date()));
                        collectionReference.add(journal).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                bar.setVisibility(View.INVISIBLE);

                                startActivity(new Intent(Postjournal.this,Journallist.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                bar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bar.setVisibility(View.INVISIBLE);
            }
        });
    }
    else
    {
        bar.setVisibility(View.INVISIBLE);
    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Code && resultCode==RESULT_OK)
        {
            if(data!=null)
            {
                Uriimg=data.getData();
                backimg.setImageURI(Uriimg);
                imageView.setVisibility(View.INVISIBLE);
            }
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null)
        {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}