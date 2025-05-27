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
    private int current;
    private String search;
    private SortCriterion sortCriterion = SortCriterion.DEFAULT;

    public PlayList() {
        audioFiles = new LinkedList<>();
        current = 0;
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
    }

    public void remove(AudioFile file) {
        audioFiles.remove(file);
        if (current >= audioFiles.size()) {
            current = -1;
        }
    }

    public int size() {
        return audioFiles.size();
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public AudioFile currentAudioFile() {
        if (current >= 0 && current < audioFiles.size()) {
            return audioFiles.get(current);
        } else {
            return null;
        }
    }

    public void nextSong() {
        if (audioFiles.isEmpty()) {
            current = -1;
            return;
        }

        if (current < 0 || current >= audioFiles.size() - 1) {
            current = 0;
        } else {
            current++;
        }
    }

    public void saveAsM3U(String pathname) {
        FileWriter writer = null;
        String sep = System.getProperty("line.separator");

        try {
            writer = new FileWriter(pathname);

            for (AudioFile file : audioFiles) {
                writer.write(file.getPathname() + sep);
            }

        } catch (IOException e) {
            throw new RuntimeException("Unable to write file: " + pathname, e);
        } finally {
            try {
                if (writer != null) writer.close();
            } catch (Exception e) {
                // ignore
            }
        }
    }

    public void loadFromM3U(String path) {
        BufferedReader reader = null;
        String line;

        try {
            reader = new BufferedReader(new FileReader(path));
            audioFiles.clear();
            current = 0;

            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    AudioFile file = AudioFileFactory.createAudioFile(line.trim());
                    audioFiles.add(file);
                } catch (NotPlayableException e) {
                    System.err.println("Error creating AudioFile for path: " + line.trim());
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Unable to read M3U file: " + path, e);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                // ignore
            }
        }
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public SortCriterion getSortCriterion() {
        return sortCriterion;
    }

    public void setSortCriterion(SortCriterion sortCriterion) {
        this.sortCriterion = sortCriterion;
    }
}
