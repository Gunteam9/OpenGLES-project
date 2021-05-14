package fr.univ.orleans.projetopengl.audio;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    private Map<String, MediaPlayer> audios = new HashMap<>();
    public static final String TAG_MUSIC = "Musique";
    public static final String TAG_SUCCES = "Son_Succes";
    public static final String TAG_OBJECT_MOVED = "Mouvement";
    public static final String TAG_FAIL = "Son_Echec";
    public static AudioManager instance = new AudioManager();

    private AudioManager()
    {

    }

    public void addAudio(Context context, int idAudio, String name)
    {
        MediaPlayer audio = MediaPlayer.create(context, idAudio);
        audios.put(name, audio);
    }

    public boolean Exist(String name)
    {
        return audios.get(name) != null;
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
        audio.release();
    }


}
