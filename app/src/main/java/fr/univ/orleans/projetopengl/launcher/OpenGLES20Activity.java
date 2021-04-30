package fr.univ.orleans.projetopengl.launcher;



import android.os.Bundle;

import android.app.Activity;

import fr.univ.orleans.projetopengl.R;
import fr.univ.orleans.projetopengl.basic.Game;
import fr.univ.orleans.projetopengl.basic.MyGLSurfaceView;

/* Ce tutorial est issu d'un tutorial http://developer.android.com/training/graphics/opengl/index.html :
openGLES.zip HelloOpenGLES20
 */

public class OpenGLES20Activity extends Activity {

    public static final Game game = new Game();
    private static MyGLSurfaceView glSurfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // le conteneur View pour faire du rendu OpenGL
        glSurfaceView = findViewById(R.id.glSurfaceView);
        glSurfaceView.init(this);


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
}
