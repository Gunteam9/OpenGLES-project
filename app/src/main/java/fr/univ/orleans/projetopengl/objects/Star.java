package fr.univ.orleans.projetopengl.objects;

import android.os.Build;

import androidx.annotation.RequiresApi;

import fr.univ.orleans.projetopengl.utils.Colors;
import fr.univ.orleans.projetopengl.utils.Vector2;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Star extends GlObject {

    // Le tableau des coordonnées des sommets
    private static final float[] initialStarCoords = {
            0.0f, 5.0f, 0.0f,
            5.0f, 2.0f, 0.0f,
            4.0f, -5.0f, 0.0f,
            -4.0f, -5.0f, 0.0f,
            -5.0f, 2.0f, 0.0f,

            -1.0f, 1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            2.0f, -1.0f, 0.0f,
            0.0f, -3.0f, 0.0f,
            -2.0f, -1.0f, 0.0f,
    };

    // Le carré est dessiné avec 2 triangles
    private static final short[] indices = {
            //Branch
            0, 5, 6,
            1, 6, 7,
            2, 7, 8,
            3, 8, 9,
            4, 9, 5,

            //Center
            5, 6, 8,
            6, 7, 9,
            7, 8, 5,
            8, 9, 6,
            9, 5, 7,
    };

    public Star(Colors starColor) {
        this(starColor, 0.5f);
    }

    public Star(Colors starColor, float scaling) {
        this(starColor, scaling, new Vector2(0.0f, 0.0f));
    }

    public Star(Colors starColor, Vector2 center) {
        this(starColor, 0.5f, center);
    }

    public Star(Colors starColor, float scaling, Vector2 center) {
        super(initialStarCoords, indices, starColor, scaling, center);
    }
}
