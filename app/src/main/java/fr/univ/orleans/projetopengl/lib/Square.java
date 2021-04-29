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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import android.graphics.Color;
import android.opengl.GLES10;
import android.os.Build;
import android.util.Log;

//import android.opengl.GLES20;
import android.opengl.GLES30;

import androidx.annotation.RequiresApi;

import org.apache.commons.lang3.ArrayUtils;


//Dessiner un carré

@RequiresApi(api = Build.VERSION_CODES.N)
public class Square implements IObject {
/* Le vertex shader avec la définition de gl_Position et les variables utiles au fragment shader
 */
    private final String vertexShaderCode =
        "#version 300 es\n"+
                "uniform mat4 uMVPMatrix;\n"+
            "in vec3 vPosition;\n" +
                "in vec4 vCouleur;\n"+
                "out vec4 Couleur;\n"+
                "out vec3 Position;\n"+
            "void main() {\n" +
                "Position = vPosition;\n"+
            "gl_Position = uMVPMatrix * vec4(vPosition,1.0);\n" +
                "Couleur = vCouleur;\n"+
            "}\n";

    private final String fragmentShaderCode =
            "#version 300 es\n"+
            "precision mediump float;\n" + // pour définir la taille d'un float
            "in vec4 Couleur;\n"+
            "in vec3 Position;\n"+
            "out vec4 fragColor;\n"+
            "void main() {\n" +
            "fragColor = Couleur;\n" +
            "}\n";

    /* les déclarations pour l'équivalent des VBO */

    private FloatBuffer vertexBuffer; // Pour le buffer des coordonnées des sommets du carré
    private ShortBuffer indiceBuffer; // Pour le buffer des indices
    private FloatBuffer colorBuffer; // Pour le buffer des couleurs des sommets

    /* les déclarations pour les shaders
    Identifiant du programme et pour les variables attribute ou uniform
     */
    private int IdProgram; // identifiant du programme pour lier les shaders
    private int IdPosition; // idendifiant (location) pour transmettre les coordonnées au vertex shader
    private int IdCouleur; // identifiant (location) pour transmettre les couleurs
    private int IdMVPMatrix; // identifiant (location) pour transmettre la matrice PxVxM

    static final int COORDS_PER_VERTEX = 3; // nombre de coordonnées par vertex
    static final int COULEURS_PER_VERTEX = 4; // nombre de composantes couleur par vertex

    int[] linkStatus = {0};

    // Le tableau des coordonnées des sommets
    final float[] initialSquareCoords = {
            -2.0f, 2.0f, 0.0f,
            -2.0f, -2.0f, 0.0f,
            2.0f, -2.0f, 0.0f,
            2.0f, 2.0f, 0.0f
    };

    float[] squareCoords = {
            -2.0f, 2.0f, 0.0f,
            -2.0f, -2.0f, 0.0f,
            2.0f, -2.0f, 0.0f,
            2.0f, 2.0f, 0.0f
    };
    // Le tableau des couleurs
    float[] squareColors;


    // Le carré est dessiné avec 2 triangles
    private final short[] Indices = { 0, 1, 2, 0, 2, 3 };
    private Vector2 center;
    private Colors color;

    private final int vertexStride = COORDS_PER_VERTEX * 4; // le pas entre 2 sommets : 4 bytes per vertex

    private final int couleurStride = COULEURS_PER_VERTEX * 4; // le pas entre 2 couleurs

    public Square(Colors squareColors) {
        this(squareColors, 1, new Vector2(0.0f, 0.0f));
    }

    public Square(Colors squareColors, float scaling) {
        this(squareColors, scaling, new Vector2(0.0f, 0.0f));
    }

    public Square(Colors squareColors, Vector2 center) {
        this(squareColors, 1, center);
    }

    public Square(Colors squareColors, float scaling, Vector2 center) {
        this.center = center;
        this.color = squareColors;
        this.squareColors = squareColors.multiplyBy(squareCoords.length / 3);

        //Rescale
        for (int i = 0; i < squareCoords.length; i++)
            initialSquareCoords[i] *= scaling;

        move(this.center);





    }

    /* La fonction Display */
    @Override
    public void draw(float[] mvpMatrix) {
        // initialisation du buffer pour les vertex (4 bytes par float)
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);


        // initialisation du buffer pour les couleurs (4 bytes par float)
        ByteBuffer bc = ByteBuffer.allocateDirect(this.squareColors.length * 4);
        bc.order(ByteOrder.nativeOrder());
        colorBuffer = bc.asFloatBuffer();
        colorBuffer.put(this.squareColors);
        colorBuffer.position(0);

        // initialisation du buffer des indices
        ByteBuffer dlb = ByteBuffer.allocateDirect(Indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        indiceBuffer = dlb.asShortBuffer();
        indiceBuffer.put(Indices);
        indiceBuffer.position(0);

        GLES10.glScalef(100, 100, 0);

        /* Chargement des shaders */
        int vertexShader = MyGLRenderer.loadShader(
                GLES30.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES30.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        IdProgram = GLES30.glCreateProgram();             // create empty OpenGL Program
        GLES30.glAttachShader(IdProgram, vertexShader);   // add the vertex shader to program
        GLES30.glAttachShader(IdProgram, fragmentShader); // add the fragment shader to program
        GLES30.glLinkProgram(IdProgram);                  // create OpenGL program executables
        GLES30.glGetProgramiv(IdProgram, GLES30.GL_LINK_STATUS,linkStatus,0);





        // Add program to OpenGL environment
        GLES30.glUseProgram(IdProgram);

           // get handle to shape's transformation matrix
        IdMVPMatrix = GLES30.glGetUniformLocation(IdProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES30.glUniformMatrix4fv(IdMVPMatrix, 1, false, mvpMatrix, 0);


        // get handle to vertex shader's vPosition member et vCouleur member
        IdPosition = GLES30.glGetAttribLocation(IdProgram, "vPosition");
        IdCouleur = GLES30.glGetAttribLocation(IdProgram, "vCouleur");

        /* Activation des Buffers */
        GLES30.glEnableVertexAttribArray(IdPosition);
        GLES30.glEnableVertexAttribArray(IdCouleur);

        /* Lecture des Buffers */
        GLES30.glVertexAttribPointer(
                IdPosition, COORDS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        GLES30.glVertexAttribPointer(
                IdCouleur, COULEURS_PER_VERTEX,
                GLES30.GL_FLOAT, false,
                couleurStride, colorBuffer);




        // Draw the square
        GLES30.glDrawElements(
                GLES30.GL_TRIANGLES, Indices.length,
                GLES30.GL_UNSIGNED_SHORT, indiceBuffer);


        // Disable vertex array
        GLES30.glDisableVertexAttribArray(IdPosition);
        GLES30.glDisableVertexAttribArray(IdCouleur);

    }

    /**
     *
     * @return Coords in a List<Vector3>
     */
    @Override
    public List<Vector3> getCoords() {
        List<Vector3> res = new ArrayList<Vector3>();

        for (int i = 0; i < squareCoords.length; i += 3) {
            res.add(new Vector3(squareCoords[i], squareCoords[i+1], squareCoords[i+2]));
        }

        return res;
    }

    @Override
    public Vector2 getCenter() {
        return center;
    }

    @Override
    public Colors getColor() {
        return color;
    }

    @Override
    public void move(Vector2 center) {
        this.center = center;

        // Move to center
        // x
        for (int i = 0; i < squareCoords.length; i+=3)
            squareCoords[i] = initialSquareCoords[i] + center.x;

        // y
        for (int i = 1; i < squareCoords.length; i+=3)
            squareCoords[i] = initialSquareCoords[i] + center.y;
    }

}
