package com.example.gerard.socialapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gerard.socialapp.R;
import com.example.gerard.socialapp.model.Message;
import com.example.gerard.socialapp.model.Post;
import com.example.gerard.socialapp.view.MessageViewHolder;
import com.example.gerard.socialapp.view.PostViewHolder;
import com.example.gerard.socialapp.view.fragment.PostsFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MessagesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public DatabaseReference mReference;
    public FirebaseUser mUser;
    public String actualPostUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actualPostUid = getIntent().getExtras().getString("uid");

        mReference = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Message>()
                .setIndexedQuery(setQuery(), mReference.child("message/data"), Message.class)
                .setLifecycleOwner(this)
                .build();

        RecyclerView recycler = findViewById(R.id.rvMessages);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(new FirebaseRecyclerAdapter<Message, MessageViewHolder>(options) {

                                @NonNull
                                @Override
                                public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                                    LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                                    return new MessageViewHolder(inflater.inflate(R.layout.item_message, viewGroup, false));
                                }

                                @Override
                                protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull Message model) {
                                    holder.author.setText(model.author);
                                    holder.content.setText(model.content);
                                }
                            });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MessagesActivity.this, NewMessageActivity.class);
                intent.putExtra("uid", actualPostUid);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.messages, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

                            startActivity(new Intent(MessagesActivity.this, PostsActivity.class));
                            finish();

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.message_manage) {

            startActivity(new Intent(MessagesActivity.this, MessagesActivity.class));
            finish();

        } else if (id == R.id.sign_out) {
            Log.e("ABC", "signout");

            AuthUI.getInstance()
                    .signOut(MessagesActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivity(new Intent(MessagesActivity.this, MainActivity.class));
                            finish();
                        }
                    });

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    Query setQuery(){
        return  mReference.child("message/post-message").child(actualPostUid).limitToFirst(100);
    }
}
