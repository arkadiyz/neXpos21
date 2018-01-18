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
    private Product item;
    private ArrayList<Product> itemsList=null;
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



public ArrayList <String> getItemsGroup(){
    ArrayList<String> list=new ArrayList<>();

    String bla=getDatabaseName();
    String s= mDatabase.toString();
    Cursor cursor=mDatabase.rawQuery(  "select * from itemsGroup",null);


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




    public ArrayList <Product> getProductsList(String category){

        String bla=getDatabaseName();
        itemsList=new ArrayList<>();
        Cursor cursor=mDatabase.rawQuery(  "select * from items left join itemsGroup ON items.ig_id=itemsGroup._id where itemsGroup.name like '%"+category+"%'",null);


        if(cursor.moveToFirst())
        {
            do{
                 String productName=cursor.getString(1);
                 float p=cursor.getFloat(2);
                 String price=Float.toString(p);
                String picPath=cursor.getString(3);
                String barcode=cursor.getString(4);
                 String ig_id=cursor.getString(5);
                 int id=Integer.parseInt(ig_id);

                item=new Product(productName,price,barcode,picPath,id);
                itemsList.add(item);

            }while(cursor.moveToNext());
        }

        return itemsList;
    }




    public void createItemIfNotExists(int id,String name, float price, String path, String barcode, int ig_id){
        String item="INSERT OR REPLACE INTO items (_id, name, price, picture_path, barcode, ig_id) VALUES("+id+", '"
                +name+"', "
                +price+", '"
                +path+"', '"
                +barcode+"', "
                +ig_id+")";

        mDatabase.execSQL(item);
    }
    public void createCategoryIfNotExists(int id , String name , String picturePath , int active)
    {
        String group="INSERT OR REPLACE INTO itemsGroup (_id, name, picture_path, active) VALUES("+id+", '"
                +name+"', '"
                +picturePath+"', "
                +active+")";
        mDatabase.execSQL(group);
    }

    public Product getProductByBarcode(String barcode) {
        Product p=null;
        String str = "SELECT * FROM items " +
                "WHERE barcode='" + barcode + "'";
        Cursor cursor = mDatabase.rawQuery(str, null);
        if (cursor.moveToFirst()) {
            do {
                String productName = cursor.getString(1);
                float pr = cursor.getFloat(2);
                String price = Float.toString(pr);
                String picPath = cursor.getString(3);
                String bar = cursor.getString(4);
                String ig_id = cursor.getString(5);
                int id = Integer.parseInt(ig_id);

                p = new Product(productName, price, barcode, picPath, id);


            } while (cursor.moveToNext());

        }
        return p;

    }




}