package fr.univ.orleans.projetopengl.alerts;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import fr.univ.orleans.projetopengl.R;
import fr.univ.orleans.projetopengl.basic.Game;
import fr.univ.orleans.projetopengl.launcher.OpenGLES20Activity;

public class GameOverFragment extends DialogFragment {

    public static String TAG = "Game Over";

    public GameOverFragment() {
        Bundle args = new Bundle();
        args.putString(TAG, "title");
        this.setArguments(args);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.game_over)
                .setPositiveButton(R.string.reset, (dialog, which) -> {
                    Game.getInstance().initializeGrid(OpenGLES20Activity.getmGLView());
//                    startActivity(Objects.requireNonNull(getActivity()).getIntent());
//                    getActivity().finish();
//                    getActivity().overridePendingTransition(0,0);
                })
                .setNegativeButton(R.string.quit, (dialog, which) -> {
                    Objects.requireNonNull(getActivity()).finish();
                    System.exit(0);
                });
        return builder.create();
    }
}
