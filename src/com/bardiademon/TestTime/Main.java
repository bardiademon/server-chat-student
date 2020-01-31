package com.bardiademon.TestTime;

import com.bardiademon.TestTime.Server.ServerChat;
import com.bardiademon.TestTime.Server.ServerLoginRegister;

public class Main
{

    public static void main (String[] args)
    {
        new ServerLoginRegister (ServerLoginRegister.PORT1000);
        new ServerLoginRegister (ServerLoginRegister.PORT2000);
        new ServerChat ();
    }
}
