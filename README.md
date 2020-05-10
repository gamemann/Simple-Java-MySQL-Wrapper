# Simple MySQL Wrapper
This is one of my first Java projects (second day of studying) and I decided to make a MySQL Wrapper via the JDBC Driver. It is not finished yet, but I just wanted to show others. I'll probably work on this as time goes on such as adding more functionality, improving current code (e.g. making it more efficient), and implementing security measures (e.g. escaping if it isn't automatically implemented).

## Initialize the database.
You can initialize the database by importing the package and doing something like the following:

```Java
// Start the config class I made (not required).
Config cfg = new Config();

// Set some config options.
cfg.setConfig("dbHost", "dbHost");
cfg.setConfig("dbUser", "dbUser");
cfg.setConfig("dbPass", "dbPass");
cfg.setConfig("dbName", "dbName");
cfg.setConfig("dbPort", 3306);

// Initialize the class.
MySQL db = new MySQL((String) cfg.getConfig("dbHost"), (String) cfg.getConfig("dbUser"), (String) cfg.getConfig("dbPass"), (String) cfg.getConfig("dbName"), (int) cfg.getConfig("dbPort"), true);

// Connect to the database.
db.connect();
```

You may disconnect the database by using the `Disconnect()` method.

## Examples
Here is an example of an `Update` query:

```Java
// Create a new `update` class.
MySQL.Update update = db.newUpdate();

/* DOING AN UPDATE FROM STANDARD QUERY (NON-PREPARED) */
// Create a list.
update.setMap("msg", "Hia everyone! This was changed from the Update example!");

// Execute the query.
update.executeQuery("test", "`id`=2");

/* DOING AN UPDATE FROM PREPARED STATEMENT */
update.prepareQuery("UPDATE `test` SET `msg`=? WHERE `id`=3", "Hia everyone! This was changed from the Update example via prepared statement!");
```

Here is an example of an `Insert` query:

```Java
// Create a new `Insert` class.
MySQL.Insert insert = db.newInsert();

/* DOING AN UPDATE FROM STANDARD QUERY (NON-PREPARED) */
// Create a list.
insert.setMap("msg", "Hia everyone! This was inserted from the Insert example!");

// Execute the query.
insert.executeQuery("test");

/* DOING AN UPDATE FROM PREPARED STATEMENT */
insert.prepareQuery("INSERT INTO `test` (`msg`) VALUES (?);", "Hia everyone! This was inserted from the Insert example via prepared statement!");
```

Here is an example of a `Select` query:

```Java
// Create a new `Insert` class.
MySQL.Select select = db.newSelect();

// Initialize a ResultSet.
java.sql.ResultSet rs;

/* DOING AN UPDATE FROM STANDARD QUERY (NON-PREPARED) */
// Create a list for the GET.
select.setMapGet("id");
select.setMapGet("msg");

// Create a list for the WHERE clause.
select.setMapWhere("id", "1", false);
select.setMapWhere("id", "3", true);
select.setMapWhere("id", "7", true);

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
```

# Credits
* [Christian Deacon](https://www.linkedin.com/in/christian-deacon-902042186/) - Creator