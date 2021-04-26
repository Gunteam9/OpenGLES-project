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
package fr.univ.orleans.projetopengl.lib;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

/* MyGLRenderer implémente l'interface générique GLSurfaceView.Renderer */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";
    //private Square mSquare;
    private final List<IObject> mObject = new ArrayList<>();

    // Les matrices habituelles Model/View/Projection

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mModelMatrix = new float[16];

    //private float[] mSquarePosition = {0.0f, 0.0f};
//    private final float[][] mSquarePosition = {{-1f, 0f}, {1f, 0f}};
    private final float[][] mSquareCoords = {
            {
                -1.0f, 1.0f, 0.0f,
                -1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f
            },

            {
                0.0f, 0.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                1.0f, -1.0f, 0.0f,
                1.0f, 0.0f, 0.0f
            }
    };

    private final float[][] mSquareColors = {
            Colors.RED.multiplyBy(4),
            Colors.GREEN.multiplyBy(4),
    };

    private final float[][] mTriangleCoords = {
            {
                -5.0f, 5.0f, 0.0f,
                -7.0f, 3.0f, 0.0f,
                -3.0f, 3.0f, 0.0f,
            }
    };

    private final float[][] mTriangleColors = {
            Colors.BLUE.multiplyBy(3),
    };

    private final float[][] mRoundCoords = {
            {
                5.0f, 10.0f, 0.0f,
                10.0f, 7.0f, 0.0f,
                9.0f, 0.0f, 0.0f,
                1.0f, 0.0f, 0.0f,
                0.0f, 7.0f, 0.0f,

                4.0f, 6.0f, 0.0f,
                6.0f, 6.0f, 0.0f,
                7.0f, 4.0f, 0.0f,
                5.0f, 2.0f, 0.0f,
                3.0f, 4.0f, 0.0f,
            }
    };

    private final float[][] mRoundColors = {
            Colors.RED.multiplyBy(10),
    };


    /* Première méthode équivalente à la fonction init en OpenGLSL */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // la couleur du fond d'écran
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mObject.add(new Square(mSquareCoords[0], mSquareColors[0]));
        mObject.add(new Square(mSquareCoords[1], mSquareColors[1]));
        mObject.add(new Triangle(mTriangleCoords[0], mTriangleColors[0]));
        mObject.add(new Round(mRoundCoords[0], mRoundColors[0], 0.5f, new float[] {0f, 0f}));
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
//        Matrix.translateM(mModelMatrix, 0, mSquarePosition[0], mSquarePosition[1], 0);
        for (int i = 0; i < mObject.size(); i++)
            Matrix.translateM(mModelMatrix, 0, 0, 0, 0);
//
//        for (int i = 0; i < mSquare.length; i++) {
//            Log.d("Renderer", "mSquarex "+Float.toString(mSquarePosition[i][0]));
//            Log.d("Renderer", "mSquarey "+Float.toString(mSquarePosition[i][1]));
//        }


        /* scratch est la matrice PxVxM finale */
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mModelMatrix, 0);

        /* on appelle la méthode dessin du carré élémentaire */
        for (IObject obj : mObject) {
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
        //Matrix.orthoM(mProjectionMatrix, 0, -10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 1.0f);
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


    /* Les méthodes nécessaires à la manipulation de la position finale du carré */
    public void setPosition(float x, float y) {
        /*mSquarePosition[0] += x;
        mSquarePosition[1] += y;*/
//        mSquarePosition[0][0] = x;
//        mSquarePosition[0][1] = y;

    }

    /**
     *
     * @return Position of object without z axis
     */
    public float[] getPosition() {
        return new float[] {0,0};
//        return mSquarePosition[0];
    }

}
