package friends;

import friends.Friends;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TestFriends {

    public static void main(String[] args) throws FileNotFoundException {

        System.out.println("TESTING method shortestChain();");
        testShortestChain();
        System.out.println("\nTESTING method cliques();");
        testCliques();
        System.out.println("\nTESTING method connectors();");
        testConnectors();
    }

    private static void testShortestChain() throws FileNotFoundException {

        Scanner sc = new Scanner(new File("graph2.txt"));
        Graph g = new Graph(sc);
        System.out.println(Friends.shortestChain(g, "sam", "aparna"));
    }

    private static void testCliques() throws FileNotFoundException {

        Scanner sc = new Scanner(new File("graph2.txt"));
        Graph g = new Graph(sc);
        System.out.println(Friends.cliques(g, "rutgers"));
    }

    private static void testConnectors() throws FileNotFoundException {

        Scanner sc = new Scanner(new File("graph2.txt"));
        Graph g = new Graph(sc);
        System.out.println(Friends.connectors(g));
    }
}
