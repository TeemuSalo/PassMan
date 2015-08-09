package com.development.tenzu.passwordmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ManageFolders extends Activity {

    ArrayAdapter<String> adapter;
    List<String> mainfiles;
    File maindir;
    ListView listView;
    AlertDialog alert;
    AlertDialog.Builder alertcreate;
    AlertDialog.Builder alertdelete;
    Toast toast;
    int duration = Toast.LENGTH_SHORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = getApplicationContext();

        maindir = new File(context.getFilesDir(), "MainDirectory");

        mainfiles = new ArrayList<String>();

        File[] MainDirlist = maindir.listFiles();

        if(MainDirlist != null) {
            for (File f : MainDirlist)
                mainfiles.add(f.getAbsoluteFile().getName());
        }
            adapter = new ArrayAdapter<String>(context, R.layout.simplelist, mainfiles);

            listView = (ListView) findViewById(R.id.ListView);
            listView.setAdapter(adapter);

            // Onclicklistener for directory list
            AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {

                    Intent intent = new Intent(ManageFolders.this, ManageFiles.class);

                    String parentdir = listView.getItemAtPosition(position).toString();

                    intent.putExtra("PARENTFILE", parentdir);

                    ManageFolders.this.startActivity(intent);

                }
            };

            listView.setOnItemClickListener(mMessageClickedHandler);


        //Bottom listview
        String[] BottomButtons = {"Create directories", "Delete directories", "Export/Import", "Exit Program"};

        final ArrayAdapter<String> adapterbottom = new ArrayAdapter<String>(context, R.layout.simplelistbottom, BottomButtons);

        final ListView listViewBottom = (ListView) findViewById(R.id.ListViewBottom);
        listViewBottom.setAdapter(adapterbottom);

        // Onclicklistener for bottom buttons
        AdapterView.OnItemClickListener bottomButtons = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                switch (position) {
                    case 0:

                        alertcreate = new AlertDialog.Builder(ManageFolders.this);

                        alertcreate.setTitle("Create Folder");
                        alertcreate.setMessage("Please give folder name");

                        // Set an EditText view to get user input
                        final EditText input = new EditText(ManageFolders.this);
                        alertcreate.setView(input);

                        alertcreate.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String foldername = input.getText().toString();

                                File folder = new File(maindir.getPath(), foldername);

                                boolean success = true;
                                if (!folder.exists()) {
                                    success = folder.mkdirs();
                                }
                                if (success) {
                                    toast = Toast.makeText(context, "success", duration);
                                    toast.show();
                                } else {
                                    toast = Toast.makeText(context, "no deal", duration);
                                    toast.show();
                                }

                                mainfiles.add(foldername);
                                adapter.notifyDataSetChanged();

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

                        alertdelete = new AlertDialog.Builder(ManageFolders.this);

                        alertdelete.setTitle("Delete Folder");
                        alertdelete.setMessage("Please give folder name");

                        // Set an EditText view to get user input
                        final EditText input2 = new EditText(ManageFolders.this);
                        alertdelete.setView(input2);

                        alertdelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                String foldernamedelete = input2.getText().toString();

                                File folderd = new File(maindir, foldernamedelete);

                                boolean success = true;
                                if (folderd.isDirectory()) {
                                    for(File f : folderd.listFiles())
                                    f.delete();
                                }
                                if (!folderd.delete()) {
                                    success = false;
                                }
                                if (success) {
                                    toast = Toast.makeText(context, "success", duration);
                                    toast.show();
                                } else {
                                    toast = Toast.makeText(context, "no deal", duration);
                                    toast.show();
                                }

                                mainfiles.remove(foldernamedelete);
                                adapter.notifyDataSetChanged();

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

                        Intent intent = new Intent(ManageFolders.this, ExportImport.class);
                        ManageFolders.this.startActivity(intent);

                        break;

                    case 3:

                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);

                        break;
                }
            }
        };

        listViewBottom.setOnItemClickListener(bottomButtons);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mainfiles.clear();
        File[] MainDirrefresh = maindir.listFiles();
        if(MainDirrefresh != null) {
            for (File f : MainDirrefresh)
                mainfiles.add(f.getAbsoluteFile().getName());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
