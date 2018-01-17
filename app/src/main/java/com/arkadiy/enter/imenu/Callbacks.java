package com.arkadiy.enter.imenu;

/**
 * Created by vadnu on 14/01/2018.
 */

public interface Callbacks {
    void onAboutToStart();
    void onSuccess(String downloadedText);
    void onReportProgress(int progress);
    void onError(int httpStatusCode,String errorMessage);
}
