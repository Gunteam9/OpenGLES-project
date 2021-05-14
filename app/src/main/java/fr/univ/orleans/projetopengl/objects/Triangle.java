package fr.univ.orleans.projetopengl.objects;

import android.os.Build;

import androidx.annotation.RequiresApi;

import fr.univ.orleans.projetopengl.utils.Colors;
import fr.univ.orleans.projetopengl.utils.Vector2;


@RequiresApi(api = Build.VERSION_CODES.N)
public class Triangle extends GlObject {
    // Le tableau des coordonnées des sommets
    private static final float[] initialTriangleCoords = {
            0.0f, 2.0f, 0.0f,
            -2.0f, -1.5f, 0.0f,
            2.0f, -1.5f, 0.0f,
    };

    // Le carré est dessiné avec 2 triangles
    private static final short[] indices = { 0, 1, 2};

    public Triangle(Colors triangleColor) {
        this(triangleColor, 1, new Vector2(0.0f, 0.0f));
    }

    public Triangle(Colors triangleColor, float scaling) {
        this(triangleColor, scaling, new Vector2(0.0f, 0.0f));
    }
    public Triangle(Colors triangleColor, Vector2 center) {
        this(triangleColor, 1, center);
    }

    public Triangle(Colors triangleColor, float scaling, Vector2 center) {
        super(initialTriangleCoords, indices, triangleColor, scaling, center);
    }
}
