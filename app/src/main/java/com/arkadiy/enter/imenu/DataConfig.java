package com.arkadiy.enter.imenu;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by vadnu on 04/01/2018.
 */

public class DataConfig extends SQLiteOpenHelper {
    public static final String DBNAME = "productsDB.db";
    public static final String DBLOCATION = "/data/data/com/arkadiy/enter/imenu/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DataConfig(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if(mDatabase!=null) {
            mDatabase.close();
        }
    }




    public ArrayList<String> getDataFromDataBase(String categoryName){
        ArrayList <String> list= new ArrayList<>();
        String bla=getDatabaseName();
        String s= mDatabase.toString();
        Cursor cursor=mDatabase.rawQuery(  "select * from items left join itemsGroup ON items.ig_id=itemsGroup._id where itemsGroup.name like '%"+categoryName+"%'",null);

        if(cursor.moveToFirst())
        {
            String record="";
            do{
                record=String.format("%s",cursor.getString(1));
                list.add(record);
                record="";

            }while(cursor.moveToNext());
        }
        return list;
    }








//    public List<Product> getListProduct() {
//        Product product = null;
//        List<Product> productList = new ArrayList<>();
//        openDatabase();
//        Cursor cursor = mDatabase.rawQuery("SELECT * FROM PRODUCT", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3));
//            productList.add(product);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        closeDatabase();
//        return productList;
//    }

public ArrayList <String> getItemsGroup(){
    ArrayList<String> list=new ArrayList<>();

    String bla=getDatabaseName();
    String s= mDatabase.toString();
    Cursor cursor=mDatabase.rawQuery(  "select * from items",null);


    if(cursor.moveToFirst())
    {
        String record="";
        do{
            record=String.format("%s",cursor.getString(1));
            list.add(record);
            record="";

        }while(cursor.moveToNext());
    }

    return list;
}
}