package com.example.android.wallmart;

import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.service.carrier.CarrierMessagingService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    ArrayList<String> list = new ArrayList<>();
    ListView listView;
    SearchView searchView;
    FirebaseAuth mAuth;
    DatabaseReference dref;
    GoogleSignInClient mGoogleSignInClient;
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.ListView);
        searchView = (SearchView) findViewById(R.id.searchView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, list);
        listView.setAdapter(adapter);
        mAuth = FirebaseAuth.getInstance();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

                                                                                                                //ERROR
            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);
                
                return false;
            }

        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Intent intent = new Intent(MainActivity.this, Item0.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(MainActivity.this, Item1.class);
                    startActivity(intent);
                } else if (position == 2) {
                    Intent intent = new Intent(MainActivity.this, Item2.class);
                    startActivity(intent);
                } else if (position == 3) {
                    Intent intent = new Intent(MainActivity.this, Item3.class);
                    startActivity(intent);
                }


            }
        });


        dref = FirebaseDatabase.getInstance().getReference();
        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value = dataSnapshot.getValue(String.class);
                list.add(value);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                list.remove(value);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, m);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.signOut){

        Toast.makeText(this, "signed out", Toast.LENGTH_LONG).show();
         FirebaseAuth.getInstance().signOut();
            AuthUI.getInstance()
                    .signOut(MainActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>(){

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Intent intent = new Intent(MainActivity.this,Authentication.class);
                            startActivity(intent);

                        }
                    });

        }
        return false;
    }


    }






