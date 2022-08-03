package com.wht.addekho.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wht.addekho.Activties.ActivityAddStore;
import com.wht.addekho.Activties.BottomsheetImagePickerActivity;
import com.wht.addekho.Constant.IConstant;
import com.wht.addekho.Helper.SharedPref;
import com.wht.addekho.MainActivity;
import com.wht.addekho.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class BottomsheetPublishAdFragment extends BottomSheetDialogFragment {


    BottomSheetBehavior bottomSheetBehavior;
    ImageButton imageButton;
    ImageView image_publish;
    public static Activity _act;
    private String stringUserToken = "null", user_profile_path;

    //Photo Editing
    //Image Picker
    private static final String TAG = ActivityAddStore.class.getSimpleName();
    public static final int REQUEST_LOGO_IMAGE = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    //Image Selection
    private String profileImagePath = "";
    private Uri ilogoUrl;
    private ImageView ivProfilePic, ivAddProfileImage;

    private RequestBody requestFileProfile = null;
    private File profileFile = null;
    private MultipartBody.Part bodyProfile = null;
    
    //newbottomsheetimae
    private static final int REQUEST_STORAGE = 0;
//    private static final int REQUEST_IMAGE_CAPTURE = REQUEST_STORAGE + 1;
    private static final int REQUEST_LOAD_IMAGE = REQUEST_IMAGE_CAPTURE + 1;
    protected BottomSheetLayout bottomSheetLayout;
    private Uri cameraImageUri = null;
//    private ImageView image_publish;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        //inflating layout
        View view = View.inflate(getContext(), R.layout.fragment_adpublish, null);
        imageButton = view.findViewById(R.id.cancelBtn);
        image_publish = view.findViewById(R.id.image_publish);
        bottomSheetLayout = (BottomSheetLayout) view.findViewById(R.id.bottomsheet);
        bottomSheetLayout.setPeekOnDismiss(true);
        _act = getActivity();

        updateProfile();
        bottomSheet.setContentView(view);

        bottomSheet.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                setupFullHeight(bottomSheet);
            }
        });


        image_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkNeedsPermission()) {
                    requestStoragePermission();
                } else {
                    showSheetView();
                }
            }
        });
        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));
        //setting Peek at the 16:9 ratio keyline of its parent.
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

                if (BottomSheetBehavior.STATE_HIDDEN == i) {
                    dismiss();
                }

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });


        //aap bar cancel button clicked
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();
            }
        });



        return bottomSheet;
    }

    private void showSheetView() {
        ImagePickerSheetView sheetView = new ImagePickerSheetView.Builder(getActivity())
                .setMaxItems(30)
                .setShowCameraOption(createCameraIntent() != null)
                .setShowPickerOption(createPickIntent() != null)
                .setImageProvider(new ImagePickerSheetView.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri, int size) {
                        Glide.with(getActivity())
                                .load(imageUri)
                                .centerCrop()
                                .into(imageView);
                    }
                })
                .setOnTileSelectedListener(new ImagePickerSheetView.OnTileSelectedListener() {
                    @Override
                    public void onTileSelected(ImagePickerSheetView.ImagePickerTile selectedTile) {
                        bottomSheetLayout.dismissSheet();
                        if (selectedTile.isCameraTile()) {
                            dispatchTakePictureIntent();
                        } else if (selectedTile.isPickerTile()) {
                            startActivityForResult(createPickIntent(), REQUEST_LOAD_IMAGE);
                        } else if (selectedTile.isImageTile()) {
                            showSelectedImage(selectedTile.getImageUri());
                        } else {
                            genericError();
                        }
                    }
                })
                .setTitle("Choose an image...")
                .create();

        bottomSheetLayout.showWithSheetView(sheetView);
    }

    @Nullable
    private Intent createPickIntent() {
        Intent picImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (picImageIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            return picImageIntent;
        } else {
            return null;
        }
    }

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = createCameraIntent();
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent != null) {
//            // Create the File where the photo should go
//            try {
//                File imageFile = createImageFile();
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
//                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
//            } catch (IOException e) {
//                // Error occurred while creating the File
//                genericError("Could not create imageFile for camera");
//            }
//        }
//    }
private void dispatchTakePictureIntent() {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }
}


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        cameraImageUri = Uri.fromFile(imageFile);
        return imageFile;
    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            Uri image_publish = null;
//            if (requestCode == REQUEST_LOAD_IMAGE && data != null) {
//                image_publish = data.getData();
//                if (image_publish == null) {
//                    genericError();
//                }
//            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {
//                // Do something with imagePath
//                image_publish = cameraImageUri;
//            }
//
//            if (image_publish != null) {
//                showSelectedImage(image_publish);
//            } else {
//                genericError();
//            }
//        }
//    }
@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        image_publish.setImageBitmap(imageBitmap);
    }
}
    private void showSelectedImage(Uri selectedImageUri) {
        image_publish.setImageDrawable(null);
        Glide.with(getActivity())
                .load(selectedImageUri)
                .fitCenter()
                .into(image_publish);
    }

    private void genericError() {
        genericError(null);
    }

    private void genericError(String message) {
        Toast.makeText(getActivity(), message == null ? "Something went wrong." : message, Toast.LENGTH_SHORT).show();
    }
    @Nullable
    private Intent createCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            return takePictureIntent;
        } else {
            return null;
        }
    }

    private boolean checkNeedsPermission() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        } else {
            // Eh, prompt anyway
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE);
        }
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSheetView();
            } else {
                // Permission denied
                Toast.makeText(getActivity(), "Sheet is useless without access to external storage :/", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public void updateProfile() {


        if (SharedPref.getPrefs(_act, IConstant.USER_PHOTO) != null && !SharedPref.getPrefs(_act, IConstant.USER_PHOTO).isEmpty() && !SharedPref.getPrefs(_act, IConstant.USER_PHOTO).equals("null") && !SharedPref.getPrefs(_act, IConstant.USER_PHOTO).equals("")) {
            Glide.with(_act)
                    .load(SharedPref.getPrefs(_act, IConstant.USER_PHOTO))
                    .apply(new RequestOptions().placeholder(R.drawable.defaultphoto).error(R.drawable.defaultphoto))
                    .into(image_publish);
        } else {
            Glide.with(_act)
                    .load(R.drawable.defaultphoto)
                    .apply(new RequestOptions().placeholder(R.drawable.defaultphoto).error(R.drawable.defaultphoto))
                    .into(image_publish);
        }




    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        NestedScrollView bottomSheet = (NestedScrollView) bottomSheetDialog.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);


//        CoordinatorLayout.LayoutParams params =
//                (CoordinatorLayout.LayoutParams) bottomSheet.getLayoutParams();
//        AppBarLayout.ScrollingViewBehavior scrollingViewBehavior =
//                (AppBarLayout.ScrollingViewBehavior) params.getBehavior();
//        scrollingViewBehavior.setOverlayTop(128); // Note: in pixels
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public void onStart() {
        super.onStart();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideAppBar(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);

    }

    private void showView(View view, int size) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int size = (int) array.getDimension(0, 0);
        return size;
    }
}