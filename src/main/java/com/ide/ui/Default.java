package com.ide.ui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import com.tree.Tree;

public class Default extends JFrame{
    //region UI Components
    private JPanel mainPanel;
    private JTextField path;
    private JEditorPane editor;
    private JButton save;
    private JButton open;
    private JTabbedPane tabbedPane;
    private JPanel editorPanel;
    private JPanel errorPanel;
    private JTextArea errorLogs;
    private JScrollPane scrollPane;
    private JScrollPane errorScroll;
    private JScrollPane treeScroll;
    private Tree tree = new Tree(new File("C:\\"));
    private JTree jTree = new JTree(tree);
    //endregion
    protected String currentPath;
    //Constructor
    public Default(){
        setContentPane(mainPanel);
        setTitle("ide");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        save.addActionListener(e->saveClicked());
        open.addActionListener(e->openClicked());
        treeScroll.setViewportView(jTree);
        jTree.setEditable(true);
    }

    public String getTime(){return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss:SSSS"));}
    public void writeErrorLog(String error,String exception){
        errorLogs.setText(errorLogs.getText()+getTime()+" | Error "+error+exception+"\n");
    }
    //Reading from file
    public boolean readFile(String file){
        try(BufferedReader reader = new BufferedReader(new FileReader(file))){
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){content.append(line+"\n");}
            editor.setText(content.toString());
            return true;
        } catch (IOException readException) {
            writeErrorLog("while reading from file: ",readException.getMessage());
            return false;

        }
    }
    public void openFileUI(){
        String fPath;
        try{
            FileDialog fOpen = new FileDialog((Frame) null, "Open File", FileDialog.LOAD);
            fOpen.setVisible(true);
            if(fOpen.getDirectory()==null || fOpen.getFile()==null)throw new IOException("Operation canceled");
            fPath = fOpen.getDirectory()+fOpen.getFile();
            path.setText(fPath);
            currentPath = fPath;
            setRootHandler(fOpen.getDirectory());
            readFile(fPath);
        }catch (IOException e){
            writeErrorLog("opening file: ",e.getMessage());
        }
    }
    //save button event listener
    public void saveClicked(){
        try(FileWriter writer = new FileWriter(path.getText())){
            if(path.getText()!=null)writer.write(editor.getText());
            else throw new IllegalArgumentException("File path is null");
        }catch(IllegalArgumentException | IOException e){
            try{
                FileDialog fSave = new FileDialog((java.awt.Frame) null, "Save File", FileDialog.SAVE);
                fSave.setVisible(true);
                if (fSave.getFile()==null || fSave.getDirectory()==null)throw new IOException("Operation canceled");
                else{
                    FileWriter writer = new FileWriter(fSave.getDirectory()+fSave.getFile());
                    writer.write(editor.getText());
                }
            }catch (IOException e2) {writeErrorLog("writing to file: ", e2.getMessage());}

        }
    }
    //open button listener
    public void openClicked(){
        try{
            if(path.getText()!=null && !path.getText().isEmpty()){
                try {
                    if (new File(path.getText()).isDirectory())setRootHandler(path.getText());
                    else if (!readFile(path.getText())||currentPath.equals(path.getText()))throw new IOException();
                    else{
                        setRootHandler(path.getText());
                        currentPath=path.getText();
                    }
                }catch (IOException e){
                    openFileUI();
                }
            }
            else throw new IllegalArgumentException("File path is null");
        }catch (IllegalArgumentException iA){
            openFileUI();
        }
    }
    public void setRootHandler(String file){
        try{
            tree.setRoot(new File(file));
        }catch (IOException e){
            writeErrorLog("opening directory: ",e.getMessage());
        }
    }
}
