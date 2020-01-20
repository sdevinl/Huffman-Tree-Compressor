import static org.junit.Assert.*;
import org.junit.*;

import java.io.*;
import java.util.Scanner;

public class HCTreeTester {

    private HCTree tree = new HCTree();
    private HCTree tree2 = new HCTree();
    private HCTree tree3 = new HCTree();


    @Before
    public void setUp() {
        int[] freq = new int[256];
        freq[97] = 17; //a
        freq[98] = 8; // b
        freq[99] = 7; // c
        freq[100] = 14; // d
        freq[101] = 9; // e
        freq[102] = 1; // f
        freq[10] = 1; // NL or \n

        int[] freq2 = new int[256];
        freq2[99] = 1; // c
        freq2[100] = 1; // e
        freq2[32] = 2; // space
        freq2[98] = 3; // b
        freq2[97] = 3; // a

        int[] freq3 = new int[256];
        freq3[97] = 3;
        freq3[32] = 2;
        freq3[98] = 3;
        freq3[99] = 1;
        freq3[100] = 0;
        freq3[0] = 0;
        freq3[1] = 0;

        tree.buildTree(freq);
        tree2.buildTree(freq2);
        tree3.buildTree(freq3);
    }

    @Test
    public void buildTree() throws IOException {

        System.out.println("Tree1");
        tree.inorder(tree.getRoot());
        System.out.println("");
        System.out.println("Tree2");
        tree2.inorder(tree2.getRoot());
        System.out.println("");
        System.out.println("Tree3");
        tree3.inorder(tree3.getRoot());
        System.out.println("");

        assertEquals(57, tree.getRoot().getFreq());
        assertEquals(10, tree2.getRoot().getFreq());
        assertEquals(9, tree3.getRoot().getFreq());


    }
}