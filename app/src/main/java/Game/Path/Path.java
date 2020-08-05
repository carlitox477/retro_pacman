package Game.Path;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Path {
    private PathNode startNode,lastNode;
    private int[] target;

    public Path(PathNode node, int[]target){
        this.startNode=node;
        this.lastNode=node;
        this.target=target;
    }

    public boolean addNode(PathNode node){
        boolean canAddNode;
        PathNode currentNode,previousNode;
        canAddNode=true;
        currentNode=this.startNode;
        while(currentNode!=null&&canAddNode){
            canAddNode=(currentNode.compareTo(node)==-1);
            currentNode=currentNode.getNextNode();
        }
        if(canAddNode){
            this.lastNode.setNextNode(node);
            this.lastNode=node;
        }
        return canAddNode;
    }

    public boolean achiveTargetPosition(){
        return this.lastNode.isPosition(this.target);
    }

    public void draw(Canvas canvas, int blocksize, Paint paint){
        PathNode currentNode;
        currentNode=this.startNode;
        while(currentNode!=null){
            currentNode.draw(canvas,blocksize,paint);
            currentNode=currentNode.getNextNode();
        }

    }

}
