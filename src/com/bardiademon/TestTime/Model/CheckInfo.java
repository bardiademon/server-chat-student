package com.bardiademon.TestTime.Model;

import com.bardiademon.TestTime.Model.NewUser.KeyUser;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public final class CheckInfo
{
    private File user;
    private String username;
    private String password;

    public CheckInfo (File User , String Username , String Password)
    {
        this.user = User;
        this.username = Username;
        this.password = Password;
    }

    public boolean isValid ()
    {
        if (user.exists ())
        {
            try
            {
                List<String> infoUserLines = Files.readAllLines (user.toPath ());

                StringBuilder infoUserString;
                infoUserString = new StringBuilder ();
                for (String infoUserLine : infoUserLines) infoUserString.append (infoUserLine);

                if (infoUserLines.size () > 0)
                {
                    JSONObject jsonUser = new JSONObject (infoUserString.toString ());
                    return (jsonUser.has (KeyUser.username.name ())
                            && jsonUser.has (KeyUser.password.name ())
                            && jsonUser.getString (KeyUser.username.name ()).equals (username)
                            && jsonUser.getString (KeyUser.password.name ()).equals (password));
                }
                else return false;

            }
            catch (IOException e)
            {
                return false;
            }
        }
        else return false;
    }
}
