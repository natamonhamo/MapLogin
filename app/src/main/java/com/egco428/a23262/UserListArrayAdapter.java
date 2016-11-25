package com.egco428.a23262;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Natamon Tangmo on 25-Nov-16.
 */
public class UserListArrayAdapter extends ArrayAdapter<Data> {
    Context context;
    List<Data> userList;
    public UserListArrayAdapter(Context context, List<Data> userList){
        super(context, 0 , userList);
        this.context = context;
        this.userList = userList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Data data = userList.get(position);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.user_item, null);

        TextView userList = (TextView)view.findViewById(R.id.userList);
        userList.setText(data.getUsername());

        return view;
    }
}
