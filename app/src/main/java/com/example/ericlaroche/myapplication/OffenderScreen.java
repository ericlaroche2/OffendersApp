package com.example.ericlaroche.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static com.example.ericlaroche.myapplication.R.id.textView;

public class OffenderScreen extends AppCompatActivity {
    String offenderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offender);

        Intent intent =getIntent();
        String message =intent.getStringExtra(DisplayMessageActivity.NAME_SEARCH);
        String currentLocation =intent.getStringExtra(DisplayMessageActivity.addLocal);
        offenderId =intent.getStringExtra(DisplayMessageActivity.ID);

        //put offender name
        TextView nametext=(TextView)findViewById(textView);
        nametext.setText(message);

        //set spinner locations
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.location, android.R.layout.simple_spinner_item);

        String[]locations;
        boolean match= false;
        int spinnerDefaultValue=0;
        locations = getResources().getStringArray(R.array.location);
        for (int i=0;i<locations.length;i++){
            if (currentLocation.equals(locations[i])){
                match=true;
                spinnerDefaultValue=i;
                break;
            }
        }

        if (match==false){
            //     adapter.clear();
            List<String> setLocations= Arrays.asList(getResources().getStringArray(R.array.location));
            ArrayList<String> setLocals= new ArrayList<String> (setLocations) ;
            setLocals.add("no location");

            ArrayAdapter<String> dataAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, setLocals);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setSelection(setLocals.size()-1);
        }else {
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(spinnerDefaultValue);
        }
    }

    public void updateLocation(View view){
        Snackbar popup = Snackbar.make(view,"Saved" ,Snackbar.LENGTH_SHORT);
        popup.show();
        try {


            //Get XML
            InputStream input = openFileInput("OffendersUpdated");
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentbuilderfactory.newDocumentBuilder();
            Document document =documentBuilder.parse(input);/////

            TransformerFactory tf=TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            // String output =writer.getBuffer().toString().replaceAll("|\r", "");

            //   Node offender =document.getElementsByTagName("row").item();//////////
            Node database =document.getElementsByTagName("database").item(0);
            NodeList allOffenders = database.getChildNodes();//odd numbers contain data
          //  int a = allOffenders.getLength();

            NodeList fields =document.getElementsByTagName("field");
            //  int c=  fields.getLength();//1000
            String newLocal="";
            for (int i=0;i<fields.getLength(); i++){
                String l= document.getElementsByTagName("field").item(i).getTextContent().toString();
                String m=offenderId.toString();
                if ( l.equals(m)){
                    //        Log.d("same","update!!"+"---" +i +"---"+ offenderId);
                    Spinner spinner =(Spinner)findViewById(R.id.spinner);
                    newLocal =spinner.getSelectedItem().toString();
                    Node location=fields.item(i+3);
                    location.setTextContent(newLocal);
                    break;
                }
            }
            //SAVE CHANGES TO XML FILE IN INTERNAL STORAGE
            TransformerFactory transformerFactory= TransformerFactory.newInstance();
            Transformer transformerupdate= transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult= new StreamResult(new File( getApplicationContext().getFilesDir(), "OffendersUpdated"));
            transformerupdate.transform(domSource, streamResult);


            DocumentBuilderFactory documentbuilderfactory2 = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder2 = documentbuilderfactory2.newDocumentBuilder();
            Document document2 =documentBuilder2.parse(openFileInput("OffendersUpdated"));/////

            TransformerFactory tf2=TransformerFactory.newInstance();
            Transformer transformer2 = tf2.newTransformer();
            transformer2.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
            StringWriter writer2 = new StringWriter();
            transformer2.transform(new DOMSource(document2), new StreamResult(writer2));


            //Update location to database
            FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
            SQLiteDatabase db =mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Location, newLocal);

            String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_IDs+ " LIKE ?";
            String[] selectionargs = {offenderId};

            int count = db.update(FeedReaderContract.FeedEntry.TABLE_NAME,values,selection,selectionargs);
            db = mDbHelper.getReadableDatabase();

        }catch(Exception e){

        }

    }



}

