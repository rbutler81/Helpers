package logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Message<T> {
    private List<T> list;
    private ReentrantLock l;

    public Message() {
        list = new ArrayList<>();
        l = new ReentrantLock();
    }

    public T getNextMsg() {
        while (l.isLocked()){}
        l.lock();
        T m = list.get(0);
        list.remove(0);
        l.unlock();
        return m;
    }

    public Message<T> addMsg(T s) {
        MessageWorkerThread<T> mwt = new MessageWorkerThread<>(list, s, l);
        Thread t = new Thread(mwt);
        t.start();
        return this;
    }
    
    public boolean isEmpty() {
    	while (l.isLocked()){}
    	l.lock();
        boolean r = list.isEmpty();
    	l.unlock();
    	return r;
    }
}
