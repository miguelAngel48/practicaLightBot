import Luz.Luz;
import Robot.Robot;

import java.util.ArrayList;
import java.util.List;

public class LightBot {
    String[] mapa;
    Robot robot;
    char[][] mapeo;
    List<Luz> luces = new ArrayList<>();
    boolean repeatAbierto = false;

    LightBot(String[] mapa) {
        //En el constructor identificamos el 'mapa' y lo convertimos en una matriz para poder recorrerla y posteriormente identificar los elementos que contiene.
        this.mapa = mapa;
        this.mapeo = mapaCreator(mapa);

    }

    void runProgram(String[] comandos) {
        List<String> subComandos = new ArrayList<>();
        for (String comando : comandos) {
            //Llamamos al método ordenesJugador pasando la String de la orden concreta para ejecutar dependiendo de cual es la orden y la lista de luces para saber cual es la 'bombilla'
            // que encendemos con el comando 'LIGHT' cambiando la variable booleana de 'encendido'.
            if (comando.contains("REPEAT") || repeatAbierto) {
                repeatAbierto = true;
                int repeticiones = valorRepetir(comando);
                if (!comando.equals("ENDREPEAT")) {
                    subComandos.add(comando);
                }

                if (comando.equals("ENDREPEAT")) {
                    repeatAbierto = false;
                    for (int i = 0; i < repeticiones; i++) {
                        for (String comandoRepeat : subComandos) {
                            robot.ordenesJugador(comandoRepeat, luces, this.mapeo);
                            actualizarMapa();
                        }
                    }

                }
            } else {
                robot.ordenesJugador(comando, luces, this.mapeo);
                actualizarMapa();
            }
        }
    }

    private int valorRepetir(String comando) {
        String[] partesComando = comando.split(" ");
        return Integer.parseInt(partesComando[1]);
    }


    void actualizarMapa() {
        for (int i = 0; i < mapeo.length; i++) {
            for (int j = 0; j < mapeo[i].length; j++) {
                char elemento = mapeo[i][j];
                mapeo[i][j] = actualizarPasosRobot(elemento, i, j);
            }
        }
    }

    private char actualizarPasosRobot(char pasos, int x, int y) {
        //Como el nombre indica actualizamos los pasos del robot y el estado de las bombillas
        switch (pasos) {
            case 'U', 'R', 'D', 'L':
                return '.';
            case 'O':
                if (encontrarLuzEncendida(x, y)) return 'X';
            default:
                return pasos;
        }
    }

    private boolean encontrarLuzEncendida(int x, int y) {
        for (Luz luz : this.luces) {
            if (x == luz.posicionX && y == luz.posicionY && luz.encendido) {
                return true;
            }
        }
        return false;
    }

    public char[][] mapaCreator(String[] mapa) {
        //Creamos la matriz a partir de la String aportada por parámetro.
        char[][] mapeo = new char[mapa.length][mapa[0].length()];
        for (int i = 0; i < mapa.length; i++) {
            for (int j = 0; j < mapa[i].length(); j++) {
                char valor = mapa[i].charAt(j);
                mapeo[i][j] = valor;
            }
        }
        encontrarPosicionesElementos(mapeo);
        return mapeo;
    }

    void encontrarPosicionesElementos(char[][] mapeo) {
        //identificamos los elementos del mapa para poder clasificarlos y poder manipularlos.
        for (int i = 0; i < mapeo.length; i++) {
            for (int j = 0; j < mapeo[0].length; j++) {
                char elemento = mapeo[i][j];
                switch (elemento) {
                    case 'R', 'D', 'L', 'U':
                        Robot.direccion dir = seleccionDireccion(elemento);
                        this.robot = new Robot(i, j, dir);
                        break;
                    case 'O':
                        luces.add(new Luz(i, j, false));
                        break;
                }
            }

        }
    }

    private static Robot.direccion seleccionDireccion(char elemento) {
        //Una función auxiliar para identificar cual es la dirección del Robot
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


    public String[] getMap() {
        //Para comprobar que el mapa coincide hay que retornar un Array de Strings con esta función convertidos la matriz en un array de Strings otra vez
        String[] mapaRetornado = new String[this.mapa.length];
        char[][] mapaPreparado = this.mapeo;
        for (int i = 0; i < mapaRetornado.length; i++) {
            String elementos = "";
            for (int j = 0; j < mapaPreparado[i].length; j++) {
                char elemento = mapaPreparado[i][j];

                elementos += elemento;
                mapaRetornado[i] = elementos;
            }
        }
        return mapaRetornado;
    }

    public int[] getRobotPosition() {
        return new int[]{robot.posicionY, robot.posicionX};

    }

    void reset() {
        //Reiniciamos el mapa y la lista de luces, sino acumularia del mismo mapa cada vez que repitamos el reset
        // e iniciemos el mapa otra vez.
        luces.clear();
        this.mapeo = mapaCreator(mapa);
    }


}
