package com.ttl.rxandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.io.IOException;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    Observable<String> myObservable
            = Observable.just("SandeepRocks");

    private TextView txtObserve;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtObserve = (TextView) findViewById(R.id.txtObserver);
        fetchFromGoogle.subscribeOn(Schedulers.newThread())//create new thread
        .observeOn(AndroidSchedulers.mainThread()) //use main thread
        .subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                txtObserve.setText(s);
            }
        });

    }
    Observable<String> fetchFromGoogle = Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> subscriber) {
            try {
                String data = fetchData();
                subscriber.onNext(data); // Emit the contents of the URL
                subscriber.onCompleted(); // Nothing more to emit
            }catch(Exception e){
                subscriber.onError(e); // In case there are network errors
            }
        }
    });

    public String fetchData(){
        String intialURL = "http://api.androidhive.info/volley/person_object.json";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(intialURL)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
