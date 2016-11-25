package com.egco428.a23262;

/**
 * Created by Natamon Tangmo on 21-Nov-16.
 */
public class Data {
    private String username;
    private String password;
    private double latitude;
    private double longitude;

    public Data(String username, String password, double latitude, double longitude){
        this.username = username;
        this.password = password;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Data(){}

    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
}
