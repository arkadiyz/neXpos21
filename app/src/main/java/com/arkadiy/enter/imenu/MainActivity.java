package com.arkadiy.enter.imenu;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private String calcString="";
    private float totalCalc = 0;
    private TextView textViewScreenCalc;
    private boolean ifHaveDot=false;
    private ListView listViewSummary;
    private static final String general="כללי";
    private   ArrayList<Product> productList;
    private static boolean flag = false;
    private GridLayout layout;
    private  LinkedList<String> productName;
    private static final String TAG = "MainActivity";
    private ProductListAdapter adapter;
    private static DataConfig dataConfig=null;
    private SQLiteDatabase productsDB=null;
    private TextView textViewTotalNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Started");
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = null;

        textViewScreenCalc = (TextView) findViewById(R.id.textViewScreenCalc);
        listViewSummary = (ListView) findViewById(R.id.listViewSummary);
        productList = new ArrayList<>();
        textViewTotalNumber = (TextView) findViewById(R.id.textViewTotalNumber);


        adapter = new ProductListAdapter(this, R.layout.adapter_view_layout, productList);


        productName = new LinkedList<String>();
        dataConfig = new DataConfig(this);
        productsDB = dataConfig.getWritableDatabase();
        dataConfig.setDb(productsDB);
        prefs = getSharedPreferences("com.arkadiy.enter.imenu", MODE_PRIVATE);


        if (prefs.getBoolean("firstrun", true)) //checks if app runs first time
        {
            dataConfig.setInsertQuery2("beers", "גולדסטאר", (float) 10.90);
            dataConfig.setInsertQuery2("beers", "היניקן", (float) 9.90);
            dataConfig.setInsertQuery2("beers", "קורונה", (float) 11.90);
            dataConfig.setInsertQuery2("beers", "קארלסברג", (float) 12.90);
            dataConfig.setInsertQuery2("beers", "מכאבי", (float) 15.93);

            dataConfig.createNewProductsTable("light_drinks");
            dataConfig.setInsertQuery2("light_drinks", "קולה", (float) 10.90);
            dataConfig.setInsertQuery2("light_drinks", "ספרייט", (float) 9.90);
            dataConfig.setInsertQuery2("light_drinks", "סודה", (float) 11.90);
            dataConfig.setInsertQuery2("light_drinks", "אינגדי", (float) 12.90);
            dataConfig.setInsertQuery2("light_drinks", "תפוחים", (float) 15.93);

            prefs.edit().putBoolean("firstrun", false).commit();

        }

        listViewSummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }

        });

    }
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
                if(calcString.isEmpty())
                    calcString="0";
                addProductToListView(general);
                setTotalPrice(calcString);
                calcString="";
                textViewScreenCalc.setText(calcString);
                ifHaveDot = false;
                break;
        }
    }
    public void setTotalPrice (String price){
        String totalPrice=null;
        totalCalc+= Float.parseFloat(price);
        totalPrice = Float.toString(totalCalc);
        textViewTotalNumber.setText(totalPrice);


    }
    public void upDateFlaotString(float price){

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
        fillInMenue(productName);
    }
    public void fillInMenue(LinkedList <String> products) {   //adds products to menue from database

        if (flag)
        {
            layout.removeAllViews();
            flag=false;
        }


        for (int i = 0; i < products.size(); i++) {
            int width = 150;
            int height = 80;


            String name = products.get(i);
            Button tempBut = new Button(MainActivity.this);
            tempBut.setLayoutParams(new ViewGroup.LayoutParams((int)setWidthInButton(width) , (int)setWidthInButton(height)));
            tempBut.setText(name);
            tempBut.setOnClickListener(new View.OnClickListener(){

                public void onClick(View view){
                    Button b=(Button)view;
                    String name=b.getText().toString();
                    addProductToListView(name);
                }
            });
            layout = (GridLayout) findViewById(R.id.gridLayoutItem);
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
        listViewSummary.setAdapter(adapter);
        Product p = new Product(name,"1",calcString);
        productList.add(p);
        listViewSummary.setSelection(adapter.getCount() - 1);
    }
    public float setWidthInButton(int dp){
        Resources resources = getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp , displayMetrics);


        return pixels;
    }
}