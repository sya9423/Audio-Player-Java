package studiplayer.audio;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

public class PlayList implements Iterable<AudioFile> {
    private List<AudioFile> audioFiles;
    private String search;
    private SortCriterion sortCriterion = SortCriterion.DEFAULT;

    private ControllablePlayListIterator iterator;
    private AudioFile currentFile;

    public PlayList() {
        audioFiles = new LinkedList<>();
        resetIterator();
    }

    public PlayList(String m3uFilePath) {
        this();
        loadFromM3U(m3uFilePath);
    }

    @Override
    public Iterator<AudioFile> iterator() {
        return new ControllablePlayListIterator(audioFiles, search, sortCriterion);
    }

    public List<AudioFile> getList() {
        return audioFiles;
    }

    public void add(AudioFile file) {
        audioFiles.add(file);
        resetIterator();
    }

    public void remove(AudioFile file) {
        audioFiles.remove(file);
        resetIterator();
    }

    public int size() {
        return audioFiles.size();
    }

    public AudioFile currentAudioFile() {
        return currentFile;
    }

    public void nextSong() {
        if (iterator != null && iterator.hasNext()) {
            currentFile = iterator.next();
        } else {
            resetIterator(); // Start from beginning if at the end
        }
    }

    public void jumpToAudioFile(AudioFile file) {
        AudioFile jumped = iterator.jumpToAudioFile(file);
        if (jumped != null) {
            currentFile = jumped;
        }
    }

    public void saveAsM3U(String pathname) {
        try (FileWriter writer = new FileWriter(pathname)) {
            String sep = System.getProperty("line.separator");
            for (AudioFile file : audioFiles) {
                writer.write(file.getPathname() + sep);
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to write file: " + pathname, e);
        }
    }

    public void loadFromM3U(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            audioFiles.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }
                try {
                    AudioFile file = AudioFileFactory.createAudioFile(line.trim());
                    audioFiles.add(file);
                } catch (NotPlayableException e) {
                    System.err.println("Error creating AudioFile for path: " + line.trim());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Unable to read M3U file: " + path, e);
        }
        resetIterator();
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
        resetIterator();
    }

    public SortCriterion getSortCriterion() {
        return sortCriterion;
    }

    public void setSortCriterion(SortCriterion sortCriterion) {
        this.sortCriterion = sortCriterion;
        resetIterator();
    }

    private void resetIterator() {
        iterator = new ControllablePlayListIterator(audioFiles, search, sortCriterion);
        if (iterator.hasNext()) {
            currentFile = iterator.next();
        } else {
            currentFile = null;
        }
    }
}
