package com.development.tenzu.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class Authorization extends ActionBarActivity {

    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    int tries;
    String MasterPassword;
    String password;
    String temppasswd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        button1 = (Button) findViewById(R.id.B1);
        button2 = (Button) findViewById(R.id.B2);
        button3 = (Button) findViewById(R.id.B3);
        button4 = (Button) findViewById(R.id.B4);
        button5 = (Button) findViewById(R.id.B5);
        button6 = (Button) findViewById(R.id.B6);
        button7 = (Button) findViewById(R.id.B7);
        button8 = (Button) findViewById(R.id.B8);
        button9 = (Button) findViewById(R.id.B9);

        MasterPassword = getResources().getString(R.string.masterpassword);

        // Button 1
        View.OnClickListener buttonlistener = new View.OnClickListener() {
            public void onClick(View v){
                switch(v.getId()){

                    case R.id.B1:
                        temppasswd = getResources().getString(R.string.passwordone);
                        password += temppasswd;
                        break;
                    case R.id.B2:
                        temppasswd = getResources().getString(R.string.passwordtwo);
                        password += temppasswd;
                        break;
                    case R.id.B3:
                        temppasswd = getResources().getString(R.string.passwordthree);
                        password += temppasswd;
                        break;
                    case R.id.B4:
                        temppasswd = getResources().getString(R.string.passwordfour);
                        password += temppasswd;
                        break;
                    case R.id.B5:
                        temppasswd = getResources().getString(R.string.passwordfive);
                        password += temppasswd;
                        break;
                    case R.id.B6:
                        temppasswd = getResources().getString(R.string.passwordsix);
                        password += temppasswd;
                        break;
                    case R.id.B7:
                        temppasswd = getResources().getString(R.string.passwordseven);
                        password += temppasswd;
                        break;
                    case R.id.B8:
                        temppasswd = getResources().getString(R.string.passwordeight);
                        password += temppasswd;
                        break;
                    case R.id.B9:
                        temppasswd = getResources().getString(R.string.passwordnine);
                        password += temppasswd;
                        break;
                }

                tries++;
                if(tries > 10) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }

                if(password.contains(MasterPassword)) {

                    password = "";

                    Intent intent = new Intent(Authorization.this, ManageFolders.class);

                    Authorization.this.startActivity(intent);

                }
            }
        };
        button1.setOnClickListener(buttonlistener);
        button2.setOnClickListener(buttonlistener);
        button3.setOnClickListener(buttonlistener);
        button4.setOnClickListener(buttonlistener);
        button5.setOnClickListener(buttonlistener);
        button6.setOnClickListener(buttonlistener);
        button7.setOnClickListener(buttonlistener);
        button8.setOnClickListener(buttonlistener);
        button9.setOnClickListener(buttonlistener);

    }

    @Override
    protected void onStop(){
        super.onStop();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
