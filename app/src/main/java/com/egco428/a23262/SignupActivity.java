package com.egco428.a23262;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity implements SensorEventListener {

    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private ArrayList<Data> dataList;

    private SensorManager sensorManager;
    private long lastUpdate;

    EditText userSignupTxt;
    EditText passSignupTxt;
    EditText latitudeTxt;
    EditText longitudeTxt;
    Button randomBtn;
    Button addUserBtn;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Sign-up Page");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //for create back button on action bar (line*)

        userSignupTxt = (EditText)findViewById(R.id.userSignupTxt);
        passSignupTxt = (EditText)findViewById(R.id.passSignupTxt);
        latitudeTxt = (EditText)findViewById(R.id.latitudeTxt);
        longitudeTxt = (EditText)findViewById(R.id.longitudeTxt);
        randomBtn = (Button)findViewById(R.id.randomBtn);
        addUserBtn = (Button)findViewById(R.id.addUserBtn);

        randomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomLatLong();
            }
        });

        dataList = new ArrayList<>();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(Config.TABLE_REFERENCE);


        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userSignupTxt.getText().toString().equals("") ||
                   passSignupTxt.getText().toString().equals("") ||
                   latitudeTxt.getText().toString().equals("") ||
                   longitudeTxt.getText().toString().equals("")){
                        Toast.makeText(SignupActivity.this, "Please fill the blanks completely.", Toast.LENGTH_SHORT).show();
                }
                else {
                    boolean check = false;
                    for(int i=0 ; i < dataList.size() ; i++){
                        if(userSignupTxt.getText().toString().equals(dataList.get(i).getUsername())){
                            check = true;
                        }
                    }
                    if(check) {
                        Toast.makeText(SignupActivity.this, "Username already exists, Please try again.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(SignupActivity.this, "Signing-up is successful.", Toast.LENGTH_SHORT).show();

                        //these codes come from this link : https://firebase.google.com/docs/database/admin/save-data
                        String user = userSignupTxt.getText().toString();
                        String pass = passSignupTxt.getText().toString();

                        String latString = latitudeTxt.getText().toString();
                        double lat = Double.parseDouble(latString);

                        String longString = longitudeTxt.getText().toString();
                        double lon = Double.parseDouble(longString);

                        Data objData = new Data(user, pass, lat, lon);
                        myRef.push().setValue(objData);
                        finish();
                    }
                }
            }
        });

        getUserPass();
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == android.R.id.home){ //home is back button id (from line *)
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            getAccelerometer(event);
        }
    }

    private void getAccelerometer(SensorEvent event){
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        long actualTime = System.currentTimeMillis();
        if (accelationSquareRoot > 2 ) {
            if (actualTime - lastUpdate < 700) {
                return;
            }
            lastUpdate = actualTime;
            count++;

            if(count == 2) {
                randomLatLong();
                count = 0;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy){
        //this method for will able error with adding implements SensorEventListener
    }

    @Override
    public void onResume(){
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    //these codes come from this link : http://stackoverflow.com/questions/9104908/random-geographic-coordinates-on-land-avoid-ocean
    public void randomLatLong(){
        double minLat = -85.000000;
        double maxLat = 85.000000;
        double latitude = minLat + Math.random() * ((maxLat - minLat) + 1);

        double minLong = -179.999989;
        double maxLong = 179.999989;
        double longitude = minLong + Math.random() * ((maxLong - minLong) + 1);

        DecimalFormat decform = new DecimalFormat("#.######");
        latitudeTxt.setText(decform.format(latitude));
        longitudeTxt.setText(decform.format(longitude));
    }

    public void getUserPass(){
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Data dataFirebase = dataSnapshot.getValue(Data.class); //get value from database in to Data form
                dataList.add(dataFirebase);
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
