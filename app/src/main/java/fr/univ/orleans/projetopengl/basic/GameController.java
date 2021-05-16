package fr.univ.orleans.projetopengl.basic;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import fr.univ.orleans.projetopengl.audio.AudioManager;
import fr.univ.orleans.projetopengl.launcher.OpenGLES20Activity;
import fr.univ.orleans.projetopengl.objects.CheckMark;
import fr.univ.orleans.projetopengl.utils.Colors;
import fr.univ.orleans.projetopengl.objects.IObject;
import fr.univ.orleans.projetopengl.objects.Square;
import fr.univ.orleans.projetopengl.objects.Star;
import fr.univ.orleans.projetopengl.objects.Triangle;
import fr.univ.orleans.projetopengl.utils.Vector2;

public class GameController {

    private static final GameController instance = new GameController();

    private final List<Integer> elementsIndex = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
    private final Random random = new Random();
    private boolean isInitializationFinished = false;
    private int score;
    private TextView scoreText;
    private boolean hasWon;
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

    private final int elementPerLine = (int) Math.sqrt(positions.size());

    private final Map<Integer, IObject> currentGrid = new HashMap<Integer, IObject>();
    private final Map<Integer, IObject> endingGrid = new HashMap<Integer, IObject>();

    private MyGLSurfaceView surfaceView;
    private MyGLRenderer renderer;
    private final AudioManager audioManager;

    private GameController() {
        audioManager = AudioManager.getInstance();
    }

    public static GameController getInstance() {
        return instance;
    }

    public void addAudio(Context context, int id, String name)
    {
        audioManager.addAudio(context, id, name);
    }

    public void playAudio(String name)
    {
        audioManager.startAudio(name);
    }

    public void stopAudio(String name)
    {
        audioManager.stopAudio(name);
    }


    public void initializeGrid(MyGLSurfaceView surfaceView) {
        this.surfaceView = surfaceView;

        isInitializationFinished = false;
        hasWon = false;

        currentGrid.clear();
        endingGrid.clear();
        surfaceView.clearObjets();

        List<IObject> objects = new ArrayList<IObject>(Arrays.asList(
                new Star(Colors.RED, positions.get(0)),
                new Star(Colors.GREEN, positions.get(1)),
                null,
                new Triangle(Colors.RED, positions.get(3)),
                new Triangle(Colors.GREEN, positions.get(4)),
                new Triangle(Colors.BLUE, positions.get(5)),
                new Square(Colors.RED, positions.get(6)),
                new Square(Colors.GREEN, positions.get(7)),
                new Square(Colors.BLUE, positions.get(8))
        ));

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) == null)
                continue;

            endingGrid.put(i, objects.get(i));
            Log.d("COORDS", objects.get(i).getColor() + " " + objects.get(i).getClass().getSimpleName() + " is at " + i + " " + objects.get(i).getCoords());
        }

        int numberOfModifications = 5;
//        int numberOfModifications = random.nextInt(100);

        //Copy the ending grid to the current
        currentGrid.putAll(endingGrid);

        //Execute random possible moves
        for (int i = 0; i < numberOfModifications; i++) {
            //Get neighbours of the the empty position
            ArrayList<Integer> keys = new ArrayList<>(getNeighbours(getEmptyPosition()));
            //Select one of them
            int randomElementInMap = keys.get(random.nextInt(keys.size()));
            //Move the selected element into the empty position
            moveObject(randomElementInMap);
        }

        surfaceView.requestRender();

        isInitializationFinished = true;
        this.score = 0;
        audioManager.startAudio(AudioManager.TAG_MUSIC);
    }



    public Map<Integer, IObject> getCurrentGrid() {
        return currentGrid;
    }

    public int getScore() { return score; }

    public boolean isHasWon() {
        return hasWon;
    }

    public void setScoreText(TextView scoreText) {

        this.scoreText = scoreText;
        updateScore(0);
    }

    private void updateScore(int score)
    {
        this.score = score;
        String string = "Score : " + this.score;
        this.scoreText.setText(string);
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
        if ((element + 1) % elementPerLine != 0)
            res.add(element + 1);
        //Est
        if (element % elementPerLine != 0)
            res.add(element - 1);

        System.out.println("Neighbours of " + element + " are " + res);

        return res;

    }



    public void moveObject(int object) {
        IObject obj = currentGrid.get(object);
        int emptyPosition = this.getEmptyPosition();
        currentGrid.remove(object);
        currentGrid.put(emptyPosition, obj);
        currentGrid.get(emptyPosition).move(positions.get(emptyPosition));


        if (!isInitializationFinished)
            return;

        updateScore(++this.score);
        audioManager.startAudio(AudioManager.TAG_OBJECT_MOVED);
        int current = 0;
        int ending = 0;

        for (int i = 0; i < positions.size(); i++) {
            if (currentGrid.get(current) != null) {
                //If there is a difference
                if (currentGrid.get(current) != endingGrid.get(ending))
                    return;
            }

            current++;
            ending++;
        }

        hasWon = true;
        //End
        audioManager.startAudio(AudioManager.TAG_WIN);
        OpenGLES20Activity.getmGLView().drawObject(new CheckMark(Colors.GREEN, 0.6f, new Vector2(0, -15)), true);

    }

    public Collection<IObject> getObjToDraw() {
        return currentGrid.values();
    }

}