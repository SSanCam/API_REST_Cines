package com.es.diecines.error;

/**
 * Clase que almacena información sobre el error acontecido
 */
public class ErrorGenerico {


    private String mensaje;
    private String uri;

    public ErrorGenerico() {
    }

    public ErrorGenerico(String mensaje, String uri) {
        this.mensaje = mensaje;
        this.uri = uri;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "ErrorGenerico{" +
                "mensaje='" + mensaje + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }

}
