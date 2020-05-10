package tests;

import MySQLWrapper.MySQL;

public class Insert
{
    public static void main(String[] args)
    {
        // Start the config class I made (not required).
        Config cfg = new Config();

        // Set some config options.
        cfg.setConfig("dbHost", "myHost");
        cfg.setConfig("dbUser", "myUser");
        cfg.setConfig("dbPass", "myPass");
        cfg.setConfig("dbName", "myDB");
        cfg.setConfig("dbPort", 3306);

        // Initialize the class.
        MySQL db = new MySQL((String) cfg.getConfig("dbHost"), (String) cfg.getConfig("dbUser"), (String) cfg.getConfig("dbPass"), (String) cfg.getConfig("dbName"), (int) cfg.getConfig("dbPort"), true);

        // Connect to the database.
        if (db.connect())
        {
            // Create a new `Insert` class.
            MySQL.Insert insert = db.newInsert();

            /* DOING AN UPDATE FROM STANDARD QUERY (NON-PREPARED) */
            // Create a list.
            insert.setMap("msg", "Hia everyone! This was inserted from the Insert example!");

            // Execute the query.
            insert.executeQuery("test");

            /* DOING AN UPDATE FROM PREPARED STATEMENT */
            insert.prepareQuery("INSERT INTO `test` (`msg`) VALUES (?);", "Hia everyone! This was inserted from the Insert example via prepared statement!");
        }

        // Disconnect.
        db.disconnect();
    }
}