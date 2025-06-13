# 🔐 Encrypted Distributed File System

A secure peer-to-peer file system built with Java that supports encrypted file transfer, multithreading, and client-server communication.

## Features
- AES 128-bit encryption for secure file transfers
- Concurrent access using multithreading
- CRUD operations: upload, download, update, delete
- Socket-based client-server architecture

## How to Run

### Prerequisites
- Java 8+
- JDK and JRE installed

### Steps
1. Compile all files:
```bash
javac src/*.java
```

2. Run server:
```bash
java -cp src Server
```

3. Run client:
```bash
java -cp src Client
```

You’ll be prompted to enter a command (UPLOAD, DOWNLOAD, UPDATE, DELETE) and file path.