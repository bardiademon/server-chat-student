package com.bardiademon.TestTime;

import org.apache.commons.io.FilenameUtils;

import java.io.File;

public final class IsExistsUsername
{
    private final String username;

    private File user;

    public IsExistsUsername (final String Username)
    {
        this.username = Username;
    }

    public boolean exists ()
    {
        File users = new File (Path.PATH_USERS);
        if (!users.exists ()) return users.mkdir ();
        else
        {
            File[] files = users.listFiles ();
            if (files == null || files.length == 0) return false;
            for (File user : files)
                if (FilenameUtils.getBaseName (user.getPath ()).equals (username))
                {
                    this.user = user;
                    return true;
                }

            return false;
        }
    }

    public File getUser ()
    {
        return user;
    }
}
