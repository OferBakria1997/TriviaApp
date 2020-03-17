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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GameConfigActivity extends AppCompatActivity {
    public static final String CAT = "com.example.user.triviaapp.CAT";
    public static final String DEF = "com.example.user.triviaapp.DEF";
    //Move a category number and difficulty to the next Activity(WorkCompletion.class)
    String selectedDifficulty = "", selectedCategory;
    boolean categorySelected = false, difficultySelected = false;
    Button startButton;
    TextView categoryLabel, difficultyLabel;
    JSONArray retrievedQuestionsJSON;
    ArrayList<String> categoriesList = new ArrayList<String>();//so save categories names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_config);

        //wiring UI elements to code
        ListView categoryList, difficultyList;
        categoryList = (ListView) findViewById(R.id.categoryList);
        difficultyList = (ListView) findViewById(R.id.difficultyList);
        startButton = (Button) findViewById(R.id.startGameBtn);
        categoryLabel = (TextView) findViewById(R.id.textView3);
        difficultyLabel = (TextView) findViewById(R.id.textView4);

        HttpTriviaCategory getRequest = new HttpTriviaCategory(this);
        try {
            String result = getRequest.execute("https://opentdb.com/api_category.php").get();//Take the list of categories from the server
            retrievedQuestionsJSON = new JSONObject(result).getJSONArray("trivia_categories");
            responseRecieved();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //define Categories String[] stringArray = list.toArray(new String[0]);
        String[] categories = categoriesList.toArray(new String[0]);
        //define difficulties
        String[] difficulties = {
                "Easy", "Medium", "Hard"
        };


        //define an adapter for categories
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, categories);
        //define an adapter for difficulties
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, difficulties);

        //define behaviour - onClick Listener for categories
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //perform any action you want in response to the  press
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = String.valueOf(position + 9);//The server categories start with id 9
                selectedCategory = item;
                categorySelected = true;
                categoryLabel.setText(parent.getItemAtPosition(position).toString());
            }
        });
        //define behaviour - onClick Listener for difficulties list
        difficultyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //perform any action you want in response to the  press
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                selectedDifficulty = item;
                difficultySelected = true;
                difficultyLabel.setText(item);
            }
        });
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (difficultySelected && categorySelected) {
                    Intent intent = new Intent(GameConfigActivity.this, WorkCompletion.class);
                    intent.putExtra(CAT, selectedCategory);
                    intent.putExtra(DEF, selectedDifficulty);
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "please select difficulty&category", Toast.LENGTH_SHORT).show();
            }
        });

        //connect Listview element to Adapter
        categoryList.setAdapter(categoryAdapter);
        //connect Listview element to Adapter
        difficultyList.setAdapter(difficultyAdapter);
    }

    public void responseRecieved() {
        Log.d("result JSON: ", retrievedQuestionsJSON.toString());
        for (int i = 0; i < retrievedQuestionsJSON.length(); i++) {
            JSONObject currentObj;
            try {
                currentObj = retrievedQuestionsJSON.getJSONObject(i);
                Category newcategory = new Category(currentObj.getString("name"));
                categoriesList.add(newcategory.name);
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

}
