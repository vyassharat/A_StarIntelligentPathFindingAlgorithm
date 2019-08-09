/*
 * Sharat Vyas
 * 3/23/17
 * ITCS 3153
 * A* Algorithm
 */

import java.util.*;
import java.io.*;

public class Driver 
{
	public static void main(String [] args)
	{
		//create 15X15 2d array of node objects to represent grid
		Node[][]grid=new Node[15][15];
		Random rand=new Random();
		Scanner kb=new Scanner(System.in);
		int [] startNode = new int[2];
		int [] goalNode = new int[2];
		for(int i=0;i<grid.length;i++)
		{
			for(int j=0;j<grid.length;j++)
			{
			    if(rand.nextInt(10)!=9)
					grid[i][j]=new Node(i,j,0);
				else
					grid[i][j]=new Node(i,j,1);
			}
		}
		printGrid(grid);
		//get input from user
		System.out.print("REMEMBER INDEXES START AT 0!!!");
		System.out.println("\nPlease Enter Starting Node Row Number: ");
		startNode[0]=kb.nextInt();
		System.out.println("\nPlease Enter Starting Node Column Number: ");
		startNode[1]=kb.nextInt();
		System.out.println("\nPlease Enter Goal Node Row Number: ");
		goalNode[0]=kb.nextInt();
		System.out.println("\nPlease Enter Goal Node Column Number: ");
		goalNode[1]=kb.nextInt();
		System.out.println("Finding path from ("+startNode[0]+","+startNode[1]+") to ("+goalNode[0]+","+goalNode[1]+")...");
		
		if(checkValid(grid, startNode)==false || checkValid(grid, goalNode)==false)
		{
			System.out.println("INVALID CHOICE!!!");
			System.exit(0);
		}
		//set the start node's parent to null and begin a* algo
		grid[startNode[0]][startNode[1]].setParent(null);
		aStar(grid,startNode,goalNode);
		
	}
	//function to print initial 15X15 grid
	public static void printGrid(Node[][] grid)
	{
		
		for(int i=0;i<grid.length;i++)
		{
			for(int j=0;j<grid.length;j++)
			{
				System.out.print(grid[i][j].getTraversable()+" | ");
			}
			System.out.println("\n------------------------------------------------------------");
		}
	}
	//function to print final 15X15 grid with a path mapped
	public static void printFinalPath(ArrayList<Node> path, Node[][]grid, int[] startNode, int[]goalNode)
	{
		for(int i=0;i<path.size();i++)
		{
			System.out.println("Going thru node: ("+path.get(i).getRow()+","+path.get(i).getCol()+")");
		}
		for(int i=0;i<grid.length;i++)
		{
			for(int j=0;j<grid.length;j++)
			{
				if(i==startNode[0]&&j==startNode[1])
					System.out.print("S | ");
				else if(i==goalNode[0]&&j==goalNode[1])
					System.out.print("G | ");
				else if(path.contains(grid[i][j]))
					System.out.print("X | ");
				else
				System.out.print(grid[i][j].getTraversable()+" | ");
			}
			System.out.println("\n------------------------------------------------------------");
		}
	}
	//function to check if start and goal nodes are valid
	public static boolean checkValid(Node[][] grid, int[] location)
	{
		if(grid[location[0]][location[1]].getTraversable()==0)
			return true;
		else if(location[0]>14 || location[1]>14)
			return false;
		else
			return false;
			
	}
	//function that implements actual a* algorithm
	public static void aStar(Node[][]grid, int[] startNode, int[] goalNode)
	{
		int [] beginningNode={startNode[0], startNode[1]};
		Node currentNode=grid[startNode[0]][startNode[1]];
		int [] currentlyVisiting = startNode;
		//Comparator is used to define how values are compared in java's verison of a priority queue.
		Comparator<Node>comparator = new FScoreComparator();
		//Java implementation of priority queue to find smallest F value
		PriorityQueue<Node> openList = new PriorityQueue<Node>(100,comparator);
		//hashtable to hold visited nodes
		Hashtable<int[], Node> closedList = new Hashtable<int[], Node>();
		//arraylists to hold path to goal node
		ArrayList<Node>reversedPath = new ArrayList<Node>();
		ArrayList<Node>actualPath = new ArrayList<Node>();
		//function call to initially populate the openList
		generateNeighbors(startNode,goalNode,openList,grid);
		
		//loop to continue to pop. open list
		do
		{
			if(openList.size()==0) //if the list is empty there is no path
			{
				System.out.println("No path could be found!!!");
				System.exit(0);
			}
			//else if a path is found
			else if(grid[currentNode.getRow()][currentNode.getCol()]==grid[goalNode[0]][goalNode[1]])
			{
				System.out.println("FOUND A PATH!!!!!!!");
				
				while(currentNode.getParent()!=null)
				{
					reversedPath.add(currentNode);
					currentNode=currentNode.getParent();
				}
				actualPath.add(grid[beginningNode[0]][beginningNode[1]]);
				for(int i=reversedPath.size()-1;i>=0;i--)
				{
					actualPath.add(reversedPath.get(i));
				}
				printFinalPath(actualPath,grid,beginningNode,goalNode);
				break;
				//System.exit(0);
			}
			//next iteration if path can't be found 
			else
			{
				closedList.put(currentlyVisiting, currentNode);
				currentNode.setVisited();
				currentNode=openList.peek();
				openList.remove(currentNode);
				currentlyVisiting[0]=currentNode.getRow();
				currentlyVisiting[1]=currentNode.getCol();
				
				generateNeighbors(currentlyVisiting,goalNode,openList,grid);
			}
		}while(grid[goalNode[0]][goalNode[1]]!=currentNode || openList.size()!=0); //conditions to continue the loop
	}
	
	public static void calculateHeuristic(int[] currentNode, int[] goalNode, Node[][]grid) //function to calculate the heuristic
	{
		//firstPart will calculate G
		Node parentNode = grid[currentNode[0]][currentNode[1]].getParent();
		
		if(parentNode!=null)
      {
   		if(parentNode.getRow()==currentNode[0] || parentNode.getCol()==currentNode[1])
   		{
   			grid[currentNode[0]][currentNode[1]].setG(parentNode.getG()+10);
   		}
   		else if((parentNode.getRow()==currentNode[0]+1 || parentNode.getRow()==currentNode[0]-1) && (parentNode.getCol()==currentNode[1]+1 || parentNode.getCol()==currentNode[1]-1))
   		{
   			grid[currentNode[0]][currentNode[1]].setG(parentNode.getG()+14);
   		}
      }
      else
      {
         grid[currentNode[0]][currentNode[1]].setG(0);
      }
		
		//calculate H value using manhattan method and then calculate f
		int total=(Math.abs(goalNode[0]-currentNode[0])+(Math.abs(goalNode[1]-currentNode[1])))*10;
		grid[currentNode[0]][currentNode[1]].setH(total);
		
		
		grid[currentNode[0]][currentNode[1]].setF();
	}
	
	//function to generate the neighbors of a specific node
	public static void generateNeighbors(int [] currentNode, int[] goalNode, PriorityQueue<Node> openList , Node [][]grid)
	{
		int[] temp=new int[2];
		//Node 
		for(int i=currentNode[0]-1;i<=currentNode[0]+1;i++)
		{
			for(int j=currentNode[1]-1;j<=currentNode[1]+1;j++)
			{
				//ensures that we are within bounds
				if(i>14||j>14||i<0||j<0)
				{
					continue;
				}
				//ignore the current node
				else if(i==currentNode[0] && j==currentNode[1])
					continue;
				//finds neighbors, calculate F and add them to the open list
				else if(grid[i][j].getTraversable()==0 && grid[i][j].getVisited()==false)
				{
					//System.out.println("Adding element at: "+i+","+j);
					temp[0]=grid[i][j].getRow();
					temp[1]=grid[i][j].getCol();
					grid[i][j].setParent(grid[currentNode[0]][currentNode[1]]);
					calculateHeuristic(temp, goalNode, grid);
					//grid[temp[0]][temp[1]]();
					openList.add(grid[i][j]);
					//openList.add(grid[i][j]);
				}
			}
		}
	}
}
