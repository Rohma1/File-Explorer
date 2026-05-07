import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.*;


public class FileExplorer {
    private JFrame frame;
    private JFileChooser fileChooser;
    private JButton openFileButton, copyFileButton, renameFileButton, deleteFileButton;
    private JButton openFolderButton, deleteFolderButton, moveFolderButton, listFilesButton;

    // Using TreeMap for storing files in sorted order
    private TreeMap<String, File> fileMap;

    // Using HashMap for quick lookups of files by path
    private HashMap<String, File> filePathMap;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FileExplorer window = new FileExplorer();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public FileExplorer() {
        fileMap = new TreeMap<>(); // Initializes the TreeMap
        filePathMap = new HashMap<>(); // Initializes the HashMap
        initialize();
    }
    private void initialize() {
        frame = new JFrame("File Manager");
        frame.setBounds(100, 100, 300, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        fileChooser = new JFileChooser();

        openFileButton = new JButton("Open a file");
        openFileButton.setBounds(30, 50, 200, 30);
        openFileButton.addActionListener(this::openFile);
        frame.add(openFileButton);

        copyFileButton = new JButton("Copy a file");
        copyFileButton.setBounds(30, 90, 200, 30);
        copyFileButton.addActionListener(this::copyFile);
        frame.add(copyFileButton);

        renameFileButton = new JButton("Rename a file");
        renameFileButton.setBounds(30, 130, 200, 30);
        renameFileButton.addActionListener(this::renameFile);
        frame.add(renameFileButton);

        deleteFileButton = new JButton("Delete a file");
        deleteFileButton.setBounds(30, 170, 200, 30);
        deleteFileButton.addActionListener(this::deleteFile);
        frame.add(deleteFileButton);

        openFolderButton = new JButton("Open a folder");
        openFolderButton.setBounds(30, 210, 200, 30);
        openFolderButton.addActionListener(this::openFolder);
        frame.add(openFolderButton);

        deleteFolderButton = new JButton("Delete a folder");
        deleteFolderButton.setBounds(30, 250, 200, 30);
        deleteFolderButton.addActionListener(this::deleteFolder);
        frame.add(deleteFolderButton);

        moveFolderButton = new JButton("Move a folder");
        moveFolderButton.setBounds(30, 290, 200, 30);
        moveFolderButton.addActionListener(this::moveFolder);
        frame.add(moveFolderButton);

        listFilesButton = new JButton("List all files in a folder");
        listFilesButton.setBounds(30, 330, 200, 30);
        listFilesButton.addActionListener(this::listFilesInFolder);
        frame.add(listFilesButton);
    }
    private void openFile(ActionEvent e) {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            filePathMap.put(file.getAbsolutePath(), file); // Store in HashMap for quick lookup
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Unable to open the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void copyFile(ActionEvent e) {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File fileToCopy = fileChooser.getSelectedFile();
            filePathMap.put(fileToCopy.getAbsolutePath(), fileToCopy); // Add to map for future reference
            JFileChooser dirChooser = new JFileChooser();
            dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int dirReturnValue = dirChooser.showSaveDialog(frame);
            if (dirReturnValue == JFileChooser.APPROVE_OPTION) {
                File dirToCopyTo = dirChooser.getSelectedFile();
                try {
                    Files.copy(fileToCopy.toPath(), new File(dirToCopyTo, fileToCopy.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    JOptionPane.showMessageDialog(frame, "File copied successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Unable to copy the file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    private void renameFile(ActionEvent e) {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String newName = JOptionPane.showInputDialog(frame, "Enter the new name for the file:");
            if (newName != null && !newName.trim().isEmpty()) {
                File newFile = new File(file.getParent(), newName + getFileExtension(file));
                if (file.renameTo(newFile)) {
                    filePathMap.put(newFile.getAbsolutePath(), newFile); // Update the map with the new path
                    JOptionPane.showMessageDialog(frame, "File renamed successfully.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Unable to rename the file.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteFile(ActionEvent e) {
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            filePathMap.remove(file.getAbsolutePath()); // Remove from the map
            if (file.delete()) {
                JOptionPane.showMessageDialog(frame, "File deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Unable to delete the file.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openFolder(ActionEvent e) {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File folder = fileChooser.getSelectedFile();
            try {
                Desktop.getDesktop().open(folder);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "Unable to open the folder.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteFolder(ActionEvent e) {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File folder = fileChooser.getSelectedFile();
            if (folder.delete()) {
                JOptionPane.showMessageDialog(frame, "Folder deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(frame, "Unable to delete the folder.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void moveFolder(ActionEvent e) {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File folderToMove = fileChooser.getSelectedFile();
            JFileChooser destinationChooser = new JFileChooser();
            destinationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int dirReturnValue = destinationChooser.showOpenDialog(frame);
            if (dirReturnValue == JFileChooser.APPROVE_OPTION) {
                File destination = destinationChooser.getSelectedFile();
                try {
                    Files.move(folderToMove.toPath(), new File(destination, folderToMove.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    JOptionPane.showMessageDialog(frame, "Folder moved successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame, "Unable to move the folder.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void listFilesInFolder(ActionEvent e) {
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(frame);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File folder = fileChooser.getSelectedFile();
            File[] files = folder.listFiles();
            if (files != null) {
                // Add files to TreeMap to maintain sorted order
                fileMap.clear(); // Clear the map each time a folder is listed
                for (File file : files) {
                    fileMap.put(file.getName(), file);
                }

                StringBuilder fileList = new StringBuilder();
                for (Map.Entry<String, File> entry : fileMap.entrySet()) {
                    fileList.append(entry.getKey()).append("\n");
                }
                JOptionPane.showMessageDialog(frame, fileList.toString(), "Files in folder", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Unable to list files in the folder.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        return (lastIndex > 0) ? name.substring(lastIndex) : "";
    }
}