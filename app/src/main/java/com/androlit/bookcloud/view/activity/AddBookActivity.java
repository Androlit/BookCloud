package com.androlit.bookcloud.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.data.model.FirebaseBook;
import com.androlit.bookcloud.view.navigator.Navigator;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Time;

/**
 * Created by rubel on 6/6/2017.
 */

public class AddBookActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int PICK_CAMERA = 201;
    private static final int PICK_GALLARY = 202;

    private Button btnAddBook;
    private ImageButton iBtnCoverImage;
    private EditText editTextTitle;
    private EditText editTextAuthor;
    private EditText editTextDescription;
    private EditText editTextIsbn;
    private EditText editTextPrice;
    private Spinner spinnerOffer;
    private Spinner spinnerCondition;
    private TextView tvImageTitle;
    private ImageView imageViewCover;


    private FirebaseBook mBook;
    private Bitmap mPic;

    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    private StorageReference mBookStorageReference;
    private DatabaseReference mBookDatabaseRef;

    private ProgressDialog mProgressDialog;

    private String photoId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        initViews();
        configFirebaseClients();
        mBook = null;
        mPic = null;
        photoId = null;
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AddBookActivity.class);
    }

    private void checkAppUser(){
        mUser = mAuth.getCurrentUser();
        if(mUser == null){
            Toast.makeText(this, "Login to upload", Toast.LENGTH_LONG).show();
            Navigator.navigateToHome(this);
            finish();
        }
    }
    private void initViews(){
        btnAddBook = (Button) findViewById(R.id.btn_book_add);
        btnAddBook.setOnClickListener(this);
        iBtnCoverImage = (ImageButton) findViewById(R.id.image_button_book_cover);
        iBtnCoverImage.setOnClickListener(this);
        editTextTitle = (EditText) findViewById(R.id.edit_text_book_title);
        editTextAuthor = (EditText) findViewById(R.id.edit_text_book_author);
        editTextDescription = (EditText) findViewById(R.id.edit_text_book_description);
        editTextIsbn = (EditText) findViewById(R.id.edit_text_book_isbn);
        editTextPrice = (EditText) findViewById(R.id.edit_text_book_price);
        spinnerOffer = (Spinner) findViewById(R.id.spinner_offers);
        spinnerCondition = (Spinner) findViewById(R.id.spinner_book_condition);
        tvImageTitle = (TextView) findViewById(R.id.tv_book_image_title);
        imageViewCover = (ImageView) findViewById(R.id.image_view_book_cover);
        mProgressDialog = new ProgressDialog(this);
    }

    private void configFirebaseClients(){
        mAuth = FirebaseAuth.getInstance();
        mBookDatabaseRef = FirebaseDatabase.getInstance().getReference().child("books");
        mBookStorageReference = FirebaseStorage.getInstance().getReference().child("books");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_book_add:
                Log.i("Button", "OK");
                if(verifyBookFormInputs()){
                    Log.i("Button", "OK");
                    uploadBookToFirebase();
                }else{
                    Log.i("Button", "FALSE");
                }
                break;
            case R.id.image_button_book_cover:
                showPictureDialog();
                break;
            default: break;
        }
    }

    private void uploadBookToFirebase() {
        showProgressDialog("Uploading book");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mPic.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        photoId = mUser.getUid() + ";" + System.currentTimeMillis();
        UploadTask task = mBookStorageReference.child(photoId)
                .putBytes(baos.toByteArray());
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                Snackbar.make(editTextAuthor, "Photo upload failed",
                        Snackbar.LENGTH_LONG).show();
            }
        });

        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests")
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                uploadBookInfoToDatabase(downloadUri);
            }
        });
    }

    private void uploadBookInfoToDatabase(Uri downloadUri) {
        mBook.setPhotoUrl(downloadUri.toString());
        Task task= mBookDatabaseRef.push().setValue(mBook);

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
                if(photoId != null){
                    mBookStorageReference.child(photoId).delete();
                }
                Snackbar.make(editTextAuthor, "Upload Books Failure", Snackbar.LENGTH_SHORT).show();
            }
        });

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                hideProgressDialog();
                Navigator.navigateToHome(AddBookActivity.this);
                finish();
            }
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select an Action");
        String[] dialogItems = {
                "Select photo from gallary",
                "Capture photo from camera" };
        pictureDialog.setItems(dialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                chooseFromGallary();
                                break;
                            case 1:
                                chooseFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void chooseFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            startActivityForResult(intent, PICK_CAMERA);
        }else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PICK_CAMERA);
        }
    }

    private void chooseFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, PICK_GALLARY);
    }

    private boolean verifyBookFormInputs() {

        mBook = new FirebaseBook();

        String title = editTextTitle.getText().toString().trim();
        if(TextUtils.isEmpty(title)) {
            editTextTitle.setError("Book title must not be empty");
            return false;
        }

        String author = editTextAuthor.getText().toString().trim();
        if(TextUtils.isEmpty(author)){
            editTextAuthor.setError("Author must not be empty");
            return false;
        }

        String description = editTextDescription.getText().toString().trim();
        if(TextUtils.isEmpty(title)) description = "no description";

        String sISBN = editTextIsbn.getText().toString().trim();
        if(TextUtils.isEmpty(sISBN)) {
            editTextIsbn.setError("ISBN is empty");
            return false;
        }else{
            mBook.setIsbn(Long.valueOf(sISBN));
        }

        String sPRICE = editTextPrice.getText().toString().trim();
        if(TextUtils.isEmpty(sPRICE)) {
            editTextPrice.setError("Price is empty");
            return false;
        }else{
            mBook.setPrice(Integer.valueOf(sPRICE));
        }

        if(tvImageTitle.getVisibility() == View.VISIBLE){
            return false;
        }

        String offer = spinnerOffer.getSelectedItem().toString();
        String conditon = spinnerCondition.getSelectedItem().toString();

        mBook.setTitle(title);
        mBook.setAuthor(author);
        mBook.setDescription(description);
        mBook.setOffer(offer);
        mBook.setCondition(conditon);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case PICK_CAMERA:
                if(resultCode == Activity.RESULT_OK){
                    mPic = (Bitmap) data.getExtras().get("data");
                    imageViewCover.setImageBitmap(mPic);
                    imageViewCover.setVisibility(View.VISIBLE);
                    tvImageTitle.setVisibility(View.GONE);
                }
                break;
            case PICK_GALLARY:
                if(resultCode == Activity.RESULT_OK){
                    try {
                        mPic = MediaStore.Images.Media.getBitmap(this.getContentResolver(),
                                data.getData());
                        imageViewCover.setImageBitmap(mPic);
                        imageViewCover.setVisibility(View.VISIBLE);
                        tvImageTitle.setVisibility(View.GONE);
                    } catch (IOException e) {
                        Snackbar.make(editTextAuthor, "Error", Snackbar.LENGTH_SHORT).show();
                    }
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAppUser();
    }

    private void showProgressDialog(String msg) {
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
