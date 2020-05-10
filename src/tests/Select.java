package tests;

import MySQLWrapper.MySQL;

public class Select
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
            MySQL.Select select = db.newSelect();

            // Initialize a ResultSet.
            java.sql.ResultSet rs;

            /* DOING AN UPDATE FROM STANDARD QUERY (NON-PREPARED) */
            // Create a list for the GET.
            select.setMapGet("id");
            select.setMapGet("msg");

            // Create a list for the WHERE clause.
            select.setMapWhere("id", "22", false);
            select.setMapWhere("id", "25", true);
            select.setMapWhere("id", "28", true);

            // Execute the query.
            select.executeQuery("test");

            // Retrieve the result set.
            rs = select.retrieveSet();

            // Attempt to retrieve all rows and print them.
            try
            {
                System.out.printf("\nRows from standard query:\n\n");

                while (rs.next())
                {
                    System.out.printf("ID - %d\nMsg - %s\n\n", rs.getInt("id"), rs.getString("msg"));
                }
            }
            catch (java.sql.SQLException e)
            {
                System.out.println("Caught SQLException - " + e.getMessage());
            }

            /* DOING AN UPDATE FROM PREPARED STATEMENT */
            select.prepareQuery("SELECT * FROM `test` WHERE `id`=? OR `id`=? OR `id`=?", "23", "27", "30");

            // Retrieve the result set.
            rs = select.retrieveSet();

            // Attempt to retrieve all rows and print them.
            try
            {
                System.out.printf("\nRows from prepared query:\n\n");

                while (rs.next())
                {
                    System.out.printf("ID - %d\nMsg - %s\n\n", rs.getInt("id"), rs.getString("msg"));
                }
            }
            catch (java.sql.SQLException e)
            {
                System.out.println("Caught SQLException - " + e.getMessage());
            }
        }

        // Disconnect.
        db.disconnect();
    }
}