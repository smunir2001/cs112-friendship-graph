package friends;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {

		// Make list.
		ArrayList<String> shortestPath = new ArrayList<>();

		// Get person, if exists.
		if (!g.map.containsKey(p1) || !g.map.containsKey(p2)) {
			return null;
		}
		Person src = getPerson(g, p1), dest = getPerson(g, p2);

		// Make queue and HashMap.
		Queue<Person> q = new Queue<>();
		HashMap<Person, Person> prev = new HashMap<Person, Person>();

		// Add source to queue and HashMap.
		q.enqueue(src);
		prev.put(src, null);

		// BFS (breadth-first-search).
		while (!q.isEmpty()) {
			Person person = q.dequeue();
			if (person == dest) {
				break;
			}
			// For each friend, add to HashMap.
			for (Person friend : getFriendsList(g, person)) {
				// If HashMap does not contain current.
				if (!prev.containsKey(friend)) {
					q.enqueue(friend);
					// Previous of friend = person.
					prev.put(friend, person);
				}
			}
		}

		// Go backwards and add to path.
		Person person = dest;
		while (person != null && prev.get(dest) != null) {
			// Add to front of ArrayList (0th element).
			shortestPath.add(0, person.name);
			// Traverse by getting parent.
			person = prev.get(person);
		}
		return shortestPath;
	}
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {

		// Return ArrayList of groups of cliques.
		ArrayList<ArrayList<String>> cliques = new ArrayList<ArrayList<String>>();
		boolean[] visited = new boolean[g.members.length];
		for (boolean b : visited) {
			// Get index of b in array.
			int i = 0;
			while (visited[i] != b) {
				i++;
			}

			// If not visited.
			if (!b) {
				Person person = g.members[i];

				// Call BFS (breadth-first-search).
				ArrayList<String> tmp = findCliques(g, school, person, visited);
				if (school.equals(person.school)) {
					cliques.add(tmp);
				}
			}
		}
		return cliques;
	}
	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {

		String[] names = new String[g.members.length];
		for (int i = 0; i < names.length; i++) {
			names[i] = g.members[i].name;
		}

		ArrayList<String> connectors = new ArrayList<String>();
		int[] dfsnum = new int[g.members.length], prev = new int[g.members.length];
		boolean[] visited = new boolean[g.members.length];
		int num = 0;

		// DFS for each visited.
		for (int i = 0; i < visited.length; i++) {
			boolean b = visited[i];
			if (!b) {
				Person person = g.members[i];
				// Call DFS (depth-first-search).
				DFS(g, num, connectors, person, visited, dfsnum, prev);
			}
		}

		// Check number of edges each connector has.
		for (int i = 0; i < connectors.size(); i++) {
			// Get person.
			Person person = getPerson(g, connectors.get(i));
			// Check # of edges by checking # of friends in friends list.
			int edges = getFriendsList(g, person).size();
			if (edges == 1) {
				connectors.remove(i);
			}
		}
		return connectors;
	}

	private static Person getPerson(Graph g, String name) {

		// Get index from Graph HashMap.
		return g.members[g.map.get(name)];
	}

	private static ArrayList<Person> getFriendsList(Graph g, Person person) {

		ArrayList<Person> friends = new ArrayList<>();
		Friend f = person.first;
		while (f != null) {
			// Get friend from Graph using fnum index.
			Person p = g.members[f.fnum];
			friends.add(p);
			f = f.next;
		}
		return friends;
	}

	private static int getIndex(Graph g, Person person) {
		return g.map.get(person.name);
	}

	private static ArrayList<String> findCliques(Graph g, String school, Person source, boolean[] visited) {

		// If source does not go to school, mark visited and return.
		if (!school.equals(source.school)) {
			visited[getIndex(g, source)] = true;
			return new ArrayList<String>();
		}

		// Create a current clique and queue for BFS.
		ArrayList<String> curr = new ArrayList<String>();
		Queue<Person> q = new Queue<Person>();

		// Add first source.
		q.enqueue(source);

		// BFS (breadth-first-search).
		while (!q.isEmpty()) {
			// Get person, mark visited, add to clique.
			Person person = q.dequeue();
			visited[getIndex(g, person)] = true;

			// Add to ArrayList if not already in.
			if (!curr.contains(person.name)) {
				curr.add(person.name);
			}

			// For each friend, repeat.
			for (Person friend : getFriendsList(g, person)) {
				// If not visited; if match, enqueue.
				if (!visited[getIndex(g, friend)] && school.equals(friend.school)) {
					q.enqueue(friend);
				}
			}
		}
		return curr;
	}

	private static void DFS(Graph g, int num, ArrayList<String> connectors, Person person, boolean[] visited, int[] dfsnum, int[] prev) {

		int indexOfCurrent = g.map.get(person.name);
		dfsnum[indexOfCurrent] = num;
		prev[indexOfCurrent] = num;
		visited[indexOfCurrent] = true;
		num++;
		ArrayList<Person> friends = getFriendsList(g, person);

		for (int i = 0; i < friends.size(); i++) {
			int indexOfFriend = g.map.get(friends.get(i).name);
			if (visited[indexOfFriend]) {
				prev[indexOfCurrent] = Math.min(prev[indexOfCurrent], prev[indexOfFriend]);
			} else {
				System.out.println("CHECKING: " + person.name);
				String tmp = person.name;
				// If person is first in list, need to check if connector.
				if (dfsnum[indexOfCurrent] == 0) {
					// Is connector.
					if (i == friends.size() - 1) {
						// If not in list, add it.
						if (!connectors.contains(tmp)) {
							connectors.add(tmp);
						}
					}
				} else {
					// If not in list, add it.
					if (!connectors.contains(tmp)) {
						connectors.add(tmp);
					}
				}
			}
		}
	}
}

