package com.arkadiy.enter.imenu;

import android.content.Context;

/**
 * Created by vadnu on 24/01/2018.
 */

public class ServerUpdate implements Callbacks, Runnable{
   private PostSender postSender ;
private Context ctx;
        public ServerUpdate(Context context){
            postSender= new PostSender(this);
            ctx=context;
            postSender.execute();



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
                Thread.sleep(300000);//every 5 minutes
                this.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        else {
            DataBaseFiller dataBaseFiller = new DataBaseFiller(this,ctx );
            dataBaseFiller.execute(downloadedText);
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
