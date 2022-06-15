package com.example.trivia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Debug;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private final int QUESTION_INDEX = 0;
    private final int ANSWER_INDEX = 1;
    private final int CATEGORY_INDEX = 2;

    private TextView questionTextView;
    private TextView categoryTextView;
    private EditText answerEditText;
    private Button answerButton;
    private String answer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up question text view
        questionTextView = (TextView) findViewById(R.id.questiontextview);

        //set up answer text input
        answerEditText = (EditText) findViewById(R.id.answerinput);

        //set up category text view
        categoryTextView = (TextView) findViewById(R.id.categorytextview);

        //set up answer button
        answerButton = (Button) findViewById(R.id.answerbutton);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String guess = String.valueOf(answerEditText.getText());
                if(guess.equals("")){
                    Toast.makeText(getApplicationContext(), "Text field can't be left blank!", Toast.LENGTH_LONG).show();
                }else{
                    if(isCorrectGuess(guess, answer)){
                        Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_LONG).show();
                        getQuestion();
                    }else{
                        Toast.makeText(getApplicationContext(), "Incorrect!! the answer was " + answer + ".", Toast.LENGTH_LONG).show();
                        getQuestion();
                    }
                }
            }
        });

        getQuestion();
    }

    private void getQuestion(){
        answerEditText.setText("");
        Random rand = new Random();
        int randIndex = rand.nextInt(33459);
        String line = "";

        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(getAssets().open("trivia-bank.json")));
            for(int i = 0; i < randIndex; i++)
            {
                line = reader.readLine();
            }

            line = line.substring(0, line.lastIndexOf(","));

            JSONObject jsonObject = new JSONObject(line);

            String question = jsonObject.getString("question");
            String category = jsonObject.getString("category_id");
            if(!question.endsWith("?")) question += "?";
            JSONArray answerArray = jsonObject.getJSONArray("answers");

            questionTextView.setText(question);
            answer = answerArray.getString(0);
            categoryTextView.setText(category);

        }catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isCorrectGuess(String guess, String answer){
        return guess.toLowerCase(Locale.ROOT).equals(answer.toLowerCase(Locale.ROOT));
    }
}