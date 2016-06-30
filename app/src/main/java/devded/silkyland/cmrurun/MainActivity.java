package devded.silkyland.cmrurun;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    // Explicit
    private static final String urlLogo = "http://swiftcodingthai.com/cmru/cmru_logo.png";
    private static final String urlJson = "http://swiftcodingthai.com/cmru/get_user_master.php";
    private ImageView imageView;
    private EditText usernameEditText, passwordEditText;
    private String userString, passwordString, strID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Widget
        imageView = (ImageView) findViewById(R.id.imageView6);
        usernameEditText = (EditText) findViewById(R.id.editText4);
        passwordEditText = (EditText) findViewById(R.id.editText5);

        // Load Logo
        Picasso.with(this).load(urlLogo).resize(150, 180).into(imageView);

    } // Main Method

    // Create Inner Class
    private class SyncUser extends AsyncTask<Void, Void, String> {
        private Context context;
        private String strURL;
        private boolean statusABoolean = true;
        private String truePasswordString, nameUserString;


        public SyncUser(Context context, String strURL) {
            this.context = context;
            this.strURL = strURL;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(strURL).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("29June", "e doInBack ==> " + e.toString());
                return null;
            }
            //return null;
        } // doInBackground

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("29June", "JSON ==> " + s);

            try {

                JSONArray jsonArray = new JSONArray(s);

                for (int i=0;i<jsonArray.length();i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (userString.equals(jsonObject.getString("User"))) {

                        statusABoolean = false;
                        truePasswordString = jsonObject.getString("Password");
                        nameUserString = jsonObject.getString("Name");
                        strID = jsonObject.getString("id");

                    }  // if

                } // for

                if (statusABoolean) {
                    MyAlert myAlert = new MyAlert();
                    myAlert.myDialog(context, "ไม่มี User นี้",
                            "ไม่มี " + userString + " ในฐานข้อมูลของเรา");
                } else if (passwordString.equals(truePasswordString)) {
                    // Password True
                    Toast.makeText(context, "Welcome " + nameUserString,
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("Name", nameUserString);
                    intent.putExtra("userID", strID);
                    startActivity(intent);
                    finish();

                } else {
                    // Password False
                    MyAlert myAlert = new MyAlert();
                    myAlert.myDialog(context, "Password False",
                            "Please Try Again Password False");

                }

            } catch (Exception e) {
                Log.d("29June", "e onPost ==> " + e.toString());
            }

        } // onPost
    } // SynUser Class

    public void clickSignIn(View view) {
        // Click Sign In
        userString = usernameEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        // Check Space
        if (userString.equals("") || passwordString.equals("")) {
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "ผิดพลาด", "โปรดกรอกข้อมูลให้ครบถ้วน");
        } else {
            checkUserPassword();
        }
    }

    private void checkUserPassword() {
        SyncUser syncUser = new SyncUser(this, urlJson);
        syncUser.execute();
    }

    public void clickSignUpMain(View view) {
        //เริ่มค้นจากหน้านี้ ใช้ชื่อคลาสจากหน้านี้ (This) ไปยัง class SignUpActivity.class
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }

}   //Main Class
