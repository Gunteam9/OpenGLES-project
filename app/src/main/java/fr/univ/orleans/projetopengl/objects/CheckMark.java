package fr.univ.orleans.projetopengl.objects;

import fr.univ.orleans.projetopengl.utils.Colors;
import fr.univ.orleans.projetopengl.utils.Vector2;

public class CheckMark extends GlObject {

    // Le tableau des coordonnées des sommets
    private static final float[] initialCheckmarkCoords = {
            -4.0f, -3.0f, 0.0f,
            -3.0f, -1.0f, 0.0f,
            1.0f, -3.0f, 0.0f,
            0.0f, -5.0f, 0.0f,
            4.0f, 4.0f, 0.0f,
            2.0f, 5.0f, 0.0f,
            -1.0f, -2.0f, 0.0f,
    };

    // Le carré est dessiné avec 2 triangles
    private static final short[] indices = {
            0, 1, 2,
            2, 3, 0,
            2, 4, 5,
            5, 6, 2,
    };

    public CheckMark(Colors checkmarkColor) {
        this(checkmarkColor, 1, new Vector2(0.0f, 0.0f));
    }

    public CheckMark(Colors checkmarkColor, float scaling) {
        this(checkmarkColor, scaling, new Vector2(0.0f, 0.0f));
    }
    public CheckMark(Colors checkmarkColor, Vector2 center) {
        this(checkmarkColor, 1, center);
    }

    public CheckMark(Colors checkmarkColor, float scaling, Vector2 center) {
        super(initialCheckmarkCoords, indices, checkmarkColor, scaling, center);
    }
}
