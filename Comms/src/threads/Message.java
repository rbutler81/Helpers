package threads;

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
        list.clone();
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

    public List<T> removeAll() {
        List<T> r = new LinkedList<>();
        while (l.isLocked()){}
        l.lock();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            r.add(list.remove(0));
        }
        l.unlock();
        return r;
    }

    public void waitUntilNotifiedOrListNotEmpty() {
        waitUntilNotifiedOrListNotEmpty(0);
    }

    public void waitUntilNotifiedOrListNotEmpty(int timeout) {
        synchronized (this) {
            while (isEmpty()) {
                try {
                    this.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (timeout > 0) break;
            }
        }
    }

    public void addMsgAndNotify(T newMsg) {
        synchronized (this) {
            addMsg(newMsg);
            this.notify();
        }
    }

}
