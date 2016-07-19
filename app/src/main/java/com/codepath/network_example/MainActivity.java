package com.codepath.network_example;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    String urlString = "http://cdn.skim.gs/image/upload/v1456344012/msi/Puppy_2_kbhb4a.jpg";
    @BindView(R.id.ivNetworkImage)
    ImageView image;

    ImageLoadingTask task;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Set up the loading task
        setUpImageLoadingTask();
        //Execute with url
        task.execute(urlString);
    }

    private void setUpImageLoadingTask() {

        task = new ImageLoadingTask(new ImageLoadingTask.ImageLoadingListener() {
            @Override
            public void onPreExecute() {
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Getting Data ...");
                dialog.show();
            }

            @Override
            public void onException() {
                Toast.makeText(MainActivity.this, "Failed to get image", Toast.LENGTH_LONG);
            }

            @Override
            public void onFinally(Bitmap result) {
                dialog.dismiss();
                image.setImageBitmap(result);
            }
        });

    }


}


