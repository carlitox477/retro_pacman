package Behaviour_Scatter;

public class ScatterTopLeftCorner extends ScatterBehaviour {

    private static int[] cornerDirectionsBase = {3,3,2,2,1,1,1,0,0,3,3};
    public ScatterTopLeftCorner(){
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