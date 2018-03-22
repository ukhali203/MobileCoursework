
// Name: Umar Khalid
// Matric Number: S1423449

package mpdproject.gcu.me.org.assignmenttest1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by UMAR on 23/02/2018.
 */


public class FeedAdapter extends ArrayAdapter<FeedData> {
    private static final String TAG = FeedAdapter.class.getSimpleName();

    List<FeedData> FeedList;

    public FeedAdapter(Context context, int resource, List<FeedData> objects) {
        super(context, resource, objects);

        FeedList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        FeedData currentFeed = FeedList.get(position);

        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.feed_current_list, parent, false);
        }

        //sets the title
        TextView Title = (TextView) listItemView.findViewById(R.id.TitleHeader);
        Title.setText(currentFeed.getTitle());

        //sets the description
        TextView Description = (TextView) listItemView.findViewById(R.id.description);
        Description.setText(currentFeed.getDescription());

        //sets the publication date
        TextView PublicationDate = (TextView) listItemView.findViewById(R.id.pubDate);
        PublicationDate.setText(currentFeed.getPubDate());


        return listItemView;
    }

}

