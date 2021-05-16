package fr.univ.orleans.projetopengl.audio;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Audio Manager
 * Manage audios in the game
 * SINGLETON
 */
public class AudioManager {
    private static final AudioManager instance = new AudioManager();

    private final Map<String, MediaPlayer> audios = new HashMap<>();
    public static final String TAG_MUSIC = "Musique";
    public static final String TAG_OBJECT_MOVED = "Mouvement";
    public static final String TAG_FAIL = "Son_Echec";
    public static final String TAG_WIN = "Partie gagnée";
    public static final String TAG_LOSE = "Partie_perdue";

    private AudioManager() {}

    public void addAudio(Context context, int idAudio, String name)
    {
        if(audios.get(name) != null)
            deleteAudio(name);

        MediaPlayer audio = MediaPlayer.create(context, idAudio);
        audios.put(name, audio);
    }

    public void deleteAudio(String name)
    {
        this.audios.remove(name);
    }

    public MediaPlayer getAudio(String name)
    {
        return this.audios.get(name);
    }

    public void startAudio(String name)
    {
        MediaPlayer audio = getAudio(name);
        audio.setLooping(false);
        audio.start();
    }

    public void stopAudio(String name)
    {
        MediaPlayer audio = getAudio(name);
        audio.pause();
        audio.seekTo(0);
    }

    public static AudioManager getInstance() {
        return instance;
    }

}
