package com.example.self;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import model.Journal;
import util.JournalApi;

public class Login_Activity extends AppCompatActivity {

    private Button login;
    private Button create;

    private EditText etmail;
    private EditText etpassword;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private  FirebaseAuth.AuthStateListener listener;

    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=firebaseFirestore.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        firebaseAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.email_signbtn);
        create=findViewById(R.id.email_createloginbtn);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        etmail=findViewById(R.id.email);
        etpassword=findViewById(R.id.password);
        progressBar=findViewById(R.id.login_progress);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login_Activity.this,Create_account.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginemailpassword(etmail.getText().toString().trim(),etpassword.getText().toString().trim());
            }
        });

    }

    private void loginemailpassword(String trim, String trim1) {
        if(!TextUtils.isEmpty(trim) && !TextUtils.isEmpty(trim1))
        {
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(trim,trim1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser curruser = firebaseAuth.getCurrentUser();
                        assert curruser != null;
                        final String userid = curruser.getUid();
                        collectionReference.whereEqualTo("userid", userid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                if (error != null) {
                                    return;
                                }
                                assert value != null;
                                if (!value.isEmpty()) {
                                    for (QueryDocumentSnapshot documentSnapshot : value) {
                                        JournalApi journalApi = JournalApi.getInstance();
                                        String name = documentSnapshot.getString("username");
                                        assert journalApi != null;
                                        journalApi.setUsername(documentSnapshot.getString("username"));
                                        journalApi.setUserid(documentSnapshot.getString("userid"));
//                                        Toast.makeText(Login_Activity.this, "" + userid + " name" + name, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login_Activity.this, Journallist.class));
                                        progressBar.setVisibility(View.INVISIBLE);
                                        finish();
                                    }
                                }
                            }
                        });
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
                    Toast.makeText(Login_Activity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(Login_Activity.this,"Please enter the correct email/password",Toast.LENGTH_SHORT).show();
        }
    }
}