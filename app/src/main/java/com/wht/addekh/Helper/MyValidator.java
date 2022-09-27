package com.wht.addekh.Helper;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by SIR.WilliamRamsay on 03-Dec-15.
 */
public class MyValidator {
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE_REGEX = "\\d{3}-\\d{7}";
    private static final String VEHICLE_REGEX = "^[A-Z]{2}[ -][0-9]{1,2}(?: [A-Z])?(?: [A-Z]*)? [0-9]{4}$";
    private static final String REQUIRED_MSG = "Field required";
    private static final String PASSWORD = "Enter valid Password";
    //private static  final String MobilePattern = "[0-9]{10}";
    Bitmap bitmap = null;

    // validating email id
    public static boolean isValidEmail(EditText editText) {
        String email = editText.getText().toString().trim();
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            editText.setError("Enter valid Email");
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    public static boolean isVehicleNumber(EditText editText) {
        String email = editText.getText().toString().trim();
        Pattern pattern = Pattern.compile(VEHICLE_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            editText.setError("Enter valid Email");
            return false;
        } else {
            editText.setError(null);

            return true;
        }
    }

    // validating password
    public static boolean isValidPassword(EditText editText) {
        String pass = editText.getText().toString().trim();
        if (pass != null && pass.length() > 3) {
            editText.setError(null);
            return true;
        } else {
            editText.setError(PASSWORD);
            return false;
        }
    }

    public static boolean isValidPasswordConFirmPswd(EditText editText) {
        String pass = editText.getText().toString().trim();
        String confirmaion = editText.getText().toString().trim();
        if (pass != confirmaion) {
            editText.setError("Password is not match");
            return true;
        } else {
            editText.setError(PASSWORD);
            return false;
        }
    }


    public static boolean isValidField(EditText editText) {
        String txtValue = editText.getText().toString().trim();

        if (txtValue.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        } else {
            editText.setError(null);
            return true;
        }
    }

    public static boolean isValidFieldTV(TextView textView) {
        String txtValue = textView.getText().toString().trim();

        if (txtValue.length() == 0) {
            textView.setError(REQUIRED_MSG);
            return false;
        } else {
            textView.setError(null);
            return true;
        }
    }

    public static boolean isValidSpinner(Spinner spinner) {
        View view = spinner.getSelectedView();
        TextView textView = (TextView) view;
        if (spinner.getSelectedItemPosition() == 0) {
            textView.setError("None selected");
            return false;
        } else {
            textView.setError(null);
            return true;
        }
    }


    public static boolean isValidMobile(EditText editText) {
        String mob = editText.getText().toString().trim();
        if (mob != null && mob.length() == 10) {
            editText.setError(null);
            return true;
        } else {
            editText.setError(REQUIRED_MSG + " Enter 10 digits");
            return false;
        }
    /*
        android.util.Patterns.PHONE
*/
    }

    public static boolean isValidAadhar(EditText editText) {
        String mob = editText.getText().toString().trim();
        if (mob != null && mob.length() == 12) {
            editText.setError(null);
            return true;
        } else {
            editText.setError(REQUIRED_MSG + " Enter 12 digits");
            return false;
        }
    }

    public static boolean isValidPhone(EditText txtPhone) {
        String phone = txtPhone.getText().toString().trim();
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 6 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                txtPhone.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check = false;
        }
        return check;
    }

    public static boolean isValidRadio(RadioGroup radioGroup, RadioButton radioButton) {

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            radioButton.setError("Please select one");
            return false;
        } else {
            radioButton.setError(null);
            return true;
        }
    }


    public static boolean isValidImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }
        return hasImage;
    }

    public static boolean isMatchPassword(EditText etNewPasword, EditText etConfirmNewPasword) {
        String newPass = etNewPasword.getText().toString().trim();
        String comfirmPass = etConfirmNewPasword.getText().toString().trim();
        if (!isValidPassword(etNewPasword)) {
            return false;
        } else if (comfirmPass.length() == 0) {
            etConfirmNewPasword.setError("Enter Confirm password");
            etConfirmNewPasword.requestFocus();
            return false;
        } else if (!newPass.equals(comfirmPass)) {
            etConfirmNewPasword.setError("new password and confirm password not match");
            etConfirmNewPasword.requestFocus();
            return false;
        } else {
            etConfirmNewPasword.setError(null);
            return true;
        }
    }

    public static boolean isValidString(String string) {

        if (string == null)
            return false;

        if (string.equalsIgnoreCase("null"))
            return false;

        if (string.equalsIgnoreCase("") || string == null)
            return false;
        if (string.equalsIgnoreCase(""))
            return false;
        return true;
    }

    public static boolean isValidNumber(String string) {
        try {
            Integer.parseInt(string);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static boolean isValidPostal(EditText editText) {
        String mob = editText.getText().toString().trim();
        if (mob != null && mob.length() == 6) {
            editText.setError(null);
            return true;
        } else {
            editText.setError(REQUIRED_MSG + " Enter 6 digits");
            return false;
        }
    }


}
