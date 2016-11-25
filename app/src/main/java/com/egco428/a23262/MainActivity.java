package com.egco428.a23262;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private ArrayList<Data> dataList;
    private ArrayAdapter userlistArrayAdapter;

    public static final String Map_Latitude = "Latitude";
    public static final String Map_Longitude = "Longitude";
    public static final String Map_Username = "Username";

    Data dataTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main Page");

        dataList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Config.TABLE_REFERENCE);
        getUserPass();

        userlistArrayAdapter = new UserListArrayAdapter(this, dataList);
        ListView listView = (ListView)findViewById(R.id.usernameListview);

        listView.setAdapter(userlistArrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                dataTemp = dataList.get(i);
                intent.putExtra(Map_Latitude, dataTemp.getLatitude());
                intent.putExtra(Map_Longitude, dataTemp.getLongitude());
                intent.putExtra(Map_Username, dataTemp.getUsername());
                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //for 'create' back button on action bar (line*)
                                                                //but there're 'no action' on this code
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){ //home is back button id (from line *)

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);  //force the user must choose one of provided options
            builder.setMessage("Are you sure you want to log out?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.create();
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    //these codes adapt from this link : https://firebase.google.com/docs/database/admin/retrieve-data
    public void getUserPass(){
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data dataFirebase = dataSnapshot.getValue(Data.class); //get value from database in to Data form
                dataList.add(dataFirebase);
                Log.e("size",dataList.size()+"");
//                if(userLoginTxt.getText().toString().equals(dataFirebase.getUsername()) &&
//                   passLoginTxt.getText().toString().equals(dataFirebase.getPassword())){
//                        check = true;
//                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);  //force the user must choose one of provided options
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }
}
