package com.example.wcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class AccountSettings extends AppCompatActivity {
    private ImageView imageView;
    private Toolbar toolbar;
    private TextInputLayout setting_name;
    private ImageButton edit_name;
    private ImageButton edit_image_button;
    private ImageButton edit_status;
    private TextInputLayout setting_status;
    private StorageReference mStorageRef;
    private static final int Gallery=1;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        imageView= findViewById(R.id.profile_image);
        toolbar=(Toolbar)findViewById(R.id.settings_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AccountSettings.this,Profile_picture.class);
                intent.putExtra("Image",image);
                startActivity(intent);
            }
        });

        setting_name=findViewById(R.id.setting_name);
        setting_status=findViewById(R.id.setting_status);
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        String uid=firebaseUser.getUid();
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        databaseReference=database.getReference().child("Users").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name=dataSnapshot.child("name").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();
                image=dataSnapshot.child("image").getValue().toString();
                String thumb=dataSnapshot.child("thumb").getValue().toString();
                setting_name.getEditText().setText(name);
                setting_status.getEditText().setText(status);
                Picasso.get().load(image).into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AccountSettings.this,"Error!! please try again",Toast.LENGTH_SHORT);
            }
        });

        edit_name=findViewById(R.id.edit_name_button);
        edit_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDisplayName();
            }
        });
        edit_status=findViewById(R.id.edit_status_button);
        edit_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditStatus();
            }
        });

        edit_image_button=findViewById(R.id.edit_image_button);
        edit_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent galleryIntent=new Intent();
                galleryIntent.setType(Intent.ACTION_CHOOSER);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE"),Gallery);*/

                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(AccountSettings.this);
            }
        });




    }

    private void EditStatus() {
        DialogFragment dialogFragment=new StatusFragment();
        dialogFragment.show(getSupportFragmentManager(),"Edit Bio");
    }

    private void EditDisplayName() {
        DialogFragment dialogue=new Dialogue();
        FragmentManager fragmentManager=getSupportFragmentManager();
        dialogue.show(fragmentManager ,"Edit name");

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                progressDialog=new ProgressDialog(AccountSettings.this);
                progressDialog.setTitle("Uploading picture...");
                progressDialog.setMessage("Please wait while we upload and process your image");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                String uid=FirebaseAuth.getInstance().getUid();
                final StorageReference profilref=mStorageRef.child("profile pictures").child(uid+".jpg");
                profilref.putFile(resultUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded conten
                                progressDialog.dismiss();
                                profilref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String DownlodUri=uri.toString();
                                        databaseReference.child("image").setValue(DownlodUri);
                                        databaseReference.child("thumb").setValue(DownlodUri);
                                    }
                                });

                                Toast.makeText(AccountSettings.this,"Profile Picture Updated!",Toast.LENGTH_SHORT);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                                // ...
                                Toast.makeText(AccountSettings.this,"Error! Please try again",Toast.LENGTH_SHORT);
                            }
                        });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
