package com.example.ericlaroche.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import static com.example.ericlaroche.myapplication.R.id.spinner_direction;

public class Multichange extends AppCompatActivity {
    ArrayList<String> sendID = new ArrayList<>();
    ArrayList <String>  name = new ArrayList<>();
    ArrayList <Integer> position= new ArrayList<>();
    TextView offenderName;
    TableRow tr;
SQLiteDatabase db;
    FeedReaderDbHelper dbHelper;
    Spinner spinnerdirection ;
    Spinner spinner_reason ;
    EditText comment ;
    String spinerDirectionString;
    String spinnerReasonString ;
    String commentString  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multichange);

         dbHelper =new FeedReaderDbHelper(getApplicationContext());
        db =  dbHelper.getWritableDatabase();

        SQLiteDatabase dbReadable = dbHelper.getReadableDatabase();
        String[] projection={
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_LastName,
                FeedReaderContract.FeedEntry.COLUMN_NAME_IDs
        };

        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_LastName + " = ?";
        String [] selectionArgs = {"My title"};

       // String sortOrder = FeedReaderContract.FeedEntry.TABLE_NAME

        Cursor cursor = dbReadable.query(
                FeedReaderContract.FeedEntry.TABLE_NAME, // The table to query
                null,                                     // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );
        while(cursor.moveToNext()) {//goes from last to first DESC
            String column1= cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry._ID));
            String column3= cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_LastName));
            String column2= cursor.getString(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_IDs));
            Log.d("inout query",column1+" | "+ column3+" | "+column2+" | ");
        }
        Cursor cursor2 = dbReadable.query(
                FeedReaderContract.FeedEntry.TABLE_InOut, // The table to query
                null,                                     // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        while(cursor2.moveToNext()) {//goes from last to first DESC
            String column1= cursor2.getString(cursor2.getColumnIndex("InOutID"));
            String column3= cursor2.getString(cursor2.getColumnIndex("OID"));
            String column2= cursor2.getString(cursor2.getColumnIndex("Direction"));
            String column4= cursor2.getString(cursor2.getColumnIndex("Reason"));
            Log.d("inout query",column1+" | "+ column3+" | "+column2+" | "+column4);
        }


        Intent intent= getIntent();
        name = intent.getStringArrayListExtra(DisplayMessageActivity.NAME_SEARCH);
        position = intent.getIntegerArrayListExtra(DisplayMessageActivity.POSITION);
        sendID = intent.getStringArrayListExtra(DisplayMessageActivity.ID);

        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.location, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner directionSpinner = (Spinner) findViewById(spinner_direction);
        ArrayAdapter<CharSequence> directionAdapter = ArrayAdapter.createFromResource(this,R.array.direction, android.R.layout.simple_spinner_item);
        directionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directionSpinner.setAdapter(directionAdapter);

        Spinner reasonSpinner = (Spinner) findViewById(R.id.spinner_reason);
        ArrayAdapter<CharSequence> reasonAdapter = ArrayAdapter.createFromResource(this,R.array.Reason, android.R.layout.simple_spinner_item);
        reasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonSpinner.setAdapter(reasonAdapter);

        TableLayout tl = (TableLayout) findViewById(R.id.table);

        for (int i=0; i<name.size();i++) {
            tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            offenderName = new TextView(this);
            String f= name.get(i);
            offenderName.setText(name.get(i));
            tr.addView(offenderName);
            tl.addView(tr);
        }

        //keep keyboard closed
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
}

    public void updateAllLocations(View view){
        Snackbar popup = Snackbar.make(view,"Saved" ,Snackbar.LENGTH_SHORT);
        popup.show();

        try {
            InputStream input = openFileInput("OffendersUpdated");
            DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentbuilderfactory.newDocumentBuilder();
            Document document =documentBuilder.parse(input);

            TransformerFactory tf=TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));

          //  Node database =document.getElementsByTagName("database").item(0);
            // <NodeList allOffenders = database.getChildNodes();//odd numbers contain data

            NodeList fields =document.getElementsByTagName("field");
            String newLocal;

            Spinner spinner = (Spinner) findViewById(R.id.spinner2);
            newLocal = spinner.getSelectedItem().toString();
            for (int j=0 ; j<sendID.size(); j++) {
                for (int i = 0 ; i < fields.getLength() ; i++) {
                    String element = document.getElementsByTagName("field").item(i).getTextContent().toString();
                    String id = sendID.get(j);
                    if (element.equals(id)) {

                        Node location = fields.item(i + 3);
                        location.setTextContent(newLocal);
                    }
                }
            }
            //SAVE CHANGES TO FILE IN INTERNAL STORAGE
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

            for (int j=0 ; j<sendID.size(); j++) {
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_Location, newLocal);

                String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_IDs+ " LIKE ?";
                String[] selectionargs = {sendID.get(j)};

                int count = db.update(FeedReaderContract.FeedEntry.TABLE_NAME,values,selection,selectionargs);

            }
/* PRINT TABLE IN CONSOLE
        db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME, // The table to query
                null,                                     // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        while(cursor.moveToNext()) {//goes from last to first DESC
            String column1= cursor.getString(cursor.getColumnIndex("Location"));
            String column3= cursor.getString(cursor.getColumnIndex("LastName"));
            String column2= cursor.getString(cursor.getColumnIndex("FullName"));
            String column4= cursor.getString(cursor.getColumnIndex("ID"));
            Log.d("inout query",column1+" | "+ column3+" | "+column2+" | "+column4);
        }
*/
        }catch(Exception e){
        }
    }

    public void writeInOutTable(View v){

        dbHelper =new FeedReaderDbHelper(getApplicationContext());
        db =  dbHelper.getWritableDatabase();

        spinnerdirection =(Spinner)findViewById(R.id.spinner_direction);
         spinerDirectionString = spinnerdirection.getSelectedItem().toString();
        spinner_reason =(Spinner)findViewById(R.id.spinner_reason);
         spinnerReasonString =spinner_reason.getSelectedItem().toString();
        comment =(EditText) findViewById(R.id.comment_area);
         commentString  = comment.getText().toString();



        new WriteDatabaseTask().execute(db,null,null);
      Snackbar popup = Snackbar.make(v,"Saved" ,Snackbar.LENGTH_SHORT);
        popup.show();
Log.d("hmm", "Success??");
    }

    private class WriteDatabaseTask extends AsyncTask<Object, Object, Void> {

        @Override
        protected Void doInBackground(Object... voids) {


            for (int i = 0 ; i < sendID.size();i++){
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry.InOut_OID, sendID.get(i));
                values.put(FeedReaderContract.FeedEntry.InOut_Direction,spinerDirectionString);
                values.put(FeedReaderContract.FeedEntry.InOut_Reason, spinnerReasonString);
                values.put(FeedReaderContract.FeedEntry.InOut_Comment, commentString);
                db.insert(FeedReaderContract.FeedEntry.TABLE_InOut, null, values);

            }

            SQLiteDatabase dbReadable = dbHelper.getReadableDatabase();
            Cursor cursor2 = dbReadable.query(
                    FeedReaderContract.FeedEntry.TABLE_InOut, // The table to query
                    null,                                     // The columns to return
                    null,                                     // The columns for the WHERE clause
                    null,                                     // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                      // The sort order
            );

            while(cursor2.moveToNext()) {//goes from last to first DESC
                String column1= cursor2.getString(cursor2.getColumnIndex("InOutID"));
                String column3= cursor2.getString(cursor2.getColumnIndex("OID"));
                String column2= cursor2.getString(cursor2.getColumnIndex("Direction"));
                String column4= cursor2.getString(cursor2.getColumnIndex("Reason"));
                String column5= cursor2.getString(cursor2.getColumnIndex("Comment"));

                Log.d("inout post group insert",column1+" | "+ column3+" | "+column2+" | "+column4 +" | comment: "+ column5);
            }

            return null;
        }
    }
}
