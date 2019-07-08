package com.example.task2.ui.main.Tab2;

import static com.example.task2.MainActivity.getContextOfApplication;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import java.util.ArrayList;

public class ImageGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Bitmap> mPictures;
    
    public ImageGridAdapter(Context context, ArrayList<Bitmap> pictures) {
        mContext = context;
        mPictures = pictures;
    }
    
    @Override
    public int getCount() {
        return mPictures.size();
    }
    
    @Override
    public Object getItem(int i) {
        return mPictures.get(i);
    }
    
    @Override
    public long getItemId(int i) {
        return 0;
    }
    
    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(mPictures.get(pos));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(330, 330));
        return imageView;
    }
}

