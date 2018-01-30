package com.arkadiy.enter.imenu;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Date;
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
    private ArrayList<Product> itemsList = null;

    public DataConfig(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;

    }


    public ArrayList<String> getDataFromDataBase(String categoryName) {
        ArrayList<String> list = new ArrayList<>();
        String bla = getDatabaseName();
        String s = mDatabase.toString();
        Cursor cursor = mDatabase.rawQuery("select * from items left join itemsGroup ON items.ig_id=itemsGroup._id where itemsGroup.name like '%" + categoryName + "%'", null);

        if (cursor.moveToFirst()) {
            String record = "";
            do {
                record = String.format("%s", cursor.getString(1));
                list.add(record);
                record = "";

            } while (cursor.moveToNext());
        }
        return list;
    }


    public ArrayList<String> getItemsGroup() {
        ArrayList<String> list = new ArrayList<>();

        String bla = getDatabaseName();
        String s = mDatabase.toString();
        Cursor cursor = mDatabase.rawQuery("select * from itemsGroup", null);


        if (cursor.moveToFirst()) {
            String record = "";
            do {
                record = String.format("%s", cursor.getString(1));
                list.add(record);
                record = "";

            } while (cursor.moveToNext());
        }

        return list;
    }


    public ArrayList<Product> getProductsList(String category) {

        String bla = getDatabaseName();
        itemsList = new ArrayList<>();
        Cursor cursor = mDatabase.rawQuery("select * from items left join itemsGroup ON items.ig_id=itemsGroup._id where itemsGroup.name like '%" + category + "%'", null);


        if (cursor.moveToFirst()) {
            do {
                String productName = cursor.getString(1);
                float p = cursor.getFloat(2);
                String price = Float.toString(p);
                String picPath = cursor.getString(3);
                String barcode = cursor.getString(4);
                String ig_id = cursor.getString(5);
                int id = Integer.parseInt(ig_id);

                item = new Product(productName, price, barcode, picPath, id);
                itemsList.add(item);

            } while (cursor.moveToNext());
        }

        return itemsList;
    }


    public void createItemIfNotExists(int id, String name, float price, String path, String barcode, int ig_id) {
        String item = "INSERT OR REPLACE INTO items (_id, name, price, picture_path, barcode, ig_id) VALUES(" + id + ", '"
                + name + "', "
                + price + ", '"
                + path + "', '"
                + barcode + "', "
                + ig_id + ")";

        try {
            mDatabase.execSQL(item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createCategoryIfNotExists(int id, String name, String picturePath, int active) {
        String group = "INSERT OR REPLACE INTO itemsGroup (_id, name, picture_path, active) VALUES(" + id + ", '"
                + name + "', '"
                + picturePath + "', "
                + active + ")";
        try {
            mDatabase.execSQL(group);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Product getProductByBarcode(String barcode) {
        Product p = null;
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


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if (mDatabase != null && mDatabase.isOpen()) {
            return;
        }
        mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
        }
    }

    public void insertIntoOrderItems(int id, int quantity, int status, String price,String name) {
        float pr = Float.parseFloat(price);
        String str = "INSERT INTO Order_items (Order_id, Quantity, Price, Status, Name) Values (" + id + ", " + quantity + ", " + pr + ", " + status +", '"+name+"')";
        mDatabase.execSQL(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int insertIntoOrders(String date) {

        String str = "INSERT INTO Orders (Date_Time, Status, Total) Values ('" + date + "', 0, 0)";
        mDatabase.execSQL(str);
        str="SELECT last_insert_rowid()";
        Cursor cursor = mDatabase.rawQuery(str, null);
        String id="";
                if (cursor.moveToFirst())
                    id = cursor.getString(0);

               int x= Integer.parseInt(id);


         return x;

    }




    }