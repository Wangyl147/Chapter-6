package com.byted.camp.todolist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.byted.camp.todolist.beans.Note;
import com.byted.camp.todolist.db.TodoContract;
import com.byted.camp.todolist.db.TodoDbHelper;
import com.byted.camp.todolist.operation.activity.DatabaseActivity;
import com.byted.camp.todolist.operation.activity.DebugActivity;
import com.byted.camp.todolist.operation.activity.SettingActivity;
import com.byted.camp.todolist.ui.NoteListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1002;

    private RecyclerView recyclerView;
    private NoteListAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(
                        new Intent(MainActivity.this, NoteActivity.class),
                        REQUEST_CODE_ADD);
            }
        });

        recyclerView = findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        notesAdapter = new NoteListAdapter(new NoteOperator() {
            @Override
            public void deleteNote(Note note) {
                MainActivity.this.deleteNote(note);
            }

            @Override
            public void updateNote(Note note) {
                MainActivity.this.updateNode(note);
            }
        });
        recyclerView.setAdapter(notesAdapter);
        notesAdapter.notifyDataSetChanged();

        notesAdapter.refresh(loadNotesFromDatabase());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.action_debug:
                startActivity(new Intent(this, DebugActivity.class));
                return true;
            case R.id.action_database:
                startActivity(new Intent(this, DatabaseActivity.class));
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD
                && resultCode == Activity.RESULT_OK) {
            notesAdapter.refresh(loadNotesFromDatabase());
        }
    }

    private List<Note> loadNotesFromDatabase() {
        // TODO 从数据库中查询数据，并转换成 JavaBeans
        TodoDbHelper todoDbHelper = new TodoDbHelper(this);
        SQLiteDatabase db = todoDbHelper.getReadableDatabase();
        String [] projection = {
                //BaseColumns._ID,
                TodoContract.NoteDBSchema.COLUMN_NAME_CONTENT,
                TodoContract.NoteDBSchema.COLUMN_NAME_TIME,
                TodoContract.NoteDBSchema.COLUMN_NAME_PRIORITY
        };
        String sortOrder = TodoContract.NoteDBSchema.COLUMN_NAME_PRIORITY +" desc," + TodoContract.NoteDBSchema.COLUMN_NAME_TIME + " desc";
        Cursor cursor = db.query(
                TodoContract.NoteDBSchema.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        List<Note> noteList = new ArrayList<Note>();
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
//            Log.d("load",id+"");
            long time = cursor.getLong(cursor.getColumnIndex(TodoContract.NoteDBSchema.COLUMN_NAME_TIME));
            String content = cursor.getString(cursor.getColumnIndex(TodoContract.NoteDBSchema.COLUMN_NAME_CONTENT));
            int priority = cursor.getInt(cursor.getColumnIndex(TodoContract.NoteDBSchema.COLUMN_NAME_PRIORITY));
            Note note = new Note(0); //id有毒
            note.setDate(new Date(time));
            note.setContent(content);
            note.setPriority(priority);
            noteList.add(note);
        }
        return noteList;
    }

    private void deleteNote(Note note) {
        // TODO 删除数据
        TodoDbHelper todoDbHelper = new TodoDbHelper(this);
        SQLiteDatabase db = todoDbHelper.getWritableDatabase();
        String selection = TodoContract.NoteDBSchema.COLUMN_NAME_CONTENT + " like ? and time = ?";
        String[] selectionArgs = {note.getContent() , String.valueOf(note.getDate().getTime())};
        db.delete(TodoContract.NoteDBSchema.TABLE_NAME,selection,selectionArgs);
        notesAdapter.refresh(loadNotesFromDatabase());
    }

    private void updateNode(Note note) {
        // 更新数据
        notesAdapter.notifyDataSetChanged();
    }

}
