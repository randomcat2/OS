#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

// Define matrices
int **matrixA, **matrixB, **resultMatrix;
int rowsA, colsA, rowsB, colsB;

// Structure to pass data to threads
typedef struct {
    int row;
    int col;
} ThreadData;

// Function to calculate one element of the result matrix
void* multiplyElement(void* arg) {
    ThreadData* data = (ThreadData*) arg;
    int row = data->row;
    int col = data->col;
    
    resultMatrix[row][col] = 0;
    for (int k = 0; k < colsA; k++) {
        resultMatrix[row][col] += matrixA[row][k] * matrixB[k][col];
    }
    free(data);  // Free allocated memory for thread data
    pthread_exit(0);
}

int main() {
    printf("Enter the number of rows for Matrix A: ");
    scanf("%d", &rowsA);
    printf("Enter the number of columns for Matrix A: ");
    scanf("%d", &colsA);
    
    printf("Enter the number of rows for Matrix B: ");
    scanf("%d", &rowsB);
    printf("Enter the number of columns for Matrix B: ");
    scanf("%d", &colsB);

    // Check if multiplication is possible
    if (colsA != rowsB) {
        printf("Matrix multiplication is not possible. The number of columns of Matrix A must equal the number of rows of Matrix B.\n");
        return 1;
    }

    // Allocate memory for matrices
    matrixA = malloc(rowsA * sizeof(int*));
    matrixB = malloc(rowsB * sizeof(int*));
    resultMatrix = malloc(rowsA * sizeof(int*));

    for (int i = 0; i < rowsA; i++) matrixA[i] = malloc(colsA * sizeof(int));
    for (int i = 0; i < rowsB; i++) matrixB[i] = malloc(colsB * sizeof(int));
    for (int i = 0; i < rowsA; i++) resultMatrix[i] = malloc(colsB * sizeof(int));

    // Input elements for Matrix A
    printf("Enter elements for Matrix A:\n");
    for (int i = 0; i < rowsA; i++) {
        for (int j = 0; j < colsA; j++) {
            scanf("%d", &matrixA[i][j]);
        }
    }

    // Input elements for Matrix B
    printf("Enter elements for Matrix B:\n");
    for (int i = 0; i < rowsB; i++) {
        for (int j = 0; j < colsB; j++) {
            scanf("%d", &matrixB[i][j]);
        }
    }

    // Create threads for matrix multiplication
    pthread_t threads[rowsA][colsB];
    for (int i = 0; i < rowsA; i++) {
        for (int j = 0; j < colsB; j++) {
            ThreadData* data = malloc(sizeof(ThreadData));
            data->row = i;
            data->col = j;
            pthread_create(&threads[i][j], NULL, multiplyElement, data);
        }
    }

    // Wait for all threads to complete
    for (int i = 0; i < rowsA; i++) {
        for (int j = 0; j < colsB; j++) {
            pthread_join(threads[i][j], NULL);
        }
    }

    // Print the result matrix
    printf("\nResult Matrix:\n");
    for (int i = 0; i < rowsA; i++) {
        for (int j = 0; j < colsB; j++) {
            printf("%d ", resultMatrix[i][j]);
        }
        printf("\n");
    }

    // Free allocated memory
    for (int i = 0; i < rowsA; i++) free(matrixA[i]);
    for (int i = 0; i < rowsB; i++) free(matrixB[i]);
    for (int i = 0; i < rowsA; i++) free(resultMatrix[i]);
    free(matrixA);
    free(matrixB);
    free(resultMatrix);

    return 0;
}