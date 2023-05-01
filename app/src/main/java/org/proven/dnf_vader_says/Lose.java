package org.proven.dnf_vader_says;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Lose extends AppCompatActivity {

    private ImageView vaderchoking;
    private Button inputnickbutton;
    private EditText inputnick;
    private String score = "";
    private static final String FILENAME = "scoreboard.txt";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParams();
        setContentView(R.layout.choke_vader_lose_game);
        instantiateElements();
        setElementsToListener();
    }

    private void setElementsToListener() {

        vaderchoking.setOnClickListener(listener);
        inputnickbutton.setOnClickListener(listener);
    }

    private void instantiateElements() {

        vaderchoking = (ImageView) findViewById(R.id.chokinglost);
        inputnickbutton = (Button) findViewById(R.id.inputnickbutton);
        inputnick = (EditText) findViewById(R.id.inputnicktext);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.chokinglost:
                    Intent maingame = new Intent(Lose.this, VaderSaysActivity.class);
                    startActivity(maingame);
                    break;
                case R.id.inputnicktext:
                    inputnick.setText(null);
                case R.id.inputnickbutton:
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("Name", inputnick.getText().toString());
                    try {
                        saveFile();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Intent scoreb = new Intent(Lose.this, ScoreBoard.class);
                    startActivity(scoreb);

                default:
                    break;
            }
        }
    };

    private void saveFile() throws FileNotFoundException {
        String savingScores = "nombre jugador: "+inputnick.getText()+" puntuación: "+score.toString()+"\n";
        FileOutputStream fos = null;
        try{
            fos = openFileOutput (FILENAME, Context.MODE_APPEND);
            fos.write(savingScores.getBytes());

        }catch(Exception e){
            Toast.makeText(
                    this,
                    "El archivo de puntuación no pudo ser salvado.",
                    Toast.LENGTH_LONG).show();
        }finally {
            if (fos != null){
                try{
                    fos.close();
                }catch (Exception e){
                    Toast.makeText(
                            this,
                            "El archivo no se cerró.",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void initParams(){
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            score = bundle.getString("Score");


        }
    }
}
