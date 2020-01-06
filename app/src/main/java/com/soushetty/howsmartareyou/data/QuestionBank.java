package com.soushetty.howsmartareyou.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.soushetty.howsmartareyou.controller.AppController;
import com.soushetty.howsmartareyou.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class QuestionBank {

    ArrayList<Question> questionArrayList=new ArrayList<>(); //an arraylist of type Question.java class
    //using the free JSON API url available to get all the data's stored in it
    private String url="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";

    //function to return the List of all questions
    public List<Question> getQuestions(final QuestionsListAsyncResponse callback){
        /*callback used , interface to let http requests send the signal once actual data is available in the list,
        need this callback method as its Asynchronous.To tell the HTTP requests that the Questions data is ready and available ,we are using
        QuestionsListAsyncResponse interface*/

        //calling Json Array request method-To retrieve all the array indices and store it in array list
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(
                Request.Method.GET,
                url,
                (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i=0;i<response.length();i++){
                            /*including it within try and catch block as we are "getting" values/ritreving values*/

                            try {
                                Question  question=new Question();
                                question.setAnswer(response.getJSONArray(i).get(0).toString());  /*JSONArray is the main array, and the items inside it is
                                                                                                  also an array and not a object.Hence calling another array within the main array*/
                                question.setAnswerTrue(response.getJSONArray(i).getBoolean(1)); //getBoolean since is true and false values

                                //adding all questions to a list
                                questionArrayList.add(question);

                               // Log.d("jsonthings", "onResponse: "+response.getJSONArray(i).get(0));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        /* each time when the array list<> has some information which it obtains from API, we need to call the interface*/
                        if(callback!=null)callback.processfinished(questionArrayList); //sending the list to interface
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);
        return questionArrayList;

    }


}
