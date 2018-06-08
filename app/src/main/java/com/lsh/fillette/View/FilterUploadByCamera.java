package com.lsh.fillette.View;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.lsh.fillette.Filtering.CameraRenderer;
import com.lsh.fillette.Filtering.Filter;
import com.lsh.fillette.Filtering.FilterData;
import com.lsh.fillette.Filtering.FilterManger;
import com.lsh.fillette.R;
import com.rtugeek.android.colorseekbar.ColorSeekBar;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by lsw on 2018-04-05.
 * Creating filter by user's flavor.
 */

public class FilterUploadByCamera extends AppCompatActivity {

    private FirebaseUser mUser;
    private Firebase filterRef;
    private final String url = "https://kkotest-41a38.firebaseio.com/Filter";
    private static final String STORAGE_URL = "gs://kkotest-41a38.appspot.com";
    private final FirebaseStorage storage = FirebaseStorage.getInstance(STORAGE_URL);
    private final StorageReference ref = storage.getReference();
    private FilterData mSharedData;
    private CameraRenderer mRenderer;
    private ImageView flip;
    SeekBar seekBar;
    ColorSeekBar colorSeekBar;
    private List<String> options = new ArrayList<>();
    private List<String> optionString = new ArrayList<>();
    private ArrayList<String> names;
    int optionIdx = 0;
    int[] progVal = new int[]{50, 50, 50, 0, 0};
    int[] colVal = new int[]{100, 255};
    TextView optionText;
    private final Gson gson = new Gson();
    private String filterName, mFilter;
    private SweetAlertDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_filter_upload);
        PreferenceManager.getDefaultSharedPreferences(this).getInt("simplePreference", 0xffff0000);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        filterRef = new Firebase(url);
        options.add("brightness");
        options.add("contrast");
        options.add("saturation");
        options.add("lomo");
        options.add("three");
        options.add("color_pick");
        names = getIntent().getStringArrayListExtra("name");

        //options.add("color_dropper");
        //options.add("marker");

        optionString.add("밝기");
        optionString.add("대비");
        optionString.add("채도");
        optionString.add("로모");
        optionString.add("삼색");
        optionString.add("컬러");

        mSharedData = new FilterData();
        mRenderer = findViewById(R.id.preview_in_filter_market);
        mRenderer.setSharedData(mSharedData);
        flip = findViewById(R.id.flip2);
        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRenderer.setCameraFront(mRenderer.isCameraFront() ? false : true);
            }
        });
        colorSeekBar = findViewById(R.id.colorSlider);
        seekBar = findViewById(R.id.seek);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (optionIdx == 0) {
                    mSharedData.mBrightness = (progress - 50) / 100f;
                    mRenderer.requestRender();
                } else if (optionIdx == 1) {
                    mSharedData.mContrast = (progress - 50) / 100f;
                    mRenderer.requestRender();
                } else if (optionIdx == 2) {
                    mSharedData.mSaturation = (progress - 50) / 100f;
                    mRenderer.requestRender();
                } else if (optionIdx == 3) {
                    mSharedData.mCornerRadius = (progress) / 100f;
                    mRenderer.requestRender();
                } else if (optionIdx == 4) {
                    mSharedData.mX = (progress) / 5000f;
                    mRenderer.requestRender();
                }
                progVal[optionIdx] = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        optionText = findViewById(R.id.option_text);
        final RecyclerView mRecyclerView = findViewById(R.id.option_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(new MyImageAdapter());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(FilterUploadByCamera.this);
                final EditText name = new EditText(FilterUploadByCamera.this);
                alert.setTitle("저장할 필터 이름을 입력하세요")
                        .setMessage("현재 카메라로 보이는 이미지가 기본 필터사진으로 업로드 됩니다")
                        .setView(name)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                filterName = name.getText().toString();
                                if (TextUtils.isEmpty(filterName)) {
                                    Toast.makeText(FilterUploadByCamera.this, "필터이름을 입력해 주세요!", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (filterName.length() > 10) {
                                    Toast.makeText(FilterUploadByCamera.this, "필터이름은 10글자 이내로 작성해 주세요!", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (names.contains(filterName)) {
                                    Toast.makeText(FilterUploadByCamera.this, "이미 사용중인 이름입니다!", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDialog = new SweetAlertDialog(FilterUploadByCamera.this, SweetAlertDialog.PROGRESS_TYPE);
                                        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                        mDialog.setTitleText("업로드 중입니다..");
                                        mDialog.setCancelable(false);
                                        mDialog.show();
                                    }
                                });
                                mFilter = gson.toJson(mSharedData);
                                FilterManger.getInstance().save(FilterUploadByCamera.this, "filter", filterName, mFilter);
                                mRenderer.shot = true;
                            }
                        }).show();
            }
        });
    }

    public class FileUpload extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            uploadFilter(strings[0], strings[1], strings[2]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mRenderer.setCameraFront(false);
            finish();
        }
    }

    public void setBitmap(Bitmap bitmap) {
        String url = upLoadImage(bitmap);
        new FileUpload().execute(mFilter, filterName, url);
    }

    public void uploadFilter(String filter, String filterName, String imageUrl) {
        Filter mFilter = new Filter();
        mFilter.setCreator(mUser.getDisplayName());
        mFilter.setUid(mUser.getUid());
        mFilter.setFilterName(filterName);
        mFilter.setFilterData(filter);
        mFilter.setDownCount(0);
        mFilter.setDate(System.currentTimeMillis());
        mFilter.setProfileUrl(FilterManger.getInstance().requestProfile());
        List<String> imgList = new ArrayList<>();
        imgList.add(imageUrl);
        mFilter.setImageList(imgList);
        filterRef.push().setValue(mFilter);
    }

    public String upLoadImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        StorageReference defualtRef = ref.child(mUser.getUid() + "/" + filterName + "/default.jpeg");
        UploadTask uploadTask = defualtRef.putBytes(data);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
        return defualtRef.getPath();
    }

    public void toRGB(int color) {
        mSharedData.mRed = (color >> 16 & 0xff) / 255f;
        mSharedData.mGreen = (color >> 8 & 0xff) / 255f;
        mSharedData.mBlue = (color & 0xff) / 255f;
        mSharedData.mAlpha = (color >> 24 & 0xff) / 255f;
    }

    class MyImageAdapter extends RecyclerView.Adapter<MyImageViewHolder> {

        @Override
        public MyImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = getLayoutInflater().inflate(R.layout.option_image, null);
            MyImageViewHolder myImageViewHolder = new MyImageViewHolder(itemView);
            return myImageViewHolder;
        }

        @Override
        public void onBindViewHolder(MyImageViewHolder holder, final int position) {
            final String imgSrc = options.get(position);

            Drawable rid = getApplicationContext().getResources().getDrawable(getApplicationContext().getResources().getIdentifier(imgSrc, "drawable", getPackageName()));
            holder.background.setImageDrawable(rid);
            holder.background.setOnClickListener(new View.OnClickListener() {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.filteranim);

                @Override
                public void onClick(View v) {
                    optionIdx = position;
                    optionText.setText(optionString.get(optionIdx));
                    if (position < 5) {
                        seekBar.setProgress(progVal[optionIdx]);
                        colorSeekBar.setVisibility(View.GONE);
                        seekBar.setVisibility(View.GONE);
                        seekBar.setVisibility(View.VISIBLE);
                        seekBar.setAnimation(animation);
                    } else if (position == 5) {
                        seekBar.setVisibility(View.GONE);
                        colorSeekBar.setMaxPosition(100);
                        colorSeekBar.setColorSeeds(R.array.material_colors);
                        colorSeekBar.setColorBarPosition(colVal[0]);
                        colorSeekBar.setAlphaBarPosition(colVal[1]);
                        colorSeekBar.setBarHeight(3);
                        colorSeekBar.setShowAlphaBar(true);
                        colorSeekBar.setThumbHeight(20);
                        colorSeekBar.setVisibility(View.VISIBLE);
                        colorSeekBar.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                            @Override
                            public void onColorChangeListener(int i, int i1, int i2) {
                                colVal[0] = i;
                                colVal[1] = i1;
                                toRGB(i2);
                                mRenderer.requestRender();
                            }
                        });
                    }
                    animation.start();
                }
            });
        }

        @Override
        public int getItemCount() {
            return options.size();
        }
    }

    class MyImageViewHolder extends RecyclerView.ViewHolder {
        ImageView background;

        public MyImageViewHolder(View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.background);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDialog != null)
            mDialog.dismiss();
    }
}
