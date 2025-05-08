package Robot;

import Instruccion.Instruccion;
import Luz.Luz;

import java.util.List;

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

    public void ordenesJugador(String orden, List<Luz> luces, char[][] mapa) {
        switch (orden) {
            case "FORWARD":
                compruebaDireccionActualRobot(direccionActual, mapa);
                break;
            case "RIGHT":
                direccionActual = rotarRobot(true);
                break;
            case "LEFT":
                direccionActual = rotarRobot(false);
                break;
            case "LIGHT":
                encenderLuz(luces);
                break;

        }
    }


    private boolean saleDelMapa(char[][] mapa) {
        return (posicionY < 0 || posicionX < 0 || posicionY >= mapa[0].length || posicionX >= mapa.length);
    }

    public void encenderLuz(List<Luz> luces) {
        for (Luz luz : luces) {
            if (this.posicionX == luz.posicionX && this.posicionY == luz.posicionY) {
                luz.encendido = !luz.encendido;

                break;
            }
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

    private void compruebaDireccionActualRobot(direccion dir, char[][] mapa) {

        switch (dir) {
            case U:
                posicionX--;  // Subimos una fila
                if (saleDelMapa(mapa)) posicionX = (mapa.length - 1); // Volvemos por el lado opuesto de la fila
                break;
            case D:
                posicionX++;  // Bajamos una fila
                if (saleDelMapa(mapa)) posicionX = 0; // Volvemos por el lado opuesto de la fila
                break;
            case L:
                posicionY--;  // Movemos a la izquierda
                if (saleDelMapa(mapa))
                    posicionY = (mapa[0].length - 1); // Volvemos por el lado opuesto del lado izquierdo
                break;
            case R:
                posicionY++;
                // Movemos a la derecha
                if (saleDelMapa(mapa)) posicionY = 0; // Volvemos por el lado opuesto del lado derecho
                break;
            default:
                // Por si acaso (no debería ocurrir)
                throw new IllegalArgumentException("Dirección desconocida: " + dir);

        }

    }

    @Override
    public String toString() {
        return "Robot{" +
                "posicionX=" + posicionX +
                ", posicionY=" + posicionY +
                ", direccionActual=" + direccionActual +
                '}';
    }
}
