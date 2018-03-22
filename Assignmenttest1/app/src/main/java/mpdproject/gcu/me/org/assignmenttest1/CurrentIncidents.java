//
//
// Starter code for the Mobile Platform Development Assignment
// Seesion 2017/2018
//
//

// Name: Umar Khalid
// Matric Number: S1423449

package mpdproject.gcu.me.org.assignmenttest1;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CurrentIncidents extends AppCompatActivity implements View.OnClickListener
{
    private String url1="http://trafficscotland.org/rss/feeds/currentincidents.aspx";
    private String result = "";
    private ListView listView;
    private List<FeedData> itemList;
    private FeedAdapter itemAdapter;
    private ProgressDialog dialog;
    private AlertDialog alertBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_incidents);

        //Back arrow
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //list
        listView = (ListView) findViewById(R.id.current_list_view);
        listView.setAdapter(itemAdapter);

        //progress dialog
        dialog = new ProgressDialog(CurrentIncidents.this);
        dialog.setMessage("Loading...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


        //Alert Dialog - alerts users when there are no items to show
        alertBox = new AlertDialog.Builder(CurrentIncidents.this).create();
        alertBox.setTitle("Nothing to Show");
        alertBox.setMessage("There are no current incidents.");

        alertBox.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "There are no current incidents.", Toast.LENGTH_LONG).show();
            }
        });

        //accesses thread
        startProgress();

    } // End of onCreate



    //Back arrow method
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public void onClick(View aview)
    {
        startProgress();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(url1)).start();
    } //



    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                //
                // Throw away the first 2 header lines before parsing
                //
                //
                //
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);
                }
                in.close();
            }


            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //
            // Now that you have the xml data you can parse it
            //

            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            CurrentIncidents.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");

                    LinkedList <FeedData> alist = null;
                    alist = parseData(result);

                }
            });

        }

    }


    private LinkedList<FeedData> parseData(String dataToParse)
    {
        itemList = new ArrayList<>();
        FeedData widget = new FeedData();
        LinkedList <FeedData> alist  = new LinkedList<FeedData>();

        try
        {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput( new StringReader( dataToParse ) );
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                // Found a start tag
                if(eventType == XmlPullParser.START_TAG)
                {
                    // Check which Tag we have
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {

                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        Log.e("MyTag","Item Start Tag found");

                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("title"))
                    {
                        // Now just get the associated text
                        String temp = xpp.nextText();
                        // Do something with text
                        Log.e("MyTag","Title is " + temp);

                        widget.setTitle(temp);
                    }
                    else
                        // Check which Tag we have
                        if (xpp.getName().equalsIgnoreCase("description"))
                        {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            // Do something with text
                            Log.e("MyTag","Description is " + temp);
                            widget.setDescription(temp);
                        }
                        else
                            // Check which Tag we have
                            if (xpp.getName().equalsIgnoreCase("pubDate"))
                            {
                                // Now just get the associated text
                                String temp = xpp.nextText();
                                // Do something with text
                                Log.e("MyTag","pubDate is " + temp);
                                widget.setPubDate(temp);
                            }
                }
                else
                if(eventType == XmlPullParser.END_TAG)
                {
                    if (xpp.getName().equalsIgnoreCase("item"))
                    {
                        Log.e("MyTag","item is " + widget.toString());
                        alist.add(widget);

                        itemList.add(new FeedData(widget.getTitle(), widget.getDescription(), widget.getPubDate()));
                        itemAdapter = new FeedAdapter(this, 0, itemList);
                        listView.setAdapter(itemAdapter);

                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {

                        int size;
                        size = alist.size();
                        Log.e("MyTag","channel size is " + size);

                        if (size == 0){
                            //Toast.makeText(this, "Nothing to show", Toast.LENGTH_LONG).show();
                            alertBox.show();
                        }
                    }

                }

                // Get the next event
                eventType = xpp.next();


            } // End of while

        }

        catch (XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());

        }

        catch (IOException ae1)
        {
            Log.e("MyTag","IO error during parsing");
        }

        Log.e("MyTag","End document");

        //this hides the dialog
        dialog.hide();

        return alist;
    }

}



