package com.itqanbs.twetter;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;




public class CustomDialog extends Dialog {
    private static Context myContext;

    public CustomDialog(@NonNull Context context) {
        super(context);
        myContext = context;
    }

    public static Context getMyContext() {
        return myContext;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setBackgroundDrawableResource(R.drawable.curve_shape);
    }
}
