package OS_Lab;

import java.util.*;

public class LRUPageReplacement {
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
        List<Integer> memory = new ArrayList<>(); // To hold the pages in memory
        int pageFaults = 0;

        System.out.println("\nStep-by-step process:");
        for (int page : referenceString) {
            if (!memory.contains(page)) { // Page fault occurs
                System.out.println("Page " + page + " caused a page fault.");
                if (memory.size() == frames) { // Memory is full
                    // Remove the least recently used page (first in the list)
                    int lruPage = memory.remove(0);
                    System.out.println("Removed least recently used page: " + lruPage);
                }

                // Add the new page to memory
                memory.add(page);
                pageFaults++;
            } else {
                System.out.println("Page " + page + " is already in memory (Page Hit).");
                // Page hit: Update the usage of the page
                memory.remove((Integer) page); // Remove the page
                memory.add(page); // Reinsert it at the end (most recently used position)
            }

            // Print the current state of memory
            System.out.println("Current memory: " + memory);
        }

        return pageFaults;
    }
}
