package com.ErCup.ui;

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
    private JTextArea editor;
    private JButton save;
    private JButton open;
    private JTabbedPane tabbedPane;
    private JPanel editorPanel;
    private JPanel errorPanel;
    private JTextArea errorLogs;
    private JScrollPane scrollPane;
    private JScrollPane errorScroll;
    private JScrollPane treeScroll;
    private JSplitPane treeSplit;
    private Tree tree = new Tree();
    private JTree jTree = new JTree(tree);
    //endregion
    protected String currentPath;
    //Constructor
    public Default(String[] args) {
        setContentPane(mainPanel);
        setTitle("ErCup");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon(getClass().getResource("/icon.png")).getImage());
        save.addActionListener(e -> saveClicked());
        open.addActionListener(e -> openClicked());
        jTree.addTreeSelectionListener(e -> selectionChanged());
        tabbedPane.addChangeListener(e -> {if(tabbedPane.getSelectedIndex()==1)tabbedPane.setBackgroundAt(1,null);});
        treeScroll.setViewportView(jTree);
        currentPath=tree.getRoot().toString();

        //checks if user has provided console argument that is a valid file path
        if (args.length > 0 && new File(args[0]).exists()) {
            path.setText(args[0]);
            openClicked();
        }
    }
    public String getTime(){return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss: SSSS"));}
    public void writeErrorLog(String error,String exception,boolean notify){
        errorLogs.setText(errorLogs.getText()+getTime()+" | Error "+error+exception+"\n");
        if(notify)tabbedPane.setBackgroundAt(1,new Color(195,63,65));
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
            writeErrorLog("while reading from file: ",readException.getMessage(),true);
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
            writeErrorLog("while opening file: ",e.getMessage(),false);
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
            }catch (IOException e2) {writeErrorLog("while writing to file: ", e2.getMessage(),true);}

        }
    }
    //open button listener
    public void openClicked(){
        try{
            if(path.getText()!=null && !path.getText().isEmpty()){
                try {
                    String pathText = path.getText();
                    File ptFile = new File(pathText);
                    File currentFile = new File(currentPath);

                    //changes working directory if path provided by user is a directory other than current directory or current file's parent directory
                    if(ptFile.isDirectory() && !ptFile.equals(currentFile) && (!currentFile.getParentFile().equals(ptFile) || (currentFile.getParentFile().isDirectory() && currentFile.isDirectory()))){
                        setRootHandler(pathText);
                        currentPath = pathText;
                    }
                    //reads file if path provided by user is a valid file & it isn't currently opened
                    else if (ptFile.isFile()&&( !ptFile.equals(currentFile))){
                        currentPath=pathText;
                        readFile(currentPath);
                        setRootHandler(pathText);
                    } else throw new IOException();
                }catch (IOException e){
                    openFileUI();
                }
                catch (NullPointerException npe){
                    writeErrorLog("while opening file (Null pointer exception)","",true);
                }
            }
            else throw new IllegalArgumentException("File path is null");
        }catch (IllegalArgumentException iA){
            openFileUI();
        }
    }
    public void setRootHandler(String fileP){
        try{
            File file = new File(fileP);
            file=(file.isFile())?file.getParentFile():file;
            tree.setRoot(file);
        }catch (IOException e){
            writeErrorLog("while opening directory: ",e.getMessage(),true);
        }
    }
    //file tree select listener
    public void selectionChanged() {
            try {
                File file = (File) jTree.getLastSelectedPathComponent();
                if(file ==  tree.getRoot() && file.getParentFile()!=null)tree.setRoot(file.getParentFile());
                else if (file==null)throw new IOException();
                else if(file.isFile()){
                    readFile(file.getPath());
                    path.setText(file.getPath());
                    currentPath = file.getPath();
                }
            }catch (IOException e){
                writeErrorLog("while handling selection change: ",e.getMessage(),false);
            }
    }
}