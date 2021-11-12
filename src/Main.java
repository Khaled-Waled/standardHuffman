public class Main
{
    public static void main(String[] args)
    {
        Huffman.compress("src/Sample.txt","src/a.txt");
        Huffman.decompress("src/a.txt","src/qqq.txt");
    }
}
