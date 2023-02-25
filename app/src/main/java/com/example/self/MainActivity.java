package com.example.self;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import util.JournalApi;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference=firebaseFirestore.collection("Users");

    private Button start_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_btn=findViewById(R.id.start_btn);
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        firebaseAuth=FirebaseAuth.getInstance();
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                firebaseUser=firebaseAuth.getCurrentUser();
                if(firebaseUser!=null)
                {
                    firebaseUser=firebaseAuth.getCurrentUser();
                    String id=firebaseUser.getUid();
                    collectionReference.whereEqualTo("userid",id).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                            if(error!=null)
                            {
                                return;
                            }
                            String name;
                            if(!value.isEmpty())
                            {
                                for(QueryDocumentSnapshot queryDocumentSnapshot:value)
                                {
                                    JournalApi journalApi=JournalApi.getInstance();
                                    journalApi.setUsername(queryDocumentSnapshot.getString("username"));
                                    journalApi.setUserid(queryDocumentSnapshot.getString("userid"));

                                }
                                startActivity(new Intent(MainActivity.this,Journallist.class));
                                finish();
                            }

                        }
                    });
                }

            }
        };
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Login_Activity.class));
                finish();
            }
        });


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