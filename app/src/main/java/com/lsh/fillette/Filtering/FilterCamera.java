package com.lsh.fillette.Filtering;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lsh.fillette.View.MainActivity;
import com.lsh.fillette.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import me.grantland.widget.AutofitTextView;

/**
 * Created by lsw on 2018-04-05.
 * Filter Camera for rendering filters.
 */

public class FilterCamera extends Fragment {

    private HashMap myFilterList = new HashMap();
    private FilterData mFilterData;
    private CameraRenderer mCameraRenderer;
    public static final String TAG = FilterCamera.class.getName();
    private MainActivity main;
    private boolean listVisible = false;
    private Set set;
    private ArrayList<String> filterList;
    private View layout;
    private final Gson gson = new Gson();

    private final Camera.CameraInfo mCameraInfo = new Camera.CameraInfo();

    public int getOrientation() {
        if (mCameraInfo == null || mFilterData == null) {
            return 0;
        }
        if (mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            return (mCameraInfo.orientation - mFilterData.mOrientationDevice + 360) % 360;
        } else {
            return (mCameraInfo.orientation + mFilterData.mOrientationDevice) % 360;
        }
    }

    public FilterCamera() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        main = (MainActivity) getActivity();
        main.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        layout = inflater.inflate(R.layout.activity_camera, container, false);
        init();
        myFilterList = FilterManger.getInstance().load(getContext(), "filter");
        set = myFilterList.keySet();
        filterList = new ArrayList<String>(set);
        mCameraRenderer = layout.findViewById(R.id.cam_render);
        mCameraRenderer.setSharedData(mFilterData);
        mCameraRenderer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                RecyclerView recyclerView = layout.findViewById(R.id.filter_list);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (listVisible) {
                        recyclerView.setVisibility(View.INVISIBLE);
                        listVisible = false;
                    }
                }
                return false;
            }
        });


        final View flashOn = layout.findViewById(R.id.flashOn);
        final View flashOff1 = layout.findViewById(R.id.flashOff);

        flashOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraRenderer.flashLightOn();
                flashOn.setVisibility(View.INVISIBLE);
                flashOff1.setVisibility(View.VISIBLE);
            }
        });


        final View flashOn1 = layout.findViewById(R.id.flashOn);
        final View flashOff = layout.findViewById(R.id.flashOff);

        flashOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraRenderer.flashLightOff();
                flashOff.setVisibility(View.INVISIBLE);
                flashOn1.setVisibility(View.VISIBLE);
            }
        });

        View flip = layout.findViewById(R.id.flip);
        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraRenderer.setCameraFront(mCameraRenderer.isCameraFront() ? false : true);
                if(mCameraRenderer.isCameraFront()) {
                    flashOff1.setVisibility(View.INVISIBLE);
                    flashOn1.setVisibility(View.INVISIBLE);
                }
                else{
                    flashOn1.setVisibility(View.VISIBLE);
                }
            }
        });

        View shot = layout.findViewById(R.id.shot);
        shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCameraRenderer.snap = true;
            }
        });
        LinearLayoutManager mLinearLayoutManager;
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        View filter = layout.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerView recyclerView = layout.findViewById(R.id.filter_list);
                if (listVisible) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    listVisible = false;
                } else {
                    listVisible = true;
                    recyclerView.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.filteranim);
                    recyclerView.setAnimation(animation);
                    animation.start();
                }
            }
        });
        RecyclerView recyclerView = layout.findViewById(R.id.filter_list);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setAdapter(new MyImageAdapter());

        return layout;
    }

    class MyImageAdapter extends RecyclerView.Adapter<MyImageViewHolder> {

        @Override
        public MyImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.card_image, null);
            MyImageViewHolder myImageViewHolder = new MyImageViewHolder(itemView);
            return myImageViewHolder;
        }

        @Override
        public void onBindViewHolder(MyImageViewHolder holder, final int position) {

            final String filters = filterList.get(position);
            holder.background.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FilterData filterData = gson.fromJson((String) myFilterList.get(filters), FilterData.class);
                    mCameraRenderer.setSharedData(filterData);
                    mCameraRenderer.requestRender();
                }
            });
            holder.filtName.setText(filters);
        }

        @Override
        public int getItemCount() {
            return filterList.size();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        onDestroy();
    }

    class MyImageViewHolder extends RecyclerView.ViewHolder {
        ImageView background;
        AutofitTextView filtName;

        public MyImageViewHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.background);
            background.setScaleType(ImageView.ScaleType.CENTER_CROP);
            filtName = itemView.findViewById(R.id.filt_name);
        }
    }

    public void init() {
        mFilterData = new FilterData();
        FilterManger.getInstance().save(getContext(), "filter", "Default", gson.toJson(mFilterData));
        mCameraRenderer = layout.findViewById(R.id.cam_render);
        mCameraRenderer.setCameraFront(false);
        mCameraRenderer.setSharedData(mFilterData);
    }



}
