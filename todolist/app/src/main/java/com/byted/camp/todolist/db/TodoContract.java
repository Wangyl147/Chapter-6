package com.byted.camp.todolist.db;

import android.provider.BaseColumns;

import com.byted.camp.todolist.beans.Note;

/**
 * Created on 2019/1/22.
 *
 * @author xuyingyi@bytedance.com (Yingyi Xu)
 */
public final class TodoContract {

    // TODO 定义表结构和 SQL 语句常量
    public static class NoteDBSchema implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_PRIORITY = "priority";
    }
    public static final String ON_CREATE_SQL = "create table "+NoteDBSchema.TABLE_NAME + " (" +
            NoteDBSchema._ID + " integer primary key," +
            NoteDBSchema.COLUMN_NAME_CONTENT + " text," +
            NoteDBSchema.COLUMN_NAME_TIME + " long)";
    public static final String ON_DELETE_SQL = "drop table if exists "+NoteDBSchema.TABLE_NAME;
    public static final String ON_UPDATE_SQL = "alter table " + NoteDBSchema.TABLE_NAME + " add column " + NoteDBSchema.COLUMN_NAME_PRIORITY + "integer";

    private TodoContract() {
    }

}
