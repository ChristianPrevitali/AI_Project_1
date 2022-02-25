/**
 * Author: Christian Previtali
 * Course: COMP445, Intro to A.I.
 * Date: 17 February 2022
 * Description: This file includes the BFS Algorithm,
 *  DFS Algorithm, and A* Algorithm
 */
import java.util.*;
import java.io.File;

public class project1 {
    //BFS Algorithm method
    public static void BFS(File f)throws Exception{
        Queue<String> queue = new LinkedList<>();
        LinkedList<String> visited = new LinkedList<>();

        Scanner sc = new Scanner(f); //scans file
        String line = sc.nextLine(); //takes each individual line
        Scanner firstWord = new Scanner(line); //scans line
        String word = firstWord.next();//takes first word in line
        queue.add(word);

        //while loop that searches until it finds Bucharest
        while(!visited.contains("Bu")){
            sc = new Scanner(f);//new scanner for file
            while (sc.hasNextLine()) {
                line = sc.nextLine();//grabs line
                firstWord = new Scanner(line);
                word = firstWord.next();//grabs first word in line
                /*if statement that finds the line in the file that contains
                 the word and makes sure it is not in visited*/
                if (word.equals(queue.element()) && !visited.contains(word)){
                    visited.add(queue.remove());
                    //while that adds new elements in line to queue
                    while (firstWord.hasNext()) {
                        word = firstWord.next();
                        if(!visited.contains(word) && !queue.contains(word))
                            queue.add(word);
                        firstWord.next();
                    }
                }
            }
        }
        //prints path
        for (String cities: visited) {
            if(cities.equals("Bu")) System.out.println(cities);
            else System.out.print(cities + " => ");
        }

        firstWord.close();
        sc.close();
    }

    //DFS Algorithm method
    public static boolean DFS(String node, File f, LinkedList<String> visited) throws Exception{
        Scanner sc = new Scanner(f);
        String line = sc.nextLine();
        Scanner word = new Scanner(line);
        String city = word.next();
        boolean found = false;

        //Base case: If the node is Bucharest, set found to true
        if(node.equals("Bu")){
            visited.add(node);
            System.out.println(node);
            found = true;
        }
        //Traverse the graph
        else{
            visited.add(node);
            System.out.print(node + " => ");
            //loop that grabs the next word of the next line
            while(!city.equals(node) && sc.hasNextLine()){
                line = sc.nextLine();
                word = new Scanner(line);
                city = word.next();
            }

            //recursive call that continues if Bucharest is not found yet
            while(word.hasNext() && !found){
                city = word.next();
                if (!visited.contains(city))
                    found = DFS(city, f, visited);//recursive call
                word.next();
            }
            word.close();
            sc.close();
        }
        return found;
    }

    //A* Algorithm Method
    public static void AStar(String root, File neighbors, File sld)throws Exception{
        Scanner sc = new Scanner(neighbors); //scans neighbor file
        Scanner sc2 = new Scanner(sld); //scans Straight line distance file
        int calc; //
        Map<String, Integer> children;
        Map<String, Integer> distance = new HashMap<>();
        Map<String, Map<String,Integer>> nbrs = new HashMap<>();

        /*Iterates through straight line distance file and creates
          a map of them*/
        while(sc2.hasNextLine()){
            String ln = sc2.nextLine();
            Scanner cit = new Scanner(ln);
            String city = cit.next();
            int value = Integer.parseInt(cit.next());
            distance.put(city,value);
            cit.close();
        }

        /*Iterates through neighbors file and creates a map
          of them*/
        String line;
        Scanner word;
        while(sc.hasNextLine()){
            line = sc.nextLine();
            word = new Scanner(line);
            String wd = word.next();
            children = new HashMap<>();
            while(word.hasNext()){
                String wd2 = word.next();
                int val = Integer.parseInt(word.next());
                children.put(wd2,val);
            }
            nbrs.put(wd, children);
            word.close();
        }

        //A*
        Map<String, Integer> q = new HashMap<>();
        LinkedList<String> path = new LinkedList<>();
        boolean found = false;
        String node = root;
        while(!found){
            path.add(node);
            //creates a map of the children of the current node
            Map<String, Integer> curCity = nbrs.get(node);
            //checks to see if Bucharest is in the children of nodes
            if(curCity.containsKey("Bu")){
                path.add("Bu");
                found = true;
            }
            /*Calculates the heuristic value and puts them in the queue with
              the name of the city*/
            else {
                for (String city : curCity.keySet()) {
                    if (!q.containsKey(city)) {
                        calc = curCity.get(city) + distance.get(city);
                        q.put(city, calc);
                    }
                }
                //grabs the lowest cost node in queue and sets it as the node to explore
                node = Collections.min(q.entrySet(), Map.Entry.comparingByValue()).getKey();
            }
        }
        //prints path
        for (String cities: path) {
            if(cities.equals("Bu")) System.out.println(cities);
            else System.out.print(cities + " => ");
        }
        sc.close();
        sc2.close();
    }

    public static void main(String[] args) throws Exception {
        File f = new File("neighbors.txt");
        File sld = new File("straightLineDistances.txt");
        LinkedList<String> visited = new LinkedList<>();

        System.out.print("BFS: ");
        BFS(f);
        System.out.print("DFS: ");
        DFS("Ar", f, visited);
        System.out.print("A*: ");
        AStar("Ar",f,sld);
    }
}
