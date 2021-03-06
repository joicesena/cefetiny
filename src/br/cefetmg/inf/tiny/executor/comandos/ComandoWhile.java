package br.cefetmg.inf.tiny.executor.comandos;

import br.cefetmg.inf.calculadora.AnalisadorExpressao;
import br.cefetmg.inf.calculadora.Calculadora;
import br.cefetmg.inf.tiny.estruturasDados.Fila;
import br.cefetmg.inf.tiny.estruturasDados.Pilha;
import br.cefetmg.inf.tiny.excecoes.ExcecaoEntradaInvalida;
import br.cefetmg.inf.tiny.excecoes.ExcecaoExpressaoInvalida;
import br.cefetmg.inf.tiny.excecoes.ExcecaoFilaVazia;
import br.cefetmg.inf.tiny.excecoes.ExcecaoListaVazia;
import br.cefetmg.inf.tiny.excecoes.ExcecaoPilhaVazia;
import br.cefetmg.inf.tiny.executor.Executor;

public final class ComandoWhile extends Comando {

    Fila filaComandosWhile;
    String expressaoWhile;

    public ComandoWhile(Fila filaComandoAtual) throws ExcecaoFilaVazia, ExcecaoPilhaVazia, ExcecaoPilhaVazia, ExcecaoExpressaoInvalida, ExcecaoEntradaInvalida, ExcecaoListaVazia {
        super(filaComandoAtual);

        filaComandosWhile = new Fila();
        expressaoWhile = "";

        analisa();
        executaComando();
    }

    @Override
    public void executaComando() throws ExcecaoFilaVazia, ExcecaoPilhaVazia, ExcecaoExpressaoInvalida, ExcecaoEntradaInvalida, ExcecaoListaVazia {
        Pilha pilhaWhile = new Pilha();
        Pilha pBase;
        pilhaWhile.empilha("while");

        boolean acabouWhile = false;

        // coloca os elementos da fila recebida na fila de execução interna do while
        while (!acabouWhile) {
            Object elementoFilaExecucao = filaComandoAtual.removeFila();
            if (((String) elementoFilaExecucao).equals("endwhile")) {
                pilhaWhile.desempilha();
                if (pilhaWhile.pilhaVazia()) {
                    acabouWhile = true;
                } else {
                    filaComandosWhile.insereFila(elementoFilaExecucao);
                }
            } else if (((String) elementoFilaExecucao).equals("while")) {
                pilhaWhile.empilha(elementoFilaExecucao);
                filaComandosWhile.insereFila(elementoFilaExecucao);
            } else {
                filaComandosWhile.insereFila(elementoFilaExecucao);
            }
        }
        
        Fila filaExecucaoWhile = new Fila();

        // executa o while
        boolean continuaWhile = true;
        while (continuaWhile) {
            copiaFila(filaComandosWhile, filaExecucaoWhile);
            // analisa expressão
            // se retornar true, continua rodando o while
            if ((((Calculadora.iniciaCalculadora(expressaoWhile))).toString()).equals("true")) {
                Executor.executaPrograma(filaExecucaoWhile);
            } else {
                continuaWhile = false;
            }
        }
        
        if(!filaComandoAtual.filaVazia()) {
            Executor.executaPrograma(filaComandoAtual);
        }
    }

    @Override
    public void analisa() throws ExcecaoFilaVazia, ExcecaoExpressaoInvalida, ExcecaoPilhaVazia {
        // recebeu uma fila
        Object temp;
        String parametro;
        Pilha pBase;

        // temp = while
        temp = filaComandoAtual.removeFila();

        // temp = expressao
        temp = filaComandoAtual.removeFila();

        parametro = temp.toString();
        parametro = parametro.substring(1, (parametro.length() - 1));

        expressaoWhile = parametro;
        pBase = Calculadora.formataAnalisaExpressao(expressaoWhile);

        if (!(AnalisadorExpressao.tipoExpressao(pBase).equals("l"))
                && !(parametro.equals("true")) && !(parametro.equals("false"))) {
            throw new ExcecaoExpressaoInvalida("Comando 'while': possui resultado não booleano");
        }

        // temp = do
        temp = filaComandoAtual.removeFila();

    }

    public void copiaFila(Fila filaOriginal, Fila filaCopia) throws ExcecaoFilaVazia {
        Fila filaTemp = new Fila();
        Object temp;
        while (!filaOriginal.filaVazia()) {
            temp = filaOriginal.removeFila();
            filaCopia.insereFila(temp);
            filaTemp.insereFila(temp);
        }
        while (!filaTemp.filaVazia()) {
            temp = filaTemp.removeFila();
            filaOriginal.insereFila(temp);
        }
    }

}
