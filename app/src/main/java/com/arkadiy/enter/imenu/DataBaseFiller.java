package com.arkadiy.enter.imenu;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vadnu on 18/01/2018.
 */

public class DataBaseFiller extends AsyncTask<String,Void,Boolean> {

    private Callbacks listener;
    private DataConfig dataConfig;

    public DataBaseFiller(Callbacks listener, Context context) {

        this.listener = listener;
        dataConfig = new DataConfig(context);
        dataConfig.openDatabase();
    }


    protected void onPreExecute() {

        listener.onAboutToStart();

    }


    @Override
    protected Boolean doInBackground(String... strings) {

        try {
            JSONObject jsonObject;

            jsonObject = new JSONObject(strings[0]);
            JSONObject result = jsonObject.getJSONArray("result").getJSONObject(0);
            JSONArray zones = result.getJSONArray("itemgroups");
            JSONArray itemGroups = null;


            for (int i = 1; i < zones.length(); i++) {
                int id=0;
                int active=1;
                itemGroups = zones.getJSONArray(i);
                if(!itemGroups.getString(0).equals(""))
                    id = Integer.parseInt(itemGroups.getString(0));

                String name = itemGroups.getString(1);
                String picturPath = itemGroups.getString(2);

                if(!itemGroups.getString(3).equals(""))
                    active = Integer.parseInt(itemGroups.getString(3));

                dataConfig.createCategoryIfNotExists(id, name, picturPath, active);


            }

            JSONArray itemsArray = result.getJSONArray("items");
            JSONArray item = null;

            for (int j = 1; j < itemsArray.length(); j++) {
                item = itemsArray.getJSONArray(j);
                int id=0;
                int ig_id=0;
                float price=0;
                if(!item.getString(0).equals(""))
                {
                    id = Integer.parseInt(item.getString(0));//id
                }
                String name = item.getString(1);//name
                if(!item.getString(2).equals(""))
                {
                    String itemsGroupId = item.getString(2);
                    ig_id = Integer.parseInt(itemsGroupId);//ig_id
                }

                if(!item.getString(3).equals(""))
                {
                    String pr = item.getString(3);
                    price = Float.parseFloat(pr);//price

                }


                dataConfig.createItemIfNotExists(id, name, price, null, null, ig_id);
            }
        } catch (JSONException ex) {
            ex.fillInStackTrace();
            return false;

        }
        return true;
    }


    protected void onPostExecute(Boolean flag) {
        if (flag) {
            listener.onSuccess("Yes");
        } else {
//            listener.onError(httpStatusCode, errorMessage);

        }


    }

}