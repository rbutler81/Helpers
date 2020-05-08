package logger;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class MessageWorkerThread<T> implements Runnable {

    private List<T> list;
    private T s;
    private ReentrantLock l;

    public MessageWorkerThread(List<T> list, T s, ReentrantLock l) {
        this.list = list;
        this.s = s;
        this.l = l;
    }

    @Override
    public void run() {

        while (l.isLocked()){}
        l.lock();
        list.add(s);
        l.unlock();
    }
}
