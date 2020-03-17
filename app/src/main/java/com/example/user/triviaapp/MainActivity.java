package com.example.user.triviaapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String highscore;
    TextView textView, textvieshighscore;
    EditText editText;
    static int help = 0;//Initializes only for the first time and then saves the highscore value when a user plays
    static boolean flag = true;//I used it to know if this is the first time the user is playing
    static Editable name;
    static SharedPreferences prefs;
    ListView listViewhs; // to display the list of high score
    HashMap<String, String> hs = new HashMap();
    int[] intArray = new int[2];//Save the maximum values of the players(2 players)
    String[] nameString = new String[2];//Save the names of the players(2 players) The corresponding values in both arrays
    String[] namehighscore = new String[2];
    int max = 0, i, j = 0;
    /*
    i,j ->Variables to be used to  arrays
    max->to save high score
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textViewhs);
        textvieshighscore = findViewById(R.id.highscore1);
        editText = findViewById(R.id.editText);
        listViewhs = findViewById(R.id.listview);
        if (flag) {
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            highscore();

        }


        if (flag == false) {
            Intent intent = getIntent();
            highscore = intent.getStringExtra(GameActivity.HS);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(name.toString(), highscore);
            editor.commit();
            highscore();
        }
        Button playButton = (Button) findViewById(R.id.playBtn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GameConfigActivity.class);
                flag = false;
                String str = editText.getText().toString();
                if (str.matches(""))//Check if a name has been entered
                    Toast.makeText(getApplicationContext(), "please insert your name", Toast.LENGTH_SHORT).show();
                else {
                    name = editText.getText();
                    startActivity(intent);
                }
            }
        });
    }

    //this func used to display list of high scores
    public void highscore() {
        SharedPreferences.Editor editor = prefs.edit();
        hs = (HashMap<String, String>) prefs.getAll();
        for (String key : hs.keySet()) {
            if (Integer.parseInt(hs.get(key)) > max)
                max = Integer.parseInt(hs.get(key));
        }
        for (i = max; i > 0; i--) {
            for (String key : hs.keySet()) {
                if (Integer.parseInt(hs.get(key)) == i && j < 2) {
                    intArray[j] = Integer.parseInt(hs.get(key));
                    nameString[j] = key;
                    namehighscore[j] = nameString[j] + ":" + intArray[j];
                    j++;
                }
            }
        }
        if (j == 1) {// only 1 player that played the game
            namehighscore[1] = "no more";
        }
        ArrayAdapter<String> namehighscoreAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, namehighscore);
        listViewhs.setAdapter(namehighscoreAdapter);
    }
}
