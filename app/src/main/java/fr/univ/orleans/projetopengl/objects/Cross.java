package fr.univ.orleans.projetopengl.objects;

import fr.univ.orleans.projetopengl.utils.Colors;
import fr.univ.orleans.projetopengl.utils.Vector2;

public class Cross extends GlObject {

    // Le tableau des coordonnées des sommets
    private static final float[] initialCrossCoords = {
            -3.5f, -2.5f, 0.0f,
            -2.5f, -3.5f, 0.0f,
            2.5f, -3.5f, 0.0f,
            3.5f, -2.5f, 0.0f,
            3.5f, 2.5f, 0.0f,
            2.5f, 3.5f, 0.0f,
            -2.5f, 3.5f, 0.0f,
            -3.5f, 2.5f, 0.0f,
    };

    // Le carré est dessiné avec 2 triangles
    private static final short[] indices = {
            0, 1, 4,
            4, 5, 0,
            2, 3, 6,
            6, 7, 2,
    };

    public Cross(Colors crossColor) {
        this(crossColor, 1, new Vector2(0.0f, 0.0f));
    }

    public Cross(Colors crossColor, float scaling) {
        this(crossColor, scaling, new Vector2(0.0f, 0.0f));
    }
    public Cross(Colors crossColor, Vector2 center) {
        this(crossColor, 1, center);
    }

    public Cross(Colors crossColor, float scaling, Vector2 center) {
        super(initialCrossCoords, indices, crossColor, scaling, center);
    }
}
