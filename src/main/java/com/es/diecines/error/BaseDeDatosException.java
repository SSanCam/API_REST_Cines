package com.es.diecines.error;

public class BaseDeDatosException extends RuntimeException {

    private static final String DESCRIPCION = "Error en la base de datos";

    public BaseDeDatosException(String detalles) {
        super(DESCRIPCION + ". " + detalles);
    }

    public BaseDeDatosException(String detalles, Throwable cause) {
        super(DESCRIPCION + ". " + detalles, cause);
    }
}
