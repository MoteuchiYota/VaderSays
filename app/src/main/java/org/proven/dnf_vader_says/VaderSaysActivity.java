package org.proven.dnf_vader_says;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class VaderSaysActivity extends AppCompatActivity {

    TextView score;
    SoundPool soundPool;
    int soundRTwo, soundLightSaber, soundBlaster, soundChewbie, soundBreath, failedadmiral;
    ImageView imgR2, imgChewbie, imgSaber, imgBlaster, imgPlay;
    int duracion = 1200, delayimage = 500;

    public static int _BLUE = 0;
    public static int _YELLOW = 1;
    public static int _RED = 2;
    public static int _GREEN = 3;


    ArrayList<Integer> sequenceMachine;
    ArrayList<Integer> sequencePlayer;
    boolean playsMachine = true;

    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vader_says);
        instantiateElements();
        setElementsToListener();
        createSoundPool();
        loadAudios();
    }

    //instanciem elements
    private void instantiateElements() {

        imgR2 = (ImageView) findViewById(R.id.ibR2);
        imgChewbie = (ImageView) findViewById(R.id.ibChewbie);
        imgSaber = (ImageView) findViewById(R.id.ibSaber);
        imgBlaster = (ImageView) findViewById(R.id.ibBlaster);
        imgPlay = (ImageView) findViewById(R.id.ibPlay);
        score = (TextView) findViewById(R.id.tvPuntos);
    }

    //listener
    private void setElementsToListener() {

        imgR2.setOnClickListener(listener);
        imgChewbie.setOnClickListener(listener);
        imgSaber.setOnClickListener(listener);
        imgBlaster.setOnClickListener(listener);
        imgPlay.setOnClickListener(listener);

    }

    //resposta al clicar imatges
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //si no juga la maquina.
            if(!playsMachine){
                switch(v.getId()){
                    case R.id.ibBlaster:
                        sequencePlayer.add(_YELLOW);
                        yellowBlaster();
                        break;
                    case R.id.ibChewbie:
                        sequencePlayer.add(_BLUE);
                        blueChewbie();
                        break;
                    case R.id.ibR2:
                        sequencePlayer.add(_RED);
                        redRTwo();
                        break;
                    case R.id.ibSaber:
                        sequencePlayer.add(_GREEN);
                        greenLightSaber();
                        break;
                    case R.id.ibPlay:
                        score.setText(String.valueOf(0));

                }
                if(checkInputColor()){
                    if(sequencePlayer.size()== sequenceMachine.size()){
                        scoreAndLevel(sequencePlayer.size());
                        sequencePlayer.clear();
                        playsMachine =true;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                playsMachine();
                            }
                        },2400);

                    }
                }else{
                    playsMachine = true;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            //lostGame();
                        }
                    },duracion);
                }
            }

            if(v.getId() == R.id.ibPlay){

                initGame();
            }
        }
    };

    private void lostGame() {
        bundle.putString("Score", score.getText().toString());
        soundPool.play(failedadmiral, 80,80,5,0,1);
        Intent chockelost = new Intent(VaderSaysActivity.this, Lose.class);
        chockelost.putExtras(bundle);
        startActivity(chockelost);
    }

    private void scoreAndLevel(int size) {
        if (size == 1){
            showlevel("Empezando nivel 1.\n¡Buena suerte, escoria rebelde!");
            score.setText(String.valueOf(1));
        }
        else if (size >= 0 && size < 5){
            score.setText(String.valueOf(size));
        }
        else if (size == 5){
            showlevel("Empezando nivel\n2");
            score.setText(String.valueOf(size*10));
        }
        else if (size >= 5 && size < 10){
            score.setText(String.valueOf(size*10));
        }
        else if (size == 10){
            showlevel("Empezando nivel\n3");
            score.setText(String.valueOf(size*30));
        }
        else if (size >= 10 && size < 25){
            score.setText(String.valueOf(size*30));
        }
        else if (size == 25){
            showlevel("Empezando nivel\n4");
            score.setText(String.valueOf(size*35));
        }
        else if (size >= 25 && size < 50){
            score.setText(String.valueOf(size*35));
        }
        else if (size == 50){
            showlevel("Empezando nivel\n5");
            score.setText(String.valueOf(size*100));
        }
        else if (size >= 50 && size < 100){
            score.setText(String.valueOf(size*100));
        }
        else if (size == 100){
            showlevel("¡100 aciertos!\n¡Último nivel!");
            score.setText(String.valueOf(size*200));
        }
        else if (size >= 100){
            score.setText(String.valueOf(size*200));
                    }

    }

    private void showlevel(String level) {
        Spannable centeredText = new SpannableString(level);
        centeredText.setSpan(
                new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0, level.length() - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
        );

        Toast promptlevel = Toast.makeText(getApplicationContext(), centeredText, Toast.LENGTH_LONG);
        promptlevel.show();
    }

    private boolean checkInputColor() {
        boolean validated = true;
        for (int i = 0; i < sequencePlayer.size(); i++) {
            if(sequencePlayer.get(i)!= sequenceMachine.get(i)){
                showCorrectButton(sequenceMachine.get(i));
                validated = false;
            }
        }
        return validated;
    }


    private void showCorrectButton(Integer integer) {
        switch(integer){
            case 0:
                Runnable r0 = new Runnable() {
                    @Override
                    public void run(){
                        blueChewbie();
                    }
                };
                handleRun(r0);
                break;

            case 1:
                Runnable r1 = new Runnable() {
                    @Override
                    public void run() {
                        yellowBlaster();
                    }
                };
                handleRun(r1);
                break;

            case 2:
                Runnable r2 = new Runnable() {
                    @Override
                    public void run() {
                        redRTwo();
                    }
                };
                handleRun(r2);
                break;

            case 3:
                Runnable r3 = new Runnable() {
                    @Override
                    public void run() {
                        greenLightSaber();
                    }

                };
                handleRun(r3);
                break;

            default:
                break;

        }
        Runnable r0b = new Runnable() {
            @Override
            public void run(){
                lostGame();
            }
        };
        Handler h0b = new Handler();
        h0b.postDelayed(r0b, 4000);
    }

    private void handleRun(Runnable r) {
        Handler h1 = new Handler();
        h1.postDelayed(r, 1000);
        Handler h2 = new Handler();
        h2.postDelayed(r, 2000);
        Handler h3 = new Handler();
        h3.postDelayed(r, 3000);
    }

    //carga de los audios
    private void loadAudios() {

        soundRTwo = soundPool.load(this.getApplicationContext(), R.raw.r2,0);
        soundChewbie = soundPool.load(this.getApplicationContext(),R.raw.chewbie,0);
        soundLightSaber = soundPool.load(this.getApplicationContext(),R.raw.saber,0);
        soundBlaster = soundPool.load(this.getApplicationContext(),R.raw.blaster,0);
        soundBreath = soundPool.load(this.getApplicationContext(),R.raw.darthvader,0);
        failedadmiral = soundPool.load(this.getApplicationContext(),R.raw.falladoultimavez,0);

    }
    /**
     * Exemple de So pel Blue il·luminant una estona el botó
     */
    public void blueChewbie() {
        imgChewbie.setImageResource(R.drawable.chewbieon);
        soundPool.play(soundChewbie,1,1,0,0,1);

        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgChewbie.setImageResource(R.drawable.chewbieof);
                }
            }, delayimage);
        } catch (Exception e) {
            Log.i("Error azul()",e.toString());
        }
    }


    //metodo amarillo cambia la imagen a otra igual iluminada
    //hace el sonido correspondiente y devuelve la imagen a la principal sin iluminar
    public void yellowBlaster() {
        imgBlaster.setImageResource(R.drawable.blasteron);
        soundPool.play(soundBlaster,1,1,0,0,1);

        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgBlaster.setImageResource(R.drawable.blasterof);
                }
            }, delayimage);
        } catch (Exception e) {
            Log.i("Error Amarillo()",e.toString());
        }
    }

    public void redRTwo() {
        imgR2.setImageResource(R.drawable.r2on);
        soundPool.play(soundRTwo,1,1,0,0,1);

        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgR2.setImageResource(R.drawable.r2off);
                }
            }, delayimage);
        } catch (Exception e) {
            Log.i("Error Rojo()",e.toString());
        }
    }

    public void greenLightSaber() {
        imgSaber.setImageResource(R.drawable.lightsaberon);

        soundPool.play(soundLightSaber,1,1,0,0,1);

        try {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgSaber.setImageResource(R.drawable.lightsaberoff);
                }
            }, delayimage);
        } catch (Exception e) {
            Log.i("Error verde()",e.toString());
        }
    }

    /**
     * How to use SoundPool on all API levels
     */
    protected void createSoundPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            createNewSoundPool();
        } else {
            createOldSoundPool();
        }
    }
    /**
     * Create SoundPool for versions >= LOLLIPOP
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }
    /**
     * Create SoundPool for deprecated versions < LOLLIPOP
     */
    @SuppressWarnings("deprecation")
    protected void createOldSoundPool() {
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
    }


    //Estructura de dades per les tirades de la maquina
    //ArrayList (Integer) 0 blue, 1 yeloow, 2 red, 3 green.
    //metode ens doni 1 dels 4 colors aleatori
    //Metode per reproduir la secuencia de audios de la maquina
    private void initGame() {
        score.setText(String.valueOf(0));
        Runnable r1 = new Runnable() {
            @Override
            public void run(){
                blueChewbie();
            }
        };

        Runnable r2 = new Runnable() {
            @Override
            public void run(){
                redRTwo();
            }
        };

        Runnable r3 = new Runnable() {
            @Override
            public void run(){
                yellowBlaster();
            }
        };

        Runnable r4 = new Runnable() {
            @Override
            public void run(){
                greenLightSaber();
            }
        };

        Runnable r5 = new Runnable() {
            @Override
            public void run() {
                sequenceMachine = new ArrayList<Integer>();
                sequencePlayer = new ArrayList<Integer>();
                playsMachine = true;
                playsMachine();

            }
        };
        Handler h1 = new Handler();
        h1.postDelayed(r1, 0);
        Handler h2 = new Handler();
        h2.postDelayed(r2, 500);
        Handler h3 = new Handler();
        h3.postDelayed(r3, 1000);
        Handler h4 = new Handler();
        h4.postDelayed(r4, 1500);
        Handler h5 = new Handler();
        h5.postDelayed(r5, 4000);

    }

    private void playsMachine() {
        //Afegir 1 tirada a la maquina
        sequenceMachine.add(getMachineSequence());
        //reproduccio de les tirades ade la maqina
        playMachineSequence();
        //quan acabi jugaMaquina = false
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playsMachine = false;

            }
        },duracion * sequenceMachine.size());



    }

    /**
     * Calculate a random value
     * @return random value between 0 and 3.
     */
    public int getMachineSequence() {
        Random rnd = new Random();
        int random = (int) rnd.nextInt(4);
        return random;
    }

    public void playMachineSequence(){
        //recorrer arraylist tirades de maquina
        for(int i = 0; i< sequenceMachine.size(); i++){
            Integer color = sequenceMachine.get(i);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    switch (color.intValue()){
                        case 0:
                            blueChewbie();
                            break;
                        case 1:
                            yellowBlaster();
                            break;
                        case 2:
                            redRTwo();
                            break;
                        case 3:
                            greenLightSaber();
                            break;
                    }
                }
            },duracion * i);
        }
        //per cada tirada
        //switch case per color
        //fer sonar aquest color = hauria de sonar al cap de x temps
        //temps = durada * index del array.

    }
}


/*
Runnable r = playCorrect(sequenceMachine.get(i));
    Handler h1 = new Handler();
                h1.postDelayed(r, 1000);
    Handler h2 = new Handler();
                h2.postDelayed(r, 2000);
    Handler h3 = new Handler();
                h3.postDelayed(r, 3000);

    private Runnable playCorrect(Integer correctButton) {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {

            }
        };
        switch(correctButton){
            case 0:
                r1 = new Runnable() {
                    @Override
                    public void run(){
                        blueChewbie();
                    }
                };
                break;
            case 1:
                r1 = new Runnable() {
                    @Override
                    public void run() {
                        yellowBlaster();
                    }
                };
                break;
            case 2:
                r1 = new Runnable() {
                    @Override
                    public void run() {
                        redRTwo();
                    }
                };

                break;
            case 3:r1 = new Runnable() {
                @Override
                public void run() {
                    greenLightSaber();
                }
            };
                break;
        }
        return r1;
    }*/