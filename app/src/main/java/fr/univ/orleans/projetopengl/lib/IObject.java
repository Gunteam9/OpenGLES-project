package fr.univ.orleans.projetopengl.lib;

import java.util.List;

public interface IObject {
    public void draw(float[] mvpMatrix);
    public List<Vector3> getCoords();
}
