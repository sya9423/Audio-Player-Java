package studiplayer.audio;

public class AudioFileFactory {

    public static AudioFile createAudioFile(String path) throws NotPlayableException {
        if (path == null || !path.contains(".")) {
            throw new NotPlayableException(path, "Unknown suffix for AudioFile");
        }

        String extension = path.substring(path.lastIndexOf('.') + 1).toLowerCase();

        switch (extension) {
            case "mp3":
            case "ogg":
                return new TaggedFile(path);
            case "wav":
                return new WavFile(path);
            default:
                throw new NotPlayableException(path, "Unknown suffix for AudioFile" +' '+ '"'+ path + '"');
        }
    }
}
