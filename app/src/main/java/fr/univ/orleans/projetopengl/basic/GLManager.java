package fr.univ.orleans.projetopengl.basic;

import android.opengl.GLES30;

/**
 * Create an opengl program
 * SINGLETON
 */
public class GLManager {

    private static final GLManager instance = new GLManager();

    private final int idProgram;

    // Le vertex shader avec la définition de gl_Position et les variables utiles au fragment shader
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

    //Basic
    private final String fragmentShaderCode =
            "#version 300 es\n"+
                    "precision mediump float;\n" + // pour définir la taille d'un float
                    "in vec4 Couleur;\n"+
                    "in vec3 Position;\n"+
                    "out vec4 fragColor;\n"+
                    "void main() {\n" +
                    "fragColor = Couleur;\n" +
                    "}\n";

    private final int[] linkStatus = {0};

    private GLManager() {

        int vertexShader = MyGLRenderer.loadShader(
                GLES30.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(
                GLES30.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        idProgram = GLES30.glCreateProgram();             // create empty OpenGL Program
        GLES30.glAttachShader(idProgram, vertexShader);   // add the vertex shader to program
        GLES30.glAttachShader(idProgram, fragmentShader); // add the fragment shader to program
        GLES30.glLinkProgram(idProgram);                  // create OpenGL program executables
        GLES30.glGetProgramiv(idProgram, GLES30.GL_LINK_STATUS, linkStatus, 0);
    }

    public int getIdProgram() {
        return idProgram;
    }

    public static GLManager getInstance() {
        return instance;
    }
}
