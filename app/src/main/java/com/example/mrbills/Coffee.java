package com.example.mrbills;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


public class Coffee extends AppCompatActivity {

    private static final int SEND_SMS_PERMISSION_REQ = 1;
    TextView amount,remarks,name,mobile_number;
    Button plus,minus,order;
    CheckBox whippedcream,chocolate;
    int bill = 0,num=0;
    Database myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);

        amount = (TextView) findViewById(R.id.amount);
        remarks = (TextView) findViewById( R.id.remarks);
        plus = (Button) findViewById(R.id.plus);
        minus = (Button) findViewById(R.id.minus);
        order = (Button) findViewById(R.id.order);
        name = (TextView) findViewById(R.id.name);
        mobile_number = (TextView) findViewById(R.id.mobile_number);
        whippedcream = (CheckBox) findViewById(R.id.whippedcream);
        chocolate = (CheckBox) findViewById(R.id.chocolate);
        myDB = new Database(this);
        if(checkPermission(Manifest.permission.SEND_SMS)) {
            order.setEnabled(true);
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQ);
        }

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num++;
                price(1);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bill > 0)
                {
                    num--;
                    price(-1);

                }

            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bill > 0)
                {
                    remarks.setText(orderSummary());
                    if(!TextUtils.isEmpty(name.getText().toString())&&!TextUtils.isEmpty(mobile_number.getText().toString()))
                    {

                        if(checkPermission(Manifest.permission.SEND_SMS))
                        {
                            Toast.makeText(Coffee.this,"Message sent",Toast.LENGTH_SHORT).show();
                            SmsManager smsManager=SmsManager.getDefault();
                            smsManager.sendTextMessage(mobile_number.getText().toString(),null,remarks.getText().toString(),null,null);
                            String a = "Rs."+String.valueOf(bill);
                            boolean insert = myDB.addData(name.getText().toString(),a,mobile_number.getText().toString());
                            if(!insert){
                                Toast.makeText(Coffee.this, "Not added to database", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(Coffee.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(Coffee.this, "fields are empty!! Fill them.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public String orderSummary(){

        String quantity = "Quantity : "+num;
        if(whippedcream.isChecked() &&  chocolate.isChecked()) {
            bill=(bill+20*num);
            return "Order Details\n" + "Name : " + name.getText().toString() + "\nMobile Number : " + mobile_number.getText().toString() + "\nWhipped Cream is also added !!" + "\nChocolate is also added !!\n" + quantity + "\nTotal bill : Rs " + (bill) + "\nThank You !!";
        }
        else if ( chocolate.isChecked()) {
            bill = (bill+10*num);
            return "Order Details\n" + "Name : " + name.getText().toString() + "\nMobile Number : " + mobile_number.getText().toString() + "\nChocolate is also added !!\n" + quantity + "\nTotal bill : Rs " + (bill ) + "\nThank You !!";
        }
        else if (whippedcream.isChecked()) {
            bill = (bill+10*num);
            return "Order Details\n" + "Name : " + name.getText().toString() + "\nMobile Number : " + mobile_number.getText().toString() + "\nWhipped Cream is also added !!\n" + quantity + "\nTotal bill : Rs " + (bill ) + "\nThank You !!";
        }
        else {
            bill = (bill);
            return "Order Details\n" + "Name : " + name.getText().toString() + "\nMobile Number : " + mobile_number.getText().toString() + "\n" + quantity + "\nTotal bill : Rs " + bill + "\nThank You !!";
        }
    }

    public void price(int n){
        bill = bill+50*n;
        amount.setText(""+num);
    }

    private boolean checkPermission(String sendSms) {

        int checkpermission= ContextCompat.checkSelfPermission(this,sendSms);
        return checkpermission== PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case SEND_SMS_PERMISSION_REQ:
                if(grantResults.length>0 &&(grantResults[0]==PackageManager.PERMISSION_GRANTED))
                {
                    order.setEnabled(true);
                }
                break;
        }
    }
}
