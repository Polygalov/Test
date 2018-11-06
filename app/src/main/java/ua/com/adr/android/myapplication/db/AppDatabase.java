package ua.com.adr.android.myapplication.db;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

@Database(entities = {Hyperlinks.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    // Test history links
    private final static List<Hyperlinks> LINKS = Arrays.asList(
            new Hyperlinks("https://cs6.pikabu.ru/post_img/big/2015/06/18/3/1434596941_632146314.jpg",
                    3, System.currentTimeMillis()),
            new Hyperlinks("https://cs6.pikabu.ru/post_img/632146314.jpg",
                    2, System.currentTimeMillis()),
            new Hyperlinks("http://developer.alexanderklimov.ru/android/images/android_cat.jpg",
                                   1, System.currentTimeMillis())

    );

    public abstract HyperlinksDao hyperlinksDao();

    private static final Object sLock = new Object();

    public static AppDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, "Links.db")
                        .allowMainThreadQueries()
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                Executors.newSingleThreadExecutor().execute(
                                        () -> getInstance(context).hyperlinksDao().saveAll(LINKS));
                            }
                        })
                        .build();
            }
            return INSTANCE;
        }
    }


}