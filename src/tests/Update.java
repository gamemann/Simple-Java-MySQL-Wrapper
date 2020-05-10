package tests;

import MySQLWrapper.MySQL;

public class Update
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
            // Create a new `update` class.
            MySQL.Update update = db.newUpdate();

            /* DOING AN UPDATE FROM STANDARD QUERY (NON-PREPARED) */
            // Create a list.
            update.setMap("msg", "Hia everyone! This was changed from the Update example!");

            // Execute the query.
            update.executeQuery("test", "`id`=2");

            /* DOING AN UPDATE FROM PREPARED STATEMENT */
            update.prepareQuery("UPDATE `test` SET `msg`=? WHERE `id`=3", "Hia everyone! This was changed from the Update example via prepared statement!");
        }

        // Disconnect.
        db.disconnect();
    }
}