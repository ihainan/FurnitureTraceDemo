package ihainan.me.androiduidesign.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ihainan.me.androiduidesign.R;
import ihainan.me.androiduidesign.model.Vote;

/**
 * 投票列表适配器
 */
public class VoteListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mVi;
    private List<Vote> mVotes;

    @Override
    public int getCount() {
        return mVotes.size();
    }

    @Override
    public Object getItem(int position) {
        return mVotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public VoteListAdapter(Context c, List<Vote> votes) {
        this.mContext = c;
        this.mVotes = votes;
        if (mContext != null)
            mVi = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Vote vote = (Vote) getItem(position);

        if (convertView == null)
            convertView = mVi.inflate(R.layout.content_vote_item, null);

        if (vote != null) {
            TextView tvLike = (TextView) convertView.findViewById(R.id.like_number), tvDislike = (TextView) convertView.findViewById(R.id.dislike_number);
            tvDislike.setText("" + vote.getVot_oppose());
            tvLike.setText("" + vote.getVot_approve());
        }

        return convertView;
    }
}
