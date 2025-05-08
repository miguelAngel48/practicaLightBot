package Instruccion;


import java.util.List;

public class Instruccion {
    public String name;
    int parametro;
    public List<String> subComandos;

    public Instruccion(String name) {
        this.name = name;

    }


    public int retornarParametroYName() {
        String[] nameYParametro = this.name.split(" ");
        this.name = nameYParametro[0];
        return this.parametro = Integer.parseInt(nameYParametro[1]);
    }


    public void setLista(List<String> comando) {
        this.subComandos = comando;
    }

}


