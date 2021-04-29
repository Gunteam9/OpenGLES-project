package fr.univ.orleans.projetopengl.lib;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

    private final List<Integer> elementsIndex = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
    private final int elementPerLine = 3;

    //Positions of objects on the screen
    public final Map<Integer, Vector2> positions = Stream.of(new Object[][] {
            {0, new Vector2(-5f, -5f)},
            {1, new Vector2(0f, -5f)},
            {2, new Vector2(5f, -5f)},
            {3, new Vector2(-5f, 0f)},
            {4, new Vector2(0f, 0f)},
            {5, new Vector2(5f, 0f)},
            {6, new Vector2(-5f, 5f)},
            {7, new Vector2(0f, 5f)},
            {8, new Vector2(5f, 5f)},
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (Vector2) data[1]));

    private Map<Integer, IObject> currentGrid = new HashMap<Integer, IObject>();
    private Map<Integer, IObject> endingGrid = new HashMap<Integer, IObject>();

    //Don't create IObject here
    public Game() {
    }

    public void initializeCurrentGrid() {
        List<Vector2> randomPosition = new ArrayList<Vector2>(this.positions.values());
        randomPosition.remove(positions.get(8));
        //Collections.shuffle(randomPosition);

        Map<Integer, Vector2> copyPosition = new HashMap<Integer, Vector2>(positions);
        copyPosition.remove(8);
        List<Integer> keys = new ArrayList<Integer>(copyPosition.keySet());
        Collections.shuffle(keys);

        List<IObject> objects = new ArrayList<IObject>(Arrays.asList(
                new Square(Colors.RED, randomPosition.get(keys.get(0))),
                new Square(Colors.GREEN, randomPosition.get(keys.get(1))),
                new Square(Colors.BLUE, randomPosition.get(keys.get(2))),
                new Triangle(Colors.RED, randomPosition.get(keys.get(3))),
                new Triangle(Colors.GREEN, randomPosition.get(keys.get(4))),
                new Triangle(Colors.BLUE, randomPosition.get(keys.get(5))),
                new Star(Colors.RED, randomPosition.get(keys.get(6))),
                new Star(Colors.GREEN, randomPosition.get(keys.get(7)))
        ));

        for (int i = 0; i < objects.size(); i++) {
            currentGrid.put(keys.get(i), objects.get(i));
            Log.d("COORDS", objects.get(i).getColor() + " " + objects.get(i).getClass().getSimpleName() + " is at " + keys.get(i) + " " + objects.get(i).getCoords());
        }

    }

    public Map<Integer, IObject> getCurrentGrid() {
        return currentGrid;
    }

    public int getEmptyPosition() {
        List<Integer> res = new ArrayList<>(elementsIndex);
        res.removeAll(currentGrid.keySet());
        if (res.size() == 1)
            return res.get(0);
        throw new RuntimeException("Error during the recovery of the empty position");
    }

    public List<Integer> getNeighbours(int element) {
        List<Integer> res = new ArrayList<>();

        //North
        if (element + elementPerLine <= positions.size() - 1)
            res.add(element + elementPerLine);
        //South
        if (element - elementPerLine >= 0)
            res.add(element - elementPerLine);
        //West
        if (element + 1 <= positions.size() - 1)
            res.add(element + 1);
        //Est
        if (element - 1 >= 0)
            res.add(element - 1);

        return res;

    }

    public void moveObject(int object) {
        IObject obj = currentGrid.get(object);
        System.out.println("Obj " + obj);
        System.out.println(obj.getCoords());
        int emptyPosition = this.getEmptyPosition();
        currentGrid.remove(object);
        currentGrid.put(emptyPosition, obj);
        currentGrid.get(emptyPosition).move(positions.get(emptyPosition));
        System.out.println(currentGrid.get(emptyPosition).getCoords());
    }
}
