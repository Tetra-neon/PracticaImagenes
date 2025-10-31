package com.devst.appmedia;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //CONSTANTES
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String YOUTUBE_VIDEO_ID = "dQw4w9WgXcQ";


    // Claves para guardar el estado
    private static final String KEY_VIDEO_POSITION = "video_position";
    private static final String KEY_VIDEO_PLAYING = "video_playing";
    private static final String KEY_IMAGE_LIST = "image_list";

    //  COMPONENTES DE UI
    private VideoView videoView;
    private Button btnPlaySound, btnStopSound, btnSelectImage;
    private TextView tvImageCount;
    private RecyclerView recyclerView;
    private YouTubePlayerView youtubePlayerView;

    //VARIABLES DE MULTIMEDIA
    private MediaPlayer mediaPlayer;

    // Variables para recordar el estado del video
    private int videoPosition = 0;
    private boolean wasPlaying = false;

    //VARIABLES DE IMÁGENES
    private ImageAdapter imageAdapter;
    private List<Uri> imageUriList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Restaurar estado si hay
        if (savedInstanceState != null) {
            videoPosition = savedInstanceState.getInt(KEY_VIDEO_POSITION, 0);
            wasPlaying = savedInstanceState.getBoolean(KEY_VIDEO_PLAYING, false);

            // Restaurar lista de imágenes
            ArrayList<String> imageStrings = savedInstanceState.getStringArrayList(KEY_IMAGE_LIST);
            if (imageStrings != null) {
                for (String uriString : imageStrings) {
                    imageUriList.add(Uri.parse(uriString));
                }
            }
        }

        // Inicializar componentes
        initializeViews();

        // Configurar cada sección
        setupVideoLocal();
        setupAudioPlayer();
        setupYoutubePlayer();
        setupImageGallery();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Guardar posición del video
        if (videoView != null) {
            outState.putInt(KEY_VIDEO_POSITION, videoView.getCurrentPosition());
            outState.putBoolean(KEY_VIDEO_PLAYING, videoView.isPlaying());
        }

        // Guardar lista de imágenes
        ArrayList<String> imageStrings = new ArrayList<>();
        for (Uri uri : imageUriList) {
            imageStrings.add(uri.toString());
        }
        outState.putStringArrayList(KEY_IMAGE_LIST, imageStrings);
    }

    /**
     * Inicializa todas las vistas del layout
     */
    private void initializeViews() {
        videoView = findViewById(R.id.videoView);
        btnPlaySound = findViewById(R.id.btnPlaySound);
        btnStopSound = findViewById(R.id.btnStopSound);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        tvImageCount = findViewById(R.id.tvImageCount);
        recyclerView = findViewById(R.id.recyclerView);
        youtubePlayerView = findViewById(R.id.youtubePlayerView);
    }

    //SECCIÓN 1: VIDEO LOCAL
    private void setupVideoLocal() {
        try {
            // Crear URI del video desde recursos
            String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video;
            Uri videoUri = Uri.parse(videoPath);

            // Configurar VideoView
            videoView.setVideoURI(videoUri);

            // Agregar controles de reproducción
            MediaController mediaController = new MediaController(this);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);

            //Listener para cuando el video esté preparado
            videoView.setOnPreparedListener(mp -> {
                // Configurar escalado del video
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);

                // Si hay una posición guardada, ir a esa posición
                if (videoPosition > 0) {
                    videoView.seekTo(videoPosition);
                }

                // Si estaba reproduciéndose antes de rotar, seguir reproduciendo
                if (wasPlaying || videoPosition == 0) {
                    videoView.start();
                }

                Toast.makeText(MainActivity.this, "Video listo", Toast.LENGTH_SHORT).show();
            });

            // Listener para manejar errores
            videoView.setOnErrorListener((mp, what, extra) -> {
                String errorMsg = "Error de video - Tipo: " + what + ", Extra: " + extra;
                Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                return true;
            });

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar video: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //SECCIÓN 2: REPRODUCTOR DE AUDIO
    private void setupAudioPlayer() {
        btnPlaySound.setOnClickListener(v -> playSound());
        btnStopSound.setOnClickListener(v -> stopSound());
    }

    private void playSound() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                Toast.makeText(this, "El audio ya se está reproduciendo", Toast.LENGTH_SHORT).show();
                return;
            }

            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this, R.raw.sonido);

            mediaPlayer.setOnCompletionListener(mp -> {
                Toast.makeText(MainActivity.this, "Audio finalizado", Toast.LENGTH_SHORT).show();
                btnPlaySound.setEnabled(true);
                btnStopSound.setEnabled(false);
                mp.release();
                mediaPlayer = null;
            });

            mediaPlayer.start();
            btnPlaySound.setEnabled(false);
            btnStopSound.setEnabled(true);
            Toast.makeText(this, "Reproduciendo audio", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error al reproducir audio", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

   // Detener el audio
    private void stopSound() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
            btnPlaySound.setEnabled(true);
            btnStopSound.setEnabled(false);
            Toast.makeText(this, "Audio detenido", Toast.LENGTH_SHORT).show();
        }
    }

    //SECCIÓN 3: YOUTUBE PLAYER

    private void setupYoutubePlayer() {
        try {
            //Agregar lifecycle ANTES de inicializar
            getLifecycle().addObserver(youtubePlayerView);

            // Ahora sí, agregar el listener
            youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    // Cargar el video
                    youTubePlayer.loadVideo(YOUTUBE_VIDEO_ID, 0);
                    Toast.makeText(MainActivity.this, "✓ YouTube listo", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStateChange(@NonNull YouTubePlayer youTubePlayer,
                                          @NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerState state) {
                    // Mostrar estado para debug
                    String stateName = "";
                    switch (state) {
                        case PLAYING:
                            stateName = "Reproduciendo";
                            break;
                        case PAUSED:
                            stateName = "Pausado";
                            break;
                        case BUFFERING:
                            stateName = "Cargando...";
                            break;
                        case ENDED:
                            stateName = "Finalizado";
                            break;
                    }
                    if (!stateName.isEmpty()) {
                        Toast.makeText(MainActivity.this, stateName, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(@NonNull YouTubePlayer youTubePlayer,
                                    @NonNull com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError error) {

                    String errorMsg = "Error YouTube: " + error.name();
                    Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_LONG).show();

                    // Mensajes detallados
                    switch (error) {
                        case VIDEO_NOT_PLAYABLE_IN_EMBEDDED_PLAYER:
                            Toast.makeText(MainActivity.this,
                                    "Este video no permite reproducción embebida.\n" +
                                            "Prueba con otro ID de video.",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case VIDEO_NOT_FOUND:
                            Toast.makeText(MainActivity.this,
                                    "Video no encontrado.\nID: " + YOUTUBE_VIDEO_ID,
                                    Toast.LENGTH_LONG).show();
                            break;
                        case INVALID_PARAMETER_IN_REQUEST:
                            Toast.makeText(MainActivity.this,
                                    "ID inválido. Debe ser solo el ID (11 caracteres)",
                                    Toast.LENGTH_LONG).show();
                            break;
                        case UNKNOWN:
                            Toast.makeText(MainActivity.this,
                                    "Error desconocido.\n" +
                                            "Verifica:\n" +
                                            "1. Conexión a Internet\n" +
                                            "2. ID correcto: " + YOUTUBE_VIDEO_ID,
                                    Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            });

        } catch (Exception e) {
            String errorMsg = "Error al inicializar YouTube: " + e.getMessage();
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    //SECCIÓN 4: GALERÍA DE IMÁGENES
    private void setupImageGallery() {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        );

        imageAdapter = new ImageAdapter(this, imageUriList);
        recyclerView.setAdapter(imageAdapter);

        btnSelectImage.setOnClickListener(v -> openImagePicker());

        updateImageCount();
    }
    // Abre el selector de imágenes
    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "Seleccionar Imagen"),
                PICK_IMAGE_REQUEST
        );
    }

   // Manejar la selección de imágenes
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            try {
                Uri imageUri = data.getData();
                imageUriList.add(imageUri);
                imageAdapter.notifyItemInserted(imageUriList.size() - 1);
                recyclerView.smoothScrollToPosition(imageUriList.size() - 1);
                updateImageCount();
                Toast.makeText(this, "Imagen agregada", Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                Toast.makeText(this, "Error al cargar imagen", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


    //Actualiza el contador de imágenes
    private void updateImageCount() {
        tvImageCount.setText("Imágenes: " + imageUriList.size());
    }

    // CICLO DE VIDA
    @Override
    protected void onPause() {
        super.onPause();
        // Guardar posición del video cuando se pausa la app
        if (videoView != null && videoView.isPlaying()) {
            videoPosition = videoView.getCurrentPosition();
            wasPlaying = true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restaurar video si es necesario
        if (videoView != null && videoPosition > 0) {
            videoView.seekTo(videoPosition);
            if (wasPlaying) {
                videoView.start();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youtubePlayerView.release();
    }
}