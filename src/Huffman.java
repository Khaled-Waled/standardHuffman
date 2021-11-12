import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Huffman
{





    public static void compress (String sourceDir,String targetDir)
    {
        String ogData = fileToString(sourceDir);
        int[][] letters= initLettersAndProps(ogData);

        /*
        // show letters[][] content
        for (int i=0; i<numOfChars; i++)
            System.out.println((char)letters[i][0]+":   "+letters[i][1]);
        */

        //make a Priority Queue (min heap) and add the nodes
        PriorityQueue<Node> queue = new PriorityQueue<Node>(letters.length,new Node.NodeComparator());
        for (int i=0; i<letters.length; i++)
            queue.add(new Node((char) letters[i][0],letters[i][1]));
        Node root = null;

        //take 2 items and add combination of them              0
        //move the Queue to a BST                              / \
        //                                                    C   0
        //                                                       / \
        //                                                      C   0
        while (queue.size()>1)
        {
            Node n1 = queue.poll();
            Node n2 = queue.poll();

            Node n0 = new Node((char) 0, n1.prob+ n2.prob);
            n0.left=  n2;
            n0.right= n1;
            queue.add(n0);
            root = n0;
        }
        String[][] codes = new String[letters.length][2];
        codes = codeLetters(root,"",letters,codes);

        /*
        //print codes[][] content

        for (int i=0; i<letters.length; i++)
            System.out.println(codes[i][0]+":   "+codes[i][1]);
        */

        String comData="";

        for (int i=0; i<ogData.length();i++)   //for each character on ogData add its code
        {

            for (int c=0; c<codes.length; c++)  //search in codes
            {
                if (ogData.charAt(i)==codes[c][0].charAt(0))
                {
                    comData+= codes[c][1];      //add its code
                    break;
                }
            }
        }



        /*
        //build header
        int mxLen=1;
        for (int i=0; i<codes.length; i++)
            mxLen = Math.max(mxLen, codes[i][1].length());

        String header = "";
        for (int i=0; i<codes.length; i++)
            header+=(toBin(codes[i][0].charAt(0),7)+toBin(Integer.parseInt(codes[i][1],2),mxLen));


        comData+=header;
        //Write to file
        int extraLen= 8-(comData.length()%8);
        if (extraLen==8)
            extraLen=0;

        comData = binaryStringToBits(comData,extraLen);
        comData = String.valueOf((byte) extraLen) + comData;
        //comData = String.valueOf((byte) mxLen) + comData;
        //comData = String.valueOf((byte) codes.length) + comData;
        */

        writeToFile(comData,targetDir);
    }

    public static void decompress(String sourceDir, String targetDir)
    {
        /*
        String comData = fileToString(sourceDir);
        //extract header
        int extraLen = comData.charAt(0) - '0';
        comData = comData.substring(1);

        int mxLen = comData.charAt(0) - '0';
        comData = comData.substring(1);
        int codesLen = comData.charAt(0) - '0';
        comData = comData.substring(1);

        int[][] codes = new int[codesLen][2];

        String header = comData.substring(0,1+(7+mxLen)*codesLen);

        String nHeader="";
        */





    }

    static int[][] initLettersAndProps(String s)
    {
        int ptr=1;
        int[][] letters = new int[s.length()][2];
        letters[0] = new int[]{s.charAt(0), 1};
        for (int i=1; i<s.length(); i++)
        {
            boolean found=false;
            for (int a=0; a<ptr; a++) //search for current char in the array
            {
                if (s.charAt(i)==(char)letters[a][0]) // if found increase its counter
                {
                    found = true;
                    letters[a][1]++;
                    break;
                }
            }
            if (!found)
            {
                letters[ptr][0] = s.charAt(i);
                letters[ptr++][1] = 1;

            }

        }
        int[][] croppedLetters = new int[ptr][2]; //remove extra empty spaces

        for (int i=0; i<ptr; i++)
        {
            croppedLetters[i][0] = letters[i][0];
            croppedLetters[i][1] = letters[i][1];
        }

        return croppedLetters;
    }

    public static String[][] codeLetters (Node currentNode, String s,int[][] letters, String[][] codes)
    {
        if (currentNode.left==null && currentNode.right==null && currentNode.letter!=0)
        //if it's a leaf node add its code to the array
        {

            for (int i=0; i<letters.length; i++)
            {
                if (letters[i][0]==currentNode.letter)
                {
                    codes[i][0] = String.valueOf((char) letters[i][0]);
                    codes[i][1] = s;
                }
            }
            return codes;
        }


        // In-order traversal of the tree
        codeLetters(currentNode.left,s+"0",letters,codes);
        codeLetters(currentNode.right,s+"1",letters,codes);

        return codes;


    }
    public static String fileToString (String dir){
        String res = "";
        try
        {
            File ogFile = new File(dir);
            Scanner reader = new Scanner(ogFile);
            String tmp;
            while (reader.hasNext())
            {
                tmp = reader.nextLine();
                res+='\n';
                res += tmp;
            }
            res = res.substring(1); //remove the first \n
        }
        catch (IOException e)
        {
            System.out.println("File Doesn't exist !");
            System.exit(1);
        }
        return res;
    }
    public static String writeToFile (String data, String dir) {
        try
        {
            File f = new File(dir);
            f.getParentFile().mkdirs();
            f.createNewFile();
            BufferedWriter w = new BufferedWriter(new FileWriter(f));
            w.write(data);
            w.close();
        }
        catch (IOException e)
        {
            System.out.println("Writing failed");
        }
        return dir;
    }

    protected static String toBin (int x, int numOfBits)  {
        if (x<0) x*= -1;
        String res = Integer.toBinaryString(x);
        while (res.length()<numOfBits)
            res = "0"+res;
        return res;
    }

}
