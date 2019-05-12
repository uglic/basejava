package ru.javawebinar.basejava;

public class HW11LiveLock2 {
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

    public static void main(String[] args) {
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
}
