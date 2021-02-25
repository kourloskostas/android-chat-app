package com.example.mlem;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    //Layout variables
    GroupAdapter adapter;
    FirebaseUser user;
    ImageButton addGroupBtn;
    TextView avatarText;

    @Override
    protected void onDestroy() {
        //  TODo
        // release recources
        //...
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate layout variables
        avatarText = findViewById(R.id.avatarTextMain);
        addGroupBtn = findViewById(R.id.addGroup);

        //Get current user instance
        user = FirebaseAuth.getInstance().getCurrentUser();

        // If user instance exists , get user data
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String Uid = user.getUid();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();
            try {
                //Set user avatar image
                avatarText.setText(MessageAdapter.getUserNameInitials(name));
            } catch (Exception e) {
            }

            //Welcome user !
            Toast.makeText(getApplicationContext(), "Welcome " + name,
                    Toast.LENGTH_SHORT).show();


            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }


        //Set on click listener to user avatar . On click log user out!
        View imV = findViewById(R.id.avatarMain);
        imV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                finish();  //Kill the activity from which you will go to next activity
                startActivity(i);

            }
        });

        //Set on click listener to group button
        addGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On click ,shows an edittext inside an Alert Dialog
                //On submit , a new group is added to the Google Firebase
                final EditText txtUrl = new EditText(MainActivity.this);
                // Set the default text to a link of the Queen
                txtUrl.setHint("Group Name");
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add new Group")
                        //.setMessage("or paste an existing group Url")
                        .setView(txtUrl)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String groupName = txtUrl.getText().toString();
                                addGroup(groupName);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })
                        .show();
            }
        });


        // get the reference of RecyclerView
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        // data to populate the RecyclerView with


        // Get a reference to our posts
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mlem-fcbba-default-rtdb.firebaseio.com");
        DatabaseReference ref = database.getReference("Groups/");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final ArrayList<Group> groups = new ArrayList<>();
                //Toast.makeText(getApplicationContext(),dataSnapshot.toString() , Toast.LENGTH_SHORT).show();

                Iterator iterator = dataSnapshot.getChildren().iterator();

                while (iterator.hasNext()) {
                    DataSnapshot snap = (DataSnapshot) iterator.next();
                    Message lastMessage = new Message();
                    //TODO GTP XALIA
                    Group group = snap.getValue(Group.class);
                    Iterator iter = snap.child("Messages").getChildren().iterator();

                    while (iter.hasNext()) {
                        lastMessage = ((DataSnapshot) iter.next()).getValue(Message.class);
                    }

                    group.setLastMessage(lastMessage);
                    groups.add(group);

                    //TODO CHANGE TO GROUP NAME
                }


                // set up the RecyclerView
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                adapter = new GroupAdapter(getApplicationContext(), groups);
                recyclerView.setAdapter(adapter);

                adapter.setClickListener(new GroupAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Toast.makeText(getApplicationContext(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), ChatActivity.class);

                        i.putExtra("GROUP_ID", adapter.getItem(position).getGroupId());
                        i.putExtra("GROUP_NAME", adapter.getItem(position).getGroupName());
                        startActivity(i);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }

    public void addGroup(String groupName) {

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mlem-fcbba-default-rtdb.firebaseio.com");
        DatabaseReference myRef = database.getReference("Groups");


        //Group group = new Group(groupName, new HashMap<String,Message>());


        String gKey = myRef.push().getKey();

        myRef = database.getReference("Groups/" + gKey);

        myRef.setValue(new Group(gKey, groupName));


    }



    /*
        SearchView searchView = (SearchView) findViewById(R.id.searchView); // inititate a search view
        searchView.setQueryHint("Πού να στα φέρουμε?"); // set the hint text to display in the query text field

        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent i = new Intent(getApplicationContext(), ResultsActivity.class);
                startActivity(i);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

     */

}

