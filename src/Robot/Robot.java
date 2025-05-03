package Robot;

public class Robot {

    public int posicionX;
    public int posicionY;
    public direccion direccionActual;

   public enum direccion {R, D, L, U}


    public Robot( int x, int y, direccion dir) {
        this.posicionX = x;
        this.posicionY = y;
        direccionActual = dir;
    }
}
