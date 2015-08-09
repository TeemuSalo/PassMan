package com.development.tenzu.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


public class ManageFiles extends ActionBarActivity {

    ArrayAdapter<String> adapter2;
    List<String> thesefiles;
    File thisdir;
    File maindir;
    ListView listView2;
    Toast toast;
    TextView toastText;
    View layout;
    int duration = Toast.LENGTH_SHORT;
    AlertDialog alert;

    String selecteditem;

    EditText inputmodify;

    File selectedtomodify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.customtoast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        final String parentdir = getIntent().getExtras().getString("PARENTFILE");

        listView2 = (ListView) findViewById(R.id.ListView2);

        final Context context = getApplicationContext();

        maindir = new File(context.getFilesDir(), "MainDirectory");

        thisdir = new File(maindir, parentdir);

        thesefiles = new ArrayList<String>();

        if(thisdir.list() != null) {

            for(String file : thisdir.list())
                thesefiles.add(file);
        }

            adapter2 = new ArrayAdapter<String>(context, R.layout.simplelist, thesefiles);

            listView2.setAdapter(adapter2);


            // Read password
            AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                    CharSequence textin="";

                    selecteditem = listView2.getItemAtPosition(position).toString();

                    File selected = new File(thisdir, selecteditem);

                    try {
                        FileInputStream fin = new FileInputStream(selected);
                        int c;
                        String temp = "";
                        while ((c = fin.read()) != -1) {
                            temp = temp + Character.toString((char) c);
                        }

                        String decryptedpasswrod = DecryptPassword(temp);
                        toastText = (TextView) layout.findViewById(R.id.text);
                        toastText.setText(decryptedpasswrod);

                        Toast toast = new Toast(getApplicationContext());
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            listView2.setOnItemClickListener(mMessageClickedHandler);



        // Longclick modify password.
        AdapterView.OnItemLongClickListener longClick = new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView parent, View v, int position, long id) {

                String modifyDecrypt = "";

                selecteditem = listView2.getItemAtPosition(position).toString();

                selectedtomodify = new File(thisdir, selecteditem);

                try{
                    if(!selectedtomodify.createNewFile()){
                        FileInputStream fin = new FileInputStream(selectedtomodify);
                        int c;
                        String temp="";
                        while( (c = fin.read()) != -1){
                            temp = temp + Character.toString((char)c);
                        }
                        fin.close();

                        modifyDecrypt = DecryptPassword(temp);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                AlertDialog.Builder alertmodify = new AlertDialog.Builder(ManageFiles.this);

                alertmodify.setTitle("Change password");
                alertmodify.setMessage("Please give new password");

                // Set an EditText view to get user input
                inputmodify = new EditText(ManageFiles.this);
                inputmodify.setText(modifyDecrypt);
                alertmodify.setView(inputmodify);

                alertmodify.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String newpassword = inputmodify.getText().toString();

                        String cryptedPassword = CryptPassword(newpassword);

                        FileOutputStream outputStream;

                        try {
                            outputStream = new FileOutputStream(selectedtomodify.getPath());
                            outputStream.write(cryptedPassword.getBytes());
                            outputStream.close();

                            toastText = (TextView) layout.findViewById(R.id.text);
                            toastText.setText("Password saved");
                            Toast toast = new Toast(getApplicationContext());
                            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        alert.dismiss();
                    }
                });

                alertmodify.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        alert.dismiss();

                    }
                });

                alert = alertmodify.create();
                alert.show();
                return true;
            }
        };

        listView2.setOnItemLongClickListener(longClick);



        //Bottom listview
        String[] BottomButtons2 = {"Create Password", "Delete Password", "Back"};

        final ArrayAdapter<String> adapterbottom2 = new ArrayAdapter<String>(context, R.layout.simplelistbottom, BottomButtons2);

        final ListView listViewBottom2 = (ListView) findViewById(R.id.ListViewBottom2);
        listViewBottom2.setAdapter(adapterbottom2);

        // Onclicklistener for bottom buttons
        AdapterView.OnItemClickListener bottomButtons2 = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                switch (position) {
                    case 0:

                        AlertDialog.Builder alertcreate = new AlertDialog.Builder(ManageFiles.this);

                        alertcreate.setTitle("Create Password title");
                        alertcreate.setMessage("Please give title name");

                        // Set an EditText view to get user input
                        final EditText input = new EditText(ManageFiles.this);
                        alertcreate.setView(input);

                        alertcreate.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String foldername = input.getText().toString();

                                File folder = new File(thisdir, foldername);

                                boolean success = false;

                                try {
                                    success = folder.createNewFile();
                                } catch (Exception e){e.printStackTrace();};

                                if (success) {
                                    toast = Toast.makeText(context, "Success", duration);
                                    toast.show();
                                } else {
                                    toast = Toast.makeText(context, "Writing failed", duration);
                                    toast.show();
                                }

                                thesefiles.add(foldername);
                                adapter2.notifyDataSetChanged();

                                alert.dismiss();
                            }
                        });

                        alertcreate.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                alert.dismiss();

                            }
                        });

                        alert = alertcreate.create();
                        alert.show();

                        break;

                    case 1:

                        AlertDialog.Builder alertdelete = new AlertDialog.Builder(ManageFiles.this);

                        alertdelete.setTitle("Delete Password");
                        alertdelete.setMessage("Please give title name");

                        // Set an EditText view to get user input
                        final EditText input2 = new EditText(ManageFiles.this);
                        alertdelete.setView(input2);

                        alertdelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String foldernamedelete = input2.getText().toString();

                                //deleteFile(foldernamedelete);
                                File folderd = new File(thisdir, foldernamedelete);

                                boolean success = true;
                                if (folderd.isDirectory()) {
                                    for(File f : folderd.listFiles())
                                        f.delete();
                                }
                                if (!folderd.delete()) {
                                    success = false;
                                }
                                if (success) {
                                    toast = Toast.makeText(context, "Success", duration);
                                    toast.show();
                                } else {
                                    toast = Toast.makeText(context, "Delete failed", duration);
                                    toast.show();
                                }

                                thesefiles.remove(foldernamedelete);
                                adapter2.notifyDataSetChanged();

                                alert.dismiss();

                            }
                        });

                        alertdelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                alert.dismiss();

                            }
                        });

                        alert = alertdelete.create();
                        alert.show();

                        break;

                    case 2:

                        finish();

                        break;

                    default:
                        break;
                }
            }
        };

        listViewBottom2.setOnItemClickListener(bottomButtons2);
    }

    public static String CryptPassword(String cryptme) {

        // Cryptpassword disabled for GitHub version

        // Returns argument

        return cryptme;
    }


    public static String DecryptPassword(String crypt) {

        // DeCryptpassword disabled for GitHub version

        // Returns argument

        return crypt;
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
