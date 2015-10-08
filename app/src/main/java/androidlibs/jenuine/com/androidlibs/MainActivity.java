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
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import im.delight.android.webview.AdvancedWebView;


public class MainActivity extends Activity implements AdvancedWebView.Listener {

    private AdvancedWebView mWebView;
    String urlString = "https://www.google.co.in/";
    private String currentURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Button button = (Button) findViewById(R.id.button);
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
        if (!mWebView.onBackPressed()) {
            return;
        }
        // ...
        super.onBackPressed();
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(String url) {
        popFragment();
    }

    private void popFragment() {
        FragmentManager fm = getFragmentManager();
        fm.popBackStack("mobileid.Splash", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
            for (org.jsoup.nodes.Element src : media) {
                if (src.tagName().equals("img")) {
//                    if (src.attr("width") != null && src.attr("width").length() > 0) {
//                        if (Integer.parseInt(src.attr("width")) > 500 && Integer.parseInt(src.attr("height")) > 500)
                    print(src.attr("abs:src"));
//                    }
                }
            }
        }
    }

    private void print(String s) {
        if (s.endsWith("jpg") && !s.contains("crop") && !s.contains("thumb") && !s.endsWith("html")) {
            Log.v("App", s);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(s));
            startActivity(i);
        }

    }
}