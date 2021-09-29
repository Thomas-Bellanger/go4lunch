package com.example.go4lunch.utils;

import android.util.Log;

import java.lang.ref.WeakReference;

public class NetWorkAsynchTask extends android.os.AsyncTask<String, Void, String> {

    private final WeakReference<Listeners> callback;

    public NetWorkAsynchTask(Listeners callback) {
        this.callback = new WeakReference<>(callback);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.callback.get().onPreExecute();
        Log.e("TAG", "AsyncTask is started");
    }

    @Override
    protected void onPostExecute(String succes) {
        super.onPostExecute(succes);
        this.callback.get().onPostExecute(succes);
        Log.e("TAG", "AsyncTask is finished");
    }

    @Override
    protected String doInBackground(String... url) {
        this.callback.get().doInBackground();
        Log.e("TAG", "AsyncTask is working");
        return HttpUrlConnexionRestaurant.startHttpRequest(url[0]);
    }

    public interface Listeners {
        void onPreExecute();

        void doInBackground();

        void onPostExecute(String succes);
    }
}
