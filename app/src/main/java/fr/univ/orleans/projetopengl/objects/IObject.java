package fr.univ.orleans.projetopengl.objects;

import java.util.List;

import fr.univ.orleans.projetopengl.utils.Colors;
import fr.univ.orleans.projetopengl.utils.Vector2;
import fr.univ.orleans.projetopengl.utils.Vector3;

public interface IObject {
    public void draw(float[] mvpMatrix);
    public Colors getColor();
    public List<Vector3> getCoords();
    public Vector2 getCenter();
    public void move(Vector2 newCenter);
}
