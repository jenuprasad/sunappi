package androidlibs.jenuine.com.androidlibs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import im.delight.android.webview.AdvancedWebView;


public class MainActivity extends Activity implements AdvancedWebView.Listener {

    private AdvancedWebView mWebView;
    String urlString = "https://www.google.co.in/";
    private String currentURL = urlString;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        mWebView.setListener(this, this);
        mWebView.loadUrl(urlString);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                onExternalPageRequest(url);
                return true;
            }
        });
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new JsoupUtil().execute();
            }
        });
        loadSplash();
    }

    private void loadSplash() {
        Fragment mFragment = new Splash();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, mFragment).addToBackStack("mobileid.Splash").commit();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onBackPressed() {
        if (button.getVisibility() == View.GONE) {
            super.onBackPressed();
            button.setVisibility(View.VISIBLE);
        } else if (!mWebView.onBackPressed()) {
            return;
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(String url) {
        popFragment("mobileid.Splash");
    }

    private void popFragment(String s) {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack(s, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
    }

    @Override
    public void onDownloadRequested(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {

    }

    @Override
    public void onExternalPageRequest(String url) {
        this.currentURL = url;
        mWebView.loadUrl(url);
    }

    ArrayList<String> arrayList;

    private class JsoupUtil extends AsyncTask<Void, Void, Elements> {

        public JsoupUtil() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Elements doInBackground(Void... params) {

            try {
                // Connect to the web site
                Document document = Jsoup.connect(currentURL).get();
                // Using Elements to get the class data
                Elements media = document.select("[src]");
                return media;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Elements media) {
            // Set downloaded image into ImageView
            new ListCreater(media).execute();
        }
    }

    private class ListCreater extends AsyncTask<Void, Void, Void> {
        Elements media;

        public ListCreater() {
        }

        public ListCreater(Elements media) {
            this.media = media;
            arrayList = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {


            for (org.jsoup.nodes.Element src : media) {
                if (src.tagName().equals("img")) {
//                    if (src.attr("width") != null && src.attr("width").length() > 0) {
//                        if (Integer.parseInt(src.attr("width")) > 500 && Integer.parseInt(src.attr("height")) > 500)
                    addToList(src.attr("abs:src"));
//                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void media) {
            // Set downloaded image into ImageView
            showDialogFragment();
        }
    }

    private void showDialogFragment() {
        button.setVisibility(View.GONE);
        Fragment mFragment = new Images();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("arraylist", arrayList);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mFragment.setArguments(bundle);
        ft.replace(R.id.container, mFragment).addToBackStack("mobileid.Images").commit();
    }

    public void openImage(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    private void addToList(String s) {
        if (s.endsWith("jpg") && !s.contains("crop") && !s.contains("thumb") && !s.endsWith("html")) {
            arrayList.add(s);
        }
    }


}