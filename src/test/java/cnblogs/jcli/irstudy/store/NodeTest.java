package cnblogs.jcli.irstudy.store;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import cnblogs.jcli.irstudy.store.support.FileStorage;
import org.junit.Test;

public class NodeTest {

    @Test
    public void testGenerateTree() {
        BTree tree = null;
        try {
            Storage store = new FileStorage(new File("/home/tony/index.data"));
            tree = new BTree(store);
            for (int i = 0; i <= 100; i++) {
                DataItem item = new DataItem();
                item.setKey((int) (new Random().nextDouble() * 100000) + "");
//                item.setKey((i) + "");
                tree.insert(item);
                System.out.println(i);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            tree.releaseSource();
        }
    }

    @Test
    public void testSearchTree() {
        BTree tree = null;
        try {
            Storage store = new FileStorage(new File("/home/tony/index.data"));
            tree = new BTree(store);
            System.out.println(tree.searchItem("6477"));
//            System.out.println(tree.searchItem("96990"));

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            tree.releaseSource();
        }
    }


    @Test
    public void testPrintTree() {
        BTree tree = null;
        try {
            Storage store = new FileStorage(new File("/home/tony/index.data"));
            tree = new BTree(store);
            tree.printTree();

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            tree.releaseSource();
        }
    }
}
