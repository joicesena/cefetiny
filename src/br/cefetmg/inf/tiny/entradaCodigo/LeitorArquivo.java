package br.cefetmg.inf.tiny.entradaCodigo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public final class LeitorArquivo {

    private static String caminhoArquivo = null;
    private static File arquivo = null;
    private static Scanner scannerArquivo;
    private static String codigo = null;

    public static void leArquivo(String caminhoArquivoTexto) throws FileNotFoundException {

        caminhoArquivo = caminhoArquivoTexto;
        arquivo = new File(caminhoArquivo);
        scannerArquivo = new Scanner(arquivo, "ISO-8859-1");

        while (scannerArquivo.hasNextLine()) {
            String linha = scannerArquivo.nextLine();
            if (codigo == null) {
                codigo = linha;
            } else {
                codigo += "\n" + linha;
            }
        }

        scannerArquivo.close();
    }

    public static void imprimeCodigo() {
        System.out.println("O arquivo " + caminhoArquivo + " foi encontrado com sucesso!\n\n"
                           + "                         Código do programa                           \n"
                           + "----------------------------------------------------------------------\n"
                           + codigo
                           + "\n----------------------------------------------------------------------\n");
    }

    public static String getCodigo() {
        return codigo;
    }
}
