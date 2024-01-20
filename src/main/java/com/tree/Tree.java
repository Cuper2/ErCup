package com.tree;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Tree implements TreeModel {

    //region definitions
    private ArrayList listeners = new ArrayList();
    private File root;
    private class TreeFile extends File{
        public TreeFile(File parent, String child){
            super(parent,child);
        }
        public String toString(){
            return getName();
        }
    }
    private JTree jTree;
    //endregion
    public Tree(){
        File root = File.listRoots()[0];
        this.root = root;
    }
    public void setRoot(File rootDir)throws IOException{
        if (!rootDir.isDirectory())throw new IOException("Chosen object is not a directory");
        root = rootDir;
        TreeModelEvent event = new TreeModelEvent(this,new TreePath(root), null, null);
        Iterator<TreeModelListener> iterator = listeners.iterator();
        while (iterator.hasNext()){
            iterator.next().treeStructureChanged(event);
        }
    }
    //region interface method implementations
    @Override
    public Object getRoot() {return root;}
    @Override
    public Object getChild(Object parent, int index) {
        File file = (File) parent;
        String[] child = file.list();
        return new TreeFile(file, child[index]);
    }
    @Override
    public int getChildCount(Object parent) {
        File file = (File) parent;
        if(file.isDirectory()){
            String[] fileList = file.list();
            if (fileList!=null){
                return file.list().length;
            }
        }
        return 0;
    }
    @Override
    public boolean isLeaf(Object node) {
        File file = (File) node;
        return file.isFile();
    }
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        File oldFile = (File) path.getLastPathComponent();
        String fileParentPath = oldFile.getParent();
        String newFileName = (String) newValue;
        File targetFile = new File(fileParentPath, newFileName);
        oldFile.renameTo(targetFile);
        File parent = new File(fileParentPath);
        int[] changedChildrenIndices = { getIndexOfChild(parent, targetFile) };
        Object[] changedChildren = { targetFile };
        fireTreeNodesChanged(path.getParentPath(), changedChildrenIndices, changedChildren);
    }
    private void fireTreeNodesChanged(TreePath parentPath, int[] indices, Object[] children) {
        TreeModelEvent event = new TreeModelEvent(this, parentPath, indices, children);
        Iterator iterator = listeners.iterator();
        TreeModelListener listener = null;
        while (iterator.hasNext()) {
            listener = (TreeModelListener) iterator.next();
            listener.treeNodesChanged(event);
        }
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        File directory = (File) parent;
        File file = (File) child;
        String[] children = directory.list();
        for (int i = 0; i < children.length; i++) {
            if (file.getName().equals(children[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {listeners.add(l);}
    @Override
    public void removeTreeModelListener(TreeModelListener l) {listeners.remove(l);}
    //endregion
}