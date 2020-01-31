package com.bardiademon.TestTime.Server;

import com.bardiademon.TestTime.IsExistsUsername;
import com.bardiademon.TestTime.Model.CheckInfo;
import com.bardiademon.TestTime.Model.NewUser;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public final class ServerLoginRegister
{

    public static final int PORT1000 = 1000;
    public static final int PORT2000 = 2000;

    private JSONObject requestClient;

    private Answer message;
    private int port;

    public ServerLoginRegister (int port)
    {
        this.port = port;
        new Thread (this::createServer).start ();
    }

    private void createServer ()
    {
        try
        {
            System.out.println ("Waiting server run port " + port);
            ServerSocket serverSocket = new ServerSocket (port);
            System.out.println ("\rServer run port " + port);

            System.out.println ("Waiting for client...");
            Socket client = serverSocket.accept ();
            System.out.println ("\rClient connected");

            InputStream inputStream = client.getInputStream ();
            OutputStream outputStream = client.getOutputStream ();

            BufferedReader reader = new BufferedReader (new InputStreamReader (inputStream));

            PrintWriter writer = new PrintWriter (outputStream);

            String line;

            JSONObject jsonAnswer;

            line = reader.readLine ();

            if (line != null)
            {
                requestClient = new JSONObject (line);
                jsonAnswer = new JSONObject ();
                jsonAnswer.put (KeyJson.ok.name () , afterGetRequest ());
                jsonAnswer.put (KeyJson.message.name () , message.name ());

                writer.println (jsonAnswer.toString ());
            }
            else writer.write ("ok");
            writer.flush ();

            client.close ();
            serverSocket.close ();
            inputStream.close ();
            outputStream.close ();

            createServer ();

        }
        catch (IOException ignored)
        {
            System.out.println (ignored.getMessage ());
        }
    }

    private boolean afterGetRequest ()
    {
        try
        {
            if (requestClient.has (KeyJson.type.name ()) && requestClient.has (KeyJson.username.name ()) && requestClient.has (KeyJson.password.name ()))
            {
                String type = requestClient.getString (KeyJson.type.name ());
                String username = requestClient.getString (KeyJson.username.name ());
                String password = requestClient.getString (KeyJson.password.name ());
                if (type.equals ("register"))
                {
                    if (username.isEmpty () || password.isEmpty ())
                    {
                        message = Answer.request_is_empty;
                        return false;
                    }
                    else
                    {
                        if (new IsExistsUsername (username).exists ())
                        {
                            message = Answer.username_is_exists;
                            return false;
                        }
                        else
                        {
                            if (new NewUser (username , password).record ())
                            {
                                message = Answer.recorded;
                                return true;
                            }
                            else
                            {
                                message = Answer.server_error;
                                return false;
                            }
                        }
                    }
                }
                else if (type.equals ("validation_login"))
                {
                    IsExistsUsername isExistsUsername;
                    if ((isExistsUsername = new IsExistsUsername (username)).exists ())
                    {
                        if (new CheckInfo (isExistsUsername.getUser () , username , password).isValid ())
                        {
                            message = Answer.info_valid;
                            return true;
                        }
                        else
                        {
                            message = Answer.info_invalid;
                            return false;
                        }
                    }
                    else
                    {
                        message = Answer.username_not_found;
                        return false;
                    }
                }
                else
                {
                    message = Answer.server_error;
                    return false;
                }
            }
            else
            {
                message = Answer.server_error;
                return false;
            }
        }
        catch (JSONException e)
        {
            message = Answer.server_error;
            return false;
        }
    }

    private enum KeyJson
    {
        type,

        username, password,


        message, ok
    }

    private enum Answer
    {
        username_is_exists, request_is_empty, server_error, recorded, username_not_found, info_valid, info_invalid
    }

}
