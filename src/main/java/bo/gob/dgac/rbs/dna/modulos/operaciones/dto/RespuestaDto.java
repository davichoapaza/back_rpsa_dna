package bo.gob.dgac.rbs.dna.modulos.operaciones.dto;


public class RespuestaDto<T> {
	private boolean exito;
    private String mensaje;
    private T datos;

    public RespuestaDto(boolean exito, String mensaje, T datos) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.datos = datos;
    }

    public static <T> RespuestaDto<T> exito(String mensaje, T datos) {
        return new RespuestaDto<>(true, mensaje, datos);
    }

    public static <T> RespuestaDto<T> error(String mensaje) {
        return new RespuestaDto<>(false, mensaje, null);
    }

    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public T getDatos() { return datos; }
    public void setDatos(T datos) { this.datos = datos; }

}
