package isi.deso.gestionhotelera;

import Infraestructura.InicializadorBD;
import Infraestructura.ServidorRest;

public class Gestionhotelera {
    public static void main(String[] args) throws Exception {
        InicializadorBD.inicializar();                
        new ServidorRest(8080).iniciar();
        System.out.println("Abri http://localhost:8080/ en el navegador.");
    }
}