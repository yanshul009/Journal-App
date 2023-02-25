package com.example.self;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.Journal;
import ui.Journalrecyclerviewadapter;
import util.JournalApi;

public class Journallist extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;

    public List<Journal>journalList;
    public RecyclerView recyclerView;
    public Journalrecyclerviewadapter journalrecyclerviewadapter;
    public CollectionReference collectionReference=db.collection("Journal");
    public TextView nojournal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journallist);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        journalList=new ArrayList<>();
        nojournal=findViewById(R.id.list_nothoughts);
        recyclerView=findViewById(R.id.recyclerview);

        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.menu,menu);
       return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_add:
                if(firebaseUser!=null && firebaseAuth!=null)
                {
                    startActivity(new Intent(Journallist.this,Postjournal.class));
                }
                break;

            case R.id.sign_out:
                if(firebaseUser!=null && firebaseAuth!=null)
                {
                    firebaseAuth.signOut();
                    startActivity(new Intent(Journallist.this,MainActivity.class));
                    finish();
                }
                break;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onStart() {
        super.onStart();
        journalList=new ArrayList<>();
//        Toast.makeText(Journallist.this,""+JournalApi.getInstance().getUserid(),Toast.LENGTH_LONG).show();
        collectionReference.whereEqualTo("userid", JournalApi.getInstance().getUserid()).get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(!queryDocumentSnapshots.isEmpty())
                            {
                                for(QueryDocumentSnapshot value:queryDocumentSnapshots)
                                {
                                    Journal journal=value.toObject(Journal.class);
                                    journalList.add(journal);
                                }

                                journalrecyclerviewadapter =new Journalrecyclerviewadapter(Journallist.this,journalList);
                                recyclerView.setAdapter(journalrecyclerviewadapter);
                                journalrecyclerviewadapter.notifyDataSetChanged();


                            }
                            else
                            {
                                nojournal.setVisibility(View.VISIBLE);
                             }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Journallist.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}