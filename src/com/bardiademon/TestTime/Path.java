package com.bardiademon.TestTime;

import java.io.File;

public abstract class Path
{
    public static final String PATH_PROJECT = System.getProperty ("user.dir");

    public static final String PATH_USERS = PATH_PROJECT + File.separator + "users" + File.separator;
}
