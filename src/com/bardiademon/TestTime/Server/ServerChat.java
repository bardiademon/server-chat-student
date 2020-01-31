package com.bardiademon.TestTime.Server;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerChat
{
    private static final int PORT = 5000;

    private List<String> username;

    private PrintWriter writer5000;

    private PrintWriter writer5001;

    public static final String OK = "000000OK000000";

    public ServerChat ()
    {
        createServer (5000);
        createServer (5001);
    }

    private void createServer (int port)
    {
        new Thread (() ->
        {
            try
            {
                ServerSocket serverSocket = new ServerSocket (port);
                Socket client = serverSocket.accept ();

                System.out.println ("Connect port " + port);

                InputStream inputStream = client.getInputStream ();
                OutputStream outputStream = client.getOutputStream ();

                BufferedReader reader = new BufferedReader (new InputStreamReader (inputStream));
                PrintWriter writer = new PrintWriter (outputStream);

                if (port == 5000) writer5000 = writer;
                else writer5001 = writer;

                String username = reader.readLine ();
                writer.println (OK);
                writer.flush ();

                if (this.username == null) this.username = new ArrayList<> ();

                if (this.username.size () > 0) this.username.remove (username);

                this.username.add (username);

                new Thread (() ->
                {
                    String line;
                    while (true)
                    {
                        try
                        {
                            if ((line = reader.readLine ()) != null)
                            {
                                if (port == 5000) writer5001.println (line);
                                else writer5000.println (line);

                                writer5000.flush ();
                                writer5001.flush ();
                            }
                        }
                        catch (IOException ignored)
                        {
                        }
                    }
                }).start ();
            }
            catch (IOException e)
            {
                e.printStackTrace ();
            }
        }).start ();

    }


}
