package Luz;

import Robot.Robot;

public class Luz {
    public int posicionX;
    public int posicionY;
    public boolean encendido;

    public Luz(int x, int y, boolean encendido) {
        this.posicionX = x;
        this.posicionY = y;
        this.encendido = encendido;
    }
}
