package com.example.pacman;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class AStar {

    boolean diagAllowed = false;

    List<Node> open;
    List<Node> closed;
    List<Node> path;

    Node now;
    private int startX;
    private int startY;
    private int endX;
    private int endY;




    GameView dv;
    int[][] map;

    private Node[][] nodes;

    public AStar(GameView dv, int sx, int sy){
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        this.startX = sx;
        this.startY = sy;
        this.dv = dv;
        this.map = dv.getLevellayout();
        this.now = new Node(null, sx,sy,0,0);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Node> findPathTo(int endX, int endY){


        this.endX = endX;
        this.endY = endY;


        closed.add(now);
        addNeighborsToOpenList();
        while (this.now.x != this.endX || this.now.y != this.endY) {
            if (this.open.isEmpty()) { // Nothing to examine
                return null;
            }
            this.now = (Node) this.open.get(0); // get first node (lowest f score)
            this.open.remove(0); // remove it
            this.closed.add(this.now); // and add to the closed
            addNeighborsToOpenList();
        }


        this.path.add(0, this.now);
        while (this.now.x != this.startX || this.now.y != this.startY) {
            this.now = this.now.parent;
            this.path.add(0, this.now);
        }
        return this.path;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addNeighborsToOpenList() {
        Node node;
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {

                if (!this.diagAllowed && x != 0 && y != 0) {
                    continue; // skip if diagonal movement is not allowed
                }
                node = new Node(this.now, this.now.x + x, this.now.y + y , this.now.g, distance(x,y));
                if ((x != 0 || y != 0) // not this.now
                        && this.now.x + x >= 0 && this.now.x + x < this.map[0].length // check maze boundaries
                        && this.now.y + y >= 0 && this.now.y + y < this.map.length
                        && this.map[this.now.y + y][this.now.x + x] != 1 // check if square is walkable
                        && !findNeighborInList(this.open, node) && !findNeighborInList(this.closed, node)) { // if not already done
                    node.g = node.parent.g + 1.; // Horizontal/vertical cost = 1.0
                    node.g += map[this.now.y + y][this.now.x + x]; // add movement cost for this square

                    // diagonal cost = sqrt(hor_cost² + vert_cost²)
                    // in this example the cost would be 12.2 instead of 11
                        /*
                        if (diag && x != 0 && y != 0) {
                            node.g += .4;	// Diagonal movement cost = 1.4
                        }
                        */
                    this.open.add(node);
                }
            }
        }
        Collections.sort(this.open);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean findNeighborInList(List<Node> array, final Node node) {
        return array.stream().anyMatch(new Predicate<Node>() {
            @Override
            public boolean test(Node n) {
                return (n.x == node.x && n.y == node.y);
            }
        });
    }

    private double distance(int dx, int dy) {
        if (this.diagAllowed) { // if diagonal movement is alloweed
            return Math.hypot(this.now.x + dx - this.endX, this.now.y + dy - this.endY); // return hypothenuse
        } else {
            return Math.abs(this.now.x + dx - this.endX) + Math.abs(this.now.y + dy - this.endY); // else return "Manhattan distance"
        }
    }

}
// Node class for convienience
class Node implements Comparable {
    public Node parent;
    public int x, y;
    public double g;
    public double h;
    Node(Node parent, int xpos, int ypos, double g, double h) {
        this.parent = parent;
        this.x = xpos;
        this.y = ypos;
        this.g = g;
        this.h = h;
    }
    // Compare by f value (g + h)
    @Override
    public int compareTo(Object o) {
        Node that = (Node) o;
        return (int)((this.g + this.h) - (that.g + that.h));
    }

}

