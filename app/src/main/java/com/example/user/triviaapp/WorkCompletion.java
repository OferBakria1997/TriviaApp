package com.example.user.triviaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class WorkCompletion extends AppCompatActivity {
    public static final String CAT = "com.example.user.triviaapp.CAT";
    public static final String DEF = "com.example.user.triviaapp.DEF";
    public static final String QUE = "com.example.user.triviaapp.QUE";
    public static final String NUM = "com.example.user.triviaapp.NUM";
    //To move a category number, difficulty level, type of questions, amount of questions to the next Activity
    ListView numbers;
    TextView txtview;
    String category, difficulty;
    Button btn;
    RadioButton tf, nottf;
    String str1, num;
    boolean flag1;// to check if the user choose numbers of qustions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_completion);
        flag1 = true;//To know if a numberof  questions have been selected
        Intent intent = getIntent();
        tf = (RadioButton) findViewById(R.id.TF);
        nottf = (RadioButton) findViewById(R.id.notTF);
        category = intent.getStringExtra(GameConfigActivity.CAT);
        difficulty = intent.getStringExtra(GameConfigActivity.DEF).toLowerCase();
        numbers = (ListView) findViewById(R.id.nums);
        btn = (Button) findViewById(R.id.conBtn);
        txtview = findViewById(R.id.numbers);
        String[] numberOfQue = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        };
        ArrayAdapter<String> numbersAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, numberOfQue);
        numbers.setAdapter(numbersAdapter);
        numbers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //perform any action you want in response to the  press
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtview.setText("Choose Quantity of questions :" + String.valueOf(position + 1));
                num = String.valueOf(position + 1);
                flag1 = false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if (tf.isChecked() || nottf.isChecked() && !flag1) {
                    if (tf.isChecked()) {
                        str1 = "boolean";
                        intent = new Intent(WorkCompletion.this, TrueFalse.class);
                    } else if (nottf.isChecked()) {
                        str1 = "multiple";
                        intent = new Intent(WorkCompletion.this, GameActivity.class);
                    }
                    intent.putExtra(CAT, category);
                    intent.putExtra(DEF, difficulty);
                    intent.putExtra(QUE, str1);
                    intent.putExtra(NUM, num);
                    startActivity(intent);

                } else {
                    Toast.makeText(getApplicationContext(), "please enter all option", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
