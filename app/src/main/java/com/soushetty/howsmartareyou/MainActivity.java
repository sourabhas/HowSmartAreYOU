package com.soushetty.howsmartareyou;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soushetty.howsmartareyou.controller.AppController;
import com.soushetty.howsmartareyou.data.QuestionBank;
import com.soushetty.howsmartareyou.data.QuestionsListAsyncResponse;
import com.soushetty.howsmartareyou.model.Question;
import com.soushetty.howsmartareyou.model.Score;
import com.soushetty.howsmartareyou.util.Preferences;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /*defining all the required widgets and variables*/
    private TextView questionCounterTextview;
    private TextView questionTextview;
    private Button trueButton;
    private Button falseButton;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private int currentQuestionIndex=0;
    private int counter=1;
    private List<Question> questionList;
    private TextView scores;
    private int scoreCounter=0;
    private Preferences preferences;
    private Score score;
    private TextView highestScore;
    private ImageView ani_image;
    public AnimationDrawable animationDrawable;
    public ImageButton sharebutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*instantiating the defined widgets by using their Id's defined in ui*/
        questionCounterTextview=findViewById(R.id.qnumbercounter);
        questionTextview=findViewById(R.id.questiondisplay);
        trueButton=findViewById(R.id.truebutton);
        falseButton=findViewById(R.id.falsebutton);
        previousButton=findViewById(R.id.previous);
        nextButton=findViewById(R.id.next);
        scores=findViewById(R.id.scoreview);
        highestScore=findViewById(R.id.highestscore);
        //animation for the image
        ani_image=(ImageView) findViewById(R.id.animate);
       /* ani_image.setBackgroundResource(R.drawable.anim); //the xml file created under res-drawble
        animationDrawable= (AnimationDrawable) ani_image.getDrawable(); *///to get all the images 1,2,3,4,5 placed in drawble xml file

        score=new Score(); //new object used to calculate scores

        preferences=new Preferences(MainActivity.this);  //Instantiating preferences Class
        //Log.d("Highest_saved","onClick" +preferences.getHighestScore());

        //going to the previous state
        currentQuestionIndex=preferences.getState();
        //calling the onclick listener's
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);

        sharebutton=findViewById(R.id.sharebutton);
        sharebutton.setOnClickListener(this);

        highestScore.setText(MessageFormat.format("Highest Score:{0}", String.valueOf(preferences.getHighestScore())));
        questionList= new QuestionBank().getQuestions(new QuestionsListAsyncResponse() {
            @Override
            public void processfinished(ArrayList<Question> questionArrayList) {
                //inside the processfinished we write the things we need to display at the beginning of the quiz
                questionTextview.setText(questionArrayList.get(currentQuestionIndex).getAnswer());   //displaying first question
                questionCounterTextview.setText((currentQuestionIndex+1)+ " / "+questionArrayList.size()); //display the number of the particular question 0/100

                Log.d("Final"," "+ questionArrayList.get(0));

            }
        });


    }

    //overriding onTouch function for animation of the images in drawable
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation startAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fadein);
                ani_image.startAnimation(startAnimation);
                //stop the animation
                //Uncomment this for FrameAnimation to work
            }
        }, 50); //chance to 50 for fadeAnimation
        return super.onTouchEvent(event);
    }

    //overriding the Onclick() listner function
    @Override
    public void onClick(View v) {
            switch (v.getId()){
                //when the user clicks next button
                case R.id.next:
                    goNext();
                    break;
                //when the user clicks previous button
                case R.id.previous:
                    if(currentQuestionIndex>0){
                        currentQuestionIndex=(currentQuestionIndex-1)%questionList.size();
                        updateQuestionView();
                    }else{
                        Toast.makeText(this,"No more previous questions",Toast.LENGTH_LONG).show();
                    }
                    break;
                //when the user clicks FALSE button
                case R.id.falsebutton:
                    checkAnswer(false);
                    updateQuestionView();
                    break;
                //when the user clicks TRUE button
                case R.id.truebutton:
                    checkAnswer(true);
                    updateQuestionView();
                    break;
                    //when share button is clicked,we need to share the score using implicit intent
                case R.id.sharebutton:
                    sharescore();
                    break;


            }
    }
    //logic for shareing the current and highest score
    private void sharescore(){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Playing an interesting game!");
        String message="My Current Score is "+score.getScore()+" and my highest score until now is "+preferences.getHighestScore()+".Why don't you try?!!";
        intent.putExtra(Intent.EXTRA_TEXT,message);
        startActivity(intent);
    }

    //function to check whether the user clicked answer is correct or wrong and display the message using Toast
    private void checkAnswer(boolean b) {
        int toastMessageId=0;
        if(questionList.get(currentQuestionIndex).isAnswerTrue()==b){
            //Toast.makeText(this,"Correct answer!",Toast.LENGTH_SHORT).show();
            fadeAnimation();
            toastMessageId=R.string.correct_answer;
            addPoints();
            //Log.d("score++", String.valueOf(scoreCounter));

        }else{
            shakeAnimation();
            toastMessageId=R.string.wrong_answer;
            deductPoints();
        }
        Toast.makeText(this,toastMessageId,Toast.LENGTH_SHORT).show();
       // Log.d("Finalscore", String.valueOf(scoreCounter));


    }
    //function to increase the scorecounter value by 1  each time user clicks right answer
    private void addPoints() {
        scoreCounter += 1;
        score.setScore(scoreCounter);
        scores.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));

       // Log.d("Score:", "addPoints: " + score.getScore());
    }
    //function to decrease the score counter value by 1 when user clicks a wrong answer
    private void deductPoints() {
        scoreCounter -= 1;
        if (scoreCounter > 0) {
            score.setScore(scoreCounter);
            Log.d("bad","score minus is :"+scoreCounter);
            scores.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        } else {
            scoreCounter = 0;
            score.setScore(scoreCounter);
            scores.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
            //Log.d("Score Bad", "deductPoints: " + score.getScore());
        }
    }


   /* //method to save the last score before exiting the app
    private void saveScores(int scoreCounter) {
        SharedPreferences sharedPreferences=getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("finalScore", String.valueOf(scoreCounter));
        Log.d("saving ",String.valueOf(scoreCounter));
        editor.apply(); //to save the data passed
        
    }
    //method to get the previous final score of the player,before exiting the app
    private void getLastScore() {
        SharedPreferences getsaveddata=getSharedPreferences(MESSAGE_ID,MODE_PRIVATE);
        String value=getsaveddata.getString("finalScore","No previous scores");
        scores.setText("Scores:"+value);
    }*/

    //method to update the question's display
    private void updateQuestionView() {
        questionTextview.setText(questionList.get(currentQuestionIndex).getAnswer());
        questionCounterTextview.setText((currentQuestionIndex+1)+ " / "+questionList.size());

    }
    //incorporating some shake animations to the cardview when the answer is wrong
    private void shakeAnimation(){
        //Instantiating and loading the Animation class
        Animation shake= AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        final CardView cardview=findViewById(R.id.cardView);
        cardview.setAnimation(shake);

        //Overiding the animation listner to add additional features to the shake animation
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardview.setCardBackgroundColor(Color.RED);//color change to red
                questionTextview.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardview.setCardBackgroundColor(getResources().getColor(R.color.cardview_color)); //setting color back to its old color even before applying animation.
                questionTextview.setTextColor(Color.BLACK);
                goNext(); //inorder to make next question appear automatically
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    //incorporating fade animations to the cardview on aswering correctly
    private void  fadeAnimation(){

        final CardView cardView=findViewById(R.id.cardView);

        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(250);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setRepeatCount(2);

        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
                //questionTextview.setTextColor(Color.YELLOW);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.cardview_color)); //setting color back to its old color even before applying animation.
                questionTextview.setTextColor(Color.BLACK);
                goNext(); //inorder to make next question appear automatically

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    //to go to next question when user clicks NEXT button
    public void goNext(){
        currentQuestionIndex=(currentQuestionIndex+1)% questionList.size();
        updateQuestionView();
    }

    //When the user leaves the application,the state and the last highest score must be saved,hence onPause() method
    @Override
    protected void onPause() {
        preferences.saveHighestScore(score.getScore());
        preferences.setState(currentQuestionIndex);     //setting the state to the question number-when the user leaves the application
        super.onPause();
    }
}
