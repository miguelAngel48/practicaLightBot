import Luz.Luz;
import Robot.Robot;

import java.util.ArrayList;
import java.util.List;

public class LightBot {
    String[] mapa;
    Robot robot;
    char[][] mapeo;

    LightBot(String[] mapa) {
        this.mapa = mapa;
        this.mapeo = mapaCreator(mapa);

    }

    void runProgram(String[] comandos) {

    }

    public static char[][] mapaCreator(String[] mapa) {
        char[][] mapeo = new char[mapa.length][mapa[0].length()];
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length(); j++) {
                char valor = mapa[i].charAt(j);
                mapeo[i][j] = valor;
            }
        }
        return mapeo;
    }

    static void encontrarPosicionesElementos(char[][] mapeo) {
        List<Luz> bombillas = new ArrayList<>();
        for (int i = 0; i < mapeo.length; i++) {
            for (int j = 0; j < mapeo[0].length; j++) {
                char elemento = mapeo[i][j];
                switch (elemento) {
                    case 'R', 'D', 'L', 'U':
                        try {
                            Robot.direccion dir = seleccionDireccion(elemento);
                            Robot rb = new Robot(i, j, dir);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        break;
                    case 'O':
                        bombillas.add(new Luz(i, j,false));
                        break;
                    case 'X':

                }
            }
        }

    }

    public static Robot.direccion seleccionDireccion(char elemento) {
        switch (elemento) {
            case 'R':
                return Robot.direccion.R;
            case 'D':
                return Robot.direccion.D;
            case 'L':
                return Robot.direccion.L;
            case 'U':
                return Robot.direccion.U;
        }
        throw new RuntimeException("La dirección del Robot es incorrecta");
    }

    private String ejecutarComando(String comando) {
        return "";
    }

    public int[] getRobotPosition() {
        return new int[]{robot.posicionY, robot.posicionX};

    }

    void reset() {
        this.mapeo = mapaCreator(mapa);
        encontrarPosicionesElementos(this.mapeo);
    }


}
