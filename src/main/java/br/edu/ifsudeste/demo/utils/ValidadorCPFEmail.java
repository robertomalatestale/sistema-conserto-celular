package br.edu.ifsudeste.demo.util;

public class ValidadorCPFEmail {

    public static void validarCPF(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório.");
        }

        String cpfLimpo = cpf.replaceAll("\\D", "");

        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter 11 dígitos.");
        }

        if (cpfLimpo.matches("(\\d)\\1{10}")) {
            throw new IllegalArgumentException("CPF inválido (dígitos repetidos).");
        }

        int soma = 0;
        int resto;

        for (int i = 1; i <= 9; i++) {
            soma += Character.getNumericValue(cpfLimpo.charAt(i - 1)) * (11 - i);
        }

        resto = (soma * 10) % 11;
        if (resto == 10 || resto == 11) {
            resto = 0;
        }
        if (resto != Character.getNumericValue(cpfLimpo.charAt(9))) {
            throw new IllegalArgumentException("CPF inválido.");
        }

        soma = 0;
        for (int i = 1; i <= 10; i++) {
            soma += Character.getNumericValue(cpfLimpo.charAt(i - 1)) * (12 - i);
        }

        resto = (soma * 10) % 11;
        if (resto == 10 || resto == 11) {
            resto = 0;
        }
        if (resto != Character.getNumericValue(cpfLimpo.charAt(10))) {
            throw new IllegalArgumentException("CPF inválido.");
        }
    }

    public static void validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório.");
        }

        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("Email inválido. Verifique o formato.");
        }
    }
}
