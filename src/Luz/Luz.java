package Luz;

import Robot.Robot;

import java.util.Objects;

public class Luz {
    public int posicionX;
    public int posicionY;
    public boolean encendido;

    public Luz(int x, int y, boolean encendido) {
        this.posicionX = x;
        this.posicionY = y;
        this.encendido = encendido;
    }

    //sobrescribimos los metodos de equals para poder comprobar las luces que requiere el mapa a la hora de añadir en mapa
    // y saber si  estan encendidas o no, mas aparte si es la misma posición en la matriz.
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Luz luz)) return false;
        return posicionX == luz.posicionX && posicionY == luz.posicionY && encendido == luz.encendido;
    }

    // Regla clave:
    //
    //Todo lo que entra en equals() debe entrar en hashCode() (y nada más).
    @Override
    public int hashCode() {
        return Objects.hash(posicionX, posicionY, encendido);
    }
}
