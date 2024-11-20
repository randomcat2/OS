package OS_Lab;

import java.util.*;

public class FIFOPageReplacement {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Input the reference string
        System.out.println("Enter the number of pages in the reference string:");
        int n = scanner.nextInt();
        int[] referenceString = new int[n];
        System.out.println("Enter the reference string (space-separated):");
        for (int i = 0; i < n; i++) {
            referenceString[i] = scanner.nextInt();
        }

        // Input the number of frames
        System.out.println("Enter the number of frames:");
        int frames = scanner.nextInt();

        // Call the function to calculate page faults
        int pageFaults = calculatePageFaults(referenceString, frames);

        // Display the result
        System.out.println("\nTotal number of page faults: " + pageFaults);
    }

    public static int calculatePageFaults(int[] referenceString, int frames) {
        Set<Integer> memory = new HashSet<>(); // To hold the pages in memory
        Queue<Integer> fifoQueue = new LinkedList<>(); // To implement FIFO
        int pageFaults = 0;

        System.out.println("\nStep-by-step process:");
        for (int page : referenceString) {
            if (!memory.contains(page)) { // Page fault occurs
                System.out.println("Page " + page + " caused a page fault.");
                if (memory.size() == frames) { // Memory is full
                    // Remove the oldest page from the FIFO queue
                    int oldestPage = fifoQueue.poll();
                    memory.remove(oldestPage);
                    System.out.println("Removed page " + oldestPage + " from memory.");
                }

                // Add the new page to memory and the queue
                memory.add(page);
                fifoQueue.add(page);
                pageFaults++;
            } else {
                System.out.println("Page " + page + " is already in memory (Page Hit).");
            }

            // Print the current state of memory
            System.out.println("Current memory: " + memory);
        }

        return pageFaults;
    }
}
