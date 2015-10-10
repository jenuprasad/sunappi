package androidlibs.jenuine.com.androidlibs;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class ImagesFragment extends Fragment {
    private static final String TAG = ImagesFragment.class.getSimpleName();

    private GridView mGridView;

    private GridViewAdapter mGridAdapter;
    private ArrayList<String> mGridData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_images, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGridView = (GridView) view.findViewById(R.id.gridView);

        //Initialize with empty data
        mGridData = getArguments().getStringArrayList("arraylist");

        if (mGridData != null) {
            Iterator<String> stringIterator = mGridData.iterator();
            while (stringIterator.hasNext()) {
                Log.v("App", stringIterator.next());
            }
        } else
            Log.v("App", "Empty arraylist");

        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);
//        mGridAdapter.setOnClickListener(new GridViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view) {
//                downFromView(view, "http://www.cinespot.net/gallery/d/2270913-2/Seetha+Photos+in+Player+Movie+_1_.jpg");
//            }
//        });
        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                String item = (String) parent.getItemAtPosition(position);
//                downit(item);
                downFromView(v, item);

            }
        });

    }

    private void downFromView(View view, String url) {
        ImageView imageView = (ImageView) view.findViewById(R.id.grid_item_image);
        ImageView bg_image = (ImageView) view.findViewById(R.id.bg_image);
        bg_image.setVisibility(View.VISIBLE);
        BitmapDrawable btmpDr = (BitmapDrawable) imageView.getDrawable();
        if (btmpDr == null) {
            downit(url);
            return;
        }
        Bitmap bmp = btmpDr.getBitmap();

/*File sdCardDirectory = Environment.getExternalStorageDirectory();*/
        try {

            File folder = new File(Environment.getExternalStorageDirectory() + "/sunappi");
            if (!folder.exists())
                folder.mkdir();
            String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
            String path = folder.getAbsolutePath() + "/" + fileName;
            Log.v(TAG, path);
            FileOutputStream outStream = new FileOutputStream(new File(path));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
    /* 100 to keep full quality of the image */
            outStream.flush();
            outStream.close();
            //Refreshing SD card
//            getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Image could not be saved : Please ensure you have SD card installed " +
                    "properly", Toast.LENGTH_LONG).show();
        }

    }

    private void downit(String item) {
        /* Starting Download Service */
        Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), DownloadService.class);
        /* Send optional extras to Download IntentService */
        intent.putExtra("url", item);
        intent.putExtra("requestId", 101);
        getActivity().startService(intent);
    }
}