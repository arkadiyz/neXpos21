package com.arkadiy.enter.imenu;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private String calcString="";
    private TextView textViewScreenCalc;
    private boolean ifHaveDot=false;
    private ListView listViewSummary;
    private ArrayAdapter<String> listAdapter;

    private static final String general="כללי";
    private Button btn_calc;
    private String numCalc;
    private TextView screenCalc;
    private ArrayList<Product> productList;
    private LinkedList<String> lightDrinks;
    private  LinkedList <String> beers;
    private static boolean flag = false;
    private GridLayout layout;
    private LinearLayout layout2=null;
    private  ArrayList<String> productName;
    private  ArrayList<String> categoryName;

    private static final String TAG = "MainActivity";
    private ProductListAdapter adapter;
    private Product p = null;
    private static DataConfig dataConfig=null;
    //    private static SQLiteDatabase productsDB.db=null;
    private static SimpleCursorAdapter cursorAdapter=null;
    private static ListView listView=null;
    private SQLiteDatabase productsDB=null;
    private String BLABLABLA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate: Started");
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = null;

        textViewScreenCalc = (TextView)findViewById(R.id.textViewScreenCalc);
        listViewSummary = (ListView)findViewById(R.id.listViewSummary);
        productList  = new ArrayList<Product>();
        dataConfig=new DataConfig(MainActivity.this);



        adapter = new ProductListAdapter(this, R.layout.adapter_view_layout,productList);
        listViewSummary.setAdapter(adapter);


        productName = new ArrayList<>();
        categoryName=new ArrayList<>();

        dataConfig.openDatabase();

        File database = getApplicationContext().getDatabasePath(DataConfig.DBNAME);

        if(false == database.exists()) {
            dataConfig.getReadableDatabase();
        }
            if(copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();

            }

            categoryName=dataConfig.getItemsGroup();
        layout=(GridLayout)findViewById(R.id.gridLayoutCategory) ;
        fillInMenue(categoryName,layout);


        prefs = getSharedPreferences("com.arkadiy.enter.imenu", MODE_PRIVATE);
        }
        //Get product list in db when db exists
//        productList = dataConfig.getDataFromDataBase("beers");









//
//        if(prefs.getBoolean("firstrun", true)) //checks if app runs first time
//        {
//        }



//            prefs.edit().putBoolean("firstrun", false).commit();





    public void calc_onClick(View view) {
        switch (view.getId())
        {
            case R.id.button0:
                calcString+="0";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button1:
                calcString+="1";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button2:
                calcString+="2";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button3:
                calcString+="3";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button4:
                calcString+="4";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button5:
                calcString+="5";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button6:
                calcString+="6";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button7:
                calcString+="7";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button8:
                calcString+="8";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.button9:
                calcString+="9";
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.buttonC:
                calcString="";
                ifHaveDot = false;
                textViewScreenCalc.setText(calcString);
                break;
            case R.id.buttonDot:
                if(!ifHaveDot) {
                    calcString += ".";
                    ifHaveDot = true;
                }
                break;
            case R.id.buttonEnter:
                addProductToListView(general);
                calcString="";
                textViewScreenCalc.setText(calcString);
                ifHaveDot = false;
                break;
        }
    }



    public void loadAll(View v) {


        switch (v.getId()) {
//            case R.id.beers:
//                changeProduct("beers");
//                flag = true;
//                break;


            case R.id.light:
                changeProduct("light_drinks");
                flag = true;
                break;


            default:
                if (flag)
                {
                    layout.removeAllViews();
                    flag=false;
                }

                break;


        }


    }


    public void changeProduct(String product){
        productName=dataConfig.getDataFromDataBase(product);
        layout = (GridLayout) findViewById(R.id.gridLayoutItem);
        fillInMenue(productName,layout);
    }



    public void fillInMenue(ArrayList <String> products,GridLayout l) {   //adds productsDB.db to menue from database


        int width = 150;
        int height = 80;
        if (flag)
        {
            layout.removeAllViews();
            flag=false;
        }
        layout = l;


        for (int i = 0; i < products.size(); i++) {
            String name = products.get(i);
            Button tempBut = new Button(MainActivity.this);
            tempBut.setLayoutParams(new ViewGroup.LayoutParams((int)setSizeInButton(width), (int)setSizeInButton(height)));
            tempBut.setText(name);
            tempBut.setOnClickListener(new View.OnClickListener(){

                public void onClick(View view){
                    Button b=(Button)view;
                    String name=b.getText().toString();
                    addProductToListView(name);
                }
            });
            layout.addView(tempBut);


        }
    }


//    public void adjustButtonSize(Button button) {
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        int width = displayMetrics.widthPixels;
//        int height = displayMetrics.heightPixels;
//        ViewGroup.LayoutParams params = button.getLayoutParams();
//        params.height = height / 10;         // 10%
//        params.width = ((width * 20) / 100); // 20%
//        button.setLayoutParams(params);
//    }



    public void addProductToListView(String name){
        p = new Product(name,"1",calcString);
        listViewSummary.setAdapter(adapter);
        productList.add(p);
    }



    private boolean copyDatabase(Context context) {


        try {

        String DB_PATH;

            InputStream inputStream = context.getAssets().open(DataConfig.DBNAME);
//            String outFileName = DataConfig.DBLOCATION + DataConfig.DBNAME;
            if(android.os.Build.VERSION.SDK_INT >= 17) {
                DB_PATH = context.getApplicationInfo().dataDir + "/databases/"+DataConfig.DBNAME;
            } else {
                DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
            }
            OutputStream outputStream = new FileOutputStream(DB_PATH);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public float setSizeInButton(int dp){
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp , displayMetrics);


        return pixels;
    }
}







