/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.univ.orleans.projetopengl.basic;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import java.util.List;
import java.util.Random;

import fr.univ.orleans.projetopengl.objects.IObject;

/* MyGLRenderer implémente l'interface générique GLSurfaceView.Renderer */

/**
 * Renderer
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    private final GameController gameController = GameController.getInstance();
    private final MyGLSurfaceView surfaceView;

    // Les matrices habituelles Model/View/Projection

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    // Objets like cross and checkmark
    private final List<IObject> objToDraw;

    public MyGLRenderer(List<IObject> objToDraw, MyGLSurfaceView surfaceView) {
        this.objToDraw = objToDraw;
        this.surfaceView = surfaceView;
    }

    /* Première méthode équivalente à la fonction init en OpenGLSL */
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // la couleur du fond d'écran
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        gameController.initializeGrid(surfaceView);
    }

    /* Deuxième méthode équivalente à la fonction Display */
    @Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16]; // pour stocker une matrice


        // glClear rien de nouveau on vide le buffer de couleur et de profondeur */
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        /* on utilise une classe Matrix (similaire à glm) pour définir nos matrices P, V et M*/

        /* Pour le moment on va utiliser une projection orthographique
           donc View = Identity
         */

        /*pour positionner la caméra mais ici on n'en a pas besoin*/
       // Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        Matrix.setIdentityM(mViewMatrix,0);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        Matrix.setIdentityM(mModelMatrix,0);

        /* Pour définir une translation on donne les paramètres de la translation
        et la matrice (ici mModelMatrix) est multipliée par la translation correspondante
         */
        Matrix.translateM(mModelMatrix, 0, 0, 0, 0);

        /* scratch est la matrice PxVxM finale */
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mModelMatrix, 0);

        /* on appelle la méthode dessin du carré élémentaire */
        for (IObject obj : gameController.getObjToDraw()) {
            obj.draw(scratch);
        }

        for (IObject obj : objToDraw) {
            obj.draw(scratch);
        }
    }

    /* équivalent au Reshape en OpenGLSL */
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        /* ici on aurait pu se passer de cette méthode et déclarer
        la projection qu'à la création de la surface !!
         */
        GLES30.glViewport(0, 0, width, height);
        Matrix.orthoM(mProjectionMatrix, 0, (int) (-width / 100), (int) (width / 100), (int) (-height / 100), (int) (height / 100), -1.0f, 1.0f);

    }

    /* La gestion des shaders ... */
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES30.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES30.GL_FRAGMENT_SHADER)
        int shader = GLES30.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);

        return shader;
    }
}
