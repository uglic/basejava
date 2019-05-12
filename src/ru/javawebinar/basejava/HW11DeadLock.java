package ru.javawebinar.basejava;

public class HW11DeadLock {
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

    public static void main(String[] args) {
        final DeadLock lockA = new DeadLock();
        final DeadLock lockB = new DeadLock();
        Thread thread0 = new Thread(() -> lockA.doSmoke(lockB));
        Thread thread1 = new Thread(() -> lockB.doSmoke(lockA));
        thread0.start();
        thread1.start();
    }
}
