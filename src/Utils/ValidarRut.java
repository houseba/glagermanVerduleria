/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

/**
 *
 * @author Bruno
 */
public class ValidarRut {
    public static boolean esValido(String rut) {
        if (rut == null || rut.isEmpty()) return false;

        // Eliminar puntos y guiones
        rut = rut.replace(".", "").replace("-", "").toUpperCase();

        // Validar formato general (7 u 8 dígitos + dígito verificador)
        if (!rut.matches("\\d{7,8}[0-9K]")) return false;

        String cuerpo = rut.substring(0, rut.length() - 1);
        char dv = rut.charAt(rut.length() - 1);

        int suma = 0;
        int multiplicador = 2;

        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            suma += Character.getNumericValue(cuerpo.charAt(i)) * multiplicador;
            multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
        }

        int resto = 11 - (suma % 11);
        char dvEsperado;

        switch (resto) {
            case 11:
                dvEsperado = '0';
                break;
            case 10:
                dvEsperado = 'K';
                break;
            default:
                dvEsperado = Character.forDigit(resto, 10);
                break;
        }

        return dv == dvEsperado;
    }
    
}
