package studiplayer.audio;

import java.util.Map;

import studiplayer.basic.TagReader;

public class TaggedFile extends SampledFile {

    private String album = null;

    public TaggedFile() {
        super();
    }

    public TaggedFile(String path){
        super(path);
        readAndStoreTags();
    }

    public void readAndStoreTags(){
        Map<String, Object> tagMap;
        try {
            tagMap = TagReader.readTags(getPathname());
        } catch (Exception e) {
            throw new NotPlayableException(getPathname(), "Could not read tags", e);
        }

        if (tagMap.containsKey("title")) {
            setTitle(((String) tagMap.get("title")).trim());
        } else {
            String filename = getFilename();
            int dotIndex = filename.lastIndexOf(".");
            if (dotIndex > 0) {
                setTitle(filename.substring(0, dotIndex));
            } else {
                setTitle(filename);
            }
        }

        if (tagMap.containsKey("author")) {
            setAuthor(((String) tagMap.get("author")).trim());
        } else {
            setAuthor("");
        }

        if (tagMap.containsKey("album")) {
            this.album = ((String) tagMap.get("album")).trim();
        }

        if (tagMap.containsKey("duration")) {
            setDuration((Long) tagMap.get("duration"));
        }
    }
    
    public String getAlbum() {
        return album;
    }

    @Override
    public String toString() {
        String formattedDuration = timeFormatter(getDuration());

        String albumString = (album != null && !album.isEmpty()) ? album : "";
        String authorString = (getAuthor() != null && !getAuthor().isEmpty()) ? getAuthor() : "";
        String titleString = (getTitle() != null && !getTitle().isEmpty()) ? getTitle() : "";

        if (!authorString.isEmpty() && !albumString.isEmpty()) {
            return authorString + " - " + titleString + " - " + albumString + " - " + formattedDuration;
        } else if (!authorString.isEmpty()) {
            return authorString + " - " + titleString + " - " + formattedDuration;
        } else if (!albumString.isEmpty()) {
            return titleString + " - " + albumString + " - " + formattedDuration;
        } else {
            return titleString + " - " + formattedDuration;
        }
    }
}
