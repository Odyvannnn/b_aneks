package com.e.b_aneks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    public Elements anek;
    public Element link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button1);
        textView = findViewById(R.id.textView1);
        final MediaPlayer doot_sound = MediaPlayer.create(this, R.raw.doot);
        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                doot_sound.start();
                MyTask mt = new MyTask();
                mt.execute();
                }
            }
        );}

    @SuppressLint("StaticFieldLeak")
    class MyTask extends AsyncTask <Void, Void, Void> {
        @Override
        public Void doInBackground(Void... params) {

            Document doc = null;
            link = null;

            boolean connected = false;
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            if(Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                    Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;
            }

            if (connected = true) {
                try {
                    doc = Jsoup.connect("https://baneks.site/random").get();
                    anek = doc.select(".block-content mdl-card__supporting-text mdl-color--grey-300 mdl-color-text--grey-900");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (doc != null)
                    link = doc.select("p").first();
                else
                    return null;
            }
            else
                return null;
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //Тут выводим итоговые данные
            if (link != null)
                textView.setText(link.text());
            else {
                Toast toast = Toast.makeText(MainActivity.this, "Проверьте соединение с интернетом", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    }

}
