package devded.silkyland.cmrurun;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class ExerciseActivity extends AppCompatActivity {

    //Explicit
    private String userIDString, nameString, goldString, avataString;
    private ImageView imageView;
    private TextView nameTextView, stationTextView, questionTextView;
    private RadioGroup radioGroup;
    private RadioButton choice1RadioButton, choice2RadioButton,
            choice3RadioButton, choice4RadioButton;
    private String[] myQuestionStrings, myChoice1Strings, myChoice2Strings,
            myChoice3Strings, myChoice4Strings, myAnswerStrings;
    private int timesAnInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        //Bind widget
        imageView = (ImageView) findViewById(R.id.imageView7);
        nameTextView = (TextView) findViewById(R.id.textView7);
        stationTextView = (TextView) findViewById(R.id.textView8);
        questionTextView = (TextView) findViewById(R.id.textView9);
        radioGroup = (RadioGroup) findViewById(R.id.ragChoice);
        choice1RadioButton = (RadioButton) findViewById(R.id.radioButton6);
        choice2RadioButton = (RadioButton) findViewById(R.id.radioButton7);
        choice3RadioButton = (RadioButton) findViewById(R.id.radioButton8);
        choice4RadioButton = (RadioButton) findViewById(R.id.radioButton9);


        // Get Value from Intent
        userIDString = getIntent().getStringExtra("userID");
        nameString = getIntent().getStringExtra("Name");
        goldString = getIntent().getStringExtra("Gold");
        avataString = getIntent().getStringExtra("Avata");


        //Show View
        MyData myData = new MyData();
        int[] iconInts = myData.getAvatarInts();
        int intIndex = Integer.parseInt(avataString);
        imageView.setImageResource(iconInts[intIndex]);


        nameTextView.setText(nameString);
        stationTextView.setText("ฐานที่ " + Integer.toString(Integer.parseInt(goldString) + 1));

        SynQuestion synQuestion = new SynQuestion();
        synQuestion.execute();

    } // Main Method

    private class SynQuestion extends AsyncTask<Void, Void, String> {

        // Explicit
        private static final String urlJSON = "http://swiftcodingthai.com/cmru/get_question.php";


        @Override
        protected String doInBackground(Void... voids) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlJSON).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {
                Log.d("1JulyV1", "e doIn ==> " + e.toString());
                return null;
            }

        }   // doIn

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("1JulyV1", "JSON ==> " + s);

            try {
                JSONArray jsonArray = new JSONArray(s);

                String[] questionStrings = new String[jsonArray.length()];
                String[] choice1Strings = new String[jsonArray.length()];
                String[] choice2Strings = new String[jsonArray.length()];
                String[] choice3Strings = new String[jsonArray.length()];
                String[] choice4Strings = new String[jsonArray.length()];
                String[] answerStrings = new String[jsonArray.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    questionStrings[i] = jsonObject.getString("Question");
                    choice1Strings[i] = jsonObject.getString("Choice1");
                    choice2Strings[i] = jsonObject.getString("Choice2");
                    choice3Strings[i] = jsonObject.getString("Choice3");
                    choice4Strings[i] = jsonObject.getString("Choice4");
                    answerStrings[i] = jsonObject.getString("Answer");

                    Log.d("1JulyV2", "question(" + i + ") = " + questionStrings[i]);

                }   // For

                myQuestionStrings = new String[5];
                myChoice1Strings = new String[5];
                myChoice2Strings = new String[5];
                myChoice3Strings = new String[5];
                myChoice4Strings = new String[5];
                myAnswerStrings = new String[5];

                for (int i = 0; i < 5; i++) {

                    Random random = new Random();
                    int randomIndex = random.nextInt(jsonArray.length());
                    myQuestionStrings[i] = questionStrings[randomIndex];
                    myChoice1Strings[i] = choice1Strings[randomIndex];
                    myChoice2Strings[i] = choice2Strings[randomIndex];
                    myChoice3Strings[i] = choice3Strings[randomIndex];
                    myChoice4Strings[i] = choice4Strings[randomIndex];
                    myAnswerStrings[i] = answerStrings[randomIndex];

                }   //For

                // Show View Times 1
                questionTextView.setText("1. " + myQuestionStrings[0]);
                choice1RadioButton.setText(myChoice1Strings[0]);
                choice2RadioButton.setText(myChoice2Strings[0]);
                choice3RadioButton.setText(myChoice3Strings[0]);
                choice4RadioButton.setText(myChoice4Strings[0]);



            } catch (Exception e) {
                Log.d("1JulyV1", "e onPost ==> " + e.toString());
            }
        }   // onPost

    }   // SynQuestion


    public void clickAnswer(View view) {

        for (int i = 0; i < 5; i++) {
            Log.d("1JulyV3", "myQuestion(" + i + ") = " + myQuestionStrings[i]);
        }   // for

        //Check Choose

        if (checkChoose()) {
            //UnChecked
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, "ไม่ได้ตอบคำถาม",
                    "กรุณาตอบคำถาม");

        } else {
            //Checked


        }

    }   // ClickAnswer

    private boolean checkChoose() {

        boolean result = true;

        if (choice1RadioButton.isChecked() ||
                choice2RadioButton.isChecked() ||
                choice3RadioButton.isChecked() ||
                choice4RadioButton.isChecked()) {

            //Checked
            result = false;

        } else {
            //UnChecked
            result = true;
        }

        return result;
    }   //CheckChoose

}   // Main Class
