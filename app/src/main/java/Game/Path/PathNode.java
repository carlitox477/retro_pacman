package Game.Path;

import android.graphics.Canvas;
import android.graphics.Paint;

public class PathNode implements Comparable<PathNode> {
    private PathNode nextNode;
    private int [] mapPosition;
    private char direction;
    private boolean finalNode;

    public PathNode(int[]mapPosition,char direction){
        this.mapPosition=mapPosition;
        this.direction=direction;
        this.nextNode=null;
        this.finalNode=true;
    }

    public void setNextNode(PathNode nextNode){
        this.nextNode=nextNode;
        this.finalNode=false;
    }

    public PathNode getNextNode(){
        return this.nextNode;
    }

    public void draw(Canvas canvas,int blocksize, Paint paint){
        int xHalfPosScreen,yHalfPosScreen;

        yHalfPosScreen=this.mapPosition[0]*blocksize+blocksize/2;
        xHalfPosScreen=this.mapPosition[1]*blocksize+blocksize/2;

        if(!this.finalNode)
        switch (this.direction){
            case 'u':
                canvas.drawLine(xHalfPosScreen,yHalfPosScreen,xHalfPosScreen,yHalfPosScreen+blocksize,paint);
                break;
            case 'd':
                canvas.drawLine(xHalfPosScreen,yHalfPosScreen,xHalfPosScreen,yHalfPosScreen-blocksize,paint);
                break;
            case 'r':
                canvas.drawLine(xHalfPosScreen,yHalfPosScreen,xHalfPosScreen+blocksize,yHalfPosScreen,paint);
                break;
            case 'l':
                canvas.drawLine(xHalfPosScreen,yHalfPosScreen,xHalfPosScreen-blocksize,yHalfPosScreen,paint);
                break;
            default:
                break;
        }
    }

    public boolean isPosition(int[]position){
        return this.mapPosition[0]==position[0] && this.mapPosition[1]==position[1];
    }

    @Override
    public int compareTo(PathNode node) {
        int sal;
        sal=-1;
        if(this.mapPosition[0]==node.mapPosition[0]&&this.mapPosition[1]==node.mapPosition[1]){
            sal=0;
        }
        return sal;
    }
}
