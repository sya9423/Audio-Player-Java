package studiplayer.audio;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ControllablePlayListIterator implements Iterator<AudioFile> {

    private List<AudioFile> audioFiles;
    private int currentIndex;

    public ControllablePlayListIterator(List<AudioFile> audioFiles) {
        this.audioFiles = audioFiles;
        this.currentIndex = 0;
    }
    
    public ControllablePlayListIterator(List<AudioFile> audioFiles, String search, SortCriterion sortCriterion) {
        this.audioFiles = new ArrayList<>();

        for (AudioFile af : audioFiles) {
            if (search == null || search.isEmpty() ||
                containsIgnoreCase(af.getAuthor(), search) ||
                containsIgnoreCase(af.getTitle(), search) ||
                containsIgnoreCase(getAlbum(af), search)) {
            	audioFiles.add(af);
            }
        }

        switch (sortCriterion) {
            case AUTHOR -> audioFiles.sort(new AuthorComparator());
            case TITLE -> audioFiles.sort(new TitleComparator());
            case ALBUM -> audioFiles.sort(new AlbumComparator());
            case DURATION -> audioFiles.sort(new DurationComparator());
            default -> {}
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
