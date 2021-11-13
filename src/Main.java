public class Main
{
    public static void main(String[] args)
    {
        Huffman.compress("src/Sample.in","src/a.in");
        Huffman.decompress("src/a.in","src/output.out");
    }
}
