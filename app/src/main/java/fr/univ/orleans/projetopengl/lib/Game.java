package fr.univ.orleans.projetopengl.lib;

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

    //Positions of objects on the screen
    private final Map<Integer, float[]> positions = Stream.of(new Object[][] {
            {0, new float[] {-5f, -5f}},
            {1, new float[] {0f, -5f}},
            {2, new float[] {5f, -5f}},
            {3, new float[] {-5f, 0f}},
            {4, new float[] {0f, 0f}},
            {5, new float[] {5f, 0f}},
            {6, new float[] {-5f, 5f}},
            {7, new float[] {0f, 5f}},
            {8, new float[] {5f, 5f}},
    }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (float[]) data[1]));

    private Map<Integer, IObject> currentGrid = new HashMap<Integer, IObject>();
    private Map<Integer, IObject> endingGrid = new HashMap<Integer, IObject>();

    //Don't create IObject here
    public Game() {
    }

    public void initializeCurrentGrid() {
        List<float[]> randomPosition = new ArrayList<float[]>(this.positions.values());
        Collections.shuffle(randomPosition);

        List<IObject> objects = new ArrayList<IObject>(Arrays.asList(
                new Square(Colors.RED, randomPosition.get(0)),
                new Square(Colors.GREEN, randomPosition.get(1)),
                new Square(Colors.BLUE, randomPosition.get(2)),
                new Triangle(Colors.RED, randomPosition.get(3)),
                new Triangle(Colors.GREEN, randomPosition.get(4)),
                new Triangle(Colors.BLUE, randomPosition.get(5)),
                new Star(Colors.RED, randomPosition.get(6)),
                new Star(Colors.GREEN, randomPosition.get(7))
        ));

        for (int i = 0; i < objects.size(); i++) {
            currentGrid.put(i, objects.get(i));
            System.out.println(objects.get(i).getClass().getSimpleName() + " (" + positions.get(i)[0] + "," + positions.get(i)[1] + ")");
        }

    }

    public Map<Integer, IObject> getCurrentGrid() {
        return currentGrid;
    }
}
