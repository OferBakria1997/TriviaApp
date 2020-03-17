package com.example.user.triviaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class TrueFalse extends AppCompatActivity {
    public static final String HS = "com.example.user.triviaapp.HS";//move the highscore to the mainactivity to check the max highscore
    JSONArray retrievedQuestionsJSON;
    ArrayList<QuestionTrueFalse> questionsList = new ArrayList<QuestionTrueFalse>();
    int currentQuestionIndex = 0, highscore = 0, flag = 1;
    TextView questionIndexLabel, questionTextLabel;
    ListView answersList;
    Button nextQuestionBtn, quitGameBtn;
    String category, difficulty, baseUrl, num, que;
    ProgressBar progressBar;
    Boolean bool;//used in loop while into the thread
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_true_false);
        Intent intent = getIntent();
        category = intent.getStringExtra(WorkCompletion.CAT);
        difficulty = intent.getStringExtra(WorkCompletion.DEF).toLowerCase();
        que = intent.getStringExtra(WorkCompletion.QUE);
        num = intent.getStringExtra(WorkCompletion.NUM);
        baseUrl = "https://opentdb.com/api.php?amount=";
        baseUrl = baseUrl.concat(num).concat("&category=").concat(category).concat("&difficulty=").concat(difficulty).concat("&type=").concat(que);
        questionIndexLabel = (TextView) findViewById(R.id.questionIndex);
        questionTextLabel = (TextView) findViewById(R.id.questionText);
        answersList = (ListView) findViewById(R.id.answersList);
        nextQuestionBtn = (Button) findViewById(R.id.nextQuestionBtn);
        quitGameBtn = (Button) findViewById(R.id.quitBtn);
        bool = true;
        progressBar = findViewById(R.id.progressbar);
        //loading (thread)
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                {
                    while (bool) {
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        try {
                            Thread.sleep(35);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (progressBar.getProgress() == 100) {
                            progressBar.setProgress(0);
                            bool = false;
                        }
                    }
                }
            }
        });
        thread.start();
        nextQuestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNextQuestion();
            }
        });
        quitGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quit();
            }
        });
        HttpTriviaGettf getRequest = new HttpTriviaGettf(this);
        try {
            String result = getRequest.execute(baseUrl).get();
            thread.join(2000);
            String response = "{\"response_code\":1,\"results\":[]}";
            //If there are no questions asked by the user then the user is sent to MainActivity;
            if (result.equals(response)) {
                Intent intentt = new Intent(TrueFalse.this, MainActivity.class);
                startActivity(intentt);
            }
            retrievedQuestionsJSON = new JSONObject(result).getJSONArray("results");
        } catch (InterruptedException e) {
            //e.printStackTrace();
        } catch (ExecutionException e) {
            //e.printStackTrace();
        } catch (JSONException e) {
            //e.printStackTrace();
        }

    }

    public void responseRecieved() {
        Log.d("result JSON: ", retrievedQuestionsJSON.toString());
        for (int i = 0; i < retrievedQuestionsJSON.length(); i++) {
            JSONObject currentObj;
            try {
                currentObj = retrievedQuestionsJSON.getJSONObject(i);
                QuestionTrueFalse newQuestion = new QuestionTrueFalse(currentObj.getString("category"), currentObj.getString("type"), currentObj.getString("difficulty"), currentObj.getString("question"), currentObj.getString("correct_answer"), currentObj.getJSONArray("incorrect_answers").getString(0));
                questionsList.add(newQuestion);
                Log.d("result object: ", newQuestion.toString());

            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        refreshDisplay();
    }

    private void refreshDisplay() {
        showQuestionAtIndex(currentQuestionIndex);
    }

    private void showQuestionAtIndex(int index) {
        currentQuestionIndex = index;
        int displayIndex = index + 1;
        String placeHolder = "Question " + displayIndex + " out of " + questionsList.size();
        questionIndexLabel.setText(placeHolder);
        questionTextLabel.setText(questionsList.get(index).question);
        refreshList(index);
    }

    public void refreshList(int index) {
        int random = (int) (Math.random() * 50 + 1);
        String[] answers;
        //Mix the questions depending on the random number
        if (random % 2 == 0)
            answers = new String[]{questionsList.get(index).incorrect_answer1, questionsList.get(index).correct_answer};
        else
            answers = new String[]{questionsList.get(index).correct_answer, questionsList.get(index).incorrect_answer1};

        ArrayAdapter<String> answersAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, answers);

        answersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //perform any action you want in response to the  press
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                String item1 = questionsList.get(currentQuestionIndex).correct_answer;
                if (item == item1) //Check whether the answer selected is equal to the correct answer
                {
                    Toast.makeText(getApplicationContext(), "CORRECT!", Toast.LENGTH_SHORT).show();
                    if (flag == 1)//I used the flag to tell if this was the first time the user clicked the options
                    {
                        highscore++;
                        flag = 0;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Wrong!", Toast.LENGTH_SHORT).show();
                    flag = 0;
                }

            }
        });
        answersList.setAdapter(answersAdapter);

    }

    public void showNextQuestion() {
        if (currentQuestionIndex + 1 > questionsList.size() - 1) {
            Intent intent = new Intent(TrueFalse.this, MainActivity.class);
            intent.putExtra(HS, String.valueOf(highscore));
            startActivity(intent);
        } else {
            flag = 1;
            showQuestionAtIndex(currentQuestionIndex + 1);
        }
    }

    private void quit() {
        Intent intent = new Intent(TrueFalse.this, MainActivity.class);
        Toast.makeText(this, "later Noob", Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }
}
