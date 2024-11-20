package OS_Lab.Scheduling;

import java.util.*;

class Process {
    int id;
    int arrivalTime;
    int burstTime;
    int remainingTime;
    int completionTime;
    int turnaroundTime;
    int waitingTime;
    
    Process(int id, int arrivalTime, int burstTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}

class SJF {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();

        List<Process> processes = new ArrayList<>();
        List<String> ganttChart = new ArrayList<>();
        
        // Input process details
        for (int i = 0; i < n; i++) {
            System.out.println("\nProcess " + (i + 1));
            System.out.print("Arrival Time: ");
            int at = sc.nextInt();
            System.out.print("Burst Time: ");
            int bt = sc.nextInt();
            
            if (bt <= 0) {
                System.out.println("Burst time must be greater than 0. Please enter again.");
                i--;
                continue;
            }
            
            processes.add(new Process(i + 1, at, bt));
        }

        System.out.print("\nChoose Scheduling Algorithm (1 for Non-Preemptive, 2 for Preemptive): ");
        int choice = sc.nextInt();

        switch (choice) {
            case 1:
                nonPreemptiveSJF(processes, ganttChart);
                break;
            case 2:
                preemptiveSJF(processes, ganttChart);
                break;
            default:
                System.out.println("Invalid choice. Please choose either 1 or 2.");
                sc.close();
                return;
        }

        // Print results
        printResults(processes, ganttChart, choice);
        sc.close();
    }

    private static void nonPreemptiveSJF(List<Process> processes, List<String> ganttChart) {
        int currentTime = 0;
        int completedProcesses = 0;
        boolean[] completed = new boolean[processes.size()];

        while (completedProcesses < processes.size()) {
            int shortestJob = -1;
            int shortestBurst = Integer.MAX_VALUE;

            // Find the process with shortest burst time among arrived processes
            for (int i = 0; i < processes.size(); i++) {
                Process p = processes.get(i);
                if (!completed[i] && p.arrivalTime <= currentTime && p.burstTime < shortestBurst) {
                    shortestJob = i;
                    shortestBurst = p.burstTime;
                }
            }

            if (shortestJob == -1) {
                // No process available, add idle time
                currentTime++;
                ganttChart.add("IDLE");
                continue;
            }

            // Execute the process
            Process selectedProcess = processes.get(shortestJob);
            for (int i = 0; i < selectedProcess.burstTime; i++) {
                ganttChart.add("P" + selectedProcess.id);
            }
            
            currentTime += selectedProcess.burstTime;
            selectedProcess.completionTime = currentTime;
            selectedProcess.turnaroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
            selectedProcess.waitingTime = selectedProcess.turnaroundTime - selectedProcess.burstTime;
            completed[shortestJob] = true;
            completedProcesses++;
        }
    }

    private static void preemptiveSJF(List<Process> processes, List<String> ganttChart) {
        int currentTime = 0;
        int completedProcesses = 0;
        boolean[] completed = new boolean[processes.size()];

        // Reset remaining time for all processes
        for (Process p : processes) {
            p.remainingTime = p.burstTime;
        }

        while (completedProcesses < processes.size()) {
            int shortestJob = -1;
            int shortestRemaining = Integer.MAX_VALUE;

            // Find process with shortest remaining time
            for (int i = 0; i < processes.size(); i++) {
                Process p = processes.get(i);
                if (!completed[i] && p.arrivalTime <= currentTime && p.remainingTime < shortestRemaining && p.remainingTime > 0) {
                    shortestJob = i;
                    shortestRemaining = p.remainingTime;
                }
            }

            if (shortestJob == -1) {
                // No process available
                currentTime++;
                ganttChart.add("IDLE");
                continue;
            }

            // Execute process for one time unit
            Process selectedProcess = processes.get(shortestJob);
            selectedProcess.remainingTime--;
            currentTime++;
            ganttChart.add("P" + selectedProcess.id);

            // Check if process is completed
            if (selectedProcess.remainingTime == 0) {
                selectedProcess.completionTime = currentTime;
                selectedProcess.turnaroundTime = selectedProcess.completionTime - selectedProcess.arrivalTime;
                selectedProcess.waitingTime = selectedProcess.turnaroundTime - selectedProcess.burstTime;
                completed[shortestJob] = true;
                completedProcesses++;
            }
        }
    }

    private static void printResults(List<Process> processes, List<String> ganttChart, int choice) {
        String algorithm = (choice == 1) ? "Non-Preemptive" : "Preemptive";
        System.out.println("\n" + algorithm + " SJF Results:");
        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT");
        
        double avgTurnaround = 0, avgWaiting = 0;
        for (Process p : processes) {
            System.out.printf("%d\t%d\t%d\t%d\t%d\t%d\n",
                p.id, p.arrivalTime, p.burstTime,
                p.completionTime, p.turnaroundTime, p.waitingTime);
            
            avgTurnaround += p.turnaroundTime;
            avgWaiting += p.waitingTime;
        }
        
        avgTurnaround /= processes.size();
        avgWaiting /= processes.size();
        
        System.out.printf("\nAverage Turnaround Time: %.2f", avgTurnaround);
        System.out.printf("\nAverage Waiting Time: %.2f\n", avgWaiting);

        // Print Gantt Chart
        System.out.println("\nGantt Chart:");
        System.out.print("|");
        String currentProcess = ganttChart.get(0);
        int count = 1;
        
        for (int i = 1; i < ganttChart.size(); i++) {
            if (ganttChart.get(i).equals(currentProcess)) {
                count++;
            } else {
                System.out.print(" " + currentProcess + "(" + count + ") |");
                currentProcess = ganttChart.get(i);
                count = 1;
            }
        }
        System.out.print(" " + currentProcess + "(" + count + ") |");
        System.out.println("\n");
    }
}