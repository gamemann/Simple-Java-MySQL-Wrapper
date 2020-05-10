package MySQLWrapper;

import java.sql.*;
import java.util.*;

public class MySQL
{
    /* Database details */
    protected String dbHost;
    protected String dbUser;
    protected String dbPass;
    protected String dbName;
    protected int dbPort;

    /* Other variables. */
    protected Connection dbConnection = null;
    protected boolean debug = false;

    public MySQL(String host, String user, String pass, String name, int port, boolean debug)
    {
        // Assign the correct values.
        this.dbHost = host;
        this.dbUser = user;
        this.dbPass = pass;
        this.dbName = name;
        this.dbPort = port;
        this.debug = debug;
    }

    public boolean connect()
    {
        try
        {
            // Retrieve the Database Driver.
            Class.forName("com.mysql.jdbc.Driver");

            // Compile the connect URL and initialize the connection.
            String connectURL = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", this.dbHost, this.dbPort, this.dbName, this.dbUser, this.dbPass);

            // Debug message.
            this.debugMsg("Connecting to database with string - " + connectURL);

            // Connect to the database.
            this.dbConnection = DriverManager.getConnection(connectURL);

            // Return whether it's a valid connection or not.
            return this.dbConnection.isValid(20);
        }
        catch (SQLException e)
        {
            System.out.println("Caught SQLException - " + e.getMessage());

            return false;
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Caught ClassNotFoundException - " + e.getMessage());

            return false;
        }
        catch (Exception e)
        {
            throw e;
        }
    }

    public void disconnect()
    {
            // Close the connection.
            try
            {
                if (this.dbConnection != null || !this.dbConnection.isClosed())
                {
                    this.dbConnection.close();
                }
            }
            catch (SQLException e)
            {
                System.out.println("Caught SQLException - " + e.getMessage());
            }
            catch (Exception e)
            {
                throw e;
            }
    }

    public MySQLWrapper.MySQL.Update newUpdate()
    {
        return new Update();
    }

    public class Update
    {
        /* Update variables */
        protected List<QueryMap> queryMaps = new ArrayList<>();
        protected StringBuffer queryStr = new StringBuffer();

        protected class QueryMap
        {
            protected String queryKey;
            protected Object queryVal;
        }

        public Update()
        {
            // Add a debug message.
            debugMsg("Initializing an Update query");
        }

        public void setMap(String key, Object val)
        {
            // Create the query map.
            QueryMap temp = new QueryMap();

            // Assign the key => value.
            temp.queryKey = key;
            temp.queryVal = val;

            // Add the key => value to the `lMaps` list.
            this.queryMaps.add(temp);
        }

        protected void compileQuery(String table, String whereClause)
        {
            // Reinitialize the sQuery variable.
            this.queryStr = new StringBuffer();

            // Build the beginning of the query (UPDATE `<table>` SET...).
            this.queryStr.append("UPDATE `" + table + "` SET ");

            // Loop through all query maps.
            for (int i = 0; i < this.queryMaps.size(); i++)
            {
                // Append the column => value to the query string.
                this.queryStr.append("`" + this.queryMaps.get(i).queryKey + "`='" + this.queryMaps.get(i).queryVal.toString() + "'");

                // Decide whether or not to add a comma and if so, append it.
                if (i != (this.queryMaps.size() - 1))
                {
                    this.queryStr.append(",");
                }

                // Now insert a space for cool organization.
                this.queryStr.append(" ");
            }

            // Finally, append the WHERE clause.
            this.queryStr.append("WHERE " + whereClause + ";");
        }

        public Statement executeQuery(String table, String whereClause)
        {
            // Initialize the statement.
            Statement sm = null;

            // Compile the query with the table and where clause.
            this.compileQuery(table, whereClause);

            // Debug message.
            debugMsg("Update->executeQuery() - Compiling query with table '" + table + "' and where clause" + whereClause);

            try
            {
                // Create the statement.
                sm = dbConnection.createStatement();

                // Execute the query using the sQuery string we compiled.
                sm.executeUpdate(this.queryStr.toString());


            }
            catch (SQLException e)
            {
                // Throw SQLException.
                System.out.println("Caught SQLException - " + e.getMessage());
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                // Reset everything.
                this.reset();


            }

            return sm;
        }

        public PreparedStatement prepareQuery(String prepareStr, Object... prepareVals)
        {
            // Initialize the prepared statement.
            PreparedStatement ps = null;

            try
            {
                // Create the prepared statement with the correct prepare string.
                ps = dbConnection.prepareStatement(prepareStr);

                // Initialize index count.
                int iCount = 1;

                // Assign values to prepare statement.
                for (Object c : prepareVals)
                {
                    // Assign the object to index.
                    ps.setObject(iCount, c);

                    // Increase the index.
                    iCount++;
                }

                // Execute the statement.
                ps.executeUpdate();
            }
            catch (SQLException e)
            {
                // Throw SQLException.
                System.out.println("Caught SQLException - " + e.getMessage());
            }
            catch (Exception e)
            {
                throw e;
            }

            // Return the prepared statement.
            return ps;
        }

        protected void reset()
        {
            this.queryStr = new StringBuffer();
            this.queryMaps.clear();
        }
    }

    public MySQLWrapper.MySQL.Insert newInsert()
    {
        return new Insert();
    }

    public class Insert
    {
        /* Insert variables */
        protected List<QueryMap> queryMaps = new ArrayList<>();
        protected StringBuffer queryStr = new StringBuffer();

        protected class QueryMap
        {
            protected String queryKey;
            protected Object queryVal;
        }

        public Insert()
        {
            // Add a debug message.
            debugMsg("Initializing an Insert query");
        }

        public void setMap(String key, Object val)
        {
            // Create the query map.
            QueryMap temp = new QueryMap();

            // Assign the key => value.
            temp.queryKey = key;
            temp.queryVal = val;

            // Add the key => value to the `lMaps` list.
            this.queryMaps.add(temp);
        }

        protected void compileQuery(String table)
        {
            // Reinitialize the sQuery variable.
            this.queryStr = new StringBuffer();

            // Build the beginning of the query (INSERT INTO `<table>`).
            this.queryStr.append("INSERT INTO `" + table + "`");

            // We're going to start the column names.
            this.queryStr.append(" (");

            // Loop through all query maps, but only add the column names.
            for (int i = 0; i < this.queryMaps.size(); i++)
            {
                // Append the column to the query string.
                this.queryStr.append("`" + this.queryMaps.get(i).queryKey + "`");

                // Decide whether or not to add a comma along with space and if so, append them.
                if (i != (this.queryMaps.size() - 1))
                {
                    this.queryStr.append(", ");
                }
            }

            // Close it.
            this.queryStr.append(")");

            // Now start the values.
            this.queryStr.append(" VALUES (");

            // Loop through all query maps, but only add the values.
            for (int i = 0; i < this.queryMaps.size(); i++)
            {
                // Append the column to the query string.
                this.queryStr.append("'" + this.queryMaps.get(i).queryVal + "'");

                // Decide whether or not to add a comma along with space and if so, append them.
                if (i != (this.queryMaps.size() - 1))
                {
                    this.queryStr.append(", ");
                }
            }

            // Close it.
            this.queryStr.append(");");
        }

        public Statement executeQuery(String table)
        {
            // Initialize the statement.
            Statement dbStatement = null;

            // Compile the query with the table.
            this.compileQuery(table);

            // Debug message.
            debugMsg("Delete->executeQuery() - Compiling query with table '" + table + "'");

            try
            {
                // Create the statement.
                dbStatement = dbConnection.createStatement();

                // Execute the query using the sQuery string we compiled.
                dbStatement.executeUpdate(this.queryStr.toString());
            }
            catch (SQLException e)
            {
                // Throw SQLException.
                System.out.println("Caught SQLException - " + e.getMessage());
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                // Reset everything.
                this.reset();
            }

            return dbStatement;
        }

        public PreparedStatement prepareQuery(String prepareStr, Object... prepareVals)
        {
            // Initialize the prepared statement.
            PreparedStatement ps = null;

            try
            {
                // Create the prepared statement with the correct prepare string.
                ps = dbConnection.prepareStatement(prepareStr);

                // Initialize index count.
                int iCount = 1;

                // Assign values to prepare statement.
                for (Object c : prepareVals)
                {
                    // Assign the object to index.
                    ps.setObject(iCount, c);

                    // Increase the index.
                    iCount++;
                }

                // Execute the statement.
                ps.executeUpdate();
            }
            catch (SQLException e)
            {
                // Throw SQLException.
                System.out.println("Caught SQLException - " + e.getMessage());
            }
            catch (Exception e)
            {
                throw e;
            }

            // Return the amount of rows.
            return ps;
        }

        protected void reset()
        {
            this.queryStr = new StringBuffer();
            this.queryMaps.clear();
        }
    }

    public MySQLWrapper.MySQL.Select newSelect()
    {
        return new Select();
    }

    public class Select
    {
        /* Select variables */
        protected List<QueryMapGet> queryMapsGet = new ArrayList<>();
        protected List<QueryMapWhere> queryMapsWhere = new ArrayList<>();
        protected StringBuffer queryStr = new StringBuffer();
        protected ResultSet rs;

        protected class QueryMapGet
        {
            protected String queryGet;
        }

        protected class QueryMapWhere
        {
            protected String queryKey;
            protected Object queryVal;
            protected boolean queryOr;
        }

        public Select()
        {
            // Add a debug message.
            debugMsg("Initializing a Select query");
        }

        public void setMapGet(String col)
        {
            // Create the query map.
            QueryMapGet temp = new QueryMapGet();

            // Assign the value.
            temp.queryGet = col;

            // Add the key => value to the `lMaps` list.
            this.queryMapsGet.add(temp);
        }

        public void setMapWhere(String key, Object val, boolean or)
        {
            // Create the query map.
            QueryMapWhere temp = new QueryMapWhere();

            // Assign the key => value.
            temp.queryKey = key;
            temp.queryVal = val;
            temp.queryOr = or;

            // Add the key => value to the `lMaps` list.
            this.queryMapsWhere.add(temp);
        }

        protected void compileQuery(String table)
        {
            // Reinitialize the sQuery variable.
            this.queryStr = new StringBuffer();

            // Build the beginning of the query (SELECT )).
            this.queryStr.append("SELECT ");

            // Loop through all query maps for get.
            for (int i = 0; i < this.queryMapsGet.size(); i++)
            {
                // Append the column to the query string.
                this.queryStr.append("`" + this.queryMapsGet.get(i).queryGet + "`");

                // Decide whether or not to add a comma, if so, append it.
                if (i != (this.queryMapsGet.size() - 1))
                {
                    this.queryStr.append(",");
                }

                // Append a space.
                this.queryStr.append(" " );
            }

            // Build the FROM and table name.
            this.queryStr.append("FROM `" + table + "` ");

            // Append the WHERE clause.
            this.queryStr.append("WHERE ");

            // Loop through all query maps for where.
            for (int i = 0; i < this.queryMapsWhere.size(); i++)
            {
                // Decide whether it's an 'AND' or 'OR'.
                if (i != 0)
                {
                    if (this.queryMapsWhere.get(i).queryOr)
                    {
                        this.queryStr.append("OR");
                    }
                    else
                    {
                        this.queryStr.append("AND");
                    }
                }

                // Append the column to the query string.
                this.queryStr.append(" `" + this.queryMapsWhere.get(i).queryKey + "`='" + this.queryMapsWhere.get(i).queryVal + "'");

                // Append a space!
                this.queryStr.append(" ");
            }

            // Close it.
            this.queryStr.append(";");
        }

        public Statement executeQuery(String table)
        {
            // Initialize the statement.
            Statement dbStatement = null;

            // Compile the query with the table.
            this.compileQuery(table);

            // Debug message.
            debugMsg("Select->executeQuery() - Compiling query with table '" + table + "'");

            try
            {
                // Create the statement.
                dbStatement = dbConnection.createStatement();

                // Execute the query using the sQuery string we compiled.
                this.rs = dbStatement.executeQuery(this.queryStr.toString());
            }
            catch (SQLException e)
            {
                // Throw SQLException.
                System.out.println("Caught SQLException - " + e.getMessage());
            }
            catch (Exception e)
            {
                throw e;
            }
            finally
            {
                // Reset everything.
                this.reset();
            }

            return dbStatement;
        }

        public PreparedStatement prepareQuery(String prepareStr, Object... prepareVals)
        {
            // Initialize the prepared statement.
            PreparedStatement ps = null;

            try
            {
                // Create the prepared statement with the correct prepare string.
                ps = dbConnection.prepareStatement(prepareStr);

                // Initialize index count.
                int iCount = 1;

                // Assign values to prepare statement.
                for (Object c : prepareVals)
                {
                    // Assign the object to index.
                    ps.setObject(iCount, c);

                    // Increase the index.
                    iCount++;
                }

                // Execute the statement.
                this.rs = ps.executeQuery();
            }
            catch (SQLException e)
            {
                // Throw SQLException.
                System.out.println("Caught SQLException - " + e.getMessage());
            }
            catch (Exception e)
            {
                throw e;
            }

            // Return the amount of rows.
            return ps;
        }

        public ResultSet retrieveSet()
        {
            return this.rs;
        }

        protected void reset()
        {
            this.queryStr = new StringBuffer();
            this.queryMapsGet.clear();
            this.queryMapsWhere.clear();
        }
    }

    protected void debugMsg(String msg)
    {
        if (this.debug)
        {
            System.out.println(msg);
        }
    }
}