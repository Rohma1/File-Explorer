# File Manager

A Java Swing-based File Manager application that provides a simple graphical interface for performing common file and folder operations.

## Features

* Open files using the system's default application
* Copy files to another folder
* Rename files
* Delete files
* Open folders
* Delete folders
* Move folders
* List all files in a folder
* Sort files alphabetically using `TreeMap`
* Store file paths for quick lookup using `HashMap`

## Technologies Used

* **Java**
* **Java Swing** – Graphical User Interface
* **Java AWT** – Desktop and event handling
* **Java NIO** – File copy and move operations
* **TreeMap** – Maintains files in sorted order
* **HashMap** – Provides quick file-path lookup

## Data Structures

### TreeMap

A `TreeMap<String, File>` is used when listing files in a folder. It automatically maintains file names in sorted order.

### HashMap

A `HashMap<String, File>` stores files using their absolute paths as keys, allowing quick lookup and management of selected files.

## Main Operations

| Operation       | Description                                                |
| --------------- | ---------------------------------------------------------- |
| Open a file     | Opens a selected file using the default system application |
| Copy a file     | Copies a selected file to another directory                |
| Rename a file   | Changes the name of a selected file                        |
| Delete a file   | Deletes the selected file                                  |
| Open a folder   | Opens a folder using the system file explorer              |
| Delete a folder | Deletes a selected folder                                  |
| Move a folder   | Moves a folder to another directory                        |
| List files      | Displays files in alphabetical order                       |

## Project Structure

```text
FileManager/
└── FileExplorer.java
```

## How to Run

1. Install the Java Development Kit (JDK).
2. Create a Java project in IntelliJ IDEA, Eclipse, or another Java IDE.
3. Add `FileExplorer.java` to the project.
4. Compile and run the `FileExplorer` class.
5. The File Manager window will appear.

## How It Works

The application uses `JFileChooser` to allow the user to select files and folders. Each button is connected to an event handler that performs a specific operation.

For example:

* **Open File** → Selects a file and opens it using `Desktop.getDesktop().open()`.
* **Copy File** → Uses `Files.copy()` to copy the selected file.
* **Rename File** → Uses Java's `renameTo()` method.
* **Delete File** → Uses the `delete()` method.
* **Move Folder** → Uses `Files.move()` to move a folder.
* **List Files** → Reads the selected folder and stores its contents in a `TreeMap` for alphabetical ordering.

## Purpose

The project demonstrates how Java can be used to interact with the operating system's file system through a graphical user interface. It also demonstrates the practical use of Java collections such as `HashMap` and `TreeMap`.

ROHMA SARFARAZ
Software Engineering Student  
Sir Syed University of Engineering & Technology
