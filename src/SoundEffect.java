// SoundEffect.init();
// SoundEffect.volume = SoundEffect.Volume.ON;
// JButton btn = new JButton("explode");
// btn.addActionListener(new ActionListener()) {
//     @Override
//     public void actionPerformed(ActionEvent e) {
//         SoundEffect.EXPLODE.play();
//     }
// }
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;

public enum SoundEffect {
    EXPLODE("sound/explode.wav"),
    BEFOREBATTLE("sound/beforeBattle.wav"),
    BGM("sound/bgm.wav"),
    VICTORY("sound/victory.wav");

    public static enum Volume {
        MUTE, ON
    }
    public static Volume volume = Volume.ON;

    private Clip clip;

    SoundEffect(String fileName) {
        try {
            File file = new File(fileName);
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(audio);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public void play() {
        if (volume != Volume.MUTE) {
            if (clip.isRunning())
                clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }
    public void alwaysPlay() {
        if (volume != Volume.MUTE) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    public void loopStop() {
        if (clip.isRunning())
            clip.stop();
    }
    static void init() {
        values(); //call the constructor for all the elements
    }
}