
// Name: Umar Khalid
// Matric Number: S1423449

package mpdproject.gcu.me.org.assignmenttest1;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class P_Roadworks extends AppCompatActivity {

    private String url3="http://trafficscotland.org/rss/feeds/plannedroadworks.aspx";

    private String result = "";
    private ListView listView;
    private Button dateBtn;
    private String dateInput;
    private DatePickerDialog.OnDateSetListener dateListener;
    private List<FeedData> itemList;
    private RoadworksAdapter itemAdapter;
    private ProgressDialog dialog;
    private AlertDialog alertBox;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_roadworks);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dateBtn = (Button) findViewById(R.id.getDate);
        dateBtn = (Button) findViewById(R.id.getDate);


        dialog = new ProgressDialog(P_Roadworks.this);
        dialog.setMessage("Loading...");
        dialog.setCanceledOnTouchOutside(false);

        //Alert Dialog - alerts users when there are no items to show
        alertBox = new AlertDialog.Builder(P_Roadworks.this).create();
        alertBox.setTitle("Nothing to Show");
        alertBox.setMessage("Please select another date.");

        alertBox.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Please select another date.", Toast.LENGTH_LONG).show();
            }
        });


        listView = (ListView) findViewById(R.id.planned_list_view);
        listView.setAdapter(itemAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Intent intent = new Intent(P_Roadworks.this, FeedSelected.class);


                        String feed[] = new String[] {
                                itemList.get(i).getTitle(),
                                itemList.get(i).getDescription(),
                                itemList.get(i).getPubDate()

                        };


                        intent.putExtra("GetFeed",feed);
                        startActivity(intent);

            }
        });

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        P_Roadworks.this,
                        dateListener,
                        year,month,day);
                dialog.show();

            }

        });

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String date = month + "/" + day + "/" + year;

                Calendar calendar = Calendar.getInstance();
                calendar.set(calendar.YEAR, year);
                calendar.set(calendar.MONTH, month);
                calendar.set(calendar.DAY_OF_MONTH, day);

                SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy");
                dateInput = format.format(calendar.getTime());

                dialog.show();
                startProgress();

            }
        };

    } // End of onCreate

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(url3)).start();

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


            //  Log.e("MyTag","in run");

            try
            {
                //  Log.e("MyTag","in try");
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

            P_Roadworks.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    LinkedList<FeedData> alist = null;
                    //  Log.d("UI thread", "I am the UI thread");
                    // urlInput.setText(result);

                    //Make call to parsing code
                    //Note this is not the best location
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
                            Log.e("MyTag","description is " + temp);
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
                    if (xpp.getName().equalsIgnoreCase("item") && widget.getPubDate().contains(dateInput))
                    {

//                        Toast.makeText(this, widget.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("MyTag","widget is " + widget.toString());
                        alist.add(widget);

                        itemList.add(new FeedData(widget.getTitle(), widget.getDescription(), widget.getPubDate()));
                        itemAdapter = new RoadworksAdapter(this, 0, itemList);
                        listView.setAdapter(itemAdapter);

                    }
                    else
                    if (xpp.getName().equalsIgnoreCase("channel"))
                    {
                        int size;
                        size = alist.size();
                        Log.e("MyTag","channel size is " + size);

                        if (size == 0){
                            //Toast.makeText(this, "Nothing to Show for the selected date", Toast.LENGTH_LONG).show();
                            alertBox.show();
                        }

                    }
                }

                //hides the dialog
                dialog.hide();

                // Get the next event
                eventType = xpp.next();

            } // End of while

            return alist;
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
        return alist;

    }

}
