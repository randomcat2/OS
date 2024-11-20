package OS_Lab;
import java.util.Scanner;

public class MatrixMultiplication {
    private static int[][] matrixA;
    private static int[][] matrixB;
    private static int[][] resultMatrix;
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Get the size of Matrix A
        System.out.print("Enter the number of rows for Matrix A: ");
        int rowsA = sc.nextInt();
        System.out.print("Enter the number of columns for Matrix A: ");
        int colsA = sc.nextInt();
        
        // Get the size of Matrix B (colsA should be equal to rowsB)
        System.out.print("Enter the number of rows for Matrix B (should be equal to columns of Matrix A): ");
        int rowsB = sc.nextInt();
        System.out.print("Enter the number of columns for Matrix B: ");
        int colsB = sc.nextInt();
        
        // Check if multiplication is possible
        if (colsA != rowsB) {
            System.out.println("Matrix multiplication is not possible. The number of columns of Matrix A must equal the number of rows of Matrix B.");
            return;
        }
        
        // Initialize matrices
        matrixA = new int[rowsA][colsA];
        matrixB = new int[rowsB][colsB];
        resultMatrix = new int[rowsA][colsB];
        
        // Fill Matrix A with user input
        System.out.println("Enter elements for Matrix A:");
        fillMatrixFromUser(matrixA, sc);
        
        // Fill Matrix B with user input
        System.out.println("Enter elements for Matrix B:");
        fillMatrixFromUser(matrixB, sc);
        
        // Print Matrix A and Matrix B
        System.out.println("\nMatrix A:");
        printMatrix(matrixA);
        
        System.out.println("\nMatrix B:");
        printMatrix(matrixB);
        
        // Create and start threads for matrix multiplication
        Thread[][] threads = new Thread[rowsA][colsB];
        
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                // Each thread calculates one element of the result matrix
                threads[i][j] = new Thread(new MultiplyTask(i, j));
                threads[i][j].start();
            }
        }
        
        // Wait for all threads to finish
        try {
            for (int i = 0; i < rowsA; i++) {
                for (int j = 0; j < colsB; j++) {
                    threads[i][j].join();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Print the result matrix
        System.out.println("\nResult Matrix:");
        printMatrix(resultMatrix);
        
        sc.close();
    }
    
    // Method to take user input for matrix
    private static void fillMatrixFromUser(int[][] matrix, Scanner sc) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print("Enter element [" + i + "][" + j + "]: ");
                matrix[i][j] = sc.nextInt();
            }
        }
    }
    
    // Method to print matrix
    private static void printMatrix(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    // Task to calculate a single element in the result matrix
    static class MultiplyTask implements Runnable {
        private int i, j;
        
        public MultiplyTask(int i, int j) {
            this.i = i;
            this.j = j;
        }
        
        @Override
        public void run() {
            resultMatrix[i][j] = 0;
            for (int k = 0; k < matrixA[0].length; k++) {
                resultMatrix[i][j] += matrixA[i][k] * matrixB[k][j];
            }
        }
    }
}
