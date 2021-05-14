package fr.univ.orleans.projetopengl.launcher;


import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import java.text.DecimalFormat;

import fr.univ.orleans.projetopengl.audio.AudioManager;
import fr.univ.orleans.projetopengl.R;
import fr.univ.orleans.projetopengl.alerts.GameOverFragment;
import fr.univ.orleans.projetopengl.basic.Game;
import fr.univ.orleans.projetopengl.basic.MyGLSurfaceView;

/* Ce tutorial est issu d'un tutorial http://developer.android.com/training/graphics/opengl/index.html :
openGLES.zip HelloOpenGLES20
 */

public class OpenGLES20Activity extends FragmentActivity {

    private final long totalTime = 10000; // nombre de millisecondes
    private final long countDownInterval = 100; // combien de millisecondes sont enlevés à chaque appel
    private static MyGLSurfaceView glSurfaceView;
    private TextView timerText;
    private TextView score;
    public static AudioManager audioManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.timerText = findViewById(R.id.timer);
        this.score = findViewById(R.id.score);
        // le conteneur View pour faire du rendu OpenGL
        glSurfaceView = findViewById(R.id.glSurfaceView);
        glSurfaceView.init(this, this.score);
        audioManager = AudioManager.instance;

        String string = "Score : " +
                game.getScore();
        this.score.setText(string);

        audioManager.addAudio(this, R.raw.music, AudioManager.TAG_MUSIC);
        audioManager.addAudio(this, R.raw.error, AudioManager.TAG_FAIL);
        audioManager.addAudio(this, R.raw.move, AudioManager.TAG_OBJECT_MOVED);
        audioManager.addAudio(this, R.raw.success, AudioManager.TAG_SUCCES);

        audioManager.startAudio(AudioManager.TAG_MUSIC);
        this.startCounter();


//        /* Pour le plein écran */
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//
//        mGLView = new MyGLSurfaceView(this);
//        /* Définition de View pour cette activité */
//        setContentView(mGLView);
    }

    public void startCounter()
    {
        CountDownTimer timer = new CountDownTimer(totalTime, countDownInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                DecimalFormat decimalFormat = new DecimalFormat("#.#");
                StringBuilder s = new StringBuilder()
                        .append("Time : ")
                        .append(decimalFormat.format(millisUntilFinished / 1000.0));

                timerText.setText(s);
                if(game.isHasWon())
                    onFinish();
            }

            @Override
            public void onFinish()
            {
                showDialog();
                audioManager.stopAudio(AudioManager.TAG_MUSIC);
                cancel();
            }
        };
        timer.start();
    }

    private void showDialog()
    {
        GameOverFragment fragment = new GameOverFragment(game.getScore());
        fragment.show(this.getSupportFragmentManager(), "dialog");
    }

    public static MyGLSurfaceView getmGLView() {
        return glSurfaceView;
    }
}
