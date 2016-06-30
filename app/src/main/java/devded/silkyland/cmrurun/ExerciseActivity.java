package devded.silkyland.cmrurun;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class ExerciseActivity extends AppCompatActivity {

    //Explicit
    private String userIDString, nameString, goldString, avataString;
    private ImageView imageView;
    private TextView nameTextView, stationTextView, questionTextView;
    private RadioGroup radioGroup;
    private RadioButton choice1RadioButton, choice2RadioButton,
            choice3RadioButton, choice4RadioButton;

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

    } // Main Method

    public void clickAnswer(View view) {

    }   // ClickAnswer

}   // Main Class
