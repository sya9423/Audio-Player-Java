package studiplayer.audio;

import java.io.File;

public abstract class AudioFile {
    private String pathname;
    private String filename;
    private String author;
    private String title;

    public AudioFile() {
        this.pathname = "";
        this.filename = "";
        this.author = "";
        this.title = "";
    }

    public AudioFile(String path) {
        this();
        parsePathname(path);

        File file = new File(pathname);
        if (!file.canRead()) {
            throw new NotPlayableException(pathname, "File cannot be read");
        }

        if (!this.filename.isEmpty()) {
            parseFilename(this.filename);
        }
    }

    public void parsePathname(String path) {
        path = path.trim();
        if (path.isEmpty()) {
            return;
        }

        String separator = System.getProperty("file.separator");
        path = path.replace("\\", separator).replace("/", separator);

        StringBuilder updated = new StringBuilder();
        boolean lastWasSeparator = false;

        for (int i = 0; i < path.length(); i++) {
            String current = String.valueOf(path.charAt(i));
            if (current.equals(separator)) {
                if (!lastWasSeparator) {
                    updated.append(current);
                    lastWasSeparator = true;
                }
            } else {
                updated.append(current);
                lastWasSeparator = false;
            }
        }

        this.pathname = updated.toString();

        int lastSeparatorIndex = this.pathname.lastIndexOf(separator);
        this.filename = this.pathname.substring(lastSeparatorIndex + 1).trim();
    }

    public void parseFilename(String file) {
        String mfile = file;
        int dotIndex = mfile.lastIndexOf(".");
        if (dotIndex != -1) {
            mfile = mfile.substring(0, dotIndex);
        }

        int sepIndex = mfile.indexOf(" - ");
        if (sepIndex != -1) {
            this.author = mfile.substring(0, sepIndex).trim();
            this.title = mfile.substring(sepIndex + 3).trim();
        } else {
            this.author = "";
            this.title = mfile.trim();
        }
    }

    @Override
    public String toString() {
        if (getAuthor().isEmpty()) {
            return getTitle();
        } else {
            return getAuthor() + " - " + getTitle();
        }
    }

    public String getPathname() {
        return this.pathname;
    }

    public String getFilename() {
        return this.filename;
    }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public abstract void play();

    public abstract void togglePause();

    public abstract void stop();

    public abstract String formatDuration();

    public abstract String formatPosition();

    public abstract long getDuration();
}
