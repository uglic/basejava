package ru.javawebinar.basejava;

public class HW11LiveLock1 {
    static class ResourceObject {
        private final String name;
        private LiveLock owner;

        ResourceObject(String name) {
            this.name = name;
        }

        synchronized boolean setOwner(LiveLock locker) {
            if (locker == null || owner == null) {
                owner = null;
                return true;
            } else {
                return false;
            }
        }

        synchronized boolean isOwner(LiveLock locker) {
            return (owner == locker);
        }
    }

    static class LiveLock {
        private String name;

        public LiveLock(String name) {
            this.name = name;
        }

        public synchronized boolean makeMove(ResourceObject resource1, ResourceObject resource2) {
            boolean ready = false;
            int counter = 0;
            while (!ready) {
                counter++;
                if (!resource1.isOwner(this)) {
                    resource1.setOwner(this);
                    if (resource1.isOwner(this)) {
                        System.out.println(Thread.currentThread().getName() + ": owns res1");
                    }
                }
                try {
                    Thread.sleep(1); //+ (int) (3 * Math.random())
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!resource2.isOwner(this)) {
                    if (!resource2.setOwner(this)) {
                        resource1.setOwner(null);
                    } else {
                        System.out.println(Thread.currentThread().getName() + ": owns res2");
                    }
                }
                if (resource1.isOwner(this) && resource2.isOwner(this)) {
                    ready = true;
                    System.out.println(Thread.currentThread().getName() + ": move is made");
                    resource1.setOwner(null);
                    resource2.setOwner(null);
                } else {
                    System.out.println(Thread.currentThread().getName() + ": not succeeded");
                    resource1.setOwner(null);
                    resource2.setOwner(null);
                }
            }
            System.out.println(Thread.currentThread().getName() + ": counter=" + counter);
            return true;
        }
    }

    public static void main(String[] args) {
        ResourceObject resA = new ResourceObject("tomatos");
        ResourceObject resB = new ResourceObject("cucumbers");
        LiveLock locker1 = new LiveLock("locker1");
        LiveLock locker2 = new LiveLock("locker2");
        Thread thread0 = new Thread(() -> locker1.makeMove(resA, resB));
        Thread thread1 = new Thread(() -> locker2.makeMove(resB, resA));
        thread0.start();
        thread1.start();
    }
}
