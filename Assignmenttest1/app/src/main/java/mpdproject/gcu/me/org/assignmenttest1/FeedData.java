
// Name: Umar Khalid
// Matric Number: S1423449

package mpdproject.gcu.me.org.assignmenttest1;

/**
 * Created by UMAR on 15/02/2018.
 */


public class FeedData
{
    private String title;
    private String description;
    private String pubDate;

    public FeedData()
    {
        title = "";
        description= "";
        pubDate = "";
    }

    public FeedData(String atitle, String adescription, String apubDate)
    {
        title = atitle;
        description = adescription;
        pubDate = apubDate;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String atitle)
    {
        title = atitle;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String adescription)
    {
        description = adescription;
    }

    public String getPubDate()
    {
        return pubDate;
    }

    public void setPubDate(String apubDate)
    {
        pubDate = apubDate;
    }

    public String toString()
    {
        String temp;

        temp = "Title: \n\n" + title + "\n\n Description: \n\n " + description + " \n\n Publication Date: \n\n" + pubDate;

        return temp;
    }

}