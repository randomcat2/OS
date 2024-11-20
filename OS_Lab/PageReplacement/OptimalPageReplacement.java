package OS_Lab;

import java.util.*;

public class OptimalPageReplacement {
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
        for (int i = 0; i < referenceString.length; i++) {
            int page = referenceString[i];

            if (!memory.contains(page)) { // Page fault occurs
                System.out.println("Page " + page + " caused a page fault.");

                if (memory.size() == frames) { // Memory is full
                    // Find the page to replace
                    int pageToReplace = findOptimalPage(memory, referenceString, i + 1);
                    memory.remove((Integer) pageToReplace); // Remove it
                    System.out.println("Removed page " + pageToReplace + " (Optimal Replacement).");
                }

                // Add the current page to memory
                memory.add(page);
                pageFaults++;
            } else {
                System.out.println("Page " + page + " is already in memory (Page Hit).");
            }

            // Print the current state of memory
            System.out.println("Current memory: " + memory);
        }

        return pageFaults;
    }

    public static int findOptimalPage(List<Integer> memory, int[] referenceString, int startIndex) {
        Map<Integer, Integer> nextUse = new HashMap<>();

        // Find when each page in memory is next used
        for (int page : memory) {
            nextUse.put(page, Integer.MAX_VALUE); // Default to infinity (not used in future)

            for (int j = startIndex; j < referenceString.length; j++) {
                if (referenceString[j] == page) {
                    nextUse.put(page, j);
                    break;
                }
            }
        }

        // Find the page with the farthest next use or not used at all
        int pageToReplace = memory.get(0);
        int maxNextUse = nextUse.get(pageToReplace);

        for (int page : memory) {
            if (nextUse.get(page) > maxNextUse) {
                pageToReplace = page;
                maxNextUse = nextUse.get(page);
            }
        }

        return pageToReplace;
    }
}
