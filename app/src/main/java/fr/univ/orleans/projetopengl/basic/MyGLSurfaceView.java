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

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.univ.orleans.projetopengl.audio.AudioManager;
import fr.univ.orleans.projetopengl.exceptions.NothingTouchedException;
import fr.univ.orleans.projetopengl.objects.Cross;
import fr.univ.orleans.projetopengl.objects.IObject;
import fr.univ.orleans.projetopengl.utils.Colors;
import fr.univ.orleans.projetopengl.utils.Vector2;
import fr.univ.orleans.projetopengl.utils.Vector3;

/* La classe MyGLSurfaceView avec en particulier la gestion des événements
  et la création de l'objet renderer

*/


/* On va dessiner un carré qui peut se déplacer grace à une translation via l'écran tactile */

public class MyGLSurfaceView extends GLSurfaceView {

    /* Un attribut : le renderer (GLSurfaceView.Renderer est une interface générique disponible) */
    /* MyGLRenderer va implémenter les méthodes de cette interface */

    private final Game game = Game.getInstance();
    private MyGLRenderer renderer;
    private final List<IObject> objToDraw = new ArrayList<IObject>();
    TextView score;
    private AudioManager audioManager;

    public MyGLSurfaceView(Context context) {
        super(context);
    }

    public MyGLSurfaceView(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    public void init(Context context, TextView score) {
        audioManager = AudioManager.getInstance();

        setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        // Création d'un context OpenGLES 2.0
        setEGLContextClientVersion(3);
        this.score = score;

        // Création du renderer qui va être lié au conteneur View créé
        renderer = new MyGLRenderer(objToDraw, this);
        setRenderer(renderer);

        // Option pour indiquer qu'on redessine uniquement si les données changent
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    /* Comment interpréter les événements sur l'écran tactile */
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // Les coordonnées du point touché sur l'écran
        float x = e.getX();
        float y = e.getY();

        float xOpengl = getWidth() / 50f * x / getWidth() - getWidth() / 100f;
        float yOpengl = -getHeight() / 50f * y / getHeight() + getHeight() / 100f;

        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            if (game.isGridFinish()) {
                game.randomizeGrid();
                return true;
            }

            clearObjets();
            
            try {
                int caseTouch = getCaseTouch(new Vector2(xOpengl, yOpengl));
                int emptyCase = game.getEmptyPosition();

                if (game.getNeighbours(emptyCase).contains(caseTouch))
                {
                    game.moveObject(caseTouch);
                    String string = "Score : " + game.getScore();
                    this.score.setText(string);
                }
                else
                {
                    audioManager.startAudio(AudioManager.TAG_FAIL);
                    drawObject(new Cross(Colors.RED, new Vector2(0, -15f)), true);
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

    public void drawObject(IObject obj, boolean cleanOther) {
        if (cleanOther)
            objToDraw.clear();

        objToDraw.add(obj);
    }

    public void clearObjets() {
        objToDraw.clear();
    }

    public MyGLRenderer getRenderer() {
        return renderer;
    }

}
