package com.example.task2.ui.main.Tab1;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.task2.R;

public class AddContactActivity extends AppCompatActivity implements View.OnClickListener{
    
    private Button cancelButton, saveButton;
    private ImageButton cameraButton;
    private EditText nameEditText, phoneEditText, emailEditText;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        
        cancelButton = findViewById(R.id.cancelBtn);
        saveButton = findViewById(R.id.saveBtn);
        cameraButton = findViewById(R.id.cameraBtn);
        nameEditText = findViewById(R.id.nameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        
        cancelButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelBtn: {
                Intent intent = new Intent();
                
                setResult(RESULT_CANCELED, intent);
                finish();
            }
            
            case R.id.saveBtn: {
                Intent intent = new Intent();
                
                String strName = nameEditText.getText().toString();
                String strPhone = phoneEditText.getText().toString();
                String strEmail = emailEditText.getText().toString();
                
                // TODO : Add photo intent
                
                if(TextUtils.isEmpty(strName)) {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(strPhone)) {
                    Toast.makeText(this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                intent.putExtra("name", strName);
                intent.putExtra("phone", strPhone);
                intent.putExtra("email", strEmail);
                intent.putExtra("photo", Long.valueOf("0"));
                
                setResult(RESULT_OK, intent);
                finish();
            }
            
            case R.id.cameraBtn: {
                // TODO : Add photo activity
            }
        }
    }
}
