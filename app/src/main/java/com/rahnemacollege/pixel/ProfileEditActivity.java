package com.rahnemacollege.pixel;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;

public class ProfileEditActivity extends AppCompatActivity {

    TagContainerLayout tags;
    EditText newTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        getSupportActionBar().setTitle(R.string.profile_edit_title);

        tags = findViewById(R.id.profile_edit_tags);
        newTag = findViewById(R.id.profile_edit_new_tag);

        // TODO Get profile header image name bio and tags from server.
    }

    public void addTag(View view) {
        tags.addTag(newTag.getText().toString());
        newTag.setText(null);
    }

    public void update(View view) {
        // TODO send information to server.
        finish();
    }
}
