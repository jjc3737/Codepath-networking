package com.codepath.network_example;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.network_example.Tasks.ImageLoadingTask;
import com.codepath.network_example.Tasks.JsonParsingTask;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    String urlString = "http://cdn.skim.gs/image/upload/v1456344012/msi/Puppy_2_kbhb4a.jpg";
    String nyUrlString = "http://api.nytimes.com/svc/search/v2/articlesearch.json";

    @BindView(R.id.ivNetworkImage)
    ImageView ivImage;

    @BindView(R.id.etSearch)
    EditText etSearch;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    ImageLoadingTask imageLoadingTask;
    JsonParsingTask parsingTask;

    ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setUpImageLoadingTask();
        loadSavedImage();

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        actionId == EditorInfo.IME_ACTION_NEXT
                        || actionId == EditorInfo.IME_ACTION_SEARCH) {

                    //Async tasks can only be executed once
                    //So create another image loading taks
                    setUpImageLoadingTask();
                    setUpJsonParsingTask();
                    //Parse json
                    parsingTask.execute(nyUrlString, v.getText().toString());
                }
                return false;
            }
        });
    }

    private void loadSavedImage() {
        String savedImage = Utils.getSavedImageURL(this);

        //If null, load puppy picture
        if (savedImage == null)
            imageLoadingTask.execute(urlString);
        else
            imageLoadingTask.execute(savedImage);

    }

    private void setUpJsonParsingTask() {

        parsingTask = new JsonParsingTask(new JsonParsingTask.JsonParsingListener() {
            @Override
            public void onPreExecute() {
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setMessage("Loading data...");
                dialog.show();
            }

            @Override
            public void onException() {
                Toast.makeText(MainActivity.this, "Error in loading", Toast.LENGTH_LONG);
            }

            @Override
            public void onFinally(JSONObject firstObject) {
                //We have the first object
                //Use utils to grab title
                tvTitle.setText(Utils.getTitleFromArticle(firstObject));

                //Get url and show image
                String imageUrl = Utils.getImageURLfromArticle(firstObject);
                if (imageUrl != null) {
                    imageLoadingTask.execute(imageUrl);

                    //Save to prefs
                    Utils.saveImageURLToPrefs(MainActivity.this, imageUrl);
                }
            }
        });

    }
    private void setUpImageLoadingTask() {
        imageLoadingTask = new ImageLoadingTask(new ImageLoadingTask.ImageLoadingListener() {
            @Override
            public void onPreExecute() {
            }

            @Override
            public void onException() {
                Toast.makeText(MainActivity.this, "Error loading", Toast.LENGTH_LONG);
            }

            @Override
            public void onFinally(Bitmap result) {
                if (dialog != null)
                    dialog.dismiss();

                ivImage.setImageBitmap(result);
            }
        });
    }
}


