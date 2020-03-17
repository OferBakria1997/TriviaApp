package com.example.user.triviaapp;

public class QuestionTrueFalse {
    String category, type, difficulty, question, correct_answer, incorrect_answer1;

    QuestionTrueFalse(String inputcategory, String inputtype, String inputdifficulty, String inputquestion, String inputcorrect_answer, String inputincorrect_answer1) {
        category = inputcategory;
        type = inputtype;
        difficulty = inputdifficulty;
        question = inputquestion;
        correct_answer = inputcorrect_answer;
        incorrect_answer1 = inputincorrect_answer1;
    }

    @Override
    public String toString() {
        int random = (int) (Math.random() * 50 + 1);
        if (random % 2 == 0) {
            String res = "category: " + category + "\n" + "type: " + type + "\n" + "difficulty: " + difficulty + "\n" + "question: " + question + "\n" + "correct_answer: " + correct_answer + "\n" + "incorrect_answer1: " + incorrect_answer1;
            return res;
        }
        return "category: " + category + "\n" + "type: " + type + "\n" + "difficulty: " + difficulty + "\n" + "question: " + question + "\n" + "incorrect_answer1: " + incorrect_answer1 + "\n" + "correct_answer: " + correct_answer;
    }
}
