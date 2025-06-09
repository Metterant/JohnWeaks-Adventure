package sound;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Logger;
import java.util.logging.Level;

public class SoundManager {
    public static final int HIT      = 0;
    public static final int SHOOT    = 1;
    public static final int PICK_UP  = 2;
    public static final int POWER_UP = 3;
    public static final int DIE      = 4;

    private final ExecutorService soundLoader = Executors.newSingleThreadExecutor();
    private final AtomicBoolean loading = new AtomicBoolean(false);
    private final Logger logger = Logger.getLogger(SoundManager.class.getName());
    private Clip[] clips = new Clip[5];
    URL[] soundURL = new URL[5];

    public static final SoundManager instance = new SoundManager();
    public static SoundManager getInstance() { return instance; }

    private SoundManager() {
        soundURL[HIT]      = getClass().getResource("/resources/sounds/hit.wav");
        soundURL[SHOOT]    = getClass().getResource("/resources/sounds/shoot.wav");
        soundURL[PICK_UP]  = getClass().getResource("/resources/sounds/pickup.wav");
        soundURL[POWER_UP] = getClass().getResource("/resources/sounds/power_up.wav");
        soundURL[DIE]      = getClass().getResource("/resources/sounds/die.wav");
    }

    public void asyncLoadAllClips() {
        if (loading.getAndSet(true)) return; // Prevent double loading
        soundLoader.submit(() -> {
            loadAllClips();
            loading.set(false);
        });
    }

    private void loadAllClips() {
        for (int i = 0; i < soundURL.length; i++) {
            if (soundURL[i] == null) {
                logger.log(Level.WARNING, "Sound resource missing for index: {0}", i);
                continue;
            }
            try {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL[i]);
                clips[i] = AudioSystem.getClip();
                clips[i].open(audioInputStream);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to load sound for index {0}: {1}", new Object[]{i, e.getMessage()});
                clips[i] = null;
            }
        }
    }

    public void playSound(int sound) {
        if (sound < 0 || sound > 4)
            throw new IllegalArgumentException("Illegal sound number");
        Clip c = clips[sound];
        if (c == null) {
            logger.log(Level.WARNING, "Clip not loaded for sound index: {0}", sound);
            return;
        }
        if (c.isRunning()) {
            c.stop();
        }
        
        c.setFramePosition(0);
        c.start();
    }
    
}
