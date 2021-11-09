package fr.cytech.b3.tp4;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class DatabaseAdapter {

    DatabaseHelper helper;
    SQLiteDatabase db;
    Context context;

    public DatabaseAdapter(Context context) {
        helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
        this.context = context;
    }

    public boolean exportDatabase() {
        /**First of all we check if the external storage of the device is available for writing.
         * Remember that the external storage is not necessarily the sd card. Very often it is
         * the device storage.
         */
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return false;
        }
        else {
            //We use the Download directory for saving our .csv file.
            File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!exportDir.exists())
            {
                exportDir.mkdirs();
            }
            File file = new File(exportDir, "Tasks.csv");
            try
            {
                file.createNewFile();
                CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
                Cursor curCSV = reloadCursor();
                //Write the name of the table and the name of the columns (comma separated values) in the .csv file.
                csvWrite.writeNext(curCSV.getColumnNames());
                while(curCSV.moveToNext())
                {
                    //Which column you want to export
                    String arrStr[] ={curCSV.getString(0),curCSV.getString(1)};
                    csvWrite.writeNext(arrStr);
                }
                csvWrite.close();
                curCSV.close();
            }
            catch(Exception exc) {
                //if there are any exceptions, return false
                return false;
            }
            //If there are no errors, return true.
            return true;
        }
    }

    public boolean importDatabase(Uri uri){
        /**First of all we check if the external storage of the device is available for writing.
         * Remember that the external storage is not necessarily the sd card. Very often it is
         * the device storage.
         */
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return false;
        }
        else {
            db.beginTransaction();
            try {
                // Read from csv file
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream));
                String[] line = csvReader.readNext();
                while ((line = csvReader.readNext()) != null) {
                    ContentValues values = new ContentValues();
                    values.put(DatabaseHelper.KEY_NAME, line[1]);
                    db.insert(DatabaseHelper.TABLE_NAME, null, values);
                }
            }
            catch(Exception exc) {
                //if there are any exceptions, return false
                return false;
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            //If there are no errors, return true.
            return true;
        }
    }

    public Cursor reloadCursor(){
        String[] columns = {DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME};

        return db.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);
    }

    public SimpleCursorAdapter populateListViewFromDB(){
        String[] columns = {DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns,null, null,null, null, null, null);
        String[] fromFieldNames = new String[]{
                DatabaseHelper.KEY_ID, DatabaseHelper.KEY_NAME
        };
        int[] toViewIDs = new int[]{R.id.item_id, R.id.item_name};
        return new SimpleCursorAdapter(
                context,
                R.layout.single_item,
                cursor,
                fromFieldNames,
                toViewIDs
        );
    }

    public boolean create(String data) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.KEY_NAME, data);
        boolean createSuccessful = db.insert(DatabaseHelper.TABLE_NAME, null, values) > 0;
        db.close();

        return createSuccessful;
    }

    public boolean delete(int id) {
        boolean deleteSuccessful;
        String[] whereArgs = {""+id};
        deleteSuccessful = db.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.KEY_ID + "=?", whereArgs) > 0;
        db.close();

        return deleteSuccessful;
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "mydb.db";
        private static final String TABLE_NAME = "Tasks";
        // When you do change the structure of the database change the version number from 1 to 2
        private static final int DATABASE_VERSION = 1;
        private static final String KEY_ID = "_id";
        private static final String KEY_NAME="name";
        private static final String CREATE_TABLE = "create table "+ TABLE_NAME+
                " ("+ KEY_ID+" integer primary key autoincrement, "+ KEY_NAME + " text)";
        private static final String DROP_TABLE = "drop table if exists "+ TABLE_NAME;
        private final Context context;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(CREATE_TABLE);
                Toast.makeText(context, "onCreate called", Toast.LENGTH_SHORT).show();
            }catch (SQLException e){
                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try{
                Toast.makeText(context, "onUpgrade called", Toast.LENGTH_SHORT).show();
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (SQLException e){
                Toast.makeText(context, ""+e, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
