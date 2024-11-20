#include <stdio.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/stat.h>

int main() {
    // 1. Open a file
    int fd = open("output.txt", O_RDWR | O_CREAT, S_IRUSR | S_IWUSR);
    if (fd == -1) {
        perror("open");
        return 1;
    }

    // 2. Write to the file
    char *message = "Hello, System Calls!\n";
    write(fd, message, 20);

    // 3. Reposition file pointer to the beginning of the file
    lseek(fd, 0, SEEK_SET); // Move to the beginning of the file

    // 4. Read from the file
    char buffer[20]; // Buffer to hold the content being read
    int bytesRead = read(fd, buffer, sizeof(buffer)); // Read the contents
    if (bytesRead > 0) {
        buffer[bytesRead] = '\0'; // Null-terminate the string read from file
        printf("Read from file: %s", buffer);
    } else {
        printf("Read failed\n");
    }

    // 5. Close the file
    close(fd);

    return 0;
}
