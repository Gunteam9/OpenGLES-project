package fr.univ.orleans.projetopengl.launcher;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.text.DecimalFormat;

import fr.univ.orleans.projetopengl.audio.AudioManager;
import fr.univ.orleans.projetopengl.R;
import fr.univ.orleans.projetopengl.alerts.GameOverFragment;
import fr.univ.orleans.projetopengl.basic.GameController;
import fr.univ.orleans.projetopengl.basic.MyGLSurfaceView;

/* Ce tutorial est issu d'un tutorial http://developer.android.com/training/graphics/opengl/index.html :
openGLES.zip HelloOpenGLES20
 */

public class OpenGLES20Activity extends FragmentActivity {

    private static MyGLSurfaceView glSurfaceView;
    private final GameController gameController = GameController.getInstance();
    private static FragmentManager fragmentManager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = this.getSupportFragmentManager();
        gameController.setScoreText(findViewById(R.id.score));
        gameController.setTimerText(findViewById(R.id.timer));
        // le conteneur View pour faire du rendu OpenGL
        glSurfaceView = findViewById(R.id.glSurfaceView);
        glSurfaceView.init(this);

        gameController.addAudio(this, R.raw.music, AudioManager.TAG_MUSIC);
        gameController.addAudio(this, R.raw.error, AudioManager.TAG_FAIL);
        gameController.addAudio(this, R.raw.move, AudioManager.TAG_OBJECT_MOVED);
        gameController.addAudio(this, R.raw.success, AudioManager.TAG_WIN);
        gameController.addAudio(this, R.raw.game_lose, AudioManager.TAG_LOSE);

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

    public static MyGLSurfaceView getmGLView() {
        return glSurfaceView;
    }

    public static FragmentManager getStaticFragmentManager()
    {
        return fragmentManager;
    }
}
