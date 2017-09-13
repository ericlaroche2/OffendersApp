package com.example.ericlaroche.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
public static final String EXTRA_MESSAGE= "com.example.myfirstapp.MESSAGE";
    ArrayList<Offender>Offenders=new ArrayList<Offender>();
    ArrayList<Offender>filteredOffenders=new ArrayList<Offender>();
    SQLiteDatabase db;
    FeedReaderDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setOffenders();

        mDbHelper = new FeedReaderDbHelper(getApplicationContext());

        // Gets the data repository in write mode
        db =  mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();

        for (int i =0;i<Offenders.size();i++) {
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Name,       Offenders.get(i).getName());
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_LastName,   Offenders.get(i).getLastName());
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Location,   Offenders.get(i).getLocation());
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_IDs,        Offenders.get(i).getID());
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Date,       Offenders.get(i).getDate());
            db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        }

    }

    @Override
    public void onRestart(){
        super.onRestart();
        setOffenders();
    }

    public void setOffenders(){
        ArrayList<String> names= new ArrayList<String>();
        ArrayList<String> lastNames= new ArrayList<String>();
        ArrayList<String> id= new ArrayList<String>();
        ArrayList<String> location= new ArrayList<String>();
        ArrayList<String> date= new ArrayList<String>();
        int numberOfOffenders=0;

        try {

//CREATE NEW INTERNALL XML OR USE ALREADY CREATED(CREATED AT COMPILATION)
            InputStream is =null;
            if(new File(getFilesDir(), "OffendersUpdated").isFile()){
                is = openFileInput("OffendersUpdated");
                try {
                    InputStream fis = openFileInput("OffendersUpdated");
                    BufferedReader scanner = new BufferedReader(new InputStreamReader(fis) );

                    scanner.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                is = this.getAssets().open("Offenders.xml");
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();
            id.clear();names.clear();lastNames.clear();location.clear();date.clear();
           // Log.d("root", "Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("field");
           // Log.d("separator", "----------------------------");
            int a = 0;
            numberOfOffenders = nList.getLength() / 5;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //Log.d("element","\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
              //      Log.d("name", " " + nList.item(temp).getTextContent());
                    String s = nList.item(temp).getTextContent();
                    if (temp % 5 == 0) {   //id
                        id.add(s);
                    }
                    if (temp % 5 == 1) {
                        names.add(s);
                    }
                    if (temp % 5 == 2) {
                        lastNames.add(s);
                    }
                    if (temp % 5 == 3) {
                        location.add(s);
                    }
                    if (temp % 5 == 4) {
                        date.add(s);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Offenders.clear();

        for (int i = 0; i < numberOfOffenders; i++) {
            Offender offender = new Offender(id.get(i), names.get(i), lastNames.get(i), location.get(i), date.get(i));
            //Log.d("offender",offender.toString());
            Offenders.add(offender);
        }

        //READ ORIGINAL FROM ASSETS
        String filename="OffendersUpdated";
        FileOutputStream outputstream;
        try{
            if(new File(getFilesDir(), "OffendersUpdated").isFile()){
                InputStream is = openFileInput("OffendersUpdated");
                InputStreamReader isr = new InputStreamReader(is);
                char[] inputBuffer = new char[is.available()];
                isr.read(inputBuffer);
                String data = new String(inputBuffer);
               // Log.d("my tag",data);
                isr.close();
                //CREATE COPY OF XML DATABASE IN INTERNAL STORAGE
                outputstream=openFileOutput(filename, Context.MODE_PRIVATE);
                outputstream.write(data.getBytes());
                outputstream.close();
            }else {
                InputStream is = this.getAssets().open("Offenders.xml");
                // String path = this.getAssets().open("Offenders.xml").toString();
                InputStreamReader isr = new InputStreamReader(is);
                char[] inputBuffer = new char[is.available()];
                isr.read(inputBuffer);
                String data = new String(inputBuffer);
                //Log.d("my tag",data);
                isr.close();
                //CREATE COPY OF XML DATABASE IN INTERNAL STORAGE
                outputstream=openFileOutput(filename, Context.MODE_PRIVATE);
                outputstream.write(data.getBytes());
                outputstream.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void sendMessage(View view){
        Toast popup = Toast.makeText(getApplicationContext(), R.string.loading ,Toast.LENGTH_SHORT);
        popup.show();
        filteredOffenders.clear();
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText =(EditText ) findViewById(R.id.editText3);
        String searchedtext = editText.getText().toString();
        String searchedtextUpper=  searchedtext.toUpperCase();
        searchedtextUpper.length();

        for(Offender o: Offenders){
            if(o.getLastName().contains(searchedtextUpper)){
                filteredOffenders.add(o);
            }else {
            }
        }
//        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, searchedtext);
        if(filteredOffenders.size()==0){
            intent.putExtra("offendersList", Offenders);
        }else{
            intent.putExtra("offendersList", filteredOffenders);
        }//File[] files0=getFilesDir().listFiles();
      //  intent.putExtra("database", db);
        startActivity(intent);
    }

    public void openScanner(View view){ // clear other fields search for one

        filteredOffenders.clear();
        Intent intent = new Intent(this, BarcodeSample1.class);
        intent.putExtra("offendersList", Offenders);
        startActivity(intent);
    }

    public void inout(View view){

        Intent intent = new Intent(this, InOut.class);
        ArrayList <String> inOuts= new ArrayList<>();

        SQLiteDatabase dbReadable = mDbHelper.getReadableDatabase();
        Cursor cursor2 = dbReadable.query(
                FeedReaderContract.FeedEntry.TABLE_InOut, // The table to query
                null,                                     // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        while(cursor2.moveToNext()) {
            String column1= cursor2.getString(cursor2.getColumnIndex("InOutID"));
            String column3= cursor2.getString(cursor2.getColumnIndex("OID"));
            String column2= cursor2.getString(cursor2.getColumnIndex("Direction"));
            String column4= cursor2.getString(cursor2.getColumnIndex("Reason"));
            String column5= cursor2.getString(cursor2.getColumnIndex("Comment"));
            Log.d("inout post group insert",column1+" | "+ column3+" | "+column2+" | "+column4 +" | comment: "+ column5);
            inOuts.add(column1+" | "+ column3+" | "+column2+" | "+column4 + " |  "+ column5+"\n");
        }
        intent.putExtra("inouts",inOuts);
        startActivity(intent);
    }
}
