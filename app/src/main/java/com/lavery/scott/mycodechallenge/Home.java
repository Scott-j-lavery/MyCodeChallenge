package com.lavery.scott.mycodechallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Home extends Activity {


    //Dropbox Variables
    private DbxAccount dbxAccount;
    private DbxAccountManager dbxAccountManager;
    private DbxFileSystem dbxFileSystem;
    public static DbxPath dbxPath;
    public static File mediaFileDirectory;

    //Button Variables
    private Button photoButton;
    private Button directoryButton;

    //Intent Variable to switch between Activities
    private Intent intent;

    //logger
    private Logger logger = Logger.getLogger(Home.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dbxAccountManager = DbxAccountManager.getInstance(getApplicationContext(), "fqlqrw7yl0cvqdx", "07c9smeigx59zc9");
        dbxAccount = dbxAccountManager.getLinkedAccount();

        //Setup the view
        setupView();
    }

    private void setupView() {
        if(dbxAccount.isLinked()) {
            photoButton = (Button)findViewById(R.id.photoButton);
            directoryButton = (Button)findViewById(R.id.directoryButton);
            createDirectory();
            createLocalDirectory();

            //Redirect depending on button click
            photoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(Home.this, Camera.class);
                    Home.this.startActivity(intent);
                    Home.this.finish();
                }
            });

            directoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(Home.this, Directory.class);
                    Home.this.startActivity(intent);
                    Home.this.finish();
                }
            });
        }

        else {
            //If User didn't log in, return to Main Activity
            intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            this.finish();
        }
    }

    //Create the directory on Dropbox if it hasn't already been created
    private void createDirectory() {
        try {
            dbxFileSystem = DbxFileSystem.forAccount(dbxAccount);
            dbxPath = new DbxPath("My Code Challenge");
            // Creates directory if one doesn't already exist
            if (!dbxFileSystem.exists(dbxPath)) {
                dbxFileSystem.createFolder(
                        dbxPath);
            }
        }
        catch (DbxException e) {
            logger.log(Level.WARNING, "UNABLE TO CREATE DROPBOX DIRECTORY");
        }
    }

    //Create local directory for image files
    private void createLocalDirectory() {
        mediaFileDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyCodeChallenge");

        if(!mediaFileDirectory.exists()) {
            if(!mediaFileDirectory.mkdirs()) {
                logger.log(Level.WARNING, "DIRECTORY CREATION FAILED");
                return;
            }
        }
    }
}
