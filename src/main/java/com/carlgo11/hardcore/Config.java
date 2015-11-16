package com.carlgo11.hardcore;

public class Config {

    private Hardcore hc;

    public Config(Hardcore plug) {
        this.hc = plug;
    }

    public String[] getDatabaseInfo() {
        String[] info = {};
        info[info.length] = hc.getConfig().getString("url");
        info[info.length] = hc.getConfig().getString("port");
        info[info.length] = hc.getConfig().getString("username");
        info[info.length] = hc.getConfig().getString("password");
        info[info.length] = hc.getConfig().getString("database");
        return info;
    }
}
