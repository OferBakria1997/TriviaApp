package com.example.user.triviaapp;

/**
 * Created by ShacharsMac on 05/06/2018.
 */

public class Question {
    String category, type, difficulty, question, correct_answer, incorrect_answer1, incorrect_answer2, incorrect_answer3;

    Question(String inputcategory, String inputtype, String inputdifficulty, String inputquestion, String inputcorrect_answer, String inputincorrect_answer1, String inputincorrect_answer2, String inputincorrect_answer3) {
        category = inputcategory;
        type = inputtype;
        difficulty = inputdifficulty;
        question = inputquestion;
        correct_answer = inputcorrect_answer;
        incorrect_answer1 = inputincorrect_answer1;
        incorrect_answer2 = inputincorrect_answer2;
        incorrect_answer3 = inputincorrect_answer3;
    }

    @Override
    public String toString() {
        String res = "category: " + category + "\n" + "type: " + type + "\n" + "difficulty: " + difficulty + "\n" + "question: " + question + "\n" + "correct_answer: " + correct_answer + "\n" + "incorrect_answer1: " + incorrect_answer1 + "\n" + "incorrect_answer2: " + incorrect_answer2 + "\n" + "incorrect_answer3: " + incorrect_answer3;
        return res;
    }
}
