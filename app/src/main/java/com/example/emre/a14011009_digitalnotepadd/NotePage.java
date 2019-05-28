package com.example.emre.a14011009_digitalnotepadd;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class NotePage extends AppCompatActivity {

    private static final char et = '1', iv = '2';
    private static final int GALLERY = 0;
    List<View> allView = new ArrayList<>();
    List<EditText> allET = new ArrayList<>();
    List<Uri> allIV = new ArrayList<>();
    List<Character> types = new ArrayList<>();

    ImageButton backButton, createObjectButton, createET, createImage, createTimer;
    LinearLayout createMenu, contentLayout;
    TextView noteTitleTV;
    String noteID;
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_page);

        Intent intent = getIntent();
        noteID = intent.getStringExtra("noteID");
        backButton = findViewById(R.id.backButton);
        createObjectButton = findViewById(R.id.createObjectButton);
        createET = findViewById(R.id.createET);
        createImage = findViewById(R.id.createImage);
        createTimer = findViewById(R.id.createTimer);
        contentLayout = findViewById(R.id.contentLayout);
        createMenu = findViewById(R.id.createMenu);
        createMenu.setVisibility(View.INVISIBLE);
        noteTitleTV = findViewById(R.id.noteTitleTV);
        noteTitleTV.setText(noteID);
        //inflater = LayoutInflater.from(getApplicationContext());


        GetSavedObjects();

        createTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Fill the blank ",Toast.LENGTH_SHORT).show();
                createMenu.setVisibility(View.INVISIBLE);

            }
        });

        createImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Fill the blank ",Toast.LENGTH_SHORT).show();
                createMenu.setVisibility(View.INVISIBLE);
                ChoosePhotoFromGallery();
            }
        });

        createET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateEditText();
                createMenu.setVisibility(View.INVISIBLE);
            }
        });
        createObjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMenu.setVisibility(View.VISIBLE);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = "";
                int i = 0, ivCount = 0, etCount = 0;
                while (i < types.size()){
                    if(types.get(i) == iv){
                        a = a + iv;
                        a = a + allIV.get(ivCount).toString();
                        a = a + "|";
                        ivCount++;
                    }else if(types.get(i) == et){
                        a = a + et;
                        a = a + allET.get(etCount).getText().toString();
                        a = a + "|";
                        etCount++;
                    }
                    i++;
                }
                SharedPreferences sp = getSharedPreferences("Objects",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.remove(noteID).commit();
                editor.putString(noteID,a).commit();
               // editor.commit();
                Intent intent = new Intent(NotePage.this, MainActivity.class);
                startActivity(intent);
            }
        });


    }


    public void ShowTimeDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Time");

    }


    public void GetSavedObjects(){
        SharedPreferences sp = getSharedPreferences("Objects",MODE_PRIVATE);
        //SharedPreferences.Editor e = sp.edit();
        //e.clear();
        // e.commit();
        String allObjects = sp.getString(noteID,null);
        if(allObjects != null){
            int i = 0, start, end;
            while (i < allObjects.length()){
                char type = allObjects.charAt(i);
                i++;
                start = i;
                while (i < allObjects.length() && allObjects.charAt(i) != '|'){
                    i++;
                }
                end = i;
                i++;
                if(type == et){
                    EditText e = CreateEditText();
                    e.setText(allObjects.substring(start,end));
                }else if(type == iv){
                    Uri imageUri = Uri.parse(allObjects.substring(start,end));
                    try {

                        Toast.makeText(NotePage.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        allIV.add(imageUri);
                        CreateImage(selectedImage);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(NotePage.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
                // titles.add(allTitles.substring(start,end));
            }
            //titles.add(allTitles);
        }
    }

    public void CreateImage(Bitmap bitmap){
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        imageView.getLayoutParams().height = 250;
        imageView.getLayoutParams().width = 250;
        types.add(iv);
        allView.add(imageView);
        contentLayout.addView(imageView);
    }

    public EditText CreateEditText(){
        EditText myEditText = new EditText(getApplicationContext()); // Pass it an Activity or Context
        myEditText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        allET.add(myEditText);
        types.add(et);
        allView.add(myEditText);
        contentLayout.addView(myEditText);
        return myEditText;
    }

    public void ChoosePhotoFromGallery() {
        try {
            if (ActivityCompat.checkSelfPermission(NotePage.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(NotePage.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                //photoURI = data.getData();
                try {
                    final Uri imageUri = data.getData();
                    //photoURI = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    //bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    Toast.makeText(NotePage.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    allIV.add(imageUri);
                    CreateImage(selectedImage);
                    //photoURI = imageUri;

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(NotePage.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case GALLERY:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, GALLERY);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                    Toast.makeText(NotePage.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
