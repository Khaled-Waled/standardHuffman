import java.util.Comparator;

public class Node
{
    public char letter;
    public int prob;
    public String code="";

    public Node left  =null;
    public Node right =null;


    public Node (char l, int p)
    {
        this.letter = l;
        this.prob=p;
    }

    static class NodeComparator implements Comparator<Node>
    {
        public int compare (Node a, Node b)
        {
            return a.prob-b.prob;
        }
    }


}
