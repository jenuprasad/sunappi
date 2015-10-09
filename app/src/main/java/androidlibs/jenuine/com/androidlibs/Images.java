package androidlibs.jenuine.com.androidlibs;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Iterator;

public class Images extends Fragment {
    private static final String TAG = Images.class.getSimpleName();

    private GridView mGridView;

    private GridViewAdapter mGridAdapter;
    private ArrayList<String> mGridData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_images, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                String item = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), DetailsActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);

                // Interesting data to pass across are the thumbnail size/location, the
                // resourceId of the source bitmap, the picture description, and the
                // orientation (to avoid returning back to an obsolete configuration if
                // the device rotates again in the meantime)

                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);
                ((MainActivity)getActivity()).openImage(item);

            }
        });

    }
}