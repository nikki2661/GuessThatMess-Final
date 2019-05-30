package com.example.guessthatmess;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.content.Intent;

import com.example.guessthatmess.classification.ImageClassifier;
import com.example.guessthatmess.classification.Result;


import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Random;


public class StartGame extends Activity {
    private View mRootView;
    private DoodleCanvas doodleCanvas; // custom drawing view
    private ImageClassifier classifier; // complete image classification

    TextView textViewResult;
    TextView textViewDraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_game);

        doodleCanvas = (DoodleCanvas) findViewById(R.id.doodleCanvas);
        doodleCanvas.init(); // initial drawing view

        textViewResult = (TextView) findViewById(R.id.txt_result_label);
        textViewDraw = (TextView) findViewById(R.id.txt_draw_label);

        // instantiate classifier
        try {
            this.classifier = new ImageClassifier(this);
        } catch (IOException e) {
            Log.e("MainActivity", "Cannot initialize tfLite model!", e);
            e.printStackTrace();
        }
        this.mRootView = this.findViewById(R.id.start_game).getRootView();


        resetView();
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

        //showImage(doodleCanvas.scaleBitmap(40, sketch));

        // create the result
        Result result = classifier.classify(sketch);


        // render results
        textViewResult.setText("");
        for (int index : result.getTopK()) {
            textViewResult.setText(
                    textViewResult.getText()
                            +"\n"
                            +classifier.getLabel(index)
                            + " ("
                            + String.format("%.02f", classifier.getProbability(index) * 100)
                            + "%)"
            );
        }

        int expectedIndex = classifier.getExpectedIndex();
        if (result.getTopK().contains(expectedIndex)) {
            mRootView.setBackgroundColor(Color.rgb(78,175,36));
        } else {
            mRootView.setBackgroundColor(Color.rgb(204, 0,0));
        }


    }


    public void onNextClick(View view) {
        resetView();
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


    private void resetView() {
        mRootView.setBackgroundColor(Color.WHITE);
        doodleCanvas.clear();
        textViewResult.setText("");

        // get a random label and set as expected class
        classifier.setExpectedIndex(new Random().nextInt(classifier.getNumberOfClasses()));
        textViewDraw.setText("Doodle ... " + classifier.getLabel(classifier.getExpectedIndex()));

    }




}

