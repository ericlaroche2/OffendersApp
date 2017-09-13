package com.example.ericlaroche.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import android.view.View.OnClickListener;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static com.example.ericlaroche.myapplication.MainActivity.EXTRA_MESSAGE;
import static com.example.ericlaroche.myapplication.R.array.location;
import static com.example.ericlaroche.myapplication.R.layout.activity_display_message;

public class DisplayMessageActivity extends AppCompatActivity implements OnClickListener {
    public static String ID="id";
    public static String addLocal="addLocal";
    public static String NAME_SEARCH="Name.Search";
    public static String POSITION="position";
    TableLayout tl;
    TableRow tr;
    TextView offenderName,offenderId;
    SQLiteDatabase db1;
    boolean idSort=false;
    boolean nameSort=true;
    ArrayList<Offender>Offenders=new ArrayList<Offender>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_display_message);
        Intent intent =getIntent();
        Offenders = (ArrayList<Offender>)intent.getSerializableExtra("offendersList");
        Collections.sort(Offenders, new SortByName());
        createDatabase();
        tl = (TableLayout) findViewById(R.id.maintable);
        tl.removeAllViews();
        sortOrder();
    }
    public void createDatabase(){
        TextView textView =(TextView ) findViewById(R.id.textViewFullName);
        textView.setOnClickListener(this);
        TextView textViewID =(TextView ) findViewById(R.id.textViewID);
        textViewID.setOnClickListener(this);

    }


    /** This function add the data to the table **/
    public void addData(){
        for (int i = 0; i < Offenders.size(); i++) {
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            CheckBox checkbox = new CheckBox(this);
            checkbox.setId(i);
            tr.addView(checkbox);

            offenderName = new TextView(this);
            offenderName.setTextSize(16);//default 14
            offenderName.setText(Offenders.get(i).getLastName()+", "+Offenders.get(i).getName());
            offenderName.setTextColor(Color.BLACK);
            offenderName.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            offenderName.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
            offenderName.setPadding(5, 5, 5, 5);
            String openName=Offenders.get(i).getLastName()+", "+ Offenders.get(i).getName()+ "  " + Offenders.get(i).getID() ;
            offenderName.setOnClickListener(new myListener(openName, Offenders.get(i).getID(), Offenders.get(i).getLocation()) );
            tr.addView(offenderName); // Adding textView to tablerow.


            offenderId = new TextView(this);
            offenderId.setText(Offenders.get(i).getID());
            offenderId.setTextColor(Color.BLACK);
            offenderId.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
            offenderId.setPadding(5, 5, 5, 5);
            offenderId.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            tr.addView(offenderId); //
            // Add the TableRow to the TableLayout
      //      tl = new TableLayout(this);
            tl.addView(tr, new TableLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            tr = new TableRow(this);
            tr.setLayoutParams(new LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            TextView pad  = new TextView(this);
            pad.setText("           ");
            tr.addView(pad);
            TextView local  = new TextView(this);
            local.setText("     "+Offenders.get(i).getLocation());
            tr.addView(local);
            TextView birthdate  = new TextView(this);
            birthdate.getCompoundDrawables();
            birthdate.setText("  "+Offenders.get(i).getDate());
            tr.addView(birthdate);

            tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    public class myListener implements OnClickListener{
        String openName;
        String id;
        String location;

        public myListener(String openName,String id, String local){
            this.openName=openName;
            this.id=id;
            this.location=local;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DisplayMessageActivity.this, OffenderScreen.class);
            String message = openName;
            intent.putExtra(NAME_SEARCH, message);
            intent.putExtra(ID, id);
            intent.putExtra(addLocal, location);
            DisplayMessageActivity.this.startActivity(intent);
        }
    }

    @Override
    public void onClick(View v){
        if (v.getId()==(R.id.textViewFullName)){
            if (nameSort==false){
                tl.removeAllViews();
                Collections.sort(Offenders, new SortByName());
                addData();
                nameSort=true;
                idSort=false;
            } else {
                Collections.sort(Offenders,new SortByReverseName());
                addData();
                nameSort=false;
                idSort=false;
            }
        }else if(v.getId()==R.id.textViewID){

            if (idSort==false) { tl.removeAllViews();
                Collections.sort(Offenders, new SortByID());
                addData();
                idSort=true;
                nameSort=false;
            }
            else {
                Collections.sort(Offenders, new SortByReverseName());
                addData();
                idSort=false;
                nameSort=false;
            }
        }
    }

    public void delete(View view) {
        db1.delete(FeedReaderContract.FeedEntry.TABLE_NAME, null, null);
    }

    public class SortByName implements Comparator<Offender> {
        @Override
        public int compare(Offender arg0, Offender arg1) {
            return arg0.getLastName().compareTo(arg1.getLastName());
        }
    }
    public class SortByReverseName implements Comparator<Offender> {
        @Override
        public int compare(Offender arg0, Offender arg1) {
            return arg1.getLastName().compareTo(arg0.getLastName());
        }
    }
    public class SortByID implements Comparator<Offender> {
        @Override
        public int compare(Offender arg0, Offender arg1) {
            return arg0.getID().compareTo(arg1.getID());
        }
    }
    public class SortByReverseID implements Comparator<Offender> {
        @Override
        public int compare(Offender arg0, Offender arg1) {
            return arg0.getID().compareTo(arg1.getID());
        }
    }
    public void sortOrder(){//String sortOrder, SQLiteDatabase db1

        tl = (TableLayout) findViewById(R.id.maintable);
        tl.removeAllViews();
        // addHeaders();
        addData();
    }
    public void reload(){ // clear other fields search for one

        ArrayList<String> names= new ArrayList<String>();
        ArrayList<String> lastNames= new ArrayList<String>();
        ArrayList<String> id= new ArrayList<String>();
        ArrayList<String> location= new ArrayList<String>();
        ArrayList<String> date= new ArrayList<String>();
        ArrayList<Offender>filteredOffenders=new ArrayList<Offender>();
        int numberOfOffenders=0;

        try {

            InputStream is =null;
            if(new File(getFilesDir(), "OffendersUpdated").isFile()){
                is = openFileInput("OffendersUpdated");
                try {String content="";
                    InputStream fis = openFileInput("OffendersUpdated");
                    BufferedReader scanner = new BufferedReader(new InputStreamReader(fis) );
                    //scanner.useDelimiter("\\Z");
                    while((content=scanner.readLine()) != null){
                     //   Log.d("TESTTING",content);
                    }
                    scanner.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                is = this.getAssets().open("myxml.xml");
            }
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            doc.getDocumentElement().normalize();
            id.clear();names.clear();lastNames.clear();location.clear();date.clear();
            NodeList nList = doc.getElementsByTagName("field");

            numberOfOffenders = nList.getLength() / 5;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

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

        try {
            InputStream input= this.getAssets().open("myxml.xml");


            InputStreamReader isr = new InputStreamReader(input);
            char[] inputBuffer = new char[input.available()];
            isr.read(inputBuffer);
            String data = new String(inputBuffer);

            isr.close();

            String content= data;
            File file;
            FileOutputStream outputStream;
            try{
                file = new File(getFilesDir(), "MyFilesDir");
                outputStream=new FileOutputStream(file);
                outputStream.write(content.getBytes());
                outputStream.close();

            }catch(IOException e){
                e.printStackTrace();
            }
        }catch(Exception e){
        }

        //READ ORIGINAL FROM ASSETS
        String filename="androidsite";
        FileOutputStream outputstream;
        try{
            InputStream input= this.getAssets().open("myxml.xml");
            String path=this.getAssets().open("myxml.xml").toString();
            String s="i";
            InputStreamReader isr = new InputStreamReader(input);
            char[] inputBuffer = new char[input.available()];
            isr.read(inputBuffer);

            String data = new String(inputBuffer);
           // Log.d("my tag",data);
            isr.close();

            //CREATE COPY OF XML DATABASE IN INTERNAL STORAGE
            outputstream=openFileOutput(filename, Context.MODE_PRIVATE);
            outputstream.write(data.getBytes());
            outputstream.close();
        }catch(Exception e){
            e.printStackTrace();
        }

       createDatabase();
    }
    @Override
    public void onRestart(){

        super.onRestart();
        reload();
        Collections.sort(Offenders, new SortByName());

        TextView textView =(TextView ) findViewById(R.id.textViewFullName);
        textView.setOnClickListener(this);
        TextView textViewID =(TextView ) findViewById(R.id.textViewID);
        textViewID.setOnClickListener(this);

        tl = (TableLayout) findViewById(R.id.maintable);
        tl.removeAllViews();
        sortOrder();
    }

    public void multiSelectSearch(View v){
        Intent intent = new Intent(DisplayMessageActivity.this, Multichange.class);
        intent.putExtra("offendersList", Offenders);
        CheckBox checked;
        ArrayList <String>  sendID = new ArrayList<>();
        ArrayList <String>  name = new ArrayList<>();
        ArrayList <Integer> position= new ArrayList<>();
        //for each checkbox check if it was checked, , add extra name to be updated
        for (int i = 0; i < Offenders.size() ; i++ ){
            checked=(CheckBox)findViewById(i);

          if (checked.isChecked()){
              name.add(Offenders.get(i).getLastName()+", "+ Offenders.get(i).getName()+ "  " + Offenders.get(i).getID());
              sendID.add(Offenders.get(i).getID());
              position.add(i);
          }
        }
        Collections.sort(Offenders, new SortByName());

        intent.putExtra(NAME_SEARCH, name);
        intent.putExtra(ID,sendID);
        intent.putExtra(POSITION,position);
        intent.putExtra("offendersList", Offenders);

        DisplayMessageActivity.this.startActivity(intent);
    }
}

