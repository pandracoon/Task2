package com.example.task2.ui.main.Tab2;

import static android.app.Activity.RESULT_OK;
import static com.example.task2.MainActivity.getContextOfApplication;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.task2.R;
import com.example.task2.Retrofit.IMyService;
import com.example.task2.Retrofit.Response;
import com.example.task2.Retrofit.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Fragment2 extends Fragment implements View.OnClickListener {
    
    GridView gridViewImages;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyService iMyService;
    
    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
    
    private static final int INTENT_REQUEST_ALBUM = 100;
    private static final int INTENT_REQUEST_CAMERA = 101;
    public static final String URL = "http://143.248.36.213:3000";
    
    private FloatingActionButton fabGalleryAlbum, fabGalleryRefresh, fabGalleryCamera, fabGalleryMenu;
    private Boolean isFabOpen = false;
    private Animation fab_open, fab_close;
    
    private Uri photoUri;
    private String mImageUrl = "", mFileName, imageFilePath;
    private ArrayList<Bitmap> mBitmaps = new ArrayList<>();
    private ImageGridAdapter imageGridAdapter;
    private OkHttpClient okHttpClient;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_fragment2, container, false);
        
        okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();
    
        Retrofit retrofitClient = RetrofitClient.getInstance();
        iMyService = retrofitClient.create(IMyService.class);
    
        fab_open = AnimationUtils.loadAnimation(getContextOfApplication(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getContextOfApplication(), R.anim.fab_close);
        
        fabGalleryAlbum = (FloatingActionButton) view.findViewById(R.id.fabGalleryAlbum);
        fabGalleryRefresh = (FloatingActionButton) view.findViewById(R.id.fabGalleryRefresh);
        fabGalleryCamera = (FloatingActionButton) view.findViewById(R.id.fabGalleryCamera);
        fabGalleryMenu = (FloatingActionButton) view.findViewById(R.id.fabGalleryMenu);
        
        fabGalleryAlbum.setOnClickListener(this);
        fabGalleryRefresh.setOnClickListener(this);
        fabGalleryCamera.setOnClickListener(this);
        fabGalleryMenu.setOnClickListener(this);
    
        gridViewImages = (GridView) view.findViewById(R.id.gridViewImages);
        imageGridAdapter = new ImageGridAdapter(this.getContext(), mBitmaps);
        downloadPicture("1");
        gridViewImages.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ImageActivity.class);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(mBitmaps.get(position), 300, 400, true);
                intent.putExtra("picture", resizedBitmap);
                startActivity(intent);
            }
        });
        gridViewImages.setAdapter(imageGridAdapter);
        
        return view;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(requestCode == INTENT_REQUEST_ALBUM) {
            if(resultCode == RESULT_OK) {
                try {
                    InputStream is = getContext().getContentResolver().openInputStream(intent.getData());
                    mFileName = getFileName(intent.getData());
                    uploadImage(getBytes(is));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == INTENT_REQUEST_CAMERA) {
            if(resultCode == RESULT_OK) {
                try {
                    InputStream is = getContext().getContentResolver().openInputStream(photoUri);
                    mFileName = getFileName(photoUri);
                    uploadImage(getBytes(is));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
    
    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();
        
        int buffSize = 1024;
        byte[] buff = new byte[buffSize];
        
        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }
        
        return byteBuff.toByteArray();
    }
    
    public void uploadImage(byte[] imageBytes) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();
        IMyService iMyService = retrofit.create(IMyService.class);
        
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
    
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", mFileName, requestFile);
        Call<Response> call = iMyService.uploadImage(body);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                if(response.isSuccessful()) {
                    Response responseBody = response.body();
                    mImageUrl = URL + responseBody.getPath();
                    Log.d("uploadImage", "여기ㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣ");
                    addPicture("1", mFileName);
                }
            }
    
            @Override
            public void onFailure(Call<Response> call, Throwable t) {
        
            }
        });
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabGalleryAlbum: {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(Media.CONTENT_TYPE);
                startActivityForResult(intent, INTENT_REQUEST_ALBUM);
                return;
            }
            
            case R.id.fabGalleryRefresh: {
                mBitmaps.clear();
                downloadPicture("1");
                return;
            }
            
            case R.id.fabGalleryCamera: {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getContext().getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(photoFile != null) {
                        photoUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName(), photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        startActivityForResult(intent, INTENT_REQUEST_CAMERA);
                    }
                }
                return;
            }
    
            case R.id.fabGalleryMenu: {
                if(isFabOpen) {
                    fabGalleryAlbum.startAnimation(fab_close);
                    fabGalleryCamera.startAnimation(fab_close);
                    fabGalleryRefresh.startAnimation(fab_close);
                    fabGalleryAlbum.setClickable(false);
                    fabGalleryCamera.setClickable(false);
                    fabGalleryRefresh.setClickable(false);
                    isFabOpen = false;
                }
                else {
                    fabGalleryAlbum.startAnimation(fab_open);
                    fabGalleryCamera.startAnimation(fab_open);
                    fabGalleryRefresh.startAnimation(fab_open);
                    fabGalleryAlbum.setClickable(true);
                    fabGalleryCamera.setClickable(true);
                    fabGalleryRefresh.setClickable(true);
                    isFabOpen = true;
                }
                return;
            }
        }
    }
    
    private void addPicture(String user_id, String file_name) {
        Log.d("addPicture", "여기ㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣ");
        compositeDisposable.add(iMyService.uploadPictureInfo(user_id, file_name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
                @Override
                public void accept(String response) throws Exception {
                    //Toast.makeText(getActivity(), ""+response, Toast.LENGTH_SHORT).show();
                    mBitmaps.clear();
                    downloadPicture("1");
                }
            }));
    }
    
    private void downloadPicture(String user_id) {
        compositeDisposable.add(iMyService.downloadPicture(user_id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<String>() {
                @Override
                public void accept(String response) throws Exception {
                    if(!isJSONValid(response))
                        return;
                    JSONArray jArray = new JSONArray(response);
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObject = jArray.getJSONObject(i);
                        String file_name = jObject.getString("file_name");
    
                        Glide.with(getContextOfApplication()).asBitmap().load(URL+"/images/"+file_name).into(
                            new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource,
                                    @Nullable Transition<? super Bitmap> transition) {
                                mBitmaps.add(resource);
                                imageGridAdapter.notifyDataSetChanged();
                                }
            
                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                
                                }
                            });
                    }
                }
            }));
    }
    
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "CAMERA_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
            imageFileName,      /* prefix */
            ".jpg",         /* suffix */
            storageDir          /* directory */
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }
    
    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}

