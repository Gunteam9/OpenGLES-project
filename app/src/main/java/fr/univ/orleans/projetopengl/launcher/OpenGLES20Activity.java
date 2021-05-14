package fr.univ.orleans.projetopengl.launcher;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;
import java.util.Map;

import fr.univ.orleans.projetopengl.audio.AudioManager;
import fr.univ.orleans.projetopengl.R;
import fr.univ.orleans.projetopengl.alerts.GameOverFragment;
import fr.univ.orleans.projetopengl.basic.CallBack;
import fr.univ.orleans.projetopengl.basic.Game;
import fr.univ.orleans.projetopengl.basic.MyGLSurfaceView;

/* Ce tutorial est issu d'un tutorial http://developer.android.com/training/graphics/opengl/index.html :
openGLES.zip HelloOpenGLES20
 */

public class OpenGLES20Activity extends FragmentActivity implements CallBack {

    private final long totalTime = 9000; // = 1 minute
    private long countDownInterval = 1000; // combien de millisecondes sont enlevés à chaque appel
    private static MyGLSurfaceView glSurfaceView;
    private TextView timerText;
    private TextView score;
    public static final Game game = new Game();
    public static AudioManager audioManager = new AudioManager();

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
        audioManager.addAudio(this, R.raw.music, AudioManager.TAG_MUSIC);
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
                timerText.setText(String.valueOf((millisUntilFinished + 1000) / 1000));
                if(game.isHasWon())
                {
                    onFinish();
                }

            }

            @Override
            public void onFinish()
            {
                finishGame();
                cancel();
            }
        };
        timer.start();

    }

    private void finishGame()
    {
        showDialog();
        audioManager.stopAudio(AudioManager.TAG_MUSIC);

    }

    private void showDialog()
    {
        GameOverFragment fragment = new GameOverFragment(game.getScore());
        fragment.show(this.getSupportFragmentManager(), "dialog");
    }

    public static MyGLSurfaceView getmGLView() {
        return glSurfaceView;
    }

    @Override
    public void updateScore(String score) {
        this.score.setText(score);
    }
}
