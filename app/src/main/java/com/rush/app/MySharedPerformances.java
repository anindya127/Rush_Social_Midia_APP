package com.rush.app;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPerformances {
    private Context context;

    public MySharedPerformances(Context context) {
        this.context = context;
    }

    public  void setMyData(String number){
        SharedPreferences.Editor editor = context.getSharedPreferences("myData", Context.MODE_PRIVATE).edit();
        editor.putString("number", number);
        editor.apply();
    }

    public String getMyNumber(){
        SharedPreferences editor = context.getSharedPreferences("myData", Context.MODE_PRIVATE);
        return editor.getString("number", null);
    }
}
