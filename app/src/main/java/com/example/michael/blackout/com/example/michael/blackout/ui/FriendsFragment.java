package com.example.michael.blackout.com.example.michael.blackout.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.example.michael.blackout.R;
import com.example.michael.blackout.adapters.UserAdapter;
import com.example.michael.blackout.utils.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Michael on 5/24/2015.
 */
public class FriendsFragment extends Fragment {

public static final String TAG = FriendsFragment.class.getSimpleName();

    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected GridView mGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_grid, container, false);

        mGridView = (GridView)rootView.findViewById(R.id.friendsGrid);
        TextView emptyTextView = (TextView)rootView.findViewById(android.R.id.empty);
        mGridView.setEmptyView(emptyTextView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        getActivity().setProgressBarIndeterminateVisibility(true);

        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if (e == null) {
                   mFriends = list;
                   String[] usernames = new String[mFriends.size()];
                   int i = 0;
                   for (ParseUser user : mFriends) {
                       usernames[i] = user.getUsername();
                       i++;
                   }
                   if (mGridView.getAdapter() == null) {
                       UserAdapter adapter = new UserAdapter(getActivity(), mFriends);
                       mGridView.setAdapter(adapter);
                   }else {
                       //
                       ((UserAdapter)mGridView.getAdapter()).refill(mFriends);
                   }
               } else {
                   Log.e(TAG, e.getMessage());
                   AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                   builder.setMessage(e.getMessage())
                           .setTitle(R.string.error_title)
                           .setPositiveButton(android.R.string.ok, null);

                   AlertDialog dialog = builder.create();
                   dialog.show();
               }
            }
        });
    }

}
