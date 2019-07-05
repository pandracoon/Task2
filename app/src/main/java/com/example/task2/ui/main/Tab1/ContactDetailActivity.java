package com.example.task2.ui.main.Tab1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.bumptech.glide.Glide;
import com.example.task2.R;

public class ContactDetailActivity extends AppCompatActivity implements View.OnClickListener{
    
    private Button backButton, editButton, deleteButton;
    private TextView nameTextView, phoneTextView, emailTextView, emailTagTextView;
    private ImageView callButton;
    private View lineView;
    
    private String initialName, initialPhone, initialEmail = "", contact_id, name, phone, email;
    private long initialPhoto, photo;
    private boolean info_changed = false;
    private int position;
    
    private static final int REQUEST_PHONE_CALL = 1;
    private static final int EDIT_DATA_REQUEST = 3;
    private static final int RESULT_DELETED = 404;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        
        backButton = findViewById(R.id.backBtn);
        editButton = findViewById(R.id.editBtn);
        callButton = findViewById(R.id.callDetailBtn);
        deleteButton = findViewById(R.id.deleteBtn);
        
        backButton.setOnClickListener(this);
        editButton.setOnClickListener(this);
        callButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        
        nameTextView = findViewById(R.id.nameTextView);
        phoneTextView = findViewById(R.id.phoneTextView);
        emailTextView = findViewById(R.id.emailTextView);
        emailTagTextView = findViewById(R.id.emailTagTextView);
        lineView = findViewById(R.id.lineView);
    
        getIncomingIntent();
    }
    
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backBtn: {
                if(info_changed) {
                    Intent intent = new Intent();
                    intent.putExtra("initialName", initialName);
                    intent.putExtra("initialPhone", initialPhone);
                    intent.putExtra("initialEmail", initialEmail);
                    intent.putExtra("initialPhoto", initialPhoto);
                    intent.putExtra("contact_id", contact_id);
                    intent.putExtra("name", name);
                    intent.putExtra("phone", phone);
                    intent.putExtra("email", email);
                    intent.putExtra("photo", photo);
                    setResult(RESULT_OK, intent);
                }
                else {
                    setResult(RESULT_CANCELED);
                }
                finish();
                return;
            }
            
            case R.id.callBtn: {
                Uri uri = Uri.parse("tel:" + phone);
                Intent intent = new Intent(Intent.ACTION_CALL, uri);
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    return;
                }
                startActivity(intent);
            }
            
            case R.id.editBtn: {
                Intent intent = new Intent(this, EditContactActivity.class);
                intent.putExtra("edit", true);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                if(!TextUtils.isEmpty(email)) {
                    intent.putExtra("email", email);
                }
                intent.putExtra("photo", photo);
                startActivityForResult(intent, EDIT_DATA_REQUEST);
            }
            
            case R.id.deleteBtn: {
                Intent intent = new Intent();
                intent.putExtra("contact_id", contact_id);
                intent.putExtra("name", name);
                intent.putExtra("phone", phone);
                intent.putExtra("email", email);
                intent.putExtra("photo", photo);
                intent.putExtra("position", position);
                setResult(RESULT_DELETED, intent);
                finish();
            }
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch(requestCode) {
            case EDIT_DATA_REQUEST: {
                if(resultCode == RESULT_OK) {
                    info_changed = true;
                    name = intent.getStringExtra("name");
                    phone = intent.getStringExtra("phone");
                    email = intent.getStringExtra("email");
                    photo = intent.getLongExtra("photo", 4);
                    
                    nameTextView.setText(name);
                    phoneTextView.setText(phone);
                    emailTextView.setText(email);
                    if(TextUtils.isEmpty(email)) {
                        emailTextView.setVisibility(View.INVISIBLE);
                        emailTagTextView.setVisibility(View.INVISIBLE);
                        lineView.setVisibility(View.INVISIBLE);
                    }
                    else {
                        emailTextView.setVisibility(View.VISIBLE);
                        emailTagTextView.setVisibility(View.VISIBLE);
                        lineView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
    
    private void getIncomingIntent() {
        initialName = getIntent().getStringExtra("name");
        initialPhone = getIntent().getStringExtra("phone");
        initialEmail = getIntent().getStringExtra("email");
        initialPhoto = getIntent().getLongExtra("phone", 0);
        contact_id = getIntent().getStringExtra("contact_id");
        position = getIntent().getIntExtra("position", 0);
    
        name = initialName;
        phone = initialPhone;
        email = initialEmail;
        photo = initialPhoto;
        
        nameTextView.setText(initialName);
        phoneTextView.setText(initialPhone);
        if(TextUtils.isEmpty(initialEmail)) {
            emailTextView.setVisibility(View.INVISIBLE);
            emailTagTextView.setVisibility(View.INVISIBLE);
            lineView.setVisibility(View.INVISIBLE);
        }
        else {
            emailTextView.setText(initialEmail);
        }
    
        ImageView photoImage = findViewById(R.id.photoImageView);
        Glide.with(this)
            .asBitmap()
            .load(R.drawable.profile)
            .into(photoImage);
//        if (getIntent().hasExtra("photo")) {
//            long photo = getIntent().getLongExtra("photo", 0);
//            ImageView photoImage = findViewById(R.id.imageView);
//            Glide.with(this)
//                .asBitmap()
//                .load(photo)
//                .into(photoImage);
//        }
    }
}
