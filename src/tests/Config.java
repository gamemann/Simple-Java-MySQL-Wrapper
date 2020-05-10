package tests;

import java.util.*;

public class Config
{
    /* Small Config class I made for the example files ;) */
    List<Mapping> list = new ArrayList<>();

    class Mapping
    {
        protected String key;
        protected Object val;
    }

    public void setConfig(String key, Object val)
    {
        // Start new mapping class.
        Mapping temp = new Mapping();

        // Set the key => value.
        temp.key = key;
        temp.val = val;

        // Add it to the ArrayList().
        this.list.add(temp);
    }

    public Object getConfig(String key)
    {
        // Loop through each item in the ArrayList and match it.
        for (int i = 0; i < this.list.size(); i++)
        {
            // If it matches the key, return it.
            if(this.list.get(i).key == key)
            {
                return this.list.get(i).val;
            }
        }

        return false;
    }

    public void clearConfig()
    {
        this.list.clear();
    }
}