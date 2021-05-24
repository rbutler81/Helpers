package com.cimcorp.communications.threads;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Message<T> {
    private LinkedList<T> list;
    private ReentrantLock lock;

    public Message() {
        list = new LinkedList<>();
        lock = new ReentrantLock(true);
    }

    public T getNextMsg() {
        while (lock.isLocked()){}
        lock.lock();
        T m = list.removeFirst();
        list.clone();
        lock.unlock();
        return m;
    }

    public Message<T> addMsg(T s) {
        while (lock.isLocked());
        lock.lock();
        list.add(s);
        lock.unlock();
        return this;
    }
    
    public boolean isEmpty() {
    	while (lock.isLocked()){}
    	lock.lock();
        boolean r = list.isEmpty();
    	lock.unlock();
    	return r;
    }

    public int size() {
        while (lock.isLocked()){}
        lock.lock();
        int r = list.size();
        lock.unlock();
        return r;
    }

    public List<T> removeAll() {
        List<T> r = new LinkedList<>();
        while (lock.isLocked()){}
        lock.lock();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            r.add(list.remove(0));
        }
        lock.unlock();
        return r;
    }

    public Message<T> lock() {
        while (lock.isLocked()) {}
        lock.lock();
        return this;
    }

    public void unlock() {
        if (lock.isLocked()) {
            lock.unlock();
        }
    }

    public LinkedList<T> getListWithoutLocking() {
        return list;
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
