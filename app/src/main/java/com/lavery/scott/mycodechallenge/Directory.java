package com.lavery.scott.mycodechallenge;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Directory extends ListActivity {



    //Dropbox Variables
    private DbxAccountManager mDbxAcctMgr;
    private DbxAccount dbxAccount;
    private DbxFileSystem dbxFileSystem;

    //ListView Variable
    private ListView photos;

    //Logger
    private Logger logger = Logger.getLogger(Directory.class.getName());

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        mDbxAcctMgr = DbxAccountManager.getInstance(getApplicationContext(), "fqlqrw7yl0cvqdx", "07c9smeigx59zc9");
        dbxAccount = mDbxAcctMgr.getLinkedAccount();

        setupView();
    }

    //onClick Listener to load photo from Dropbox
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Toast.makeText(this, position+1  + " was selected", Toast.LENGTH_SHORT).show();

        Object temp = getListAdapter().getItem(position);
        DbxFileInfo tempFile = (DbxFileInfo)temp;

        try {
            dbxFileSystem.open(tempFile.path);
        }
        catch(DbxException e) {
            logger.log(Level.WARNING, "COULD NOT OPEN FILE!");
        }

    }

    //Record Back Key Press to go back to Home Screen
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    //Setup the list of items in the directory on Dropbox
    private void setupView() {

        try {
            dbxFileSystem = DbxFileSystem.forAccount(dbxAccount);
            List<DbxFileInfo> list = dbxFileSystem.listFolder(new DbxPath(Home.dbxPath + Home.mediaFileDirectory.getPath()));
            Object[] listArray = list.toArray();
            ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(this, android.R.layout.simple_list_item_1,
                    listArray);
            setListAdapter(adapter);
        }

        catch(DbxException e) {
            logger.log(Level.WARNING, e.getMessage());
        }
    }
}