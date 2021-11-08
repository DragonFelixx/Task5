package company.vel;

import company.vel.binaryTree.SimpleBinaryTree;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("Enter tree in bracket notation: \n");
        SimpleBinaryTree<Integer> tree = new SimpleBinaryTree<>(Integer::parseInt);
        tree.fromBracketNotation(readString());
        tree.deleteSingleNodes();
        System.out.println("Tree without single nodes: \n");
        System.out.println(tree.toBracketStr());
    }

    private static String readString()
    {
        Scanner scn = new Scanner(System.in);
        return scn.nextLine();
    }
}
