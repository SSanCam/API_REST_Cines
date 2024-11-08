package com.es.diecines.error;

public class BaseDeDatosExceptioin extends RuntimeException {

    private static final String DESCRIPCION = "Error en la base de datos";

    public BaseDeDatosExceptioin(String detalles) {
        super(DESCRIPCION + ". " + detalles);
    }

}
