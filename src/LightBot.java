import Instruccion.Instruccion;
import Luz.Luz;
import NoLuz.NoLuz;
import Robot.Robot;

import java.util.*;

public class LightBot {
    String[] mapa;
    Robot robot;
    NoLuz noLuz;
    char[][] mapeo;
    List<Luz> luces = new ArrayList<>();
    List<NoLuz> noLuces = new ArrayList<>();
    Stack<Instruccion> pilasRepeats = new Stack<>();
    boolean bodyFunction = false;
    boolean repeatAbierto = false;
    String nombreFuncion;
    Map<String, List<Instruccion>> funcionesMap = new HashMap<>();
    List<Instruccion> instComandos = new ArrayList<>();
    Stack<String> pilaFunciones = new Stack<>();

    LightBot(String[] mapa) {
        //En el constructor identificamos el 'mapa' y lo convertimos en una matriz para poder recorrerla y posteriormente identificar los elementos que contiene.
        this.mapa = mapa;
        this.mapeo = mapaCreator(mapa);

    }


    void runProgram(String[] comandos) {

        for (String comando : comandos) {
            instComandos.add(new Instruccion(comando));
            indentificarInstruccion(comando);


        }

    }

    private void indentificarInstruccion(String comando) {

        if (crearFunciones(comando, pilaFunciones, instComandos, funcionesMap)) ;
        else if (llamadaDeFuncion(instComandos.getLast(), funcionesMap)) ;
        else identificarInstrucciones(instComandos.getLast());
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
            robot.ordenesJugador(comando.name, luces, this.mapeo, noLuces);
            actualizarMapa();
        }
    }

    private boolean llamadaDeFuncion(Instruccion comando, Map<String, List<Instruccion>> funcionesMap) {
        if (comando.name.startsWith("CALL")) {
            int parametro = comando.parametro.parametroNumPadre;
            List<Instruccion> nombreFuncion = encuentraLlamadaFuncion(comando, funcionesMap);

            for (Instruccion instruccionFuncion : nombreFuncion) {
                if (comando.parametro.parametroNumPadre != 0) {
                    comando.calledByFunction = true;
                    instruccionFuncion.setParametroNum(parametro);
                }

                identificarInstrucciones(instruccionFuncion);
            }
            return true;
        }
        return false;
    }

    private List<Instruccion> encuentraLlamadaFuncion(Instruccion comando, Map<String, List<Instruccion>> funcionesMap) {
        return funcionesMap.get(comando.nameFunction);

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
        int nVeces = repeat.getParametroNum();
        for (int i = 0; i < nVeces; i++) {

            for (Instruccion comando : repeat.subComandos) {
                if (comando.name.equals("CALL")) llamadaDeFuncion(comando, funcionesMap);
                else {
                    robot.ordenesJugador(comando.name, luces, this.mapeo, noLuces);
                    actualizarMapa();
                }

            }
        }
    }


    public void actualizarMapa() {
        for (int i = 0; i < mapeo.length; i++) {
            for (int j = 0; j < mapeo[i].length; j++) {
                char elemento = mapeo[i][j];
                mapeo[i][j] = actualizarPasosRobot(elemento, i, j);
                System.out.printf("%s", mapeo[i][j]);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("---------------");
        System.out.println();
    }

    private char actualizarPasosRobot(char pasos, int x, int y) {
        //Como el nombre indica actualizamos los pasos del robot y el estado de las bombillas

        if (encontrarLuzEncendida(x, y)) {
            return 'X'; // Prioridad máxima: luz encendida
        }  // Segundo: intento de luz
        else if (pasos == 'U' || pasos == 'R' || pasos == 'D' || pasos == 'L' || pasos == '.') {
            if (encontrarIntentoLuz(x, y)) {
                return 'x';
            } else {
                return '.'; // Tercero: limpia la posición del robot
            }

        } else {
            return pasos; // Si nada cambió, deja lo que había
        }
    }


    private boolean encontrarIntentoLuz(int x, int y) {
        for (NoLuz noluzs : this.noLuces) {
            if (x == noluzs.x && y == noluzs.y && noluzs.intento) {
                return true;
            }
        }
        return false;
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
                        noLuces.add(new NoLuz(i, j, false));
                        this.robot = new Robot(i, j, dir);
                        break;
                    case 'O':
                        luces.add(new Luz(i, j, false));
                        break;
                    case '.':

                        noLuces.add(new NoLuz(i, j, false));
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
