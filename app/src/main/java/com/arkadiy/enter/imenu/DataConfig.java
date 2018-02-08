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
                int itemId= cursor.getInt(0);
                String productName = cursor.getString(1);
                float p = cursor.getFloat(2);
                String price = Float.toString(p);
                String picPath = cursor.getString(3);
                String barcode = cursor.getString(4);
                String ig_id = cursor.getString(5);
                int id = Integer.parseInt(ig_id);

                item = new Product(productName, price, barcode, picPath, id,itemId);
                itemsList.add(item);

            } while (cursor.moveToNext());
        }

        return itemsList;
    }

public Product getProductById(int itemId){
    Product p = null;
    String str = "SELECT * FROM items " +
            "WHERE _id=" + itemId;
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

            p = new Product(productName, price, bar, picPath, id,itemId);


        } while (cursor.moveToNext());

    }
    return p;
}
    synchronized public void createItemIfNotExists(int id, String name, float price, String path, String barcode, int ig_id) {
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

    synchronized public void createCategoryIfNotExists(int id, String name, String picturePath, int active) {
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
                int itemId= cursor.getInt(0);
                String productName = cursor.getString(1);
                float pr = cursor.getFloat(2);
                String price = Float.toString(pr);
                String picPath = cursor.getString(3);
                String bar = cursor.getString(4);
                String ig_id = cursor.getString(5);
                int id = Integer.parseInt(ig_id);

                p = new Product(productName, price, barcode, picPath, id,itemId);


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

    synchronized public void insertIntoOrderItems(int id, int quantity, int status, String price,String name,int itemId) {
        float pr = Float.parseFloat(price);
        String str = "INSERT INTO Order_items (Order_id, Quantity, Price, Status, Item_id) Values (" + id + ", " + quantity + ", " + pr + ", " + status +", '"+itemId+"')";
        mDatabase.execSQL(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    synchronized public int insertIntoOrders(String date) {

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


    public void createNewPayment(float summ,String dateTime,int orderId,int type){
        String str = "INSERT INTO Payment (Payment_type, Date_Time, Summ, Order_id) Values ("+type+", '"+dateTime+"', "+summ+", "+orderId+")";
        mDatabase.execSQL(str);

    }

    public void updateTotalOrder(int orderId,float total){
        String str="Update Orders set Total="+total+" WHERE Order_id = "+orderId+";";
        mDatabase.execSQL(str);

    }

    public void updateStatusOrder(int orderId){
        String str="Update Orders set Status="+1+" WHERE Order_id = "+orderId+";";
        mDatabase.execSQL(str);

    }
    public void updateStatusItem(int orderId){
        String str="Update Order_items set Status="+1+" WHERE Order_id = "+orderId+";";
        mDatabase.execSQL(str);

    }


    public void insertPaymentIntoOrder(int indexOrder)
    {

    }

    public CashInformation getCashInformation() {
        CashInformation cashInformation = null;
        String str = "Select * from Global";

        try {
            Cursor cursor = mDatabase.rawQuery(str, null);
            if (cursor.moveToFirst()) {
                cashInformation = new CashInformation(cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cashInformation;
    }


    public String getMasKabala() {
        int answer = 0;
        String str="";
        try {
             str= "Select NumInvoice from In_num";
            Cursor cursor = mDatabase.rawQuery(str, null);
            if (cursor.moveToFirst()) {
                answer = cursor.getInt(0);
                str = "UPDATE In_num SET NumInvoice=NumInvoice+1";
                mDatabase.execSQL(str);

            }

        }catch(Exception ex){
                ex.printStackTrace();
            }

            String cashId=getCashInformation().getCashId();

        str=cashId+"/"+Integer.toString(answer);
            return str;

        }


public ArrayList<Payment> getAllPaymentsBetweenTwoDates(String firstDate,String secondDate) {
    String str = "select * from Payment where Date_Time between '" + firstDate + "' and '" + secondDate + "'";
    Cursor cursor = mDatabase.rawQuery(str, null);
    Payment payment;
    ArrayList<Payment> payments = new ArrayList<>();
    if (cursor.moveToFirst()) {
        do {
            int type = cursor.getInt(1);
            String date = cursor.getString(2);
            float amount = cursor.getFloat(3);
            int orderId = cursor.getInt(4);
            String cardCompany=cursor.getString(5);


            payment = new Payment(type, amount, date, orderId,cardCompany);
            payments.add(payment);

        } while (cursor.moveToNext());

    }
    return payments;
}
    public ArrayList<Payment> getAllPayments() {
        String str = "select * from Payment";
        Cursor cursor = mDatabase.rawQuery(str, null);
        Payment payment;
        ArrayList<Payment> payments = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                int type = cursor.getInt(1);
                String date = cursor.getString(2);
                float amount = cursor.getFloat(3);
                int orderId = cursor.getInt(4);
                String cardCompany=cursor.getString(5);


                payment = new Payment(type, amount, date, orderId,cardCompany);
                payments.add(payment);

            } while (cursor.moveToNext());

        }
        return payments;
    }


public ArrayList<Payment>getZed(String dateAndTimeNow,Employee employee,String lastDate){

        String str;


        if(lastDate==null)
        {
            str="INSERT INTO DohZed (Date_Time, Employee_id) Values ('"+dateAndTimeNow+"',"+employee.getId()+")";
            mDatabase.execSQL(str);
            return getAllPayments();
        }
        str="INSERT INTO DohZed (Date_Time, Employee_id) Values ('"+dateAndTimeNow+"',"+employee.getId()+")";
        mDatabase.execSQL(str);

        return getAllPaymentsBetweenTwoDates(lastDate,dateAndTimeNow);
}

public String getLastZedDateTime(){
    String str="SELECT Date_Time FROM DohZed WHERE _id = (SELECT MAX(_id)  FROM DohZed)";
    Cursor cursor = mDatabase.rawQuery(str, null);
    cursor.moveToFirst();
    if (cursor.getCount()==-1)
    {
        cursor.close();
        return null;
    }else
        return cursor.getString(0);

}

public ArrayList<Payment> getDohX(String dateStartDoh,String dateTimeNow,Employee employee){
    String dateStart=dateStartDoh;

    if(dateStart==null)
        dateStart=getFirstOrderClosedDate();

    String str="INSERT INTO DohX (Date_Time, Employee_id) Values ('"+dateTimeNow+"',"+employee.getId()+")";
    mDatabase.execSQL(str);

    return getAllPaymentsBetweenTwoDates(dateStart,dateTimeNow);

}


public String getFirstOrderClosedDate(){
    String str="select Date_Time from Orders where Status=1 order by Date_Time asc limit 1";
    Cursor cursor = mDatabase.rawQuery(str, null);
    str=cursor.getString(0);
    return str;
}


    }



