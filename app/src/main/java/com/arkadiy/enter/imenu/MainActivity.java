package com.arkadiy.enter.imenu;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.arkadiy.enter.imenu.Fragments.CashFragment;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements  Callbacks,CashFragment.CashFragmentInteractionListener,CalculateChangeListener {
    private String calcString = "";
    private TextView textViewScreenCalc;
    private boolean ifHaveDot = false;
    private SwipeMenuListView listViewSummary;
    private ArrayAdapter<String> listAdapter;

    private static final String general = "כללי";
    private String numCalc;
    private TextView screenCalc;
    private ArrayList<Product> productList;
    private ArrayList<Product> itemsList;
    private LinkedList<String> lightDrinks;
    private LinkedList<String> beers;
    private boolean flag = true;
    private boolean flag2=true;
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
    private static int height = 73;// db convert to pixelh
    private TextView textViewBarCode;
    private TextView textViewTotalNumber;
    private int butWidth = 0;
    private int butHeight = 0;
    private double sizeScreen;
    private String str = "";
    private Button serverButton = null;
    private ProgressDialog progressDialog;
    private String s = "";
    private ArrayList<Order>orders;
    private LinearLayout linearLayoutOrders;
    private static int[] colors={Color.RED,Color.GREEN,Color.BLUE,Color.WHITE,Color.YELLOW};
    private static int COLORCOUNT=0;
    private int index=-1;
    private    int indexData=-1;
    private ArrayList<Product> products2;
    private Thread orderUpdater;
    private Thread orderItemsUpdater;
    public Handler handler2;
    public int butWidthOrders;
    public int butHeightOrders;
    private PrintReceipt printReceipt;
    private Handler handler;
    private Bundle bundleTotal;
    private CashInformation cashInformation;

    private Button plus;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Started");
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = null;
        serverButton = (Button) findViewById(R.id.btnUpData);
         handler=new Handler(){
            @Override
             public void handleMessage(Message msg){
                indexData=msg.arg1;
                orders.get(index).setIndex(indexData);
            }
        };
        textViewScreenCalc = (TextView) findViewById(R.id.textViewScreenCalc);
        listViewSummary = (SwipeMenuListView) findViewById(R.id.listViewSummary);
        productList = new ArrayList<Product>();
        products2 = new ArrayList<Product>();
        itemsList = new ArrayList<>();
        dataConfig = new DataConfig(MainActivity.this);
        adapter = new ProductListAdapter(this, R.layout.adapter_view_layout, productList);
        listViewSummary.setAdapter(adapter);
        prefs = getSharedPreferences("com.arkadiy.enter.imenu", MODE_PRIVATE);
        productName = new ArrayList<>();
        categoryName = new ArrayList<>();
        orders=new ArrayList<>();
        bundleTotal=new Bundle();

        linearLayoutOrders=(LinearLayout) findViewById(R.id.linearLayoutOpenOrders);
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
        adjustSizeButtonOrders();

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
                    if(p!=null)
                    addProductToListView2(p);
                }
                textViewScreenCalc.setFocusable(false);
                textViewScreenCalc.setFocusableInTouchMode(false);
                textViewBarCode.requestFocus();

                onKeyUp(0, event);
                return true;


            }
        });
        plus=new Button(this);

        index=COLORCOUNT;


        plus.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                linearLayoutOrders.removeView(plus);
                if(COLORCOUNT<5){
                    openNewButtonOrders();
                    orderUpdater.run();
                }
                index=COLORCOUNT-1;
                adapter.clear();
                adapter.notifyDataSetChanged();
                textViewTotalNumber.setText("0");
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
        ServerUpdate serverUpdate=new ServerUpdate(this);


        orderUpdater=new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Message msg=Message.obtain();
                msg.arg1=addOrderToOrders();
                handler.sendMessage(msg);
            }

        });



        orderItemsUpdater=new Thread(new Runnable() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void run() {
                Looper.prepare();




                handler2 = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        Product product=new Product();
                       product=(Product)msg.obj;
                       while(indexData==-1){

                       }
                       try{

                           dataConfig.insertIntoOrderItems(indexData, 1, 0, product.getPrice(), product.getProductName(),product.getItemId());
                           dataConfig.updateTotalOrder(indexData,orders.get(index).getTotal());

                       }catch(Exception ex){
                           ex.printStackTrace();
                       }
                    }
                };
                Looper.loop();
            }

        });
        orderItemsUpdater.start();
        cashInformation=dataConfig.getCashInformation();

        textViewTotalNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                String sl=s.toString();
                if(!sl.equals("0"))
                {
                    Button b= (Button)linearLayoutOrders.findViewById(index);
                    b.setText(textViewTotalNumber.getText());

                }
            }
        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(listViewSummary.getWidth()/6);
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(listViewSummary.getWidth()/6);
                // set a icon
                deleteItem.setIcon(R.drawable.delete_item);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        listViewSummary.setMenuCreator(creator);

        listViewSummary.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        break;
                    case 1:
                        deleteProductFromListView(position);
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    public void showCashDialog(){
        FragmentManager manager1=getSupportFragmentManager();
        CashDialog cashDialog=new CashDialog();
        cashDialog.show(manager1,"cashDialog");
        cashDialog.setArguments(bundleTotal);

        try{

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }


    //=======================================================
    @RequiresApi(api = Build.VERSION_CODES.O)
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
            case R.id.buttonCash:
                if(!textViewTotalNumber.getText().toString().equals(""))
                {
                    bundleTotal.putString("total",textViewTotalNumber.getText().toString());
                    showCashDialog();

                }

                break;
            case R.id.buttonEnter:
                Product pr= new Product(general);
                addProductToListView2(pr);
                calcString = "";
                textViewScreenCalc.setText(calcString);
                ifHaveDot = false;

                break;

                case R.id.dohZ:
               printDohZed();

                break;

            case R.id.dohX:
                printDohX();

                break;
        }

    }
    private void printReceipt(String str){
        if(index==-1)
            index=0;

        printReceipt = new PrintReceipt(this,orders.get(index),getDateTime(),cashInformation,str);
    }

    private void printReceiptTemp(){
        if(index==-1)
            index=0;

        printReceipt = new PrintReceipt(getDateTime(),this,orders.get(index),cashInformation);
    }

    //=======================================================
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void loadAll(View v) {

        Button b = (Button) v;

        if (layout.getId() == R.id.gridLayoutItem)
            layout.removeAllViews();


        changeProduct(b.getText().toString());

    }

    //=======================================================
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void changeProduct(String product) {
        getCategoryProductsList(product);
        layout = (GridLayout) findViewById(R.id.gridLayoutItem);
        fillInMenue(productName, layout);

    }

    //=========================================================
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void fillInMenue(ArrayList<String> products, GridLayout l) {   //adds productsDB.db to menue from database


        layout = l;


        for (int i = 0; i < products.size(); i++) {
            String name = products.get(i);

            Button tempBut = new Button(MainActivity.this);
            tempBut.setText(name);
            tempBut.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);

            tempBut.setGravity(1);
            tempBut.setLayoutParams(new ViewGroup.LayoutParams(butWidth, butHeight));

            if (layout.getId() == R.id.gridLayoutItem) {
                if (flag) {
                    getSize();
                    flag = false;
                }
                tempBut.setId(itemsList.get(i).getItemId());


                tempBut.setOnClickListener(new View.OnClickListener() {


                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(View view) {
                        Button b = (Button) view;
                        Product p = dataConfig.getProductById(view.getId());

                        String name = b.getText().toString();
                        addProductToListView2(p);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private int addOrderToOrders(){
        int lastOrderIndex=-1;
    String curTime=getDateTime();
        lastOrderIndex=dataConfig.insertIntoOrders(curTime);
        return lastOrderIndex;
    }

    public String getDateTime(){
        DateFormat timeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
        String curTime = timeFormat.format(new Date());
        return curTime;
    }

    public void clickedOrderButton(View v){

        try{
            int num=((Button)v).getId();
            products2.clear();
            if(orders.size()>=num){
                if(orders.size()==1){

                    num=0;
                    index=0;
                }else{

                    index=v.getId();
                }

                int size= orders.get(num).getProducts().size();
                ArrayList<Product> prod=orders.get(num).getProducts();
                for (int i = 0; i <size; i++) {
                    products2.add(prod.get(i));
                }
            }

            adapter.clear();
            adapter.notifyDataSetChanged();
            textViewTotalNumber.setText("0");

            if(!products2.isEmpty()){
                for (int i = 0; i < products2.size(); i++) {
                    adapter.insert(products2.get(i),i);
                }
                textViewTotalNumber.setText(new DecimalFormat("##.##").format(orders.get(index).getTotal()));

                adapter.notifyDataSetChanged();
            }

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }


    private void openNewButtonOrders(){
        Button temp=new Button(this);

        temp.setLayoutParams(new ViewGroup.LayoutParams(butWidthOrders,linearLayoutOrders.getHeight()));

        temp.setId(COLORCOUNT);
        Order order=new Order(indexData,COLORCOUNT,getDateTime());
        orders.add(order);

        temp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                clickedOrderButton(v);
            }
        });


        temp.setBackgroundColor(colors[COLORCOUNT++]);
        linearLayoutOrders.addView(temp);
        plus.setText("+");
        plus.setLayoutParams(new ViewGroup.LayoutParams(90, linearLayoutOrders.getHeight()));
        linearLayoutOrders.addView(plus);
        plus.setId(-1);
    }



    //========================================================


    public void addProductToListView2(Product prod) {

        String price = prod.getPrice();
        String name=prod.getProductName();
        int id=prod.getItemId();
        Message msg=Message.obtain();
        Message msg2=Message.obtain();

        try{
            if(flag2){
                flag2=false;
                openNewButtonOrders();
                orderUpdater.run();
            }

            if(!groupAllItems(name,price)){
                p = new Product(name, "1", price,id);
                productList.add(p);
                products2.add(p);
                orders.get(index).addToProducts(p);
            }



            listViewSummary.setAdapter(adapter);
            textViewTotalNumber.setText(new DecimalFormat("##.##").format(orders.get(index).getTotal()));
            listViewSummary.setSelection(adapter.getCount() - 1);
            indexData=orders.get(index).getIndex();
            msg2.obj=p;
            handler2.sendMessage(msg2);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }



    }

    public void deleteProductFromListView(int  ind) {

        Order current=orders.get(index);
        String amount;
//        String name=prod.getProductName();
//        int id=prod.getItemId();
//        Message msg=Message.obtain();
//        Message msg2=Message.obtain();
        int num=Integer.parseInt(current.getProducts().get(ind).getAmount());
        try{
            if(num>1){
                num--;
                amount=Integer.toString(num);
               current.getProducts().get(ind).setAmount(amount);
                productList.get(ind).setAmount(amount);
                products2.get(ind).setAmount(amount);
            }
            else{
                productList.remove(ind);
                products2.remove(ind);
                orders.get(index).removeFromProducts(ind);
                listViewSummary.setSelection(adapter.getCount() - 1);

            }

//            adapter.remove(productList.get(ind));
//            listViewSummary.setSelection(adapter.getCount() - 1);

            textViewTotalNumber.setText(new DecimalFormat("##.##").format(orders.get(index).getTotal()));
            listViewSummary.setAdapter(adapter);

            dataConfig.deleteItemFromOrder(current.getIndex());//add to thread!!!!!!!!!!!!!!!!!!!!!!!!!!!NEED

//            indexData=orders.get(index).getIndex();
//            msg2.obj=p;
//            handler2.sendMessage(msg2);

        }catch(Exception ex)
        {
            ex.printStackTrace();
        }



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

    private void adjustSizeButtonOrders() {

        if (sizeScreen > 10) {
            butWidthOrders = (int) setSizeInButton(115);
            butHeightOrders = (int) setSizeInButton(height);
        } else if (sizeScreen <= 9) {
            butWidthOrders = (int) setSizeInButton(115);
            butHeightOrders = (int) setSizeInButton(height);
        } else if (sizeScreen <= 10 && sizeScreen >= 9.1) {
            butWidthOrders = (int) setSizeInButton(115);
            butHeightOrders = (int) setSizeInButton(height);
        }

    }

    //=======================================================



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


    public void payCash(float summ) {

        try {
            int cash = 1;
            float change;
            Order current = orders.get(index);
            float total = current.getTotal();
            int indexInData = current.getIndex();
            DateFormat timeFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            timeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jerusalem"));
            String curTime = timeFormat.format(new Date());
            current.addPayment(cash, summ, curTime, current.getIndex(), null);
            dataConfig.createNewPayment(summ, curTime, indexInData, cash);
            dataConfig.updateStatusItem(indexInData);
            dataConfig.updateStatusOrder(indexInData);
            //open draw!!!!!!!!!!!


        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
        public Doh getDohZed(Employee employee){

            Doh doh =new Doh();
            String dateTimeNow=getDateTime();
            String dateTimeLastZed=dataConfig.getLastZedDateTime();
            float cash=0;
            float visa=0;
            ArrayList<Payment>paymentDohZed=dataConfig.getZed(dateTimeNow,employee,dateTimeLastZed);
           for(Payment p:paymentDohZed){
               float summ=p.getAmount();
               switch(p.getType()) {
                   case 1:
                       doh.addToCash(p);
                       doh.addToTotalCash( summ);
                       break;
                   case 2:
                       doh.addToVisa(p);
                       doh.addToTotalVisa(summ);
                       break;
                   case 3:
                       doh.addToCards(p);
                       break;

               }
               doh.addToTotal(summ);
           }
           if(dateTimeLastZed!=null)
           doh.setStartDateTime(dateTimeLastZed);
            doh.setFinishDateTime(dateTimeNow);

         return doh;
    }

    public void printDohZed(){
            try{
                Doh doh =getDohZed(new Employee("Vadim",1));
                doh.getCards();
                //print doh zed

            }catch(Exception ex){
                ex.printStackTrace();
            }

    }

    public void printDohX(){
        try{
            Doh doh =getDohX(new Employee("Vadim",1));
            doh.getCards();
            //print doh X

        }catch(Exception ex){
            ex.printStackTrace();
        }

    }


    public Doh getDohX(Employee employee){

        Doh doh =new Doh();
        String dateStart=dataConfig.getLastZedDateTime();
        if(dateStart==null){
            dateStart=dataConfig.getFirstOrderClosedDate();
        }
        String dateTimeNow=getDateTime();
        float cash=0;
        float visa=0;
        ArrayList<Payment>paymentDoh=dataConfig.getDohX(dateStart,dateTimeNow,employee);
        for(Payment p:paymentDoh){
            float summ=p.getAmount();
            switch(p.getType()) {
                case 1:
                    doh.addToCash(p);
                    doh.addToTotalCash( summ);
                    break;
                case 2:
                    doh.addToVisa(p);
                    doh.addToTotalVisa(summ);
                    break;
                case 3:
                    doh.addToCards(p);
                    break;

            }
            doh.addToTotal(summ);
        }
        if(dateStart!=null)
            doh.setStartDateTime(dateStart);
        doh.setFinishDateTime(dateTimeNow);

        return doh;
    }


    @Override
    public void onCashFragmentInteraction(Uri uri) {


    }


    @Override
    public void Calculate(float change,float payed) {
        String masKabalaMakor="";
        if(change<=0)
        {
            payCash(payed+change);
            Toast.makeText(this,"עודף"+change,Toast.LENGTH_LONG).show();
            masKabalaMakor=dataConfig.getMasKabala();
            printReceipt(masKabalaMakor);



            orders.remove(index);
            adapter.clear();
            linearLayoutOrders.removeViewInLayout(linearLayoutOrders.findViewById(index));
            adapter.notifyDataSetChanged();
            textViewTotalNumber.setText("0");
            int size=linearLayoutOrders.getChildCount();
            if(size==1)
            {
                linearLayoutOrders.removeAllViews();
                COLORCOUNT=0;
                flag2=true;
                return;
            }
            else if(size>1){
                if(index==0){
                    clickedOrderButton((Button)linearLayoutOrders.findViewById(index+1));
                }
                else{
                    clickedOrderButton((Button)linearLayoutOrders.findViewById(index-1));
                }
                COLORCOUNT-=1;
            }


            if(index<=orders.size()-1)
            {

                Button b;
                for (int i = 0; i <orders.size() ; i++) {
                    b=(Button)linearLayoutOrders.getChildAt(i);
                    b.setId(i);

                }
            }


        }


    }

    public boolean groupAllItems(String name,String price2){
        String num;
        int n;
        float pric;
        float price=Float.parseFloat(price2);
        boolean x=false;
        Product p;
        for(int i=0;i<productList.size();i++)
        {
            p=productList.get(i);
            if(name.equals(p.getProductName()))
            {
                num=p.getAmount();
                n=Integer.parseInt(num);
                n++;
                productList.get(i).setAmount(Integer.toString(n));
                orders.get(index).getProducts().get(i).setAmount(Integer.toString(n));
//                num=p.getPrice();
//                pric=Float.parseFloat(num);
//                pric+=price;
//                productList.get(i).setPrice(Float.toString(pric));
//                orders.get(index).getProducts().get(i).setPrice(Float.toString(pric));
                x=true;

            }
        }
        return x;
    }

}



