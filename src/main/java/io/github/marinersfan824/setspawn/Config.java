package io.github.marinersfan824.setspawn;

public class Config {
    private boolean enabled;
    private boolean useGlobalConfig;
    private Seed[] seeds;

    public Config() {

    }
    public Config(boolean enabled, boolean useGlobalConfig, Seed[] seeds) {
        this.enabled = enabled;
        this.useGlobalConfig = useGlobalConfig;
        this.seeds = seeds;
    }
    public boolean isEnabled() {
        return enabled;
    }
    public boolean isUseGlobalConfig() {
        return useGlobalConfig;
    }
    public Seed[] getSeeds() {
        return seeds;
    }
}
