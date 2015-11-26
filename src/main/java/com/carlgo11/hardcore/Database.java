package com.carlgo11.hardcore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private Hardcore hc;

    public Database(Hardcore parent)
    {
        this.hc = parent;
    }

    public String[] getDatabaseInfo()
    {
        String[] info = new String[5];
        info[0] = hc.getConfig().getString("mysql.url");
        info[1] = hc.getConfig().getString("mysql.port");
        info[2] = hc.getConfig().getString("mysql.username");
        info[3] = hc.getConfig().getString("mysql.password");
        info[4] = hc.getConfig().getString("mysql.database");
        return info;
    }

    public String url;
    public String port;
    public String username;
    public String password;
    public String database;

    public void updateConnection(String[] info)
    {
        this.url = info[0];
        this.port = info[1];
        this.username = info[2];
        this.password = info[3];
        this.database = info[4];
    }

    private void createTables()
    {
        //TODO
    }

    public String getData(String name, String table)
    {
        Connection con;
        Statement st;
        ResultSet rs;
        try {
            con = DriverManager.getConnection(url + database, username, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT * from " + table + "WHERE `name` = " + name);

            return rs.getString(3);  //@Usage: int = row
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
