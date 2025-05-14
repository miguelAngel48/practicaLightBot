package Instruccion;


import java.util.ArrayList;
import java.util.List;


public class Instruccion {
    public String name;
    public String nameFunction;
    public Parametro parametro;
    public List<Instruccion> subComandos;
    public boolean calledByFunction = false;

    public Instruccion(String name) {
        this.name = name;
        parametro = new Parametro();
        if (this.name.startsWith("REPEAT")) {
            retornarParametroYName();
            this.subComandos = new ArrayList<>();
        }
        if (this.name.startsWith("CALL")) {
            retornarLlamada();
        }
        if (this.name.startsWith("FUNCTION")) {
            nameFunction = nombreDeFuncion();
        }


    }

    private void retornarLlamada() {
        extraerParametro();
        String[] nombreYFuncion = this.name.split(" ");
        this.name = nombreYFuncion[0];
        this.nameFunction = nombreYFuncion[1];
        if (parametro.parametroStr != null) {
            parametro.parametroNumPadre = Integer.parseInt(parametro.parametroStr);


        }


    }


    private String nombreDeFuncion() {
        extraerParametro();
        String[] nombreYFuncion = this.name.split(" ");
        this.name = nombreYFuncion[0];
        return nombreYFuncion[1];
    }

    private void extraerParametro() {
        int inicioP = this.name.indexOf('(');
        int finP = this.name.indexOf(')');
        if (inicioP != -1 && finP != -1 && inicioP < finP) {
            parametro.parametroStr = this.name.substring(inicioP + 1, finP);
            String[] nombreYParametro = this.name.split("\\(");
            this.name = nombreYParametro[0];
        }


    }

    public Instruccion(Instruccion otra) {
        this.name = otra.name;
        this.parametro = otra.parametro;
        this.nameFunction = otra.nameFunction;
        this.subComandos = new ArrayList<>();
        this.parametro.parametroStr = otra.parametro.parametroStr;
        this.calledByFunction = otra.calledByFunction;
        if (otra.subComandos != null) {
            for (Instruccion subInstruccion : otra.subComandos) {
                this.subComandos.add(new Instruccion(subInstruccion));
            }
        }
    }

    public void retornarParametroYName() {
        String[] nameYParametro = this.name.split(" ");
        this.name = nameYParametro[0];
        parametro.parametroStr = nameYParametro[1];
        char c = nameYParametro[1].charAt(0);
        if (Character.isLetter(c)){
            parametro.parametroStr = nameYParametro[1];
        } else {
            parametro.parametroNum = Integer.parseInt(nameYParametro[1]);
        }


    }


    public void addOnList(Instruccion comandoIn) {
        this.subComandos.add(comandoIn);
    }

    public void setListaRepetidaXVeces(List<Instruccion> comando) {
        int vecesRepeat = parametro.parametroNum;
        // Copia segura
        List<Instruccion> copia = new ArrayList<>();
        for (Instruccion inst : comando) {
            copia.add(new Instruccion(inst));
        }
        // Empieza desde uno sino hace una iteración de mas
        for (int i = 1; i < vecesRepeat; i++) {
            for (Instruccion inst : copia) {
                this.subComandos.add(new Instruccion(inst));
            }
        }

    }

    public void addLista(List<Instruccion> listComando) {
        for (Instruccion inst : listComando) {
            this.subComandos.add(new Instruccion(inst));
        }
    }

    @Override
    public String toString() {
        return "Instruccion{" +
                "name='" + name + '\'' +
                ", parametro=" + parametro +
                ", subComandos=" + subComandos +
                '}';
    }

    public int getParametroNum() {
        return parametro.parametroNum;
    }

    public String getParametroStr() {
        return parametro.parametroStr;
    }

    public void setParametroNum(int parametroNum) {
        this.parametro.parametroNum = parametroNum;
    }


}

