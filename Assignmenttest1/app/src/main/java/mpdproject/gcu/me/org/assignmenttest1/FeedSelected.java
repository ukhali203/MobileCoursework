
// Name: Umar Khalid
// Matric Number: S1423449

package mpdproject.gcu.me.org.assignmenttest1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedSelected extends AppCompatActivity {

    private TextView selected, titleTitle, titleContent, durationTitle, durationContent, descriptionTitle, descriptionContent,pubDateTitle,pubDateContent;
    private View layoutbg;
    private String start;
    private String end, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_selected);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        titleTitle = (TextView)findViewById(R.id.titleTitle);
        titleContent = (TextView)findViewById(R.id.titleContent);
        durationTitle = (TextView)findViewById(R.id.durationTitle);
        selected = (TextView)findViewById(R.id.durationContent);
        descriptionTitle = (TextView)findViewById(R.id.descriptionTitle);
        descriptionContent = (TextView)findViewById(R.id.descriptionContent);
        pubDateTitle = (TextView)findViewById(R.id.pubDateTitle);
        pubDateContent = (TextView)findViewById(R.id.pubDateContent);

        layoutbg = (View) findViewById(R.id.selected_layout);

        Bundle bundle = this.getIntent().getExtras();

        String[] feed =  getIntent().getStringArrayExtra("GetFeed");

        String title = feed[0];
        String description = feed[1];
        String pubDate = feed[2];

        selected.setText("");
        titleContent.setText(title);
        descriptionContent.setText(description);
        pubDateContent.setText(pubDate);

        //Extracting start date from description
        Pattern patternstart = Pattern.compile("Start Date: (.*?) -");
        Matcher matcherstart = patternstart.matcher(description);
        if (matcherstart.find())
        {
            start = matcherstart.group(1);
        }

        //extracting end date from description
        Pattern patternend = Pattern.compile("End Date: (.*?) -");
        Matcher matcherend = patternend.matcher(description);
        if (matcherend.find())
        {
            end = matcherend.group(1);
        }

        //removing start and end date from description. only shows description.
        Pattern patterndesc = Pattern.compile("Works:(.*)$");
        Matcher matcherdesc = patterndesc.matcher(description);
        if (matcherdesc.find())
        {
            desc = matcherdesc.group(1);
        }


        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy");

        Date datestart = new Date();
        try {
            datestart = sdf.parse(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date dateend = new Date();

        try {
            dateend = sdf.parse(end);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //converting milliseconds to days
        long milli = dateend.getTime() - datestart.getTime();
        long duration = TimeUnit.DAYS.convert(milli, TimeUnit.MILLISECONDS);


        selected.setText("Start Date: " + start + "\n\n" + "End Date: " + end + "\n\n Duration: " + duration + " days");
        descriptionContent.setText(desc);

        //changes to appropriate colour depending on duration
        if (duration >= 0 && duration <=3){
            layoutbg.setBackgroundColor(getColor(R.color.Lime));
        } else if (duration >3 && duration <7){
            layoutbg.setBackgroundColor(getColor(R.color.Orange));
        } else if (duration >=7){
            layoutbg.setBackgroundColor(getColor(R.color.Red));
        }

    }


    //Back arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);

    }
}
