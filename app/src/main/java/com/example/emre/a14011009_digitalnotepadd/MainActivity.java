package com.example.emre.a14011009_digitalnotepadd;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> titles = new ArrayList<String>();

    public Context context;
    ImageButton createNoteButton;
    LinearLayout titleMenu;
    Button createButton, cancelButton;
    EditText titleET;


    public String allTitles=null;


    public RecyclerView recyclerView;
    public RecyclerView.Adapter mAdapter;
    public RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //titles.add("aab");
        GetSavedNotes();
        InitializeObjects();
        InitializeRecyclerView();

        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleMenu.setVisibility(View.VISIBLE);
            }
        });


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = 0;
                boolean isExist = false;
                while (i<titles.size()) {
                    if(titleET.getText().toString().equals(titles.get(i))){
                        isExist = true;
                    }
                    i++;
                }
                if(titleET.getText().toString().equals("") || titleET.getText().toString().equals(" ") || isExist) {
                    if (isExist){
                        Toast.makeText(getApplicationContext(),"There is same title",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Fill the blank",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    titles.add(titleET.getText().toString());
                    mAdapter.notifyDataSetChanged();
                    titleMenu.setVisibility(View.INVISIBLE);
                    titleET.setText("");
                }

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleMenu.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void GetSavedNotes(){
        SharedPreferences sp = getSharedPreferences("Titles",MODE_PRIVATE);
       // SharedPreferences.Editor e = sp.edit();
       // e.clear();
       // e.commit();
        //SharedPreferences sp2 = getSharedPreferences("Objects",MODE_PRIVATE);
       // SharedPreferences.Editor e2 = sp2.edit();
      //  e2.clear();
       // e2.commit();
        allTitles = sp.getString("title",null);
        if(allTitles != null){
            int i = 0, start, end;
            while (i < allTitles.length()){
                start = i;
                while (i < allTitles.length() && allTitles.charAt(i) != '|'){
                    i++;
                }
                end = i;
                i++;
                titles.add(allTitles.substring(start,end));
            }
            //titles.add(allTitles);
        }
    }

    public void InitializeObjects(){
        createNoteButton = findViewById(R.id.createNoteButton);
        titleMenu = findViewById(R.id.titleMenu);
        titleMenu.setVisibility(View.INVISIBLE);
        createButton = findViewById(R.id.createButton);
        cancelButton = findViewById(R.id.cancelButton);
        titleET = findViewById(R.id.titleET);
    }

    public void InitializeRecyclerView(){
        recyclerView = findViewById(R.id.my_recycler_view);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyAdapter(this,titles, this);
        //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), recyclerView, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                ShowNotesInput(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                titles.remove(position);
                mAdapter.notifyDataSetChanged();
                String a = "";

                int i = 0;
                while(i<titles.size()){
                    a = a + titles.get(i);
                    a = a+"|";
                    i++;
                }
                //Toast.makeText(getApplicationContext(),"Fill the blank "+position+a,Toast.LENGTH_SHORT).show();
                SharedPreferences sp = getSharedPreferences("Titles",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.putString("title",a);
                editor.commit();
            }
        }));
    }

    public void ShowNotesInput(int position){
        String a = "";

        int i = 0;
        while(i<titles.size()){
            a = a + titles.get(i);
            a = a+"|";
            i++;
        }
        //Toast.makeText(getApplicationContext(),"Fill the blank "+position+a,Toast.LENGTH_SHORT).show();
        SharedPreferences sp = getSharedPreferences("Titles",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.putString("title",a);
        editor.commit();
        Intent intent = new Intent(MainActivity.this, NotePage.class);
        intent.putExtra("noteID",titles.get(position));
        startActivity(intent);
    }
}
