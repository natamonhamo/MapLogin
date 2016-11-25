package com.egco428.a23262;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private DatabaseReference myRef;
    private FirebaseDatabase database;

    private ArrayList<Data> dataList = new ArrayList<>();

    EditText userLoginTxt;
    EditText passLoginTxt;
    Button signinBtn;
    Button cancelBtn;
    Button signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login Page");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Config.TABLE_REFERENCE);
        getUserPass();

        userLoginTxt = (EditText)findViewById(R.id.userLoginTxt);
        passLoginTxt = (EditText)findViewById(R.id.passLoginTxt);
        signinBtn = (Button)findViewById(R.id.signinBtn);
        cancelBtn = (Button)findViewById(R.id.cancelBtn);
        signupBtn = (Button)findViewById(R.id.signupBtn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserPass();
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(intent,0);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLoginTxt.setText("");
                passLoginTxt.setText("");
            }
        });
    }

    public void checkUserPass(){
        boolean check = false;
        for(int i=0 ; i < dataList.size() ; i++){
            if(userLoginTxt.getText().toString().equals(dataList.get(i).getUsername()) &&
               passLoginTxt.getText().toString().equals(dataList.get(i).getPassword())) {
                    check = true;
            }
        }
        if(check){
            Toast.makeText(LoginActivity.this, "Login success.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivityForResult(intent,0);
            userLoginTxt.setText("");
            passLoginTxt.setText("");
        }
        else{
            Toast.makeText(LoginActivity.this, "Login fail.", Toast.LENGTH_SHORT).show();
        }
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
}
