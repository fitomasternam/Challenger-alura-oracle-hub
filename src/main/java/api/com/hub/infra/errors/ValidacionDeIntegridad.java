package api.com.hub.infra.errors;

public class ValidacionDeIntegridad extends RuntimeException {
    public ValidacionDeIntegridad(String s) {

        super(s);
    }
}