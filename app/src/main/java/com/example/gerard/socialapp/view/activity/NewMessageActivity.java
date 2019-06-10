package com.example.gerard.socialapp.view.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gerard.socialapp.R;
import com.example.gerard.socialapp.model.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class NewMessageActivity extends AppCompatActivity {

    DatabaseReference mReference;
    FirebaseUser mUser;

    EditText mPostTextField;
    Button mPublishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        setTitle("New Message Activity");

        mReference = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mPostTextField = findViewById(R.id.postText);
        mPublishButton = findViewById(R.id.publish);

        mPublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitMessage();
            }
        });

    }

    void submitMessage(){
        final String postText = mPostTextField.getText().toString();

        if(postText.isEmpty()){
            mPostTextField.setError("Required");
            return;
        }

        mPublishButton.setEnabled(false);

        writeNewMessage(postText);
    }

    private void writeNewMessage(String messageText) {
        String messageKey = mReference.push().getKey();
        String postKey = getIntent().getExtras().getString("uid");

        Message message = new Message(mUser.getUid(), mUser.getDisplayName(), messageText);
        Map<String, Object> messageValues = message.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("message/data/" + messageKey, messageValues);
        childUpdates.put("message/post-message/"+ postKey + "/" + messageKey, true);

        Toast.makeText(this, "Message Sent",
                Toast.LENGTH_LONG).show();

        mReference.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
            }
        });
    }
}
