# Java Operating System Simulation

This project is a Java-based simulation of an operating system. The project includes various components such as process management, device handling, virtual memory management, and scheduling. The structure and classes provided are intended to simulate the behavior of an operating system, providing a learning platform for understanding OS concepts.

## Project Structure

The project contains the following key components:

### Core Components

- **Kernel.java**: The core of the operating system, managing system calls, process scheduling, and resource allocation.
- **OS.java**: The main class that initializes the operating system and starts the kernel.
- **Scheduler.java**: Implements the scheduling algorithms used by the operating system to manage processes.
- **PCB.java**: Represents a Process Control Block, holding information about each process.
- **UserlandProcess.java**: Represents user-level processes that run under the operating system.

### Device Management

- **Device.java**: Abstract class for devices managed by the OS.
- **RandomDevice.java**: A simulated device that generates random data.
- **Process_for_devicetesting.java**: Process for testing device interactions.
- **Test_process2_for_devices.java**: Another test process for device interactions.
- **Ping.java**: Simulated ping process for networking.
- **Pong.java**: Simulated pong process for networking.
- **background.java**: A background process simulation.
- **idle_process.java**: Simulates an idle process when the CPU has no other tasks to execute.

### Virtual Memory Management

- **VirtualToPhysicalMapping.java**: Manages virtual to physical memory address translations.
- **Virtual_Memory_TEST.java**: Test class for virtual memory management.
- **pagingprogram.java**: Simulated program to demonstrate paging.
- **pagingprogram_2.java**: Another simulated program to demonstrate paging.

### File System

- **FakeFileSystem.java**: A mock file system for testing file operations.
- **VFS.java**: Virtual File System layer that interfaces with different file systems.

### Process Testing and Messaging

- **Devices_test.java**: Test class for device interactions.
- **KernelMessages.java**: Handles messaging within the kernel.
- **Process_for_devicetesting.java**: Specific process class for device testing.
- **test_messages.java**: Test class for kernel messaging.

### Example Programs

- **HelloWorld.java**: A simple program that prints "Hello, World!".
- **GoodbyeWorld.java**: A simple program that prints "Goodbye, World!".

### Specialized Processes

- **Realtime.java**: Represents real-time processes with specific scheduling needs.
- **interactive.java**: Represents interactive processes that require frequent user interaction.
- **demotion_test.java**: Test class for process priority demotion.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- An IDE such as Eclipse or IntelliJ IDEA, or a text editor with Java support

### Cloning the Repository

1. **Clone the repository**:
   ```sh
   git clone <repository-url>
   cd <repository-directory>
   ```

2. **Open the project in your IDE**:
   - If you are using Eclipse:
     - Go to `File` > `Open Projects from File System...`
     - Click `Directory...` and select the cloned repository directory
     - Click `Finish`

### Running the Simulation

1. **Compile the project**:
   - In Eclipse, right-click on the project in the Project Explorer and select `Build Project`.
   - Alternatively, you can compile using the command line:
     ```sh
     javac src/*.java
     ```

2. **Run the main class**:
   - In Eclipse, right-click on `Main.java` and select `Run As` > `Java Application`.
   - Alternatively, you can run using the command line:
     ```sh
     java src/Main
     ```

### Running Tests

To run the test classes, compile and execute them individually. For example:
```sh
javac src/Devices_test.java
java src/Devices_test
```

## Contributing

Contributions are welcome! Please follow these steps to contribute:

1. **Fork the repository**
2. **Create a new branch**:
   ```sh
   git checkout -b feature-branch
   ```
3. **Commit your changes**:
   ```sh
   git commit -m "Add some feature"
   ```
4. **Push to the branch**:
   ```sh
   git push origin feature-branch
   ```
5. **Create a pull request**

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

---

This README provides an overview of the Java operating system simulation project, explaining the components, how to set up and run the project, and guidelines for contributing.
