/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsons;

/**
 *
 * @author veri
 */
public class ActivationJSON {

//    private boolean activate;
    private int duration;

    /**
     * @param activate
     * @param duration
     */
    public ActivationJSON(int duration) {
//        this.activate = activate;
        this.duration = duration;
    }

//    public boolean isActivate() {
//        return activate;
//    }
//
//    public void setActivate(boolean activate) {
//        this.activate = activate;
//    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
