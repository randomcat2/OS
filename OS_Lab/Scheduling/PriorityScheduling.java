package OS_Lab.Scheduling;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PriorityScheduling {
    // Inner class to represent a Process
    static class Process {
        int pid;
        int arrivalTime;
        int burstTime;
        int completionTime;
        int turnaroundTime;
        int waitingTime;
        int priority;
        int remainingTime;
        
        Process(int pid, int at, int bt, int priority) {
            this.pid = pid;
            this.arrivalTime = at;
            this.burstTime = bt;
            this.priority = priority;
            this.remainingTime = bt;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        
        List<Process> processes = new ArrayList<>();
        List<Process> executionOrder = new ArrayList<>();
        
        // Input process details
        for(int i = 0; i < n; i++) {
            System.out.println("\nProcess " + (i + 1) + " details:");
            System.out.print("Arrival Time: ");
            int at = sc.nextInt();
            System.out.print("Burst Time: ");
            int bt = sc.nextInt();
            System.out.print("Priority (Lower number means higher priority): ");
            int priority = sc.nextInt();
            
            processes.add(new Process(i + 1, at, bt, priority));
        }
        
        System.out.print("\nChoose Algorithm (1 for Non-Preemptive, 2 for Preemptive): ");
        int choice = sc.nextInt();
        
        if(choice == 1) {
            // Non-Preemptive Priority Scheduling
            int currentTime = 0;
            int completed = 0;
            
            while(completed < n) {
                Process selectedProcess = null;
                int highestPriority = Integer.MAX_VALUE;
                
                // Find process with highest priority among arrived processes
                for(Process p : processes) {
                    if(p.remainingTime > 0 && p.arrivalTime <= currentTime && p.priority < highestPriority) {
                        highestPriority = p.priority;
                        selectedProcess = p;
                    }
                }
                
                if(selectedProcess == null) {
                    currentTime++;
                } else {
                    // Execute process completely
                    selectedProcess.completionTime = currentTime + selectedProcess.burstTime;
                    selectedProcess.turnaroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                    selectedProcess.waitingTime = selectedProcess.turnaroundTime - selectedProcess.burstTime;
                    selectedProcess.remainingTime = 0;
                    completed++;
                    currentTime = selectedProcess.completionTime;
                    executionOrder.add(selectedProcess);
                }
            }
            
            // Print results
            System.out.println("\nNon-Preemptive Priority Scheduling Results (In Execution Order):");
            printResults(executionOrder);
            
        } else if(choice == 2) {
            // Preemptive Priority Scheduling
            int currentTime = 0;
            int completed = 0;
            Process lastExecutedProcess = null;
            
            while(completed < n) {
                Process selectedProcess = null;
                int highestPriority = Integer.MAX_VALUE;
                
                // Find process with highest priority among arrived processes
                for(Process p : processes) {
                    if(p.remainingTime > 0 && p.arrivalTime <= currentTime && p.priority < highestPriority) {
                        highestPriority = p.priority;
                        selectedProcess = p;
                    }
                }
                
                if(selectedProcess == null) {
                    currentTime++;
                } else {
                    // If we switched to a different process, add the previous one to execution order
                    if(lastExecutedProcess != null && lastExecutedProcess != selectedProcess) {
                        executionOrder.add(lastExecutedProcess);
                    }
                    
                    selectedProcess.remainingTime--;
                    currentTime++;
                    
                    if(selectedProcess.remainingTime == 0) {
                        selectedProcess.completionTime = currentTime;
                        selectedProcess.turnaroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                        selectedProcess.waitingTime = selectedProcess.turnaroundTime - selectedProcess.burstTime;
                        completed++;
                        executionOrder.add(selectedProcess);
                    }
                    
                    lastExecutedProcess = selectedProcess;
                }
            }
            
            // Print results
            System.out.println("\nPreemptive Priority Scheduling Results (In Execution Order):");
            printResults(executionOrder);
        }
        
        sc.close();
    }
    
    static void printResults(List<Process> executionOrder) {
        System.out.println("\nProcess\tAT\tBT\tPriority\tCT\tTAT\tWT");
        
        float avg_tat = 0, avg_wt = 0;
        
        // Remove duplicates while maintaining order
        List<Process> uniqueProcesses = new ArrayList<>();
        for(Process p : executionOrder) {
            if(!uniqueProcesses.contains(p)) {
                uniqueProcesses.add(p);
            }
        }
        
        for(Process p : uniqueProcesses) {
            System.out.printf("%d\t%d\t%d\t%d\t\t%d\t%d\t%d\n", 
                p.pid, p.arrivalTime, p.burstTime, p.priority, 
                p.completionTime, p.turnaroundTime, p.waitingTime);
            avg_tat += p.turnaroundTime;
            avg_wt += p.waitingTime;
        }
        
        avg_tat /= uniqueProcesses.size();
        avg_wt /= uniqueProcesses.size();
        
        System.out.printf("\nAverage Turnaround Time: %.2f", avg_tat);
        System.out.printf("\nAverage Waiting Time: %.2f\n", avg_wt);
    }
}