package Instruccion;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Instruccion {
    public String name;
    public String nameFunction;
    public int parametro;
    public List<Instruccion> subComandos;


    public Instruccion(String name) {
        this.name = name;
        if (this.name.startsWith("REPEAT")) {
            parametro = retornarParametroYName();
            this.subComandos = new ArrayList<>();
        }
        if (this.name.startsWith("FUNCTION")) {
            nameFunction = nombreDeFuncion();
        }

    }

    private String nombreDeFuncion() {
        String[] nombreYFuncion = this.name.split(" ");
        this.name = nombreYFuncion[0];
        return nombreYFuncion[1];
    }

    public Instruccion(Instruccion otra) {
        this.name = otra.name;
        this.parametro = otra.parametro;
        this.nameFunction = otra.nameFunction;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Instruccion that)) return false;
        return Objects.equals(nameFunction, that.nameFunction);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nameFunction);
    }
}


