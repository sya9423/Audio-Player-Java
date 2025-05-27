package studiplayer.audio;

import studiplayer.basic.WavParamReader;

public class WavFile extends SampledFile {

    public WavFile() {
        super();
    }

    // REMOVE `throws` here completely
    public WavFile(String path) {
        super(path);

        parsePathname(getPathname());
        readAndSetDurationFromFile();
    }

    public void readAndSetDurationFromFile() {
        try {
            WavParamReader.readParams(getPathname());
        } catch (Exception e) {
            throw new NotPlayableException(getPathname(), "Could not read WAV parameters", e);
        }

        float frameRate = WavParamReader.getFrameRate();
        long numberOfFrames = WavParamReader.getNumberOfFrames();

        long calculatedDuration = computeDuration(numberOfFrames, frameRate);

        setDuration(calculatedDuration);
    }

    @Override
    public String toString() {
        return getAuthor() + " - " + getTitle() + " - " + formatDuration();
    }

    public static long computeDuration(long numberOfFrames, float frameRate) {
        return Math.round(((double) numberOfFrames / frameRate) * 1_000_000);
    }

    @Override
    public long getDuration() {
        return super.getDuration();
    }
    
}
