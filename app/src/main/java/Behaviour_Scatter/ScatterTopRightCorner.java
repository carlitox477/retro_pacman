package Behaviour_Scatter;

public class ScatterTopRightCorner extends ScatterBehaviour {
    private static int[] cornerDirectionsBase = {1,1,2,2,3,3,3,0,0,1,1};

    public ScatterTopRightCorner(){
        this.setCorners();
        inCorner = false;
        step = 0;
        matchX=15;
        matchY=1;
    }

    private void setCorners(){
        cornerDirections = new int[cornerDirectionsBase.length];
        for(int i=0;i<cornerDirectionsBase.length;i++){
            cornerDirections[i]=cornerDirectionsBase[i];
        }
    }
}