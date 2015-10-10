package androidlibs.jenuine.com.androidlibs;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import im.delight.android.webview.AdvancedWebView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment implements AdvancedWebView.Listener {


    private AdvancedWebView mWebView;
    String urlString = "https://www.google.co.in/";
    private String currentURL = urlString;
    private Button button;
    private ImageView imageView;
    private FrameLayout progressLayout;
    private Bundle webViewBundle;

    public WebViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView = (AdvancedWebView) view.findViewById(R.id.webview);
        if (webViewBundle == null) {
            mWebView.loadUrl(urlString);
        } else {
            progressLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            mWebView.restoreState(webViewBundle);
        }
        mWebView.setListener(this, this);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                onExternalPageRequest(url);
                return true;
            }
        });
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                    Log.v("App", "Backpressed");
                    mWebView.onBackPressed();
                    return true;
                }
                return false;
            }
        });
        button = (Button) view.findViewById(R.id.button);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        progressLayout = (FrameLayout) view.findViewById(R.id.progressLayout);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressLayout.setVisibility(View.VISIBLE);
                mWebView.stopLoading();
                new JsoupUtil().execute();
            }
        });
    }

    private void rotate() {
        Animation rotation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        rotation.setFillAfter(true);
        imageView.startAnimation(rotation);
    }

    @Override
    public void onPause() {
        super.onPause();
        webViewBundle = new Bundle();
        mWebView.saveState(webViewBundle);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onPageStarted(String url, Bitmap favicon) {
    }

    @Override
    public void onPageFinished(String url) {
        button.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.GONE);
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
                Log.v("App", document.location());
                return media;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Elements media) {
            // Set downloaded image into ImageView
            if (media != null)
                new ListCreater(media).execute();
        }
    }

    private class ListCreater extends AsyncTask<Void, Void, Void> {
        Elements media;

        public ListCreater() {
        }

        public ListCreater(Elements media) {
            this.media = media;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList = new ArrayList<>();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.v("App", "Iterating the HTML");
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
            progressLayout.setVisibility(View.GONE);
            if (arrayList.size() > 0)
                ((MainActivity) getActivity()).showImagesFragment(arrayList);
            else
                Toast.makeText(getActivity(), "Engine cant fetch Image URL's", Toast.LENGTH_LONG).show();
        }
    }


    private void addToList(String s) {
        if (s.endsWith("jpg") && !s.contains("crop") && !s.contains("thumb") && !s.endsWith("html")) {
            Log.v("App", s);
            arrayList.add(s);
        }
    }
}
