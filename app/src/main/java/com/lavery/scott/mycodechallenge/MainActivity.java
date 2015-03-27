package com.lavery.scott.mycodechallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;

public class MainActivity extends Activity {

    private Button loginButton;

    //Dropbox Manager Variable
    private DbxAccountManager mDbxAcctMgr;
    private DbxAccount dbxAccount;

    //Create the Activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), "fqlqrw7yl0cvqdx", "07c9smeigx59zc9");
        dbxAccount = mDbxAcctMgr.getLinkedAccount();

        //Setup the view
        setupView();
    }

    private static final int REQUEST_LINK_TO_DBX = 0;

    //Dropbox Authentication
    void onClickLinkToDropbox(View view) {
        mDbxAcctMgr.startLink(this, REQUEST_LINK_TO_DBX);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LINK_TO_DBX) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent(this, Home.class);
                this.startActivity(intent);
                this.finish();
            } else {
                //Show Toast to try again after login fail or cancellation
                Toast.makeText(this, "Sign in failed, try again", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Method to setup view references from layout
    private void setupView() {
        if (dbxAccount == null) {
            loginButton = (Button) findViewById(R.id.loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickLinkToDropbox(v);
                }
            });
        } else if (dbxAccount.isLinked()) {
            Intent intent = new Intent(this, Home.class);
            this.startActivity(intent);
            this.finish();
        } else {
            loginButton = (Button) findViewById(R.id.loginButton);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickLinkToDropbox(v);
                }
            });

        }
    }
}
