import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Scanner;


class aStar {

	int[][] board = new int[3][3];
	int f = 0, g = 0, h = 0;
	aStar Parent;

	aStar(int[][] cells) {
		board[0][0] = cells[0][0];
		board[0][1] = cells[0][1];
		board[0][2] = cells[0][2];
		board[1][0] = cells[1][0];
		board[1][1] = cells[1][1];
		board[1][2] = cells[1][2];
		board[2][0] = cells[2][0];
		board[2][1] = cells[2][1];
		board[2][2] = cells[2][2];
	}

}

public class solve8Puzzle {

	static PriorityQueue<aStar> openList = new PriorityQueue<aStar>(new comp());
	static ArrayList<aStar> closedList = new ArrayList<aStar>();
	int pathCost = 0;

	//checks if board is solvable 
	public boolean isSolvable(aStar n) {
		int inversions = 0;

		for (int i = 0; i < 9; i++) {
			int currentRow = i / 3;
			int currentCol = i % 3;

			for (int j = i; j < 9; j++) {
				int row = j / 3;
				int col = j % 3;

				if (n.board[row][col] != 0 && n.board[row][col] < n.board[currentRow][currentCol]
						&& n.board[currentRow][currentCol] != 0) {
					inversions++;
				}
			}
		}
		System.out.println("Number of Inversions: " + inversions);

		if (inversions % 2 != 0)
			return false;

		return true;
	}

	//returns the heuristic for a given state based on the no of misplaced tiles.
	public int getHeuristic(aStar s) {
		int misplaced = 0;
		int count = 1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (s.board[i][j] != count % 9) {
					misplaced++;
				}
				count++;
			}
		}
		return misplaced;
	}

	//function to check if the state is present in the open List
	public boolean qContains(PriorityQueue<aStar> openList, aStar node) {
		java.util.Iterator<aStar> iterator = openList.iterator();

		while (iterator.hasNext()) {
			aStar a = iterator.next();
			if (Arrays.deepEquals(a.board, node.board))
				return true;
		}
		return false;
	}

	//checks if the state is present in the closed list
	public boolean LContains(ArrayList<aStar> list, aStar node) {
		ListIterator<aStar> listIterator = list.listIterator();

		while (listIterator.hasNext()) {
			aStar a = listIterator.next();
			if (Arrays.deepEquals(a.board, node.board))
				return true;
		}
		return false;
	}

	//checks if the state is already present and removes, adds states based on the path cost(g).
	public void qContains1(PriorityQueue<aStar> openList, aStar node) {
		java.util.Iterator<aStar> iterator = openList.iterator();

		while (iterator.hasNext()) {
			aStar a = iterator.next();
			if (Arrays.deepEquals(a.board, node.board) && node.g < a.g) {//compare path cost of current state and open list state
				openList.remove(a);
				openList.add(node);
				return;
			}
		}
	}

	//function to get the neighbouring states of the current state
	public void getNeighbours(aStar s) {
		int row = 0, col = 0;		

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (s.board[i][j] == 0) {
					row = i;
					col = j;
					break;
				}
			}
		}

		if (row - 1 >= 0) {
			aStar a = new aStar(s.board);
			a.board[row][col] = s.board[row - 1][col];
			a.board[row - 1][col] = 0;			
			int h = getHeuristic(a);
			a.h = h;
			a.g = s.g + 1;
			a.f = a.h + a.g;
			a.Parent = s;
			if (!qContains(openList, a) && !LContains(closedList, a))
				openList.add(a);
			else
				qContains1(openList, a);
		}
		if (row + 1 <= 2) {
			aStar a = new aStar(s.board);
			a.board[row][col] = s.board[row + 1][col];
			a.board[row + 1][col] = 0;			
			int h = getHeuristic(a);
			a.h = h;
			a.g = s.g + 1;
			a.f = a.h + a.g;
			a.Parent = s;
			if (!qContains(openList, a) && !LContains(closedList, a))
				openList.add(a);
			else
				qContains1(openList, a);
		}
		if (col + 1 <= 2) {
			aStar a = new aStar(s.board);
			a.board[row][col] = s.board[row][col + 1];
			a.board[row][col + 1] = 0;			
			int h = getHeuristic(a);
			a.h = h;
			a.g = s.g + 1;
			a.f = a.h + a.g;
			a.Parent = s;
			if (!qContains(openList, a) && !LContains(closedList, a))
				openList.add(a);
			else
				qContains1(openList, a);
		}
		if (col - 1 >= 0) {
			aStar a = new aStar(s.board);
			a.board[row][col] = s.board[row][col - 1];
			a.board[row][col - 1] = 0;			
			int h = getHeuristic(a);
			a.h = h;
			a.g = s.g + 1;
			a.f = a.h + a.g;
			a.Parent = s;
			if (!qContains(openList, a) && !LContains(closedList, a))
				openList.add(a);
			else
				qContains1(openList, a);
		}
	}

	//checks if the given board is goal board
	public boolean isGoal(aStar board) {
		int count = 1;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board.board[i][j] != count % 9) {
					return false;
				}
				count++;
			}
		}
		return true;
	}

	//prints the path from initial board to goal board
	public void getPath(aStar goal) {
		pathCost += goal.g;
		if (goal.Parent != null) {

			getPath(goal.Parent);
			System.out.println("");
			System.out.println(" to");
			System.out.println("");
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(goal.board[i][j] + " ");
			}
			System.out.println("");
		}
	}

	//function to expand states by popping from the queue and adding neighbours to the queue
	public void Expand(PriorityQueue<aStar> q) {

		while (!q.isEmpty()) {
			aStar current = q.remove();
			closedList.add(current);			
			if (isGoal(current)) {
				getPath(current);
				System.out.println("Path cost : " + pathCost);
				System.out.println("Number of nodes expanded : " + closedList.size());
				return;
			}
			getNeighbours(current);
		}		
	}

	public static void main(String[] args) {
		try {

			File file = new File("input.txt");
			Scanner sc = new Scanner(file);
			String s;

			int[][] board = new int[3][3];
			while (sc.hasNextLine()) {
				s = sc.nextLine();
				String[] str = s.split(",");
				int k = 0;
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						board[i][j] = Integer.parseInt(str[k]);
						k++;
					}
				}
				openList.clear();
				System.out.println("Initial State is");
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						System.out.print(board[i][j] + " ");
					}
					System.out.println("");
				}
				aStar a = new aStar(board);
				solve8Puzzle n = new solve8Puzzle();
				int h = n.getHeuristic(a);
				a.h = h;
				a.f = a.g + a.h;
				
				closedList.clear();
				openList.add(a);
				if (!n.isSolvable(a)) {
					System.out.println("The board is not solvable.");
					continue;
				} else {
					System.out.println("The board is solvable.");
					System.out.println("");
					n.Expand(openList);
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}

//comparator for priority queue which compares based on f(n)
class comp implements Comparator<aStar> {

	@Override
	public int compare(aStar a1, aStar a2) {	
		return a1.f - a2.f;
	}

}
