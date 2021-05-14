package fr.univ.orleans.projetopengl.audio;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    private Map<String, MediaPlayer> audios = new HashMap<>();
    public static final String TAG_MUSIC = "Musique";
    public static final String TAG_SUCCES = "Son_Succes";
    public static final String TAG_FAIL = "Son_Echec";

    public void addAudio(Context context, int idAudio, String name)
    {
        MediaPlayer audio = MediaPlayer.create(context, idAudio);
        audios.put(name, audio);
    }

    public void deleteAudio(String name)
    {
        this.audios.remove(name);
    }

    public MediaPlayer getAudio(String string)
    {
        return this.audios.get(string);
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
