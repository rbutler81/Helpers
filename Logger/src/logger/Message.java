package logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Message<T> {
    private LinkedList<T> list;
    private ReentrantLock l;

    public Message() {
        list = new LinkedList<>();
        l = new ReentrantLock(true);
    }

    public T getNextMsg() {
        while (l.isLocked()){}
        l.lock();
        T m = list.removeFirst();
        l.unlock();
        return m;
    }

    public Message<T> addMsg(T s) {
        while (l.isLocked());
        l.lock();
        list.add(s);
        l.unlock();
        return this;
    }
    
    public boolean isEmpty() {
    	while (l.isLocked()){}
    	l.lock();
        boolean r = list.isEmpty();
    	l.unlock();
    	return r;
    }

    public void waitUntilNotifiedOrListNotEmpty() {
        synchronized (this) {
            while (isEmpty()) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
