package com.soushetty.howsmartareyou.data;

import com.soushetty.howsmartareyou.model.Question;

import java.util.ArrayList;
//this interface is used to inform HTTP requests about the available data as there are NOT Synchronous.
public interface QuestionsListAsyncResponse {
    void processfinished(ArrayList<Question> questionArrayList);
    // Main class will obtain the Array list of values when it implements
}
