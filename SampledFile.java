package studiplayer.audio;

import studiplayer.basic.BasicPlayer;

public abstract class SampledFile extends AudioFile {

    private long duration;

    public SampledFile() {
        super();
    }

    public SampledFile(String path){
        super(path);
    }

    @Override
    public void play(){
        try {
            BasicPlayer.play(getPathname());
        } catch (RuntimeException e) {
            // Wrap the caught exception in your custom NotPlayableException
            throw new NotPlayableException("Cannot play file: " + getPathname(), e);
        }
    }

    @Override
    public void togglePause() {
        BasicPlayer.togglePause();
    }

    @Override
    public void stop() {
        BasicPlayer.stop();
    }

    @Override
    public String formatDuration() {
        return timeFormatter(duration);
    }

    @Override
    public String formatPosition() {
        return timeFormatter(BasicPlayer.getPosition());
    }

    protected static String timeFormatter(long timeInMicroSeconds) {
        if (timeInMicroSeconds < 0) {
            throw new RuntimeException("Negative time value");
        }
        long totalSeconds = timeInMicroSeconds / 1_000_000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;

        if (minutes >= 100) {
            throw new RuntimeException("Time value overflows format");
        }

        return String.format("%02d:%02d", minutes, seconds);
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
