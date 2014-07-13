package cnblogs.jcli.irstudy.store;

import cnblogs.jcli.irstudy.utils.NumberUtils;

import java.util.*;

public class BTree {

    private Storage storage;

    private Node root;

    public BTree(Storage storage) {
        this.storage = storage;
        byte[] b = new byte[4];
        storage.seek(0);
        int len = storage.read(b);
        if (len == 4) {
            int rootPointer = NumberUtils.byteArrayToInt(b);
            root = StorageUtils.loadNode(storage, rootPointer);
        }
        if (root == null) {//init a new b_tree
            storage.seek(0);
            storage.write(NumberUtils.intToByteArray(4));
        }
    }

    public void insert(DataItem item) {
        System.out.println("begin insert : " + item.getKey());
        Node insertedLeaf = searchInsertedLeaf1(root, item);
        if (insertedLeaf == null) {//the tree is empty,create root node
            root = StorageUtils.allocateNewNode(storage);
            root.addDataItem(item);
            StorageUtils.flushToStorage(root, storage);
        } else {
            insertedLeaf.addDataItem(item);
            StorageUtils.flushToStorage(insertedLeaf, storage);
//			}
        }
        System.out.println("insert end : " + item.getKey());
    }

    private void splitUp(Node node) {
        if (node == null || node.getCount() < Node.DATAITEMS_COUNT) {//if null or not be full
            return;
        }
        if (node.getParent() == null) {//split up the root node
            //create the new root node
            Node newRoot = StorageUtils.allocateNewNode(storage);
            //reset the new root ref
            root = newRoot;
            storage.seek(0);
            storage.write(NumberUtils.intToByteArray((int) root.getStoragePointer()));
            //crate the new right sibling node
            Node right = StorageUtils.allocateNewNode(storage);
            //process the new root
            newRoot.clear();
            newRoot.setLeaf(false);
            newRoot.addDataItem(node.getDataItems()[1], node, right);// move the middle node to new root
            StorageUtils.flushToStorage(newRoot, storage);
            //process the new right
            right.clear();
            right.setLeaf(node.isLeaf());
            right.setParent(newRoot);
            right.addDataItem(node.getDataItems()[2], node.getChild()[2], node.getChild()[3]);
            StorageUtils.flushToStorage(right, storage);
            //process the original node
            node.setParent(newRoot);
            node.setCount(1);
            StorageUtils.flushToStorage(node, storage);
        } else {//split up the other nodes except the root
            //create the new rightsibling node
            Node right = StorageUtils.allocateNewNode(storage);
            //process the new right
            right.clear();
            right.setLeaf(node.isLeaf());
            right.setParent(node.getParent());
            right.addDataItem(node.getDataItems()[2], node.getChild()[2], node.getChild()[3]);
            StorageUtils.flushToStorage(right, storage);
            //process the parent node
            Node parent = StorageUtils.loadNode(storage, node.getParent().getStoragePointer());
            parent.addDataItem(node.getDataItems()[1], right);
            StorageUtils.flushToStorage(parent, storage);
            //process the original node
            node.setCount(1);
            StorageUtils.flushToStorage(node, storage);
        }


    }

    public Node searchInsertedLeaf1(Node node, DataItem item) {
        Node posNode = node;
        while (posNode != null) {
            posNode = StorageUtils.loadNode(storage, posNode.getStoragePointer());
            if (posNode.isFull()) {
                splitUp(posNode);
//                node = StorageUtils.loadNode(storage, node.getStoragePointer());
                posNode = StorageUtils.loadNode(storage, posNode.getParent().getStoragePointer());
                continue;
            }

            if (posNode.isLeaf()) {
                return posNode;
            }

            for (int i = 0; i < posNode.getCount(); i++) {
                DataItem currentItem = posNode.getDataItems()[i];
                if (item.compareTo(currentItem) == 0) {
                    throw new RuntimeException("it is denied to insert duplicated data item");
                } else if (item.compareTo(currentItem) < 0) {
                    posNode = StorageUtils.loadNode(storage, posNode.getChild()[i].getStoragePointer());
                    break;
                } else {
                    if (i == posNode.getCount() - 1) {
                        posNode = StorageUtils.loadNode(storage, posNode.getChild()[posNode.getCount()].getStoragePointer());
                        break;
                    }
                }
            }
        }
        return null;
    }

    public DataItem searchItem(String key) {
        Node posNode = root;

        while (true) {

            if (posNode == null) {
                break;
            }

            int itemPos;
            System.out.println("================node :" + posNode.getCount());
            for (itemPos = 0; itemPos < posNode.getCount(); itemPos++) {
                DataItem currentItem = posNode.getDataItems()[itemPos];
                System.out.println("compare key :" + currentItem.getKey());
                if (key.equals(currentItem.getKey())) {
                    return currentItem;
                } else if (key.compareTo(currentItem.getKey()) < 0) {
                    if (posNode.isLeaf()) {
                        return null;
                    } else {
                        posNode = StorageUtils.loadNode(storage, posNode.getChild()[itemPos].getStoragePointer());
                        break;
                    }
                } else {
                    if (itemPos == posNode.getCount() - 1) {
                        if (posNode.isLeaf()) {
                            return null;
                        } else {
                            posNode = StorageUtils.loadNode(storage, posNode.getChild()[posNode.getCount()].getStoragePointer());
                            break;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void printNode(Map<Integer, List<String>> result, Node posNode, int level) {

        if (posNode == null) {
            return;
        }
        int itemPos;
        if (result.get(level) == null)
            result.put(level, new ArrayList<String>());
        for (itemPos = 0; itemPos < posNode.getCount(); itemPos++) {

            result.get(level).add(posNode.getDataItems()[itemPos].getKey() + "");
        }
        result.get(level).add(" | ");
        int childPos;
        if (posNode.isLeaf()) {
            return;
        }
        for (childPos = 0; childPos <= posNode.getCount(); childPos++) {

            printNode(result, StorageUtils.loadNode(storage, posNode.getChild()[childPos].getStoragePointer()), level + 1);
        }

    }

    public void printTree() {
        Map<Integer, List<String>> result = new HashMap<Integer, List<String>>();
        printNode(result, root, 1);
        SortedSet<Integer> keys = new TreeSet<Integer>(result.keySet());
        for (Integer key : keys) {
            List<String> values = result.get(key);
            for (int i = 0; i <= key; i++) {
                System.out.print("---");
            }
            System.out.println("");
            for (String value : values) {
                System.out.print(value + "  ");
            }
            System.out.println("");
        }

    }

    public void releaseSource() {
        storage.close();
    }

}
