package fct;

import fct.Modelo.Estado;
import java.io.IOException;

/** Puerto acotado de UD1. Un fichero ausente o mal formado es error, no vacío. */
public interface AlmacenFct {
    Estado recuperar() throws IOException;
    void guardar(Estado estado) throws IOException;
}
