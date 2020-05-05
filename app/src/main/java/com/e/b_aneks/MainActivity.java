package com.e.b_aneks;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.ContentHandler;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private TextView textView;
    public Elements anek;
    public Element link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button1);
        textView = (TextView)findViewById(R.id.textView1);
        final MediaPlayer doot_sound = MediaPlayer.create(this, R.raw.doot);
        button.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View view) {
                MyTask mt = new MyTask();
                doot_sound.start();
                mt.execute();
                }
            }
        );}

    class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {

            Document doc = null;
            try {
                doc = Jsoup.connect("https://baneks.site/random").get();
                anek = doc.select(".block-content mdl-card__supporting-text mdl-color--grey-300 mdl-color-text--grey-900");
            } catch (IOException e) {
                //Если не получилось считать
                e.printStackTrace();
            }
            //Если всё считалось, что вытаскиваем из считанного html документа заголовок
            if (doc != null)
                link = doc.select("p").first();
            else
                link = doc.text("Нет соединения");
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //Тут выводим итоговые данные
            textView.setText(link.text());
        }
    }

}
