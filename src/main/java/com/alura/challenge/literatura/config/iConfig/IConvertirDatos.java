package com.alura.challenge.literatura.config.iConfig;

public interface IConvertirDatos {

    <T> T convertirDatosJsonAJava(String json , Class<T> clase);

}
