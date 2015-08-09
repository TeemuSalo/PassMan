package com.development.tenzu.passwordmanager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;;

public class ExportImport extends Activity {

    File maindir;
    Toast toast;
    int duration = Toast.LENGTH_SHORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eximplayout);

        final Context context = getApplicationContext();

        maindir = new File(context.getFilesDir(), "MainDirectory");

        // Listview
        String[] BottomButtons = {"Export", "Import", "Back"};

        final ArrayAdapter<String> adapterbottom = new ArrayAdapter<String>(context, R.layout.simplelist, BottomButtons);

        final ListView listViewBottom = (ListView) findViewById(R.id.ListViewExImp);
        listViewBottom.setAdapter(adapterbottom);

        // Onclicklistener for bottom buttons
        AdapterView.OnItemClickListener bottomButtons = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                switch (position) {

                    case 0:
                        //Export
                        if(!(isExternalStorageWritable())){
                            toast.makeText(context, "Cannot write to external", duration).show();
                        }

                        String targetfilestring = "ManPass";
                        File mainsourcefile = maindir;
                        File maintargetfile= new File(Environment.getExternalStorageDirectory(), targetfilestring);

                        if (mainsourcefile.isDirectory()){

                            if(!maintargetfile.exists()){
                                //Target main directory
                                toast.makeText(context, "Creating export directory", duration).show();
                                if(!maintargetfile.mkdirs()) toast.makeText(context, "Cannot create export directory", duration).show();
                            }

                            //List of subfolders on source
                            File subfolders[] = mainsourcefile.listFiles();
                            if(subfolders.length == 0) {
                                toast.makeText(context, "No source folders found", duration).show();
                            }

                            //For every subfolder in mainsourcedirectory
                            for (File srcsubfolder : subfolders){

                                String content = "";

                                //If source subfolder is a directory
                                if(srcsubfolder.isDirectory()){

                                    //Make target Subdirectories
                                    File targetsubfolder = new File(maintargetfile, srcsubfolder.getName());
                                    if(targetsubfolder.exists()){
                                        toast.makeText(context, "Subfolder exists", duration).show();
                                    } else if(!targetsubfolder.mkdirs()){
                                        toast.makeText(context, "Cannot create subfolder", duration).show();
                                    }

                                    //Get list of filenames in source subfolder
                                    String[] sourcesubfolderfiles = srcsubfolder.list();
                                    if(sourcesubfolderfiles.length == 0){
                                        toast.makeText(context, "Source subfolder empty", duration).show();
                                    }

                                    for (String subfolderfilename : sourcesubfolderfiles){

                                        //Target subdirectory Passwordfile
                                        File targetchild = new File(targetsubfolder, subfolderfilename);
                                        if(targetchild.exists()){
                                            toast.makeText(context, "External passwordfile exists, rewriting", duration).show();
                                        }

                                        try{
                                            File getinput = new File(srcsubfolder, subfolderfilename);
                                            FileInputStream fin = new FileInputStream(getinput);
                                            int c;
                                            String temp="";
                                            while( (c = fin.read()) != -1){
                                                temp = temp + Character.toString((char)c);
                                            }
                                            content = temp;
                                            fin.close();
                                            //toast.makeText(context, "luettu", duration).show();
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }

                                        try {
                                            File output = new File(targetsubfolder, subfolderfilename);
                                            FileOutputStream fout = new FileOutputStream(targetchild);
                                            fout.write(content.getBytes());
                                            fout.close();
                                            //toast.makeText(context, "kirjoitettu", duration).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                toast.makeText(context, "Export complete", duration).show();
                            }
                        }

                        break;

                    case 1:
                        //Import

                        if(!(isExternalStorageReadable())){
                            toast.makeText(context, "Cannot read external storage", duration).show();
                        }

                        String sourcefilestring = "ManPass";
                        File mainimporttarget = maindir;
                        File mainimportsource= new File(Environment.getExternalStorageDirectory(), sourcefilestring);

                        if (mainimportsource.exists() && mainimportsource.isDirectory()){

                            //List of subfolders on source
                            File subfolders[] = mainimportsource.listFiles();
                            if(subfolders.length == 0) {
                                toast.makeText(context, "mainsource listfiles fail", duration).show();
                            }

                            //For every subfolder in mainsourcedirectory
                            for (File srcsubfolder : subfolders){

                                toast.makeText(context, srcsubfolder.toString(), duration).show();

                                String content = "";

                                //If source subfolder is a directory
                                if(srcsubfolder.isDirectory()){

                                    //Make target Subdirectories
                                    File targetsubfolder = new File(mainimporttarget, srcsubfolder.getName());

                                    if(targetsubfolder.exists()){
                                        toast.makeText(context, "Subfolder exists", duration).show();
                                    } else if(!targetsubfolder.mkdirs()){
                                        toast.makeText(context, "Cannot create subfolder", duration).show();
                                    }

                                    //Get list of filenames in source subfolder
                                    String[] sourcesubfolderfiles = srcsubfolder.list();
                                    if(sourcesubfolderfiles.length == 0){
                                        toast.makeText(context, "External subfolder empty", duration).show();
                                    }

                                    for (String subfolderfilename : sourcesubfolderfiles){

                                        //Target subdirectory Passwordfile
                                        File targetchild = new File(targetsubfolder, subfolderfilename);
                                        if(targetchild.exists()){
                                            toast.makeText(context, "Passwordfile exists, rewriting", duration).show();
                                        }

                                        try{
                                            File getinput = new File(srcsubfolder, subfolderfilename);
                                            FileInputStream fin = new FileInputStream(getinput);
                                            int c;
                                            String temp="";
                                            while( (c = fin.read()) != -1){
                                                temp = temp + Character.toString((char)c);
                                            }
                                            content = temp;
                                            fin.close();
                                            //toast.makeText(context, "luettu", duration).show();
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }

                                        try {
                                            File output = new File(targetsubfolder, subfolderfilename);
                                            FileOutputStream fout = new FileOutputStream(output);
                                            fout.write(content.getBytes());
                                            fout.close();
                                            //toast.makeText(context, "kirjoitettu", duration).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                            toast.makeText(context, "Import successful", duration).show();

                        }else {toast.makeText(context, "No import file found", duration).show();}

                        File[] deldirs = mainimportsource.listFiles();
                        for( File delfil : deldirs){
                            if(delfil.isDirectory()){
                                File[] delfiles = delfil.listFiles();
                                for(File delete : delfiles){
                                    delete.delete();
                                }
                            }
                            delfil.delete();
                        }
                        mainimportsource.delete();

                        break;

                    case 2:

                        finish();

                        break;
                }
            }
        };

        listViewBottom.setOnItemClickListener(bottomButtons);
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
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
