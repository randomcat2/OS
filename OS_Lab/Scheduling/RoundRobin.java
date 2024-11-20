package OS_Lab.Scheduling;

import java.util.*;

public class RoundRobin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        
        // Process details arrays
        int[] processId = new int[n];
        int[] arrivalTime = new int[n];
        int[] burstTime = new int[n];
        int[] completionTime = new int[n];
        int[] turnAroundTime = new int[n];
        int[] waitingTime = new int[n];
        int[] remainingTime = new int[n];
        
        // Input process details
        for(int i = 0; i < n; i++) {
            System.out.println("\nProcess " + (i + 1) + " details:");
            processId[i] = i + 1;
            System.out.print("Arrival Time: ");
            arrivalTime[i] = sc.nextInt();
            System.out.print("Burst Time: ");
            burstTime[i] = sc.nextInt();
            remainingTime[i] = burstTime[i];
        }
        
        System.out.print("\nEnter Time Quantum: ");
        int timeQuantum = sc.nextInt();
        
        // Ready Queue implementation using LinkedList
        Queue<Integer> readyQueue = new LinkedList<>();
        boolean[] inQueue = new boolean[n];
        boolean[] completed = new boolean[n];
        int currentTime = 0;
        int completedProcesses = 0;
        
        // Process scheduling
        while(completedProcesses < n) {
            // Check for newly arrived processes
            for(int i = 0; i < n; i++) {
                if(arrivalTime[i] <= currentTime && !completed[i] && !inQueue[i]) {
                    readyQueue.add(i);
                    inQueue[i] = true;
                }
            }
            
            // If ready queue is empty, increment time
            if(readyQueue.isEmpty()) {
                currentTime++;
                continue;
            }
            
            // Get next process from queue
            int currentProcess = readyQueue.poll();
            inQueue[currentProcess] = false;
            
            // Calculate execution time for current quantum
            int executeTime = Math.min(timeQuantum, remainingTime[currentProcess]);
            remainingTime[currentProcess] -= executeTime;
            currentTime += executeTime;
            
            // Check for newly arrived processes during this quantum
            for(int i = 0; i < n; i++) {
                if(arrivalTime[i] <= currentTime && !completed[i] && !inQueue[i] && i != currentProcess) {
                    readyQueue.add(i);
                    inQueue[i] = true;
                }
            }
            
            // If process is not completed, add back to queue
            if(remainingTime[currentProcess] > 0) {
                readyQueue.add(currentProcess);
                inQueue[currentProcess] = true;
            } else {
                // Process is completed
                completionTime[currentProcess] = currentTime;
                turnAroundTime[currentProcess] = completionTime[currentProcess] - arrivalTime[currentProcess];
                waitingTime[currentProcess] = turnAroundTime[currentProcess] - burstTime[currentProcess];
                completed[currentProcess] = true;
                completedProcesses++;
            }
        }
        
        // Print results
        System.out.println("\nRound Robin Scheduling Results (Time Quantum = " + timeQuantum + "):");
        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");
        
        float avgTAT = 0, avgWT = 0;
        
        for(int i = 0; i < n; i++) {
            System.out.printf("%d\t%d\t%d\t%d\t%d\t%d\n", 
                processId[i], arrivalTime[i], burstTime[i], 
                completionTime[i], turnAroundTime[i], waitingTime[i]);
            avgTAT += turnAroundTime[i];
            avgWT += waitingTime[i];
        }
        
        avgTAT /= n;
        avgWT /= n;
        
        System.out.printf("\nAverage Turnaround Time: %.2f", avgTAT);
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWT);
        
        // Print Gantt Chart
        System.out.println("\nGantt Chart:");
        System.out.print("|");
        
        // Reset variables for Gantt Chart creation
        currentTime = 0;
        completedProcesses = 0;
        readyQueue.clear();
        Arrays.fill(inQueue, false);
        Arrays.fill(completed, false);
        for(int i = 0; i < n; i++) {
            remainingTime[i] = burstTime[i];
        }
        
        while(completedProcesses < n) {
            for(int i = 0; i < n; i++) {
                if(arrivalTime[i] <= currentTime && !completed[i] && !inQueue[i]) {
                    readyQueue.add(i);
                    inQueue[i] = true;
                }
            }
            
            if(readyQueue.isEmpty()) {
                System.out.print(" IDLE |");
                currentTime++;
                continue;
            }
            
            int currentProcess = readyQueue.poll();
            inQueue[currentProcess] = false;
            
            int executeTime = Math.min(timeQuantum, remainingTime[currentProcess]);
            System.out.printf(" P%d |", processId[currentProcess]);
            remainingTime[currentProcess] -= executeTime;
            currentTime += executeTime;
            
            for(int i = 0; i < n; i++) {
                if(arrivalTime[i] <= currentTime && !completed[i] && !inQueue[i] && i != currentProcess) {
                    readyQueue.add(i);
                    inQueue[i] = true;
                }
            }
            
            if(remainingTime[currentProcess] > 0) {
                readyQueue.add(currentProcess);
                inQueue[currentProcess] = true;
            } else {
                completed[currentProcess] = true;
                completedProcesses++;
            }
        }
        
        System.out.println("\n");
        sc.close();
    }
} 
