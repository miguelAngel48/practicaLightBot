package Robot;

public class Robot {

    public int posicionX;
    public int posicionY;
    public direccion direccionActual;

    public enum direccion {R, D, L, U}


    public Robot(int x, int y, direccion dir) {
        this.posicionX = x;
        this.posicionY = y;
        direccionActual = dir;
    }

    public void ordenesJugador(String orden) {
        switch (orden) {
            case "FORWARD":
                compruebaDireccionActualRobot(direccionActual);
            case "RIGHT":
                direccionActual = rotarRobot(true);
            case "LEFT":
                direccionActual = rotarRobot(false);
            case "LIGHT":


        }
    }

    private direccion rotarRobot(boolean giro) {
        direccion[] direcciones = direccion.values();
        int n = direcciones.length;
        int nuevaDireccion = (giro) ? (direccionActual.ordinal() + 1) % n : (direccionActual.ordinal() - 1) % n;
        if (nuevaDireccion < 0) {
            nuevaDireccion += n;
        }
        return direcciones[nuevaDireccion];
    }

    private void compruebaDireccionActualRobot(direccion dir) {
        switch (dir) {
            case U: // Up
                posicionX--;  // Subimos una fila
                break;
            case D: // Down
                posicionX++;  // Bajamos una fila
                break;
            case L: // Left
                posicionY--;  // Movemos a la izquierda
                break;
            case R: // Right
                posicionY++;  // Movemos a la derecha
                break;
            default:
                // Por si acaso (no debería ocurrir)
                throw new IllegalArgumentException("Dirección desconocida: " + dir);
        }

    }
}
