package com.bardiademon.TestTime.Model;

import com.bardiademon.TestTime.Path;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;

public class NewUser
{
    private final String username;
    private final String password;

    public NewUser (final String Username , final String Password)
    {
        this.username = Username;
        this.password = Password;
    }

    public boolean record ()
    {
        File user = new File (Path.PATH_USERS + File.separator + username + ".json");
        if (user.exists ()) return false;
        else
        {
            try
            {
                if (user.createNewFile ())
                {
                    JSONObject newUser = new JSONObject ();
                    newUser.put (KeyUser.username.name () , username);
                    newUser.put (KeyUser.password.name () , password);
                    newUser.put (KeyUser.created_at.name () , LocalDateTime.now ().toString ());
                    Files.write (user.toPath () , newUser.toString ().getBytes ());
                    return true;
                }
                else return false;
            }
            catch (IOException e)
            {
                return false;
            }
        }
    }

    public enum KeyUser
    {
        username, password, created_at
    }

}
