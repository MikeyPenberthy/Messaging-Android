package com.example.michaelpenberthy.messaging_android;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessageActivity extends AppCompatActivity implements TextWatcher {

    private static final String TAG = "MessageController";

    String fakeUser1 = "IogQTXFINIWDwEVjNAc2YTtsnVk2";
    String fakeUser2 = "GI4zEcs4S9QTYbah4Qzql3rD0A52";
    String toUser = "";
    String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private List<Message> messages = new ArrayList<>();

    RecyclerView messagesRecyclerView;
    Button sendButton;
    EditText messageEditText;

    MessagesAdapter adapter;

    DatabaseReference myMessagesRef;
    DatabaseReference matchMesagesRef;

    ChildEventListener messagesListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        setupUI();
        setupMessagesRefs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        messages.clear();
        fetchMessages();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (messagesListener != null && myMessagesRef != null) {
            myMessagesRef.removeEventListener(messagesListener);
        }
    }


    // region Setup UI
    private void setupUI() {

        if (currentUser.equals(fakeUser1)) {
            toUser = fakeUser2;
        } else {
            toUser = fakeUser1;
        }

        adapter = new MessagesAdapter(this, messages);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        messagesRecyclerView = (RecyclerView) findViewById(R.id.messagesRecyclerView);
        messagesRecyclerView.setLayoutManager(layoutManager);
        messagesRecyclerView.setAdapter(adapter);
        messagesRecyclerView.setItemAnimator(new DefaultItemAnimator());

        messageEditText = (EditText) findViewById(R.id.messageEditText);
        messageEditText.addTextChangedListener(this);
        messageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                messagesRecyclerView.scrollToPosition(messages.size() - 1);
            }
        });

        sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setEnabled(false);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    // endregion

    // region Firebase
    private void setupMessagesRefs() {
        myMessagesRef = FirebaseDatabase.getInstance().getReference(currentUser)
                .child(String.format("matches/%s/messages", toUser));
        matchMesagesRef = FirebaseDatabase.getInstance().getReference(toUser)
                .child(String.format("matches/%s/messages", currentUser));
    }

    private void fetchMessages() {
        messagesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message m = dataSnapshot.getValue(Message.class);
                messages.add(m);

                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    messagesRecyclerView.scrollToPosition(messages.size() - 1);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        myMessagesRef.addChildEventListener(messagesListener);
    }
    // endregion

    // region Messages
    private void sendMessage() {
        Message message = new Message(currentUser, messageEditText.getText().toString());
        myMessagesRef.push().setValue(message);
        matchMesagesRef.push().setValue(message);
        messageEditText.setText(null);
        messagesRecyclerView.scrollToPosition(messages.size() - 1);
    }
    // endregion

    // region Text watcher
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (messageEditText.getText().length() > 0) {
            sendButton.setEnabled(true);
            sendButton.setTextColor(ContextCompat.getColor(this, R.color.colorBlueChatBubble));
        } else {
            sendButton.setEnabled(false);
            sendButton.setTextColor(ContextCompat.getColor(this, R.color.colorLightGray));
        }
    }
    // endregion


}