package ru.javawebinar.basejava;

public class MainHW11Locks {
    static class DeadLock {
        public DeadLock() {
        }

        public synchronized void doSmoke(DeadLock lock) {
            try {
                Thread.sleep(10);  // efficiently rises the probability of the deadlock
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Call first: " + Thread.currentThread().getName());
            synchronized (lock) {
                System.out.println("Call final: " + Thread.currentThread().getName());
            }
        }
    }

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

    static class ResourceObject2 {
        private final String name;
        private LiveLock2 owner;

        ResourceObject2(String name) {
            this.name = name;
        }

        synchronized boolean setOwner(LiveLock2 locker) {
            if (locker == null || owner == null) {
                owner = null;
                return true;
            } else {
                return false;
            }
        }

        synchronized boolean isOwner(LiveLock2 locker) {
            return (owner == locker);
        }
    }

    static class LiveLock2 {
        private String name;
        private boolean isOnDutyToday;

        public LiveLock2(String name) {
            this.name = name;
            isOnDutyToday = true;
        }

        synchronized public boolean isOnDutyToday() {
            return isOnDutyToday;
        }

        public void clearDesk() {
            System.out.println(Thread.currentThread().getName() + ": I cleared the desk");
        }

        public void beginLesson(ResourceObject2 desk, LiveLock2 helper) {
            int counter = 0;
            while (isOnDutyToday) {
                counter++;
                if (!desk.isOwner(this)) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (helper.isOnDutyToday()) {
                    desk.setOwner(helper);
                    System.out.println(Thread.currentThread().getName() + ": Friends must help us! Try #" + counter);
                } else {
                    clearDesk();
                    isOnDutyToday = false;
                }
            }
        }
    }

    public static void makeDeadLock() {
        final DeadLock lockA = new DeadLock();
        final DeadLock lockB = new DeadLock();
        Thread thread0 = new Thread(() -> lockA.doSmoke(lockB));
        Thread thread1 = new Thread(() -> lockB.doSmoke(lockA));
        thread0.start();
        thread1.start();
    }

    public static void makeLiveLock() {
        ResourceObject resA = new ResourceObject("tomatos");
        ResourceObject resB = new ResourceObject("cucumbers");
        LiveLock locker1 = new LiveLock("locker1");
        LiveLock locker2 = new LiveLock("locker2");
        Thread thread0 = new Thread(() -> locker1.makeMove(resA, resB));
        Thread thread1 = new Thread(() -> locker2.makeMove(resB, resA));
        thread0.start();
        thread1.start();
    }

    public static void makeLiveLock2() {
        ResourceObject2 desk = new ResourceObject2("Andy");
        LiveLock2 locker1 = new LiveLock2("Andy");
        LiveLock2 locker2 = new LiveLock2("Bazz");
        LiveLock2 locker3 = new LiveLock2("Will");
        Thread thread0 = new Thread(() -> locker1.beginLesson(desk, locker2));
        Thread thread1 = new Thread(() -> locker2.beginLesson(desk, locker3));
        Thread thread2 = new Thread(() -> locker3.beginLesson(desk, locker1));
        thread0.start();
        thread1.start();
        thread2.start();
    }

    public static void main(String[] args) {
        makeLiveLock2();
        makeDeadLock();
        //makeLiveLock();
    }
}
