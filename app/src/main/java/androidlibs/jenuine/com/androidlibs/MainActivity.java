package androidlibs.jenuine.com.androidlibs;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

//import com.crashlytics.android.Crashlytics;
//import io.fabric.sdk.android.Fabric;


public class MainActivity extends Activity {

    ImagesFragment imagesFragment;
    WebViewFragment webViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        showWebViewFragment();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showImagesFragment(ArrayList<String> arrayList) {
        if(webViewFragment!=null){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.hide(webViewFragment).commit();
        }
        imagesFragment = new ImagesFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("arraylist", arrayList);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        imagesFragment.setArguments(bundle);
        ft.add(R.id.container, imagesFragment).addToBackStack("mobileid.Images").commit();
    }

    public void showWebViewFragment() {
        webViewFragment = new WebViewFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.container, webViewFragment).addToBackStack("mobileid.WebViewFragment").commit();
    }

    @Override
    public void onBackPressed() {
        Log.v("App", "onBackPressed");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment images = getFragmentManager().findFragmentById(R.id.container);
        if (images.getClass().getName().equalsIgnoreCase(imagesFragment.getClass().getName())) {
            Log.v("App", "images popBackStack");
            ft.show(webViewFragment);
            ft.hide(imagesFragment);
            ft.commit();
        } else
            finish();
    }

}