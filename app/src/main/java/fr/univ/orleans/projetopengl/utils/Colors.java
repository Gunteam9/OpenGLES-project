package fr.univ.orleans.projetopengl.utils;

import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;

public enum Colors {
    RED(new float[] {1, 0, 0, 1}),
    GREEN(new float[] {0, 1, 0, 1}),
    //On remplace le jaune par le bleu
    BLUE(new float[] {0, 0, 1, 1});

    public final float[] val;

    Colors(float[] val) {
        this.val = val;
    }

    public float[] multiplyBy(int nb) {
        if (nb == 0)
            return new float[] {0,0,0,0};
        else if (nb == 1)
            return this.val;
        else {
            float[] res = this.val;
            for (int i = 1; i < nb; i++) {
                res = ArrayUtils.addAll(res, this.val);
            }

            return res;
        }

    }

}
