# SlottedPageStorageManager

SlottedPageStorageManager is a Java project that provides functionalities for managing storage space using the slotted page format with buffering. It includes classes and interfaces for creating and manipulating slotted pages within a file-based storage system.

## Overview

The project consists of the following components:

- **BufferedFileManager**: A class that extends `FileManager` and implements buffering for efficient storage management using the slotted page format.

- **FileManager**: An interface defining methods for managing a storage space, such as adding, putting, getting, removing, iterating over objects, and clearing data from a file.

- **SlottedPageFile**: A class representing a file consisting of slotted pages. It provides methods for accessing and manipulating slotted pages within the file, including saving and retrieving objects.

- **SlottedPage**: A class representing a slotted page, which can store objects of possibly different sizes in a byte array. It provides methods for adding, getting, putting, removing, and iterating over objects stored in the page.

- **StorageManager**: An interface defining methods for managing storage space. It includes methods for adding, putting, getting, removing, iterating over objects, clearing data from a file, and handling invalid locations.

## Usage

To use the SlottedPageStorageManager in your Java project, follow these steps:

1. Include the necessary Java files (`BufferedFileManager.java`, `FileManager.java`, `SlottedPageFile.java`, `SlottedPage.java`, `StorageManager.java`) in your project directory.

2. Import the required classes/interfaces into your Java files as needed.

3. Create an instance of `BufferedFileManager` or implement your own storage manager using the provided interfaces (`FileManager`, `StorageManager`).

4. Use the methods provided by the storage manager to manage storage space, add, retrieve, update, or remove objects from slotted pages.

## Example

```java
// Example usage of SlottedPageStorageManager

// Create a BufferedFileManager instance with slotted page size and buffer size
BufferedFileManager manager = new BufferedFileManager(slottedPageSize, bufferSize);

// Add objects to the storage space
int fileID = 1;
Object object = new Object();
manager.add(fileID, object);

// Retrieve an object from the storage space
Object retrievedObject = manager.get(fileID, location);

// Update an object in the storage space
Object updatedObject = new Object();
manager.put(fileID, location, updatedObject);

// Remove an object from the storage space
Object removedObject = manager.remove(fileID, location);

// Iterate over objects stored in the file
Iterator<Object> iterator = manager.iterator(fileID);
while (iterator.hasNext()) {
    Object obj = iterator.next();
    // Process each object
}

// Clear data from a file
manager.clear(fileID);
