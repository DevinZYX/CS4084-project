package com.example.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FriendSearchActivity extends AppCompatActivity {

    private ListView searchResultsListView;
    private String userName;
    private List<Friend> friends;
    private List<Friend> originalFriends;
    private FriendListAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("Friends");
        userName = getIntent().getStringExtra("userName");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_search);

        searchResultsListView = findViewById(R.id.searchResultsListView);

        // Populate friends list with sample data
        originalFriends = new ArrayList<>();
        originalFriends.add(new Friend("001", "John Smith"));
        originalFriends.add(new Friend("002", "Mary Jones"));
        originalFriends.add(new Friend("003", "Bob Johnson"));
        originalFriends.add(new Friend("004", "Jane Doe"));

        friends = new ArrayList<>(originalFriends);

        // Set up adapter and attach it to list view
        adapter = new FriendListAdapter(this, friends);
        searchResultsListView.setAdapter(adapter);

        // Set up search view
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        // Set onClick listener to navigate to friend info page
        searchResultsListView.setOnItemClickListener((parent, view, position, id) -> {
            Friend selectedFriend = (Friend) parent.getItemAtPosition(position);
            Intent intent = new Intent(this, FriendInfoActivity.class);
            intent.putExtra("friendId", selectedFriend.getId());
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FriendSearchActivity.this, MainActivity.class);
        intent.putExtra("userName",userName);
        startActivity(intent);
        finish();
    }

    private void filter(String keyword) {
        if (keyword.isEmpty()) {
            friends.clear();
            friends.addAll(originalFriends);
        } else {
            List<Friend> filteredFriends = new ArrayList<>();

            for (Friend friend : originalFriends) {
                if (friend.getName().toLowerCase().contains(keyword.toLowerCase())) {
                    filteredFriends.add(friend);
                }
            }

            friends.clear();
            friends.addAll(filteredFriends);
        }

        adapter.notifyDataSetChanged();

        if (friends.size() == 0) {
            Toast.makeText(this, "No friend found", Toast.LENGTH_SHORT).show();
        }
    }

    public class Friend {
        private String id;
        private String name;

        public Friend(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public class FriendListAdapter extends ArrayAdapter<Friend> {
        private Context context;
        private List<Friend> friends;

        public FriendListAdapter(Context context, List<Friend> friends) {
            super(context, 0, friends);
            this.context = context;
            this.friends = friends;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_friend_info, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.nameTextView = convertView.findViewById(R.id.nameTextView);
                viewHolder.idTextView = convertView.findViewById(R.id.idTextView);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Friend friend = friends.get(position);

            viewHolder.nameTextView.setText(friend.getName());
            viewHolder.idTextView.setText(friend.getId());

            return convertView;
        }

        private class ViewHolder {
            private TextView nameTextView;
            private TextView idTextView;
        }
    }
}



