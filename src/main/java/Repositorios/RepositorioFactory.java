package Repositorios;

public final class RepositorioFactory {

    private RepositorioFactory() { }

    public static IRepositorioResponsable getRepositorioResponsable() {
        return new RepositorioResponsableBD();
    }

    public static IRepositorioFacturas getRepositorioFacturas() {
        return new RepositorioFacturasBD();
    }
}