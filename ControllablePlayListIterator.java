package studiplayer.audio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ControllablePlayListIterator implements Iterator<AudioFile> {

    private List<AudioFile> audioFiles;
    private int currentIndex;

    public ControllablePlayListIterator(List<AudioFile> audioFiles) {
        this.audioFiles = new ArrayList<>(audioFiles); // Copy to avoid side effects
        this.currentIndex = 0;
    }

    public ControllablePlayListIterator(List<AudioFile> inputFiles, String search, SortCriterion sortCriterion) {
        this.audioFiles = new ArrayList<>();

        for (AudioFile af : inputFiles) {
            if (search == null || search.isEmpty()
                    || containsIgnoreCase(af.getAuthor(), search)
                    || containsIgnoreCase(af.getTitle(), search)
                    || containsIgnoreCase(getAlbum(af), search)
                    || containsIgnoreCase(af.getFilename(), search)) {
                this.audioFiles.add(af); // ✅ Correct list to add to
            }
        }

        switch (sortCriterion) {
            case AUTHOR -> this.audioFiles.sort(new AuthorComparator());
            case TITLE -> this.audioFiles.sort(new TitleComparator());
            case ALBUM -> this.audioFiles.sort(new AlbumComparator());
            case DURATION -> this.audioFiles.sort(new DurationComparator());
            default -> {} // DEFAULT means: keep original order
        }

        this.currentIndex = 0;
    }

    private boolean containsIgnoreCase(String source, String target) {
        return source != null && source.toLowerCase().contains(target.toLowerCase());
    }

    private String getAlbum(AudioFile af) {
        if (af instanceof TaggedFile tagged) {
            return tagged.getAlbum();
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < audioFiles.size();
    }

    @Override
    public AudioFile next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more audio files.");
        }
        return audioFiles.get(currentIndex++);
    }

    public AudioFile jumpToAudioFile(AudioFile file) {
        int pos = audioFiles.indexOf(file);
        if (pos >= 0) {
            currentIndex = pos + 1;
            return file;
        } else {
            return null;
        }
    }
}
