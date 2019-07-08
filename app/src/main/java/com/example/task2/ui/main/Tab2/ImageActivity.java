package com.example.task2.ui.main.Tab2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.example.task2.R;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends Activity implements View.OnClickListener{
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab2_imageview);
    
        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        
        backButton.setOnClickListener(this);
        setImage(imageView);
    }
    
    private void setImage(ImageView imageView) {
        Bitmap picture = (Bitmap) getIntent().getParcelableExtra("picture");
        
        imageView.setScaleType(ScaleType.CENTER_INSIDE);
        imageView.setImageBitmap(picture);
        PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
    }
    
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.backButton) {
            finish();
            return;
        }
    }
}
