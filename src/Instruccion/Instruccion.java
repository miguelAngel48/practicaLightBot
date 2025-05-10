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
        int vecesReoeat = parametro;
        for (int i = 0; i < vecesReoeat; i++) {
            this.subComandos.addAll(comando);
        }

    }

    public void addLista(List<Instruccion> listComando) {
        this.subComandos.addAll(listComando);
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


