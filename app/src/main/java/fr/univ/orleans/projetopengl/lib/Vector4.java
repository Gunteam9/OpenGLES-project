package fr.univ.orleans.projetopengl.lib;

public class Vector4 {
    public float x;
    public float y;
    public float z;
    public float a;

    public Vector4(float x, float y, float z, float a) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.a = a;
    }

    @Override
    public String toString() {
        return "Vector4{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", a=" + a +
                '}';
    }
}
