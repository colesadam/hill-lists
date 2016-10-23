package uk.colessoft.android.hilllist.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import uk.colessoft.android.hilllist.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HillImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HillImageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private ScaleGestureDetector scaleGestureDetector;


    public HillImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment HillImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HillImageFragment newInstance(String param1) {
        HillImageFragment fragment = new HillImageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.full_image, container, false);


        Glide.with(getActivity()).load("http://t0.geograph.org.uk/stamp.php?id=" + mParam1 + "&font=Helvetica&style=&weight=&gravity=South&pointsize=").asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                SubsamplingScaleImageView hillImageView = (SubsamplingScaleImageView) view.findViewById(R.id.hill_image);
                hillImageView.setMinimumDpi(80);
                hillImageView.setImage(ImageSource.bitmap(resource));
            }
        });


        return view;
    }


}
