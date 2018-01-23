package com.arkadiy.enter.imenu;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements Callbacks {
    private String calcString = "";
    private TextView textViewScreenCalc;
    private boolean ifHaveDot = false;
    private ListView listViewSummary;
    private ArrayAdapter<String> listAdapter;

    private static final String general = "כללי";
    private Button btn_calc;
    private String numCalc;
    private TextView screenCalc;
    private ArrayList<Product> productList;
    private ArrayList<Product> itemsList;
    private LinkedList<String> lightDrinks;
    private LinkedList<String> beers;
    private boolean flag = true;
    private GridLayout layout;
    private LinearLayout layout2 = null;
    private ArrayList<String> productName;
    private ArrayList<String> categoryName;
    private PrinterManager printerManager;

    private static final String TAG = "MainActivity";
    private ProductListAdapter adapter;
    private Product p = null;
    private static DataConfig dataConfig = null;
    //    private static SQLiteDatabase productsDB.db=null;
    private static SimpleCursorAdapter cursorAdapter = null;
    private static ListView listView = null;
    private SQLiteDatabase productsDB = null;
    private ArrayList<Product> categoryProductsList = null;
    private static int width = 150;// db convert to pixel
    private static int height = 80;// db convert to pixelh
    private TextView textViewBarCode;
    private TextView textViewTotalNumber;
    private int butWidth = 0;
    private int butHeight = 0;
    private double sizeScreen;
    private String str = "";
    private Button serverButton = null;
    private ProgressDialog progressDialog;
    private String s = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Started");
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = null;
        serverButton = (Button) findViewById(R.id.btnUpData);
        serverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateServer();
            }
        });
        textViewScreenCalc = (TextView) findViewById(R.id.textViewScreenCalc);
        listViewSummary = (ListView) findViewById(R.id.listViewSummary);
        productList = new ArrayList<Product>();
        itemsList = new ArrayList<>();
        dataConfig = new DataConfig(MainActivity.this);
        adapter = new ProductListAdapter(this, R.layout.adapter_view_layout, productList);
        listViewSummary.setAdapter(adapter);
        prefs = getSharedPreferences("com.arkadiy.enter.imenu", MODE_PRIVATE);
        productName = new ArrayList<>();
        categoryName = new ArrayList<>();

        File database = getApplicationContext().getDatabasePath(DataConfig.DBNAME);
        if (false == database.exists()) {
            dataConfig.getReadableDatabase();
        }

        dataConfig.openDatabase();
        if (prefs.getBoolean("firstrun", true)) //checks if app runs first time
        {
            if (copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();

            }
        }
        prefs.edit().putBoolean("firstrun", false).commit();


        categoryName = dataConfig.getItemsGroup();
        layout = (GridLayout) findViewById(R.id.gridLayoutCategory);
        getSize();

        fillInMenue(categoryName, layout);
        prefs = getSharedPreferences("com.arkadiy.enter.imenu", MODE_PRIVATE);
        textViewTotalNumber = (TextView) findViewById(R.id.textViewTotalNumber);


        textViewBarCode = (TextView) findViewById(R.id.textViewBarCode);
        textViewScreenCalc.setFocusable(true);
        textViewBarCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                str = textViewBarCode.getText().toString();
                textViewBarCode.setText("");

                if (!str.isEmpty()) {
                    Product p = dataConfig.getProductByBarcode(str);
                    addProductToListView2(p);
                }
                textViewScreenCalc.setFocusable(false);
                textViewScreenCalc.setFocusableInTouchMode(false);
                textViewBarCode.requestFocus();

                onKeyUp(0, event);
                return true;


            }
        });
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            UsbInterface intf = device.getInterface(0);

        }

        s = "ארקדי הלך לניורופידבק ויחזור עוד איזה שעה וחצי";

        this.printerManager = PrinterManager_.getInstance_(this);


    }

    //=======================================================
    public void calc_onClick(View view) {

        switch (view.getId()) {
            case R.id.button0:
                calcString += "0";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button1:
                calcString += "1";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button2:
                calcString += "2";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button3:
                calcString += "3";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button4:
                calcString += "4";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button5:
                calcString += "5";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button6:
                calcString += "6";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button7:
                calcString += "7";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button8:
                calcString += "8";
                textViewScreenCalc.setText(calcString);

                break;
            case R.id.button9:
                calcString += "9";
                textViewScreenCalc.setText(calcString);

                break;
            case R.id.buttonC:
                calcString = "";
                ifHaveDot = false;
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.buttonDot:
                if (!ifHaveDot) {
                    calcString += ".";
                    ifHaveDot = true;
                }
                break;
            case R.id.buttonEnter:
                addProductToListView(general);
                calcString = "";
                textViewScreenCalc.setText(calcString);
                ifHaveDot = false;
//                printerManager.printJob(new PrinterJob(s));
//                printerManager.printJob(new PrinterJob("\n"));
                printReceipt();
                break;
        }

    }

    //=======================================================
    public void loadAll(View v) {

        Button b = (Button) v;

        if (layout.getId() == R.id.gridLayoutItem)
            layout.removeAllViews();


        changeProduct(b.getText().toString());

    }

    //=======================================================
    public void changeProduct(String product) {
        getCategoryProductsList(product);
        layout = (GridLayout) findViewById(R.id.gridLayoutItem);
        fillInMenue(productName, layout);

    }

    //=========================================================
    public void fillInMenue(ArrayList<String> products, GridLayout l) {   //adds productsDB.db to menue from database


        layout = l;


        for (int i = 0; i < products.size(); i++) {
            String name = products.get(i);
            Button tempBut = new Button(MainActivity.this);
            tempBut.setText(name);
            tempBut.setLayoutParams(new ViewGroup.LayoutParams(butWidth, butHeight));

            if (layout.getId() == R.id.gridLayoutItem) {
                if (flag) {
                    getSize();
                    flag = false;
                }

                tempBut.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View view) {
                        Button b = (Button) view;

                        String name = b.getText().toString();
                        addProductToListView(name);
                    }
                });

            }

            if (layout.getId() == R.id.gridLayoutCategory) {


                tempBut.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View view) {
                        loadAll(view);
                    }
                });
            }

            layout.addView(tempBut);


        }

    }

    //========================================================
    public void addProductToListView(String name) {


        String price = getPrice(name);

        p = new Product(name, "1", price);
        listViewSummary.setAdapter(adapter);

        productList.add(p);
        listViewSummary.setSelection(adapter.getCount() - 1);

    }

    public void addProductToListView2(Product prod) {


        String price = prod.getPrice();
        listViewSummary.setAdapter(adapter);
        productList.add(prod);
        listViewSummary.setSelection(adapter.getCount() - 1);

    }

    //=======================================================
    private boolean copyDatabase(Context context) {
        try {

            String DB_PATH;
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                DB_PATH = context.getApplicationInfo().dataDir + "/databases/" + DataConfig.DBNAME;
            } else {
                DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
            }

            InputStream inputStream = context.getAssets().open(DataConfig.DBNAME);
            OutputStream outputStream = new FileOutputStream(DB_PATH);
            byte[] buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity", "DB copied");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //=======================================================
    public float setSizeInButton(int dp) {
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);


        return pixels;
    }

    //=======================================================
    public void getCategoryProductsList(String name) {
        itemsList = dataConfig.getProductsList(name);
        setItemsNames();
    }

    //=======================================================
    public void setItemsNames() {
        int length = itemsList.size();
        productName.clear();
        for (int i = 0; i < length; i++) {
            productName.add(itemsList.get(i).getProductName());

        }
    }

    //=======================================================
    public String getPrice(String itemName) {
        String p = null;
        if (itemName.equals(general))
            return calcString;

        for (int i = 0; i < itemsList.size(); i++) {
            if (itemsList.get(i).getProductName() == itemName) {
                p = itemsList.get(i).getPrice();
                break;
            }
        }

        return p;
    }

    //=======================================================
    public String getBarcode(String itemName) {
        String b = null;

        for (int i = 0; i < itemsList.size(); i++) {
            if (itemsList.get(i).getProductName() == itemName) {
                b = itemsList.get(i).getBarcode();
                break;
            }

        }
        return b;

    }

    //=======================================================
    private void getSize() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
        double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
        double screenInches = Math.sqrt(x + y);
        Log.d("debug", "Screen Inches:" + screenInches);
        sizeScreen = screenInches;
        adjustSize();

    }

    //=======================================================
    private void adjustSize() {

        if (sizeScreen > 10) {
            butWidth = (int) setSizeInButton(width + 7);
            butHeight = (int) setSizeInButton(height);
            layout.setColumnCount(5);
        } else if (sizeScreen <= 9) {
            butWidth = (int) setSizeInButton(width - 2);
            butHeight = (int) setSizeInButton(height);
            layout.setColumnCount(3);
        } else if (sizeScreen <= 10 && sizeScreen >= 9.1) {
            butWidth = (int) setSizeInButton(width + 25);
            butHeight = (int) setSizeInButton(height);
            layout.setColumnCount(4);
        }

    }

    //=======================================================
    public void updateServer() {
        PostSender postSender = new PostSender(this);
        postSender.execute();
    }


    @Override
    public void onAboutToStart() {
//        progressDialog=new ProgressDialog(this);
//        progressDialog.setTitle("Downloading...");
//        progressDialog.setMessage("Please Wait...");
//        progressDialog.show();
    }


    @Override
    public void onSuccess(String downloadedText) {

        if (downloadedText.equals("yes"))
            return;
        else {
            DataBaseFiller dataBaseFiller = new DataBaseFiller(this, this);
            dataBaseFiller.execute(downloadedText);
        }
        return;


    }

    @Override
    public void onReportProgress(int progress) {

    }

    @Override
    public void onError(int httpStatusCode, String errorMessage) {
        progressDialog.dismiss();

        int x = httpStatusCode;
    }

    @Override
    protected void onResume() {
        super.onResume();
        printerManager.registerReceivers();
    }

    @Override
    protected void onDestroy() {
        printerManager.unRegisterReceivers();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        printerManager.unRegisterReceivers();
        super.onPause();
    }


    private void printReceipt() {
        for (Product product : productList) {
            {
                printerManager.printJob(new PrinterJob(product.getProductName()));
                printerManager.printJob(new PrinterJob("\n"));

            }

        }
    }

}






