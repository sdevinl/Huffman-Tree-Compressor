
import java.awt.image.FilteredImageSource;
import java.io.*;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Stack;

/**
 * The Huffman Coding Tree
 */
public class HCTree {

    private static final int NUM_CHARS = 256; // alphabet size of extended ASCII
    private static final int BYTE_BITS = 8; // number of bits in a byte

    private HCNode root; // the root of HCTree
    private HCNode [] leaves = new HCNode[NUM_CHARS]; // the leaves of HCTree that contain all the symbols

    /**
     * The Huffman Coding Node
     */
    protected class HCNode implements Comparable<HCNode> {

        byte symbol; // the symbol contained in this HCNode
        int freq; // the frequency of this symbol
        HCNode c0, c1, parent; // c0 is the '0' child, c1 is the '1' child
        
        /**
         * Initialize a HCNode with given parameters
         * @param symbol the symbol contained in this HCNode
         * @param freq the frequency of this symbol
         */
        HCNode(byte symbol, int freq) {
            
            //todo
            setSymbol(symbol);
            setFreq(freq);

        }

        /**
         * Getter for symbol
         * @return the symbol contained in this HCNode
         */
        byte getSymbol() {
            
            //todo
            return symbol;
        }

        /**
         * Setter for symbol
         * @param symbol the given symbol
         */
        void setSymbol(byte symbol) {
            
            //todo
            this.symbol = symbol;
        }

        /**
         * Getter for freq
         * @return the frequency of this symbol
         */
        int getFreq() {
            
            //todo
            return freq;
        }

        /**
         * Setter for freq
         * @param freq the given frequency
         */
        void setFreq(int freq) {
            
            //todo
            this.freq = freq;
        }

        /**
         * Getter for '0' child of this HCNode
         * @return '0' child of this HCNode
         */
        HCNode getC0() {
            
            //todo
            return c0;
        }

        /**
         * Setter for '0' child of this HCNode
         * @param c0 the given '0' child HCNode
         */
        void setC0(HCNode c0) {
            
            //todo
            this.c0 = c0;
        }

        /**
         * Getter for '1' child of this HCNode
         * @return '1' child of this HCNode
         */
        HCNode getC1() {
            
            //todo
            return c1;

        }

        /**
         * Setter for '1' child of this HCNode
         * @param c1 the given '1' child HCNode
         */
        void setC1(HCNode c1) {
            
            //todo
            this.c1 = c1;
        }

        /**
         * Getter for parent of this HCNode
         * @return parent of this HCNode
         */
        HCNode getParent() {
            
            //todo
            return parent;
        }

        /**
         * Setter for parent of this HCNode
         * @param parent the given parent HCNode
         */
        void setParent(HCNode parent) {
            
            //todo
            this.parent = parent;
        }

        /**
         * Check if the HCNode is leaf
         * @return if it's leaf, return true. Otherwise, return false.
         */
        boolean isLeaf() {

            //todo
            if( getC1() == null && getC0() == null) {
                return true;
            }
            return false;

        }
        /**
         * Defines how object is printed in console
         */
        public String toString() {
            return "Symbol: " + this.symbol + "; Freq: " + this.freq;
        }

        /**
         * compareTo method
         */
        public int compareTo(HCNode o) {

            if (this.freq > o.freq) {
                return 1;
            }
            else if (this.freq < o.freq) {
                return -1;
            }
            else {

                if (this.symbol > o.symbol) {
                    return 1;
                }
                else if (this.symbol < o.symbol) {
                    return -1;
                }
                return 0;
            }
        }
    }


    /**
     * Returns the root of the tree
     * @return the root of the tree
    */
    public HCNode getRoot() {
        
        //todo
        return root;

    }

    /**
     * Sets the root of the tree
     * @param root A HCNode to be the root of the tree
    */
    public void setRoot(HCNode root) {
        
        //todo
        this.root = root;

    }

    /**
     * Builds the HCTree
     * @param freq a count array of bytes
    */
    public void buildTree(int[] freq) {

        //todo
        PriorityQueue<HCNode> pq = new PriorityQueue<>();

        for (int i=0; i < freq.length; i++) {
            byte symbol = (byte)i;
            int count = freq[i];
            if (count > 0) {
                HCNode node = new HCNode(symbol, count);
                leaves[i] = node;
                pq.offer(leaves[i]);
            }
        }

        while (pq.size() > 1) {
            HCNode c0 = pq.poll();
            HCNode c1 = pq.poll();

            HCNode parent = new HCNode(c0.getSymbol(), c0.getFreq() + c1.getFreq());

            parent.setC0(c0);
            parent.setC1(c1);

            c0.setParent(parent);
            c1.setParent(parent);

            pq.add(parent);
        }
        setRoot(pq.peek());
    }

    /**
     * Writes the encoding bits to out
     * @param symbol Symbol to search for
     * @param out Output to write bits onto
    */
    public void encode(byte symbol, BitOutputStream out) throws IOException{

        //todo
        Stack<Integer> reversed_bits = new Stack<>();
        int ascii = symbol & 0xff;
        HCNode node = leaves[ascii];
        HCNode parent = node.getParent();

        while(parent != null) {
            if (parent.getC0() != null && parent.getC0() == node) {
                reversed_bits.push(0);
            }
            else if (parent.getC1() != null && parent.getC1() == node) {
                reversed_bits.push(1);
            }

            node = node.getParent();
            parent = node.getParent();
        }

        while(!reversed_bits.empty()) {
            out.writeBit(reversed_bits.pop());
        }
    }

    /**
     * Decode bits from in and returns a byte of the symbol of the tree
     * @param in Bits from in to decode
     * @return the byte symbol
    */
    public byte decode(BitInputStream in) throws IOException {

        //todo
        HCNode node = getRoot();

        while (!node.isLeaf()) {
            int bit = in.readBit();

            if (bit == 0) {
                node = node.getC0();
            }
            else if (bit == 1) {
                node = node.getC1();
            }
        }

        return node.getSymbol();

    }

    public void inorder(HCNode root) {
        if (root.getC0() != null) {
            inorder(root.getC0());
        }
        System.out.println(root.toString());
        if (root.getC1() != null) {
            inorder(root.getC1());
        }
    }

}
