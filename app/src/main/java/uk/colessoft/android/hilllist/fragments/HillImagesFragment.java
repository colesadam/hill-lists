package uk.colessoft.android.hilllist.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.colessoft.android.hilllist.R;
import uk.colessoft.android.hilllist.adapter.GalleryAdapter;
import uk.colessoft.android.hilllist.model.GeographImageDetail;
import uk.colessoft.android.hilllist.model.GeographImageSearchResponse;
import uk.colessoft.android.hilllist.model.Hill;
import uk.colessoft.android.hilllist.service.GeographService;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HillImagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HillImagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HillImagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private GeographService geographService;
    private RecyclerView recyclerView;
    private ProgressDialog pDialog;
    private List<Image> images;
    private GalleryAdapter mAdapter;

    public HillImagesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HillImagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HillImagesFragment newInstance(String param1, String param2) {
        HillImagesFragment fragment = new HillImagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Retrofit geographRetrofit = new Retrofit.Builder()
                .baseUrl("http://api.geograph.org.uk")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geographService = geographRetrofit.create(GeographService.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_hill_images, container, false);

        pDialog = new ProgressDialog(getActivity());
        images = new ArrayList<>();
        mAdapter = new GalleryAdapter(getActivity().getApplicationContext(), images);

        final int columns = getResources().getInteger(R.integer.gallery_columns);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), columns);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(
                new GalleryAdapter.RecyclerTouchListener(getActivity(), recyclerView, new GalleryAdapter.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Log.d(TAG,images.get(position).getTitle());
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(images.get(position).getTitle());
                        mListener.onFragmentInteraction(images.get(position).uuid);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                } )
        );
        return recyclerView;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void getImages(Hill hill) {
        Log.d(TAG, "Getting Images");
        Call<GeographImageSearchResponse> call = geographService.imageSearch(hill.getLatitude() + "," + hill.getLongitude());

        call.enqueue(new Callback<GeographImageSearchResponse>() {
            @Override
            public void onResponse(Call<GeographImageSearchResponse> call, Response<GeographImageSearchResponse> response) {
                images.clear();
                for (GeographImageDetail detail : response.body().getItems()) {
                    images.add(new Image(detail.getThumb(), detail.getTitle().split(": ")[1], detail.getAuthor(), detail.getGuid()));
                }
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0
                        && getActivity().getSupportFragmentManager().getFragments().size()==1
                        && getActivity().findViewById(R.id.hill_image_fragment) != null) {
                    mListener.onFragmentInteraction(images.get(0).uuid);
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(images.get(0).getTitle());
                }
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<GeographImageSearchResponse> call, Throwable t) {
                System.out.println(call.request().url());
            }
        });


    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String guid);
    }

    public class Image {
        private String thumbnailUrl;
        private String uuid;

        public Image(String thumbnailUrl, String title, String author, String uuid) {
            this.thumbnailUrl = thumbnailUrl;
            this.title = title;
            this.author = author;
            this.uuid = uuid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        private String title;
        private String author;

        public String getThumbnailUrl() {
            return thumbnailUrl;
        }

        public void setThumbnailUrl(String thumbnailUrl) {
            this.thumbnailUrl = thumbnailUrl;
        }
    }
}
