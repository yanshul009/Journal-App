package com.example.self;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.JournalApi;

public class Create_account extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private EditText email;
    private EditText password;
    private ProgressBar progressBar;
    private EditText username;
    private Button create;
    private CollectionReference collectionReference=db.collection("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        email=findViewById(R.id.email_create);
        password=findViewById(R.id.password_create);
        username=findViewById(R.id.user_acc);
        create=findViewById(R.id.email_createbtn);
        progressBar=findViewById(R.id.login_progress_create);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        firebaseAuth=FirebaseAuth.getInstance();
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

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty() || username.getText().toString().isEmpty())
                {
                    Toast.makeText(Create_account.this,"Please enter all the fields",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Create_account.this,"Present here button",Toast.LENGTH_SHORT).show();
                    String email1=email.getText().toString().trim();
//            Toast.makeText(Create_account.this,"Present",Toast.LENGTH_SHORT).show();
                    String password1=password.getText().toString().trim();
                    final String username1=username.getText().toString().trim();
                    createaccountuseremail(email1,password1,username1);
                }

            }
        });
    }

    private void createaccountuseremail(String email1, String password1, final String username1) {
        if(!email1.isEmpty() && !password1.isEmpty() && !username1.isEmpty())
        {
        progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Create_account.this,"Present here",Toast.LENGTH_SHORT).show();

                        firebaseUser=firebaseAuth.getCurrentUser();
                        assert firebaseUser != null;
                        final String currid=firebaseUser.getUid();
                        Map<String,String> userobj=new HashMap<>();
                        userobj.put("userid",currid);
                        userobj.put("username",username1);
                        collectionReference.add(userobj).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(Objects.requireNonNull(task.getResult()).exists())
                                        {
                                            Toast.makeText(Create_account.this,"Present here okkkkkkkkk",Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                            String name=task.getResult().getString("username");
                                            JournalApi journalApi=JournalApi.getInstance();
                                            journalApi.setUserid(currid);
                                            journalApi.setUsername(username1);
                                            Intent intent=new Intent(Create_account.this,Postjournal.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else
                                        {
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                        
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Create_account.this,"Present here"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Create_account.this,"Not successful",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Create_account.this,"Present here111"+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(Create_account.this,"empty",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
}