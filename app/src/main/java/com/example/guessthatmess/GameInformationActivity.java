package com.example.guessthatmess;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.guessthatmess.classification.ImageClassifier;

import java.io.IOException;


public class GameInformationActivity extends AppCompatActivity {

    private Button buttonStartGame;
    TextView textViewDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameinformation);


        textViewDraw = (TextView) findViewById(R.id.txt_draw_label);
        buttonStartGame = findViewById(R.id.btn_startgame);

        textViewDraw.setText("Let's Play!\n\n\n"+"To win, draw an accurate doodle of the given object\n\n"+"The computer will guess your drawing to see if it is correct");
        buttonStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(getApplication(),MainActivity.class);
                startActivity(intent);

            }
        });


    }
}