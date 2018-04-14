/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proceso;

/**
 *
 * @author netbeans
 */
public class Sender extends Thread {
    int mpr, crr;
    public Sender(int msjPorRonda, int cantRondasRealizar) {
        this.mpr = msjPorRonda;
        this.crr = cantRondasRealizar;
    }
    
    @Override
    public void run() {
        
    }    
}
