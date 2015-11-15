package com.carlgo11.hardcore;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Database {

    private Hardcore plugin;

    public Database(Hardcore plug)
    {
        this.plugin = plug;

    }

    public String url;
    public String port;
    public String username;
    public String password;
    public String database;

    public void updateConnection(String url, String port, String username, String password, String database)
    {
        this.url = url;
        this.port = port;
        this.username = username;
        this.password = password;
        this.database = database;
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

            return rs.getString(3).toString();
        } catch (Exception ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
