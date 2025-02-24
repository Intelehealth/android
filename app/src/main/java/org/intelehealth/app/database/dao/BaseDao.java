package org.intelehealth.app.database.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import org.intelehealth.app.app.IntelehealthApplication;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Vaghela Mithun R. on 24-02-2025 - 10:35.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class BaseDao<T> {
    abstract String tableName();
    public void executeWithBackgroundThread() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(this::task);
        System.out.println("BaseDao.execute");

//        new Thread(this::task).start();
    }

    @SafeVarargs
    final void bulkInsert(T... t) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            for (T t1 : t) {
//                insert(t1);
            }
        });
    }

    abstract void task();

    public void bulkInsertWithStatement(List<String> names) {
        SQLiteDatabase db = IntelehealthApplication.inteleHealthDatabaseHelper.getWriteDb();
        String sql = "INSERT INTO mytable (name) VALUES (?)";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        try {
            for (String name : names) {
                statement.clearBindings();
                statement.bindString(1, name);
                statement.execute();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }
}
