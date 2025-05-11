package Instruccion;


import java.util.ArrayList;
import java.util.List;

public class Instruccion {
    public String name;
    public int parametro;
    public List<Instruccion> subComandos;


    public Instruccion(String name) {
        this.name = name;
        if (this.name.startsWith("REPEAT")) {
            retornarParametroYName();
            this.subComandos = new ArrayList<>();
        }

    }
    public Instruccion(Instruccion otra) {
        this.name = otra.name;
        this.parametro = otra.parametro;
        this.subComandos = new ArrayList<>();
        if (otra.subComandos != null) {
            for (Instruccion subInstruccion : otra.subComandos) {
                this.subComandos.add(new Instruccion(subInstruccion)); // Recursivo
            }
        }
    }
    public int retornarParametroYName() {
        String[] nameYParametro = this.name.split(" ");
        this.name = nameYParametro[0];
        return this.parametro = Integer.parseInt(nameYParametro[1]);

    }

    public String getName() {
        return name;
    }

    public void clearList() {
        subComandos.clear();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addOnList(Instruccion comandoIn) {
        this.subComandos.add(comandoIn);
    }

    public void setListaRepetidaXVeces(List<Instruccion> comando) {
        int vecesRepeat = parametro;
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
}


