package com.arkadiy.enter.imenu;

import android.content.Context;
import android.os.Handler;

import java.util.logging.LogRecord;

/**
 * Created by vadnu on 24/01/2018.
 */

public class ServerUpdate implements Callbacks, Runnable{
   private PostSender postSender ;
   Handler handler;
private Context ctx;
        public ServerUpdate(Context context){
            postSender= new PostSender(this);
            ctx=context;
            postSender.execute();
            handler=new Handler();



        }


    @Override
    public void run() {
        postSender= new PostSender(this);
        postSender.execute();

    }


    @Override
    public void onAboutToStart() {

    }

    @Override
    public void onSuccess(String downloadedText) {
        if (downloadedText.equals("Yes")){
            try {

                handler.postDelayed(this,30000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else {
            try {
                DataBaseFiller dataBaseFiller = new DataBaseFiller(this,ctx );
                dataBaseFiller.execute(downloadedText);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }



    }

    @Override
    public void onReportProgress(int progress) {

    }

    @Override
    public void onError(int httpStatusCode, String errorMessage) {

        int x = httpStatusCode;
    }


}
