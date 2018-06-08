package com.lsh.fillette.View;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.lsh.fillette.AppManaging.AppInfo;
import com.lsh.fillette.AppManaging.License;
import com.lsh.fillette.Filtering.Filter;
import com.lsh.fillette.Filtering.FilterManger;
import com.lsh.fillette.R;
import com.lsh.fillette.databinding.ActivityMyPageBinding;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;

/**
 * Created by lsh on 2018-04-05.
 * Filter Market which used for sharing user's filter.
 * A simple {@link Fragment} subclass.
 */

public class FilterMarketFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    public static final String FIREBASE_FILTER_URL = "https://kkotest-41a38.firebaseio.com/Filter";
    private static final String STORAGE_URL = "gs://kkotest-41a38.appspot.com";
    private static final String TAG = FilterMarketFragment.class.getName();
    private Firebase filterRef;
    private final FirebaseStorage storage = FirebaseStorage.getInstance(STORAGE_URL);
    private final StorageReference ref = storage.getReference();
    private ProgressBar loginProgress;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private CircleImageView profileImgView;
    private TextView userEmail, userNickName;
    protected ActivityMyPageBinding binding;
    private String path;
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private FilterMarketFragment.MyAdapter mAdapter;
    private ArrayList<Filter> mFilters = new ArrayList<>();
    private ArrayList<String> keys = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private View layout;
    private MainActivity currActivity;
    private FirebaseUser currentUser;
    boolean isCompleteAll = false;

    public FilterMarketFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.fragment_market_my_page, container, false);
        mRecyclerView = layout.findViewById(R.id.recyclerView);
        new MyAppGlideModule();
        currActivity = (MainActivity) getActivity();
        binding = currActivity.getBinding();
        toolbar = layout.findViewById(R.id.toolbar_main);
        toolbar.setSubtitleTextColor(Color.GRAY);
        toolbar.setTitleTextColor(Color.GRAY);
        currActivity.setSupportActionBar(toolbar);

        currentUser = currActivity.getCurrentUser();
        currActivity.setSupportActionBar(toolbar);
        drawer = layout.findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                currActivity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawer.setDrawerLockMode(0);
        MobileAds.initialize(currActivity, getResources().getString(R.string.appId));

        com.getbase.floatingactionbutton.FloatingActionButton createWithCamera = layout.findViewById(R.id.filter_preview);
        createWithCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(currActivity, FilterUploadByCamera.class);
                intent.putStringArrayListExtra("name", names);
                startActivity(intent);
                currActivity.curr = 0;
            }
        });

        navigationView = layout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headView = navigationView.getHeaderView(0);
        profileImgView = headView.findViewById(R.id.profile_img_view);
        userEmail = headView.findViewById(R.id.user_id);
        userNickName = headView.findViewById(R.id.user_name);

        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1, 1);
        mStaggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        //mStaggeredGridLayoutManager.setReverseLayout(true);
        mStaggeredGridLayoutManager.setOrientation(StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new FilterMarketFragment.MyAdapter();
        mRecyclerView.setLayoutManager(mStaggeredGridLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        final RecyclerRefreshLayout refreshStyle = layout.findViewById(R.id.refresh);
        refreshStyle.setOnRefreshListener(new RecyclerRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.notifyDataSetChanged();
                refreshStyle.setRefreshing(false);
            }
        });

        filterRef = new Firebase(FIREBASE_FILTER_URL);
        filterRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Filter value = dataSnapshot.getValue(Filter.class);
                String key = dataSnapshot.getKey();

                if (s == null) {
                    mFilters.add(0, value);
                    keys.add(0, key);
                } else {
                    int nextIdx = keys.indexOf(s) + 1;
                    if (nextIdx == mFilters.size()) {
                        mFilters.add(value);
                        keys.add(key);
                    } else {
                        mFilters.add(nextIdx, value);
                        keys.add(nextIdx, key);
                    }
                }
                names.add(value.getFilterName());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Filter value = dataSnapshot.getValue(Filter.class);
                int index = keys.indexOf(key);
                mFilters.set(index, value);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int index = keys.indexOf(key);
                names.remove(index);
                keys.remove(index);
                mFilters.remove(index);

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                Filter newModel = dataSnapshot.getValue(Filter.class);
                int index = keys.indexOf(key);
                mFilters.remove(index);
                keys.remove(index);

                if (s == null) {
                    mFilters.add(0, newModel);
                    keys.add(0, key);
                } else {
                    int nextIndex = keys.indexOf(s) + 1;
                    if (nextIndex == mFilters.size()) {
                        mFilters.add(newModel);
                        keys.add(key);
                    } else {
                        mFilters.add(nextIndex, newModel);
                        keys.add(nextIndex, key);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                firebaseError.toException().printStackTrace();
            }
        });
        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (currentUser != null) {
            setProfile();
        }
    }

    private void setProfile() {
        path = FilterManger.getInstance().requestProfile();
        userEmail.setText(currentUser.getEmail());
        userNickName.setText(currentUser.getDisplayName());
        Activity activity = getActivity();
        if (activity == null || activity.isFinishing())
            return;
        if (!TextUtils.isEmpty(path)) {
            Glide.with(activity)
                    .load(path)
                    .into(profileImgView);
        } else {
            Glide.with(activity)
                    .load(R.mipmap.ic_launcher_round)
                    .into(profileImgView);
        }
    }

    public String getPath() {
        return this.path;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView background, star, update, download, delete;
        TextView creator, filterName, filterRate;
        CardView cardView;
        CircleImageView profile;
        PageIndicatorView pageIndicatorView;
        ViewPager viewPager;
        AdView adView;

        public MyViewHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.background);
            profile = itemView.findViewById(R.id.profile_img);
            creator = itemView.findViewById(R.id.creatorId);
            cardView = itemView.findViewById(R.id.card_view);
            download = itemView.findViewById(R.id.download);
            pageIndicatorView = itemView.findViewById(R.id.filter_img);
            viewPager = itemView.findViewById(R.id.imageVP);
            filterName = itemView.findViewById(R.id.filter_name);
            filterRate = itemView.findViewById(R.id.filter_rate);
            update = itemView.findViewById(R.id.update);
            star = itemView.findViewById(R.id.star);
            delete = itemView.findViewById(R.id.del_filter);
            adView = itemView.findViewById(R.id.adView);
        }
    }

    class vpAdapter extends PagerAdapter {
        LayoutInflater inflater;
        List<String> imageUrls;

        public vpAdapter(LayoutInflater inflater, List arrayList) {
            this.inflater = inflater;
            this.imageUrls = arrayList;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.filter_list_image, null);
            ImageView imageView = view.findViewById(R.id.img_viewpager_childimage);
            Log.e(TAG, imageUrls.get(position));
            Glide.with(FilterMarketFragment.this).load(ref.child(imageUrls.get(position))).into(imageView);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<FilterMarketFragment.MyViewHolder> {

        @Override
        public FilterMarketFragment.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.card_post, null);
            FilterMarketFragment.MyViewHolder myViewHolder = new FilterMarketFragment.MyViewHolder(itemView);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(final FilterMarketFragment.MyViewHolder holder, final int position) {
            final Filter filter = mFilters.get(position);
            final vpAdapter vp = new vpAdapter(getLayoutInflater(), filter.getImageList());
            final int numStar = filter.getDownCount();
            AdRequest adRequest = new AdRequest.Builder().build();
            holder.creator.setText(filter.getCreator());
            holder.update.setVisibility(filter.getCreator().equals(currentUser.getDisplayName()) ? View.VISIBLE : View.INVISIBLE);
            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TedBottomPicker bottomPicker = new TedBottomPicker.Builder(currActivity)
                            .showCameraTile(false)
                            .setPreviewMaxCount(5000)
                            .showGalleryTile(false)
                            .setSelectMaxCount(5 - filter.getImageList().size())
                            .setOnMultiImageSelectedListener(new TedBottomPicker.OnMultiImageSelectedListener() {
                                @Override
                                public void onImagesSelected(ArrayList<Uri> uriList) {
                                    upLoadImages(position, filter.getImageList().size(), filter.getUid(), filter.getFilterName(), uriList);
                                }
                            }).create();
                    bottomPicker.show(currActivity.getSupportFragmentManager());
                    vp.notifyDataSetChanged();
                    onResume();
                }
            });
            if (10 <= numStar && numStar < 100) {
                holder.star.setImageDrawable(getResources().getDrawable(R.drawable.star_copper));
            } else if (100 <= numStar && numStar < 500) {
                holder.star.setImageDrawable(getResources().getDrawable(R.drawable.star_silver));
            } else if (500 <= numStar && numStar < 1000) {
                holder.star.setImageDrawable(getResources().getDrawable(R.drawable.star_gold));
            } else if (1000 <= numStar) {
                holder.star.setImageDrawable(getResources().getDrawable(R.drawable.star_red));
            } else {
                holder.star.setImageDrawable(getResources().getDrawable(R.drawable.star_default));
            }
            holder.filterRate.setText("다운로드  :  " + filter.getDownCount());
            holder.filterName.setText("필터이름  :  " + filter.getFilterName());
            holder.viewPager.setAdapter(vp);
            holder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    holder.pageIndicatorView.setSelection(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            holder.pageIndicatorView.setCount(holder.viewPager.getAdapter().getCount());
            holder.pageIndicatorView.setSelection(1);
            holder.pageIndicatorView.setViewPager(holder.viewPager);
            final Activity activity = getActivity();
            if (activity == null || activity.isFinishing())
                return;
            if (!TextUtils.isEmpty(filter.getProfileUrl())) {
                Glide.with(FilterMarketFragment.this)
                        .load(filter.getProfileUrl())
                        .into(holder.profile);
            } else {
                Glide.with(FilterMarketFragment.this)
                        .load(R.mipmap.ic_launcher_round)
                        .into(holder.profile);
            }
            holder.delete.setVisibility(filter.getCreator().equals(currentUser.getDisplayName()) ? View.VISIBLE : View.INVISIBLE);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("필터를 삭제하시겠습니까?")
                            .setConfirmText("OK")
                            .setCancelText("NO")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    filterRef.child(keys.get(position)).removeValue();
                                    StorageReference storage = FirebaseStorage.getInstance().getReference().child(currentUser.getUid() + "/" + filter.getFilterName()+"/");
                                    storage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                                    sDialog.dismissWithAnimation();
                                }
                            }).show();
                    Log.e(TAG, "PATH : " + STORAGE_URL + "/" + currentUser.getUid() + "/" + filter.getFilterName());
                }
            });
            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("해당 필터를 다운받으시겠습니까?")
                            .setConfirmText("OK")
                            .setCancelText("NO")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.cancel();
                                    final SweetAlertDialog s = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                    s.setConfirmText("OK")
                                            .setTitleText("다운로드 완료")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    FilterManger.getInstance().save(getContext(), "filter", filter.getFilterName(), filter.getFilterData());
                                                    filterRef.child(keys.get(position)).child("h").setValue(filter.getDownCount() + 1);
                                                    s.cancel();
                                                    currActivity.onStart();
                                                }
                                            }).show();
                                }
                            }).show();
                }
            });
            holder.adView.loadAd(adRequest);
        }

        @Override
        public int getItemCount() {
            return mFilters.size();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        onDestroy();
    }

    public void upLoadImages(int position, int num, String uid, String filterName, ArrayList<Uri> list) {
        StorageReference[] childRef = new StorageReference[list.size()];
        UploadTask[] uploadTask = new UploadTask[list.size()];
        Filter newFilter = mFilters.get(position);
        final List arrayList = newFilter.getImageList();

        for (int i = 0; i < list.size(); ++i) {
            childRef[i] = ref.child(uid + "/" + filterName + "/" + (num + i) + ".jpeg");
            arrayList.add(childRef[i].getPath());
            Log.e(TAG, childRef[i].getPath().toString());
            uploadTask[i] = childRef[i].putFile(list.get(i));

            uploadTask[i].addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    isCompleteAll = false;
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    isCompleteAll = true;
                }
            });
        }
        filterRef.child(keys.get(position)).child("f").setValue(arrayList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_info:
                Intent intent1 = new Intent(getContext(), AppInfo.class);
                startActivity(intent1);
                break;

            case R.id.action_license:
                Intent intent2 = new Intent(getContext(), License.class);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_gallery) {
            Uri uri = Uri.parse("content://media/internal/images/media");
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } else if (id == R.id.logout) {
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    FirebaseAuth.getInstance().signOut();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            currActivity.setUI();
                        }
                    });
                }
            });
        }
        DrawerLayout drawer = layout.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}