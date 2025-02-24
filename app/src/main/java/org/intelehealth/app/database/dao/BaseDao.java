package org.intelehealth.app.database.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Build;

import org.intelehealth.app.app.IntelehealthApplication;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Vaghela Mithun R. on 24-02-2025 - 10:35.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class BaseDao {
    abstract String tableName();

    public void execute(Runnable task) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(task);
        System.out.println("BaseDao.execute");
//        new Thread(this::task).start();
    }

    public Runnable insert(HashMap<String, Object> row) {
        throwException();
        return () -> {
            if (row == null || row.isEmpty()) {
                return;
            }
            SQLiteDatabase db = IntelehealthApplication.inteleHealthDatabaseHelper.getWriteDb();
            String sql = buildInsertQuery(Objects.requireNonNull(row));
            SQLiteStatement statement = db.compileStatement(sql);
            try {
                db.beginTransaction();
                executeStatement(statement, row);
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
            db.close();
        };
    }

    public Runnable bulkInsert(List<HashMap<String, Object>> rows) {
        throwException();
        return () -> {
            if (rows == null || rows.isEmpty()) {
                return;
            }

            SQLiteDatabase db = IntelehealthApplication.inteleHealthDatabaseHelper.getWriteDb();
            String sql = buildInsertQuery(Objects.requireNonNull(rows.get(0)));
            SQLiteStatement statement = db.compileStatement(sql);
            try {
                db.beginTransaction();
                for (HashMap<String, Object> row : rows) {
                    executeStatement(statement, row);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
//            db.close();
        };
    }

    private void throwException() {
        if (tableName() == null || tableName().isEmpty()) {
            throw new RuntimeException("Table name is not defined");
        }
    }

    private void executeStatement(SQLiteStatement statement, HashMap<String, Object> row) {
        try {
            statement.clearBindings();
            int index = 1;
            for (String key : row.keySet()) {
                Object value = row.get(key);
                if (value instanceof String) {
                    statement.bindString(index, (String) value);
                } else if (value instanceof Integer) {
                    statement.bindLong(index, (Integer) value);
                } else if (value instanceof Long) {
                    statement.bindLong(index, (Long) value);
                } else if (value instanceof Double) {
                    statement.bindDouble(index, (Double) value);
                } else if (value instanceof byte[]) {
                    statement.bindBlob(index, (byte[]) value);
                } else {
                    statement.bindNull(index);
                }

                index++;
            }
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildInsertQuery(HashMap<String, Object> row) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (String key : row.keySet()) {
            columns.append(key).append(", ");
            values.append("?").append(", ");
        }
        columns.deleteCharAt(columns.length() - 2);
        values.deleteCharAt(values.length() - 2);
        return "INSERT INTO " + tableName() + " (" + columns + ") VALUES (" + values + ")";
    }
}
