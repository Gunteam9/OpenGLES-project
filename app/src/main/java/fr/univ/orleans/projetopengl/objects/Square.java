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
package fr.univ.orleans.projetopengl.objects;

import android.os.Build;

import androidx.annotation.RequiresApi;

import fr.univ.orleans.projetopengl.utils.Colors;
import fr.univ.orleans.projetopengl.utils.Vector2;

@RequiresApi(api = Build.VERSION_CODES.N)
public class Square extends GlObject {

    // Le tableau des coordonnées des sommets
    private static final float[] initialSquareCoords = {
            -2.0f, 2.0f, 0.0f,
            -2.0f, -2.0f, 0.0f,
            2.0f, -2.0f, 0.0f,
            2.0f, 2.0f, 0.0f
    };

    // Le carré est dessiné avec 2 triangles
    private static final short[] indices = { 0, 1, 2, 0, 2, 3 };

    public Square(Colors squareColor) {
        this(squareColor, 1, new Vector2(0.0f, 0.0f));
    }

    public Square(Colors squareColor, float scaling) {
        this(squareColor, scaling, new Vector2(0.0f, 0.0f));
    }

    public Square(Colors squareColor, Vector2 center) {
        this(squareColor, 1, center);
    }

    public Square(Colors squareColor, float scaling, Vector2 center) {
        super(initialSquareCoords, indices, squareColor, scaling, center);
    }
}
