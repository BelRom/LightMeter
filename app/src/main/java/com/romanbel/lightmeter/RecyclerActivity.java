package com.romanbel.lightmeter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerActivity extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private ArrayList<Line> list;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerDictionaryAdapter mAdapter;
    Button mBtnClearAll;
    ImageButton mBtnBack;
    TextView mTxtNotSaved;
    private final String mFileName = "file";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        loadArray();
        Intent i = getIntent();
        ArrayList<Line> listParcelable = i.getParcelableArrayListExtra("List");
        list.addAll(listParcelable);

        mTxtNotSaved = findViewById(R.id.txt_not_saved);
        mBtnBack = findViewById(R.id.btn_back);
        mBtnClearAll = findViewById(R.id.btn_clear_all);
        mBtnClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter.remuveAll();
                mTxtNotSaved.setVisibility(View.VISIBLE);
            }
        });
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerDictionaryAdapter(list, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    }

    private void loadArray() {

        FileInputStream fis;
        ObjectInputStream in;
        try {
            fis = new FileInputStream(this.getFilesDir() + "/" + mFileName);
            in = new ObjectInputStream(fis);
            list = (ArrayList<Line>) in.readObject();
            in.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (list == null){
            list = new ArrayList<Line>();
        }
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof RecyclerDictionaryAdapter.ViewHolder) {

            // remove the item from recycler view
            mAdapter.removeItem(viewHolder.getAdapterPosition());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveArray();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!list.isEmpty()){
            mTxtNotSaved.setVisibility(View.GONE);
        }
    }

    private void saveArray() {
        ObjectOutputStream oos;
        try
        {
            oos = new ObjectOutputStream(new FileOutputStream(
                    new File(this.getFilesDir() + "/" + mFileName)));
            oos.writeObject(list);
            oos.flush();
            oos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
