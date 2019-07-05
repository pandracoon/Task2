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

public class EditContactActivity extends AppCompatActivity implements View.OnClickListener{
    
    private Button cancelButtonE, saveButtonE;
    private ImageButton cameraButtonE;
    private EditText nameEditTextE, phoneEditTextE, emailEditTextE;
    
    private String initialName, initialPhone, initialEmail;
    private long initialPhoto;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        
        Intent intent = getIntent();
    
        cancelButtonE = findViewById(R.id.cancelBtnE);
        saveButtonE = findViewById(R.id.saveBtnE);
        cameraButtonE = findViewById(R.id.cameraBtnE);
        nameEditTextE = findViewById(R.id.nameEditTextE);
        phoneEditTextE = findViewById(R.id.phoneEditTextE);
        emailEditTextE = findViewById(R.id.emailEditTextE);
        
        initialName = intent.getStringExtra("name");
        initialPhone = intent.getStringExtra("phone");
        initialEmail = intent.getStringExtra("email");
        initialPhoto = intent.getLongExtra("photo", 0);
    
        nameEditTextE.setText(initialName);
        phoneEditTextE.setText(initialPhone);
        emailEditTextE.setText(initialEmail);
        
        cancelButtonE.setOnClickListener(this);
        saveButtonE.setOnClickListener(this);
        cameraButtonE.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelBtnE: {
                Intent intent = new Intent();
                
                setResult(RESULT_CANCELED, intent);
                finish();
            }
            
            case R.id.saveBtnE: {
                Intent intent = new Intent();
                
                String strName = nameEditTextE.getText().toString();
                String strPhone = phoneEditTextE.getText().toString();
                String strEmail = emailEditTextE.getText().toString();
                
                // TODO : Add photo intent
                
                if(TextUtils.isEmpty(strName)) {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(strPhone)) {
                    Toast.makeText(this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                if(sameAsInitial()) {
                    setResult(RESULT_CANCELED, intent);
                    finish();
                    return;
                }
                
                intent.putExtra("name", strName);
                intent.putExtra("phone", strPhone);
                intent.putExtra("email", strEmail);
                intent.putExtra("photo", initialPhoto);
                
                setResult(RESULT_OK, intent);
                finish();
            }
            
            case R.id.cameraBtnE: {
                // TODO : Add photo activity
            }
        }
    }
    
    public boolean sameAsInitial() {
        return (initialName.equals(nameEditTextE.getText().toString()) &&
            initialPhone.equals(phoneEditTextE.getText().toString()) &&
            initialEmail.equals(emailEditTextE.getText().toString()));
    }
}
