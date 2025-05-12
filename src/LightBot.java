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
    boolean bodyFunction = false;
    boolean repeatAbierto = false;
    String nombreFuncion;


    LightBot(String[] mapa) {
        //En el constructor identificamos el 'mapa' y lo convertimos en una matriz para poder recorrerla y posteriormente identificar los elementos que contiene.
        this.mapa = mapa;
        this.mapeo = mapaCreator(mapa);

    }


    void runProgram(String[] comandos) {
        Map<String, List<Instruccion>> funcionesMap = new HashMap<>();
        List<Instruccion> instComandos = new ArrayList<>();
        Stack<String> pilaFunciones = new Stack<>();
        for (String comando : comandos) {
            instComandos.add(new Instruccion(comando));

            if (crearFunciones(comando, pilaFunciones, instComandos, funcionesMap)) ;
            else identificarInstrucciones(instComandos.getLast());
            llamadaDeFuncion(instComandos.getLast(), funcionesMap);
        }

    }

    private boolean crearFunciones(String comando, Stack<String> pilaFunciones, List<Instruccion> instComandos, Map<String, List<Instruccion>> funcionesMap) {

        if (comando.startsWith("FUNCTION")) {
            this.nombreFuncion = instComandos.getLast().nameFunction;
            pilaFunciones.push(this.nombreFuncion);
            funcionesMap.put(this.nombreFuncion, new ArrayList<>());
            bodyFunction = true;
            return true;
        } else if (!comando.equals("ENDFUNCTION") && bodyFunction) {
            String ultimoDeLaPila = pilaFunciones.peek();
            funcionesMap.get(ultimoDeLaPila).add(instComandos.getLast());
            return true;
        } else if (comando.equals("ENDFUNCTION")) {
            pilaFunciones.pop();
            if (pilaFunciones.empty()) {
                bodyFunction = false;
            }
            return true;
        }
        return false;
    }

    private void identificarInstrucciones(Instruccion comando) {
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

    private void llamadaDeFuncion(Instruccion comando, Map<String, List<Instruccion>> funcionesMap) {
        if (comando.name.startsWith("CALL")) {
            List<Instruccion> nombreFuncion = encuentraLlamadaFuncion(comando, funcionesMap);
            for (Instruccion instruccionFuncion : nombreFuncion) {
                identificarInstrucciones(instruccionFuncion);
            }
        }
    }

    private List<Instruccion> encuentraLlamadaFuncion(Instruccion comando, Map<String, List<Instruccion>> funcionesMap) {
        String nameFuncion = devolverNombreFuncion(comando.name);
        return funcionesMap.get(nameFuncion);

    }


    private String devolverNombreFuncion(String comando) {
        String[] separarLlamada = comando.split(" ");
        return separarLlamada[1];
    }

    private void extraerUltimoDeLaPila() {
        if ((pilasRepeats.size() > 1)) {
            Instruccion ultimaInstruccion = pilasRepeats.pop();
            ultimaInstruccion.setListaRepetidaXVeces(ultimaInstruccion.subComandos);
            pilasRepeats.peek().addLista(ultimaInstruccion.subComandos);

        } else {
            Instruccion ultimaInst = pilasRepeats.pop();
            actuarRobotRepeat(ultimaInst);
            //importante limpiar la lista sino cada vez que llamemos  con una función que tenga un repeat añadira otra vez la lista al mismo repeat
            ultimaInst.subComandos.clear();
        }
    }


    private void actuarRobotRepeat(Instruccion repeat) {
        int nVeces = repeat.parametro;
        System.out.println("Repetir " + nVeces + " veces con " + repeat.subComandos.size() + " instrucciones.");
        for (int i = 0; i < nVeces; i++) {
            System.out.println(i);
            for (Instruccion comando : repeat.subComandos) {
                System.out.println("Ejecutando: " + comando.name);
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
