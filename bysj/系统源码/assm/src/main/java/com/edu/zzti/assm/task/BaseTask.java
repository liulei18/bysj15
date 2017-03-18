package com.edu.zzti.assm.task;

        import com.edu.zzti.assm.util.ViewParams;

/**
 * Created by feng on 2015/2/12.
 */
public abstract class BaseTask {
    private ViewParams params = null;
    protected AsyncTask asyncTask = null;

    public BaseTask(ViewParams params){
        this. params= params;
    }

    public void startTask(){
        asyncTask.start();
    }
}
