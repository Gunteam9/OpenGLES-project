package fr.univ.orleans.projetopengl.objects;

import android.opengl.GLES10;
import android.opengl.GLES30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

import fr.univ.orleans.projetopengl.basic.GLManager;
import fr.univ.orleans.projetopengl.utils.Colors;
import fr.univ.orleans.projetopengl.utils.Vector2;
import fr.univ.orleans.projetopengl.utils.Vector3;

public abstract class GlObject implements IObject {
    private final int coordsPerVertex = 3; // nombre de coordonnées par vertex
    private final int colorsPerVertex = 4; // nombre de composantes couleur par vertex

    private final int vertexStride = coordsPerVertex * 4; // le pas entre 2 sommets : 4 bytes per vertex
    private final int colorStride = colorsPerVertex * 4; // le pas entre 2 couleurs

    /* les déclarations pour l'équivalent des VBO */

    private FloatBuffer vertexBuffer; // Pour le buffer des coordonnées des sommets du carré
    private ShortBuffer indiceBuffer; // Pour le buffer des indices
    private FloatBuffer colorBuffer; // Pour le buffer des couleurs des sommets

    /* les déclarations pour les shaders
    Identifiant du programme et pour les variables attribute ou uniform
     */
    private int idProgram; // identifiant du programme pour lier les shaders
    private int idPosition; // idendifiant (location) pour transmettre les coordonnées au vertex shader
    private int idColor; // identifiant (location) pour transmettre les couleurs
    private int IdMVPMatrix; // identifiant (location) pour transmettre la matrice PxVxM

    protected float[] initialCoords;
    protected float[] coords;
    protected short[] indices;
    protected Colors color;
    protected float[] colors;

    protected float scaling;
    protected Vector2 center;

    public GlObject(float[] initialCoords, short[] indices, Colors color, float scaling, Vector2 center) {
        this.initialCoords = initialCoords;
        this.coords = new float[this.initialCoords.length];
        this.indices = indices;
        this.color = color;
        this.colors = color.multiplyBy(this.initialCoords.length / 3);
        this.scaling = scaling;
        this.center = center;

        move(center);
    }

    //La fonction display
    @Override
    public void draw(float[] mvpMatrix) {
        // initialisation du buffer pour les vertex (4 bytes par float)
        ByteBuffer bb = ByteBuffer.allocateDirect(this.coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(this.coords);
        vertexBuffer.position(0);


        // initialisation du buffer pour les couleurs (4 bytes par float)
        ByteBuffer bc = ByteBuffer.allocateDirect(this.colors.length * 4);
        bc.order(ByteOrder.nativeOrder());
        colorBuffer = bc.asFloatBuffer();
        colorBuffer.put(this.colors);
        colorBuffer.position(0);

        // initialisation du buffer des indices
        ByteBuffer dlb = ByteBuffer.allocateDirect(this.indices.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        indiceBuffer = dlb.asShortBuffer();
        indiceBuffer.put(this.indices);
        indiceBuffer.position(0);

        GLES10.glScalef(100, 100, 0);

        idProgram = GLManager.getInstance().getIdProgram();

        // Add program to OpenGL environment
        GLES30.glUseProgram(idProgram);

        // get handle to shape's transformation matrix
        IdMVPMatrix = GLES30.glGetUniformLocation(idProgram, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES30.glUniformMatrix4fv(IdMVPMatrix, 1, false, mvpMatrix, 0);


        // get handle to vertex shader's vPosition member et vCouleur member
        idPosition = GLES30.glGetAttribLocation(idProgram, "vPosition");
        idColor = GLES30.glGetAttribLocation(idProgram, "vCouleur");

        /* Activation des Buffers */
        GLES30.glEnableVertexAttribArray(idPosition);
        GLES30.glEnableVertexAttribArray(idColor);

        /* Lecture des Buffers */
        GLES30.glVertexAttribPointer(
                idPosition, coordsPerVertex,
                GLES30.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        GLES30.glVertexAttribPointer(
                idColor, colorsPerVertex,
                GLES30.GL_FLOAT, false,
                colorStride, colorBuffer);

        // Draw the square
        GLES30.glDrawElements(
                GLES30.GL_TRIANGLES, indices.length,
                GLES30.GL_UNSIGNED_SHORT, indiceBuffer);


        // Disable vertex array
        GLES30.glDisableVertexAttribArray(idPosition);
        GLES30.glDisableVertexAttribArray(idColor);
    }

    @Override
    public Colors getColor() {
        return color;
    }

    @Override
    public List<Vector3> getCoords() {
        List<Vector3> res = new ArrayList<Vector3>();

        for (int i = 0; i < coords.length; i += 3) {
            res.add(new Vector3(coords[i], coords[i+1], coords[i+2]));
        }

        return res;
    }

    @Override
    public Vector2 getCenter() {
        return center;
    }

    @Override
    public void move(Vector2 newCenter) {
        this.center = newCenter;

        // x
        for (int i = 0; i < initialCoords.length; i+=3)
            coords[i] = initialCoords[i] * this.scaling + center.x;

        // y
        for (int i = 1; i < initialCoords.length; i+=3)
            coords[i] = initialCoords[i] * this.scaling + center.y;
    }
}
