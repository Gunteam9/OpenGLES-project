package fr.univ.orleans.projetopengl.lib;

import java.util.List;

public interface IObject {
    public void draw(float[] mvpMatrix);
    public Colors getColor();
    public List<Vector3> getCoords();
    public Vector2 getCenter();
    public void move(Vector2 newCenter);
}
