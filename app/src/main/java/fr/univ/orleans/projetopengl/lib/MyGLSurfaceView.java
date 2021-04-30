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

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import java.util.Map;

import fr.univ.orleans.projetopengl.exceptions.NothingTouchedException;

import static fr.univ.orleans.projetopengl.lib.OpenGLES20Activity.game;

/* La classe MyGLSurfaceView avec en particulier la gestion des événements
  et la création de l'objet renderer

*/


/* On va dessiner un carré qui peut se déplacer grace à une translation via l'écran tactile */

public class MyGLSurfaceView extends GLSurfaceView {

    /* Un attribut : le renderer (GLSurfaceView.Renderer est une interface générique disponible) */
    /* MyGLRenderer va implémenter les méthodes de cette interface */

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        // Création d'un context OpenGLES 2.0
        setEGLContextClientVersion(3);

        // Création du renderer qui va être lié au conteneur View créé
        mRenderer = new MyGLRenderer();
        setRenderer(mRenderer);

        // Option pour indiquer qu'on redessine uniquement si les données changent
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    /* Comment interpréter les événements sur l'écran tactile */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // Les coordonnées du point touché sur l'écran
        float x = e.getX();
        float y = e.getY();

        // la taille de l'écran en pixels
//        float screen_x = getWidth();
//        float screen_y = getHeight();


        // Des messages si nécessaires */
//        Log.d("message", "x"+Float.toString(x));
//        Log.d("message", "y"+Float.toString(y));
//        Log.d("message", "screen_x="+Float.toString(screen_x));
//        Log.d("message", "screen_y="+Float.toString(screen_y));


        /* Conversion des coordonnées pixel en coordonnées OpenGL
        Attention l'axe x est inversé par rapport à OpenGLSL
        On suppose que l'écran correspond à un carré d'arête 2 centré en 0
         */

        //Permet d'adapter à la taille de création
        float xOpengl = 20.0f*x/getWidth() - 10.0f;
        float yOpengl = -20.0f*y/getHeight() + 10.0f;

//        Log.d("message","x_opengl="+Float.toString(xOpengl));
//        Log.d("message","y_opengl="+Float.toString(yOpengl));


        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            try {
                System.out.println("position: " + xOpengl + " " + yOpengl);
                int caseTouch = getCaseTouch(new Vector2(xOpengl, yOpengl));
                System.out.println("Case touched: " + caseTouch);
                int emptyCase = game.getEmptyPosition();

                if (game.getNeighbours(emptyCase).contains(caseTouch)) {
                    game.moveObject(caseTouch);
                }

                requestRender(); // équivalent de glutPostRedisplay pour lancer le dessin avec les modifications.

            } catch (NothingTouchedException nothingTouchedException) {
                System.out.println("Nothing touched");
            }
        }

        return true;
    }

    private int getCaseTouch(Vector2 touchedPoint) throws NothingTouchedException {
        for (Map.Entry<Integer, IObject> entry : game.getCurrentGrid().entrySet()) {

            Vector2 max = new Vector2(Integer.MIN_VALUE, Integer.MIN_VALUE);
            Vector2 min = new Vector2(Integer.MAX_VALUE, Integer.MAX_VALUE);

            // We take the north - west point and the south - est point
            for (Vector3 vec : entry.getValue().getCoords()) {
                if (max.x < vec.x)
                    max.x = vec.x;
                if (max.y < vec.y)
                    max.y = vec.y;
                if (min.x > vec.x)
                    min.x = vec.x;
                if (min.y > vec.y)
                    min.y = vec.y;
            }

            if (touchedPoint.x < max.x && touchedPoint.x > min.x && touchedPoint.y < max.y && touchedPoint.y > min.y)
                return entry.getKey();
        }

        throw new NothingTouchedException();
    }
}
