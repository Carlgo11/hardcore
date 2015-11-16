package com.carlgo11.hardcore;

public class Config {

    private Hardcore hc;

    public Config(Hardcore plug) {
        this.hc = plug;
    }

    public String[] getDatabaseInfo() {
        String[] info = new String[5];
        info[0] = hc.getConfig().getString("mysql.url");
        info[1] = hc.getConfig().getString("mysql.port");
        info[2] = hc.getConfig().getString("mysql.username");
        info[3] = hc.getConfig().getString("mysql.password");
        info[4] = hc.getConfig().getString("mysql.database");
        return info;
    }
}
