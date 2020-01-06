package com.soushetty.howsmartareyou.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/* Interface for accessing and modifying preference data returned by Context#getSharedPreferences.
 For any particular set of preferences, there is a single instance of this class that all clients share*/
public class Preferences {

    private SharedPreferences preferences;
    //defining the constructor
    public Preferences(Activity activity) {
        this.preferences = activity.getPreferences(Context.MODE_PRIVATE); //Mode_private as only this application can access the data saved and retrieve it
    }
    /*In this project,we need to save the last highest score scored by the user,hence should use shared preferences to compare the
    last score and already saved highest score*/
    public void saveHighestScore(int score) {
        int lastScore = preferences.getInt("high_score", 0);

        if (score > lastScore) {
            //we have a new highest and we save it!
            preferences.edit().putInt("high_score", score).apply();
        }

    }

    public int getHighestScore() {
        return preferences.getInt("high_score", 0);
    }

    //method to save the state when the user leaves the application and saving the data by using .apply()
    public void setState(int index){
        preferences.edit().putInt("index_state",index).apply();

    }
    //method to return to the last state where the user left the application
    public int getState(){
        return preferences.getInt("index_state",0);
    }
}
