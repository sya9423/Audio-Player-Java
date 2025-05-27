package studiplayer.audio;

import java.util.Comparator;

public class DurationComparator implements Comparator<AudioFile> {
    @Override
    public int compare(AudioFile af1, AudioFile af2) {
        long duration1 = getDurationFromAudioFile(af1);
        long duration2 = getDurationFromAudioFile(af2);

        return Long.compare(duration1, duration2);
    }

    private long getDurationFromAudioFile(AudioFile file) {
        if (file instanceof WavFile) {
            return ((WavFile) file).getDuration();
        } else if (file instanceof TaggedFile) {
            return ((TaggedFile) file).getDuration();
        } else {
            return 0L; 
        }
    }
}
