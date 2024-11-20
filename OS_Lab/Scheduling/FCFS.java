package OS_Lab.Scheduling;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

class FCFS {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Input: number of processes
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        // Arrays to hold burst times, arrival times, finish times, turnaround times, and waiting times
        int[] bt = new int[n]; // Burst time
        int[] at = new int[n]; // Arrival time
        int[] ft = new int[n]; // Finish time
        int[] tat = new int[n]; // Turnaround time
        int[] wt = new int[n]; // Waiting time
        int[] originalIndex = new int[n]; // To store original indices of processes

        // Input: Arrival times
        System.out.println("Enter arrival times:");
        for (int i = 0; i < n; i++) {
            System.out.print("Process " + (i + 1) + " Arrival Time: ");
            at[i] = sc.nextInt();
            originalIndex[i] = i; // Store the original index of the process
        }

        // Input: Burst times
        System.out.println("Enter burst times:");
        for (int i = 0; i < n; i++) {
            System.out.print("Process " + (i + 1) + " Burst Time: ");
            bt[i] = sc.nextInt();
        }

        // Sort the processes based on arrival times using the original indices
        Integer[] indices = new Integer[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }

        // Sort the indices array based on the arrival time (at[])
        Arrays.sort(indices, Comparator.comparingInt(i -> at[i]));

        // Calculate finish times, turnaround times, and waiting times
        int currentTime = 0;
        for (int i = 0; i < n; i++) {
            int index = indices[i];
            // If the current time is less than the arrival time, wait for the process to arrive
            if (currentTime < at[index]) {
                currentTime = at[index];
            }
            currentTime += bt[index];
            ft[index] = currentTime;
            tat[index] = ft[index] - at[index]; // Turnaround time = Finish time - Arrival time
            wt[index] = tat[index] - bt[index]; // Waiting time = Turnaround time - Burst time
        }

        // Output the results sorted by arrival times
        System.out.println("\nProcess\tAT\tBT\tFT\tTAT\tWT");
        for (int i = 0; i < n; i++) {
            int index = indices[i]; // Use sorted index to display in the order of arrival times
            System.out.println((index + 1) + "\t" + at[index] + "\t" + bt[index] + "\t" + ft[index] + "\t" + tat[index] + "\t" + wt[index]);
        }

        sc.close();
    }
}
