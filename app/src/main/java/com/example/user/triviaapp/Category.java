package com.example.user.triviaapp;

public class Category {
    String  name;
    Category( String inputname){
        name = inputname;
    }

    @Override
    public String toString() {
        return "name: " + name  ;
    }
}
