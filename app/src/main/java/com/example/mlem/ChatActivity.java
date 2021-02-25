package com.example.mlem;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    EditText editText;
    TextView groupNameV;
    TextView groupNameInits;
    FirebaseUser user;
    String groupId;
    String userName;
    String groupName;
    String GROUPPATH;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        editText = findViewById(R.id.editText);
        groupNameV = findViewById(R.id.groupName);
        groupNameInits = findViewById(R.id.groupNameInitsV);
        messageAdapter = new MessageAdapter(this);
        messagesView = findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                groupId = "xxx";
                groupName = "xxx";
            } else {
                groupId = extras.getString("GROUP_ID");
                groupName = extras.getString("GROUP_NAME");
            }
        } else {
            groupId = (String) savedInstanceState.getSerializable("GROUP_ID");
            groupName = (String) savedInstanceState.getSerializable("GROUP_NAME");
        }

        GROUPPATH = "Groups/" + groupId + "/Messages/";
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mlem-fcbba-default-rtdb.firebaseio.com");
        DatabaseReference myRef = database.getReference(GROUPPATH);
        user = FirebaseAuth.getInstance().getCurrentUser();
        Uid = user.getUid();
        userName = user.getDisplayName();

        groupNameV.setText(groupName);
        groupNameInits.setText(groupName.substring(0, 2).toUpperCase());
        final List<Message> msgs = new ArrayList<Message>();


        //Toast.makeText(getApplicationContext(), , Toast.LENGTH_SHORT).show();


        /*
        FirebaseListAdapter adapter = new FirebaseListAdapter<Message>(this, Message.class,
                R.layout.their_message, myRef) {
            @Override
            protected void populateView(View v, Message model, int position) {
                // Get references to the views of message.xml

                TextView messageText = (TextView)v.findViewById(R.id.message_body);
                if (model.getSender().equals("Kostas")){
                    messageText.setBackgroundColor(14);

                }
                // Set their text
                messageText.setText(model.getText());

            }
        };

        messagesView.setAdapter(adapter);
    /*





        Query q = myRef.orderByPriority();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while(iterator.hasNext()){

                    Message mes = (Message) ((DataSnapshot)iterator.next()).getValue(Message.class);

                    //String str = ((DataSnapshot)iterator.next()).get.getValue().toString();
                    messageAdapter.add(mes);
                    //TODO CHANGE TO GROUP NAME
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Message message = dataSnapshot.getValue(Message.class);
                //String str = (String) ((DataSnapshot)iterator.next()).getValue().toString();
                boolean self = true;
                //TODO CHANGE KOSTAS TO USER.GETID()
                //if (Uid.equals(message.getSender())){message.setBelongsToCurrentUser(true);}
                msgs.add(message);
                //notifyDataSetChanged(msgs);
                messageAdapter.add(message);


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }


        });


    }

    public void notifyDataSetChanged(List<Message> values) {
        for (Message obj : values) {
            messageAdapter.add(obj);
        }
    }


    public void sendMessage(View view) {

        String msg = editText.getText().toString();
        if (msg.length() > 0) {

            editText.getText().clear();


            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://mlem-fcbba-default-rtdb.firebaseio.com");
            DatabaseReference myRef = database.getReference(GROUPPATH);

            Date date = new Date(); // This object contains the current date value
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Message chatMessage = new Message(msg, user.getUid(), user.getDisplayName(), formatter.format(date));
            myRef.push().setValue(chatMessage);


        }


    }
}
