package ua.com.adr.android.myapplication;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ua.com.adr.android.myapplication.db.AppDatabase;
import ua.com.adr.android.myapplication.db.Hyperlinks;
import ua.com.adr.android.myapplication.db.HyperlinksDao;

public class HyperlinksViewModel extends AndroidViewModel {

    private HyperlinksDao mHyperlinksDao;
    private ExecutorService executorService;

    public HyperlinksViewModel(@NonNull Application application) {
        super(application);
        mHyperlinksDao = AppDatabase.getInstance(application).hyperlinksDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    LiveData<List<Hyperlinks>> getAllPosts() {
        return mHyperlinksDao.findAll();
    }

    LiveData<List<Hyperlinks>> getAllPostsByStatus() {
        return mHyperlinksDao.findAllByStatus();
    }

    LiveData<List<Hyperlinks>> getAllPostsByDate() {
        return mHyperlinksDao.findAllByDate();
    }

    void savePost(Hyperlinks link) {
        executorService.execute(() -> mHyperlinksDao.save(link));
    }

    void updatePost(Hyperlinks link) {
        executorService.execute(() -> mHyperlinksDao.update(link));
    }

    void deletePost(Hyperlinks link) {
        executorService.execute(() -> mHyperlinksDao.delete(link.getUrl()));
    }
}
