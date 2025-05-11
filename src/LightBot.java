import Instruccion.Instruccion;
import Luz.Luz;
import Robot.Robot;

import java.util.*;

public class LightBot {
    String[] mapa;
    Robot robot;
    char[][] mapeo;
    List<Luz> luces = new ArrayList<>();
    Stack<Instruccion> pilasRepeats = new Stack<>();

    boolean repeatAbierto = false;
    Instruccion repeat;


    LightBot(String[] mapa) {
        //En el constructor identificamos el 'mapa' y lo convertimos en una matriz para poder recorrerla y posteriormente identificar los elementos que contiene.
        this.mapa = mapa;
        this.mapeo = mapaCreator(mapa);

    }


    void runProgram(String[] comandos) {
        List<Instruccion> instComandos = new ArrayList<>();

        for (String comando : comandos) {
            instComandos.add(new Instruccion(comando));
            bucleDelRepeat(instComandos.getLast());

        }

    }

    private void bucleDelRepeat(Instruccion comando) {

        //Llamamos al método ordenesJugador pasando la String de la orden concreta para ejecutar dependiendo de cual es la orden y la lista de luces para saber cual es la 'bombilla'
        // que encendemos con el comando 'LIGHT' cambiando la variable booleana de 'encendido'.
        if (comando.name.startsWith("REPEAT")) {
            pilasRepeats.push(comando);

            repeatAbierto = true;

        } else if (!comando.name.equals("ENDREPEAT") && repeatAbierto) {
            pilasRepeats.peek().addOnList(comando);

        } else if (comando.name.equals("ENDREPEAT")) {
            extraerUltimoDeLaPila();
            if (pilasRepeats.empty()) repeatAbierto = false;

        } else {
            robot.ordenesJugador(comando.name, luces, this.mapeo);
            actualizarMapa();
        }
    }

    private void extraerUltimoDeLaPila() {
        if ((pilasRepeats.size() > 1)) {
            Instruccion ultimaInstruccion = pilasRepeats.pop();
            ultimaInstruccion.setListaRepetidaXVeces(ultimaInstruccion.subComandos);
            pilasRepeats.peek().addLista(ultimaInstruccion.subComandos);

        } else {
            Instruccion ultimaInst = pilasRepeats.pop();
            actuarRobotRepeat(ultimaInst);
        }
    }


    private void actuarRobotRepeat(Instruccion repeat) {
        int nVeces = repeat.parametro;
        for (int i = 0; i < nVeces; i++) {
            for (Instruccion comando : repeat.subComandos) {
                robot.ordenesJugador(comando.name, luces, this.mapeo);
                actualizarMapa();

            }
        }
    }


    public void actualizarMapa() {
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
