package com.example.a9_smsandemail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {
        switch (view.getId())
        {
            case R.id.buttonPhone:
                dialPhoneNumber("5555215554");
                break;
            case R.id.buttonPhoneDirect:
                composeDirectPhone("5554");
                break;
            case R.id.buttonSMS:
                composeSMSMessage("Test SMS from Lianne");
                break;
            case R.id.buttonSMSDirect:
                composeDirectMmsMessage("SMS from Lianne Direct");
                break;
            case R.id.buttonEmail:
                String [] to = {"lwong@fanshawec.ca","lwong@fanshaweonline.ca"};
                composeEmail(to, "email from Lianne");
                break;
            case R.id.buttonAlarm:
                createAlarm("Alarm for lianne - Wake up",8,0);
                break;
        }
    }

    //DIAL THE NUMBER ONLY, NOT CALL
    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    String TAG = "EmailSMS";

    //WILL IMMEDIATELY CALL THE PHONE NUMBER ARGUMENT
    public void composeDirectPhone(String message) {
        Log.i(TAG, "composeDirectPhone: ");
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "composeDirectPhoneMessage: granting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},1);
        }

        // Permission already granted
        try {
            String number = "tel:" + "5554";
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(number));
            startActivity(intent);
        }
        catch (SecurityException e)
        {
            e.printStackTrace();;
        }
        Log.i(TAG, "composeDirectPhoneMessage: phoning... ");
    }

    public void composeSMSMessage(String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:"+"5555215554"));  // This ensures only SMS apps respond
        intent.putExtra("sms_body", message);
//        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


    public void composeDirectMmsMessage(String message) {
        Log.i(TAG, "composeDirectMmsMessage: ");
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "composeDirectMmsMessage: granting permission");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},1);
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_PHONE_STATE},1);
        }
// Permission already granted
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage("5554",null,
                message,null,null);
        Log.i(TAG, "composeDirectMmsMessage: message sent ");
    }

    public void createAlarm(String message, int hour, int minutes) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void composeEmail(String[] addresses, String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, "Body of the email from Lianne");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }



}