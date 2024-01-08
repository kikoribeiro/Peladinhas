package com.example.peladinhas;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GroupsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ArrayAdapter<String> groupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ListView groupListView = findViewById(R.id.groupListView);
        groupAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        groupListView.setAdapter(groupAdapter);

        loadGroups();

        EditText groupNameEditText = findViewById(R.id.groupNameEditText);
        Button createGroupButton = findViewById(R.id.createGroupButton);
        Button addUsersButton = findViewById(R.id.addUsersButton);

        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = groupNameEditText.getText().toString().trim();
                if (!groupName.isEmpty()) {
                    createGroup(groupName);
                }
            }
        });

        addUsersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add logic to navigate to user selection screen or implement user addition
            }
        });
    }

    private void loadGroups() {
        db.collection("groups")
                .whereArrayContains("members", Objects.requireNonNull(mAuth.getCurrentUser()).getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            groupAdapter.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String groupName = document.getString("name");
                                groupAdapter.add(groupName);
                            }
                        }
                    }
                });
    }

    private void createGroup(String groupName) {
        Map<String, Object> groupData = new HashMap<>();
        groupData.put("name", groupName);

        db.collection("groups")
                .add(groupData)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            // Group creation successful
                            loadGroups(); // Refresh the list of groups
                        } else {
                            // Handle error
                        }
                    }
                });
    }
}
