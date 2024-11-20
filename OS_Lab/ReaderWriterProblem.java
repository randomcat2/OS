package OS_Lab;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReaderWriterProblem {
    private int readCount = 0; // To keep track of number of readers
    private Lock mutex = new ReentrantLock();  // Mutex for critical section
    private Lock writeLock = new ReentrantLock(); // Lock for writers
    
    // Reader Thread class
    class Reader implements Runnable {
        @Override
        public void run() {
            try {
                mutex.lock();  // Enter critical section
                readCount++;
                if (readCount == 1) {
                    writeLock.lock();  // If this is the first reader, lock writers
                }
                mutex.unlock();  // Exit critical section
                
                // Simulating reading operation
                System.out.println(Thread.currentThread().getName() + " is reading.");
                Thread.sleep(3000);  // Simulating time taken to read
                
                mutex.lock();  // Enter critical section to modify readCount
                readCount--;
                if (readCount == 0) {
                    writeLock.unlock();  // If this is the last reader, unlock the writer
                }
                mutex.unlock();  // Exit critical section
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Writer Thread class
    class Writer implements Runnable {
        @Override
        public void run() {
            try {
                writeLock.lock();  // Enter critical section to ensure exclusive access
                System.out.println(Thread.currentThread().getName() + " is writing.");
                Thread.sleep(2000);  // Simulating time taken to write
                writeLock.unlock();  // Release the lock once writing is done
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        ReaderWriterProblem rw = new ReaderWriterProblem();
        
        // Create and start reader threads
        Thread reader1 = new Thread(rw.new Reader(), "Reader 1");
        Thread reader2 = new Thread(rw.new Reader(), "Reader 2");
        Thread reader3 = new Thread(rw.new Reader(), "Reader 3");
        
        // Create and start writer threads
        Thread writer1 = new Thread(rw.new Writer(), "Writer 1");
        Thread writer2 = new Thread(rw.new Writer(), "Writer 2");
        
        // Start threads
        reader1.start();
        reader2.start();
        writer1.start();
        reader3.start();
        writer2.start();
    }
}
 
