package com.example.uploadvideoapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class CheckTypesTask extends AsyncTask<Void, Void, Void> {

    Context context;
    ProgressDialog asyncDialog;
    public CheckTypesTask(Context context) {
        this.context = context;
        asyncDialog = new ProgressDialog(context);
    }



    @Override
    protected void onPreExecute() {
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        asyncDialog.setMessage("로딩중입니다..");

        // show dialog
        asyncDialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... arg0) {
        try {
            for (int i = 0; i < 5; i++) {
                asyncDialog.setProgress(i * 30);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        asyncDialog.dismiss();
        super.onPostExecute(result);
    }
}



