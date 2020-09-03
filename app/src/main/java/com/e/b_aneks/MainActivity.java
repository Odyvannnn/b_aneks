package com.e.b_aneks;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.muddzdev.styleabletoast.StyleableToast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private TextView textView;
    public Elements anek;
    public Element link;
    public String last_str;
    public String str_1;
    public Elements link_3;


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
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
                textView.startAnimation(animation);
                mt.execute();
                }
            }
        );
    }


    @SuppressLint("StaticFieldLeak")
    class MyTask extends AsyncTask <Void, Void, Void> {
        @Override
        public Void doInBackground(Void... params) {

            Document doc = null;
            link = null;
            //проверяем соединение с интернетом
            boolean connected = false;
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            if(Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED ||
                    Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
            //если подключены - берется анекдот с сайта
            if (connected = true) {
                try {
                    doc = Jsoup.connect("https://baneks.site/random").get();
                    anek = doc.select(".block-content mdl-card__supporting-text mdl-color--grey-300 mdl-color-text--grey-900");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (doc != null) {
                    link = doc.select("p").first();
                    doc.outputSettings(new Document.OutputSettings().indentAmount(0).prettyPrint(false));
                    String link_1 = link.toString();
                    Document link_2 = Jsoup.parse(link_1);
                    link_2.select("br").after("\\n");
                    link_3 = link_2.select("p").before("\\n");
                    last_str = link_3.html().replaceAll("\\\\n", "\n");
                    str_1 = Jsoup.clean(last_str, "", Whitelist.none(), new Document.OutputSettings().indentAmount(0).prettyPrint(false));
                }
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
            //тут выводим итоговые данные
            if (link != null){
                textView.setText(str_1);
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
                textView.startAnimation(animation);}
            else {
                textView.setText("");
                StyleableToast.makeText(MainActivity.this, "Проверьте интернет-соединение", R.style.NetToast).show();
            }
        }
    }

}
