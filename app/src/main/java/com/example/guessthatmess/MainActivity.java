package com.example.guessthatmess;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import com.example.guessthatmess.classification.ImageClassifier;
import com.example.guessthatmess.classification.Result;


public class MainActivity extends AppCompatActivity {

    private View mainView;
    private int userscore;
    private int compscore;
    private DoodleCanvas doodleCanvas; // custom drawing view
    private ImageClassifier classifier; // complete image classification
    public int counter;
    private CountDownTimer mCountDownTimer;


    TextView textViewResult;
    TextView textViewDraw;
    TextView textTimerShow;
    Button   btn_detect;
    Button   btn_clear;
    Button   btn_next;
    Animation animBlink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doodleCanvas = (DoodleCanvas) findViewById(R.id.doodleCanvas);
        doodleCanvas.init(); // initial drawing view

        textViewResult = (TextView) findViewById(R.id.txt_result_label);
        textViewDraw = (TextView) findViewById(R.id.txt_draw_label);
        textTimerShow = (TextView) findViewById(R.id.txt_timer_label);
        btn_detect = (Button) findViewById(R.id.btn_detect);
        btn_clear = (Button) findViewById(R.id.btn_clear3);
        btn_next = (Button) findViewById(R.id.btn_next);
        animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.blink);


        // instantiate classifier
        try {
            this.classifier = new ImageClassifier(this);
        } catch (IOException e) {
            Log.e("MainActivity", "Cannot initialize tfLite model!", e);
            e.printStackTrace();
        }

        this.mainView = this.findViewById(R.id.activity_main).getRootView();


        resetView();
        //mCountDownTimer = this.createTimer();
    }





    private CountDownTimer createTimer(){
        return new CountDownTimer(15000, 1000) {

            public void onTick(long millisUntilFinished) {

                textTimerShow.setText("Timer:" +String.valueOf(counter));
                textTimerShow.startAnimation(animBlink);
                counter++;
            }

            public void onFinish() {
                textTimerShow.setText("Time is up!!");
                textTimerShow.clearAnimation();
                counter=0;
                //paintView.clear();
                //textViewDraw.setText("");
                btn_detect.setVisibility(View.INVISIBLE);
                btn_clear.setVisibility(View.INVISIBLE);
                btn_next.setVisibility(View.VISIBLE);



                //resetView();
            }

        }.start();
    }



    public void onClearClick(View view) {
        Log.i("MainActivity", "Clear sketch event triggers");
        doodleCanvas.clear();

    }

    public void onDetectClick(View view) {
        Log.i("MainActivity", "Detect sketch event triggers");
        if (classifier == null) {
            Log.e("MainActivity", "Cannot initialize tfLite model!");
            return;
        }


        Bitmap sketch = doodleCanvas.getNormalizedBitmap(); // get resized bitmap

        //showImage(paintView.scaleBitmap(40, sketch));

        // create the result
        Result result = classifier.classify(sketch);
        int expectedIndex = classifier.getExpectedIndex();


        // render results
        textViewResult.setText("Top 3 Guesses:\n");
        for (int index : result.getTopK())
            textViewResult.setText(
                    textViewResult.getText()

                            +classifier.getLabel(index)
                            + " ("
                            + String.format("%.02f", classifier.getProbability(index) * 100)
                            + "%)"
                            + " | "
            );


        //int expectedIndex = classifier.getExpectedIndex();
        if (result.getTopK().contains(expectedIndex)) {
            userscore++;
            textViewResult.append("\nYay! Our brains are synced up");
            textViewResult.append("\nUser score is: " + userscore +" and");
            if(userscore<compscore)
            {
                textViewResult.append("\tUser is behind by " + (compscore-userscore));
            }
            if(userscore>compscore)
            {
                textViewResult.append("\tUser is leading by " + (userscore-compscore));
            }
            if(userscore==compscore)
            {
                textViewResult.append("\tUser is tied with the Computer");
            }

            mainView.setBackgroundColor(Color.rgb(78,175,36));
        } else {
            compscore++;
            textViewResult.append("\nTry Again! I don't understand this art yet.");
            textViewResult.append("\nComputer score is: " + compscore + " and");
            if(userscore==compscore)
            {
                textViewResult.append("\tComputer is tied with the User");
            }
            if(compscore>userscore)
            {
                textViewResult.append("\tComputer is leading by " + (compscore-userscore) );
            }
            if(compscore<userscore)
            {
                textViewResult.append("\tComputer is behind by " + (userscore-compscore));
            }
            mainView.setBackgroundColor(Color.rgb(204, 0,0));
        }


    }


    public void onNextClick(View view) {
        mCountDownTimer.cancel();
        btn_detect.setVisibility(View.VISIBLE);
        btn_clear.setVisibility(View.VISIBLE);
        resetView();
        //mCountDownTimer = this.createTimer();
    }

    // debug: ImageView with rescaled 28x28 bitmap
    private void showImage(Bitmap bitmap) {
        Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
            }
        });

        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }





    private void resetView(){
        btn_next.setVisibility(View.INVISIBLE);
        mainView.setBackgroundColor(Color.WHITE);
        doodleCanvas.clear();
        textViewResult.setText("");
        textTimerShow.setText("");
        ArrayList<Integer> p=new ArrayList<>();
        p.add(59);
        p.add(50);
        p.add(36);
        p.add(64);
        p.add(54);
        p.add(71);
        p.add(14);
        p.add(77);
        p.add(85);
        p.add(99);
        p.add(20);
        p.add(34);
        p.add(66);
        p.add(69);
        p.add(13);
        p.add(45);
        p.add(97);
        p.add(75);
        p.add(82);
        //int[]p=new int[]{59,36,64,54,71,14,77,85,7,99,50};
        Random rand=new Random();
        int r=p.get(rand.nextInt(p.size()));

        //classifier.setExpectedIndex(new Random().nextInt(classifier.getNumberOfClasses()));
        classifier.setExpectedIndex(r);
        textViewDraw.setText("Doodle under 15 seconds.\n Draw ... "
                + classifier.getLabel(classifier.getExpectedIndex()));

        mCountDownTimer = this.createTimer();


    }



}
