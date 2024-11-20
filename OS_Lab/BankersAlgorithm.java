package OS_Lab;

import java.util.Scanner;

public class BankersAlgorithm {
    private int processes;        // Number of processes
    private int resources;        // Number of resources
    private int[][] allocation;   // Currently allocated resources to each process
    private int[][] maxNeed;      // Maximum resources needed by each process
    private int[][] need;         // Remaining resource need of each process
    private int[] available;      // Available resources in the system
    private boolean[] finished;   // Track if process is finished in safety check

    public BankersAlgorithm(int processes, int resources) {
        this.processes = processes;
        this.resources = resources;
        this.allocation = new int[processes][resources];
        this.maxNeed = new int[processes][resources];
        this.need = new int[processes][resources];
        this.available = new int[resources];
        this.finished = new boolean[processes];
    }

    // Initialize the system state with example data
    public void initializeExample() {
        /* Example Input:
         * Number of Processes: 5
         * Number of Resources: 3
         * 
         * Allocation Matrix:
         * P0: 0 1 0
         * P1: 2 0 0
         * P2: 3 0 2
         * P3: 2 1 1
         * P4: 0 0 2
         * 
         * Max Need Matrix:
         * P0: 7 5 3
         * P1: 3 2 2
         * P2: 9 0 2
         * P3: 2 2 2
         * P4: 4 3 3
         * 
         * Available Resources: 3 3 2
         */
        
        // Initialize Allocation Matrix
        allocation = new int[][]{
            {0, 1, 0},
            {2, 0, 0},
            {3, 0, 2},
            {2, 1, 1},
            {0, 0, 2}
        };

        // Initialize Maximum Need Matrix
        maxNeed = new int[][]{
            {7, 5, 3},
            {3, 2, 2},
            {9, 0, 2},
            {2, 2, 2},
            {4, 3, 3}
        };

        // Initialize Available Resources
        available = new int[]{3, 3, 2};

        // Calculate Need Matrix
        calculateNeedMatrix();
    }

    // Take input from user
    public void inputSystemState() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the Allocation Matrix:");
        for (int i = 0; i < processes; i++) {
            System.out.println("For Process " + i + ":");
            for (int j = 0; j < resources; j++) {
                allocation[i][j] = scanner.nextInt();
            }
        }

        System.out.println("\nEnter the Maximum Need Matrix:");
        for (int i = 0; i < processes; i++) {
            System.out.println("For Process " + i + ":");
            for (int j = 0; j < resources; j++) {
                maxNeed[i][j] = scanner.nextInt();
            }
        }

        System.out.println("\nEnter the Available Resources:");
        for (int i = 0; i < resources; i++) {
            available[i] = scanner.nextInt();
        }

        calculateNeedMatrix();
    }

    // Calculate Need Matrix (Need = MaxNeed - Allocation)
    private void calculateNeedMatrix() {
        for (int i = 0; i < processes; i++) {
            for (int j = 0; j < resources; j++) {
                need[i][j] = maxNeed[i][j] - allocation[i][j];
            }
        }
    }

    // Check if the system is in safe state
    public boolean isSafeState() {
        boolean[] finished = new boolean[processes];
        int[] work = available.clone();
        int[] safeSequence = new int[processes];
        int count = 0;

        // Print initial state
        System.out.println("\nChecking for Safe State:");
        System.out.println("Initial Work (Available) Vector: " + arrayToString(work));

        while (count < processes) {
            boolean found = false;

            // Find an unfinished process whose needs can be satisfied
            for (int i = 0; i < processes; i++) {
                if (!finished[i] && canAllocate(i, work)) {
                    // Add this process to safe sequence
                    safeSequence[count] = i;
                    count++;
                    finished[i] = true;
                    found = true;

                    // Add allocated resources back to work
                    for (int j = 0; j < resources; j++) {
                        work[j] += allocation[i][j];
                    }

                    // Print step information
                    System.out.println("\nProcess P" + i + " executed");
                    System.out.println("Work vector after execution: " + arrayToString(work));
                    break;
                }
            }

            // If no process found that can be allocated resources
            if (!found) {
                System.out.println("\nSystem is NOT in safe state!");
                return false;
            }
        }

        // Print safe sequence
        System.out.println("\nSystem is in SAFE STATE!");
        System.out.print("Safe Sequence is: ");
        for (int i = 0; i < processes; i++) {
            System.out.print("P" + safeSequence[i]);
            if (i < processes - 1) {
                System.out.print(" → ");
            }
        }
        System.out.println();
        return true;
    }

    // Check if process can be allocated required resources
    private boolean canAllocate(int process, int[] work) {
        for (int i = 0; i < resources; i++) {
            if (need[process][i] > work[i]) {
                return false;
            }
        }
        return true;
    }

    // Print current system state
    public void printSystemState() {
        System.out.println("\nCurrent System State:");
        
        System.out.println("\nAllocation Matrix:");
        printMatrix(allocation);
        
        System.out.println("\nMaximum Need Matrix:");
        printMatrix(maxNeed);
        
        System.out.println("\nNeed Matrix:");
        printMatrix(need);
        
        System.out.println("\nAvailable Resources:");
        System.out.println(arrayToString(available));
    }

    // Helper method to print matrix
    private void printMatrix(int[][] matrix) {
        for (int i = 0; i < processes; i++) {
            System.out.print("P" + i + ": ");
            for (int j = 0; j < resources; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Helper method to convert array to string
    private String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder("[ ");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i]);
            if (i < array.length - 1) {
                sb.append(" ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Banker's Algorithm - Deadlock Avoidance");
        System.out.println("=====================================");
        
        System.out.println("\nChoose input method:");
        System.out.println("1. Use example data");
        System.out.println("2. Enter custom data");
        System.out.print("Enter choice (1 or 2): ");
        
        int choice = scanner.nextInt();
        
        // Create instance with 5 processes and 3 resources
        BankersAlgorithm banker = new BankersAlgorithm(5, 3);
        
        if (choice == 1) {
            banker.initializeExample();
        } else {
            banker.inputSystemState();
        }
        
        // Print the current state
        banker.printSystemState();
        
        // Check if system is in safe state
        banker.isSafeState();
    }
}