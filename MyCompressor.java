import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class MyCompressor {

    private static final int NUM_CHAR = 256; // alphabet size of extended ASCII
    private String corpusLocation;
    private HCTree HuffmanTree;

    /**
     * Compresses or decompresses the specified files based on the specified corpus
     * @param args corpus then compress/decompress files
     *
     */
    public static void main(String[] args) throws IOException {

        //todo
        //run compression and decompression here

        String corpus = args[0];
        String compressed = args[1];

        MyCompressor compressor = new MyCompressor(corpus);

        int FILES_START_HERE = 2;

        for(int i=FILES_START_HERE; i<args.length; i++){
            String input = args[i];
            int index = input.lastIndexOf("/") + 1;
            String output = input.substring(0, index);

            if (compressed.equals("compress")) {
                output += "compressed_" + input.substring(index);
                compressor.compress(input, output);
            }

            else if (compressed.equals("decompress")) {
                output += "decompressed_" + input.substring(index);
                compressor.decompress(input, output);

            }

        }

    }


    /**
     * Constructor of my compressor and decompressor object
     * @param corpusLocation directory to corpus text file
     */
    public MyCompressor(String corpusLocation) throws  IOException {

        //todo
        HuffmanTree = new HCTree();
        this.corpusLocation = corpusLocation;
        HuffmanTree = buildHuffmanTree(corpusLocation);
    }

    /**
     * Helper Method
     * Finds count of each ascii character and builds Huffman tree
     * @param corpusLocation directory to corpus text file
     * @return reference to the built HCTree
     */
    private HCTree buildHuffmanTree(String corpusLocation) throws IOException{

        //todo
        byte[] input = Files.readAllBytes(Paths.get(corpusLocation));

        int[] freq = new int[NUM_CHAR];
        for (int i=0; i<input.length; i++) {
            int ascii = input[i] & 0xff;
            freq[ascii]++;
        }

        HuffmanTree.buildTree(freq);
        return HuffmanTree;
    }

    /**
     * Compress Method
     * @param inputFile directory of file that is to be compressed.
     * @param compressedFile directory of compressed file.
     */
    public void compress(String inputFile, String compressedFile) throws IOException {
        
        //todo
        byte[] input = Files.readAllBytes( Paths.get(inputFile) );

        FileOutputStream fileOut = new FileOutputStream(compressedFile);
        DataOutputStream out = new DataOutputStream(fileOut);
        BitOutputStream bitOut = new BitOutputStream(out);

        out.writeInt(input.length);
        //System.out.println(input.length);

        //out.flush();
        for (byte symbol: input) {
            HuffmanTree.encode(symbol, bitOut);
        }
        bitOut.flush();
        out.close();
        fileOut.close();
    }

    /**
     * Decompress Method
     * @param compressedFile directory of compressed file.
     * @param outputFile directory of uncompressed file.
     */
    public void decompress(String compressedFile, String outputFile) throws IOException {

        //todo
        FileInputStream fileIn = new FileInputStream(compressedFile);
        DataInputStream in = new DataInputStream(fileIn);
        BitInputStream bitIn = new BitInputStream(in);

        int bytes = in.readInt();

        FileOutputStream fileOut = new FileOutputStream(outputFile);
        DataOutputStream out = new DataOutputStream(fileOut);

        for (int i=0; i < bytes; i++) {
            out.writeByte(HuffmanTree.decode(bitIn));
        }

        in.close();
        fileIn.close();

        out.close();;
        fileOut.close();

    }
}
