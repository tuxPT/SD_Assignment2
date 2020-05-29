package comInf;

import java.io.*;

/**
 * Este tipo de dados define as mensagens que são trocadas entre os clientes e o
 * servidor numa solução do Problema dos Barbeiros Sonolentos que implementa o
 * modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento
 * estático dos threads barbeiro. A comunicação propriamente dita baseia-se na
 * troca de objectos de tipo Message num canal TCP.
 */

public class Message implements Serializable {
    /**
     * Chave de serialização
     */

    private static final long serialVersionUID = 1001L;

    /* Tipos das mensagens */

    // PASSENGER
    // enterTheBus(Integer Passenger_ID);
    public static final int ENTER_BUS = 1;

    // BUS_DRIVER
    // announcingBusBoarding();
    public static final int ANN_BUS_BOARD = 2;

    // goToDepartureTerminal();
    public static final int GO2DEP_TERMINAL = 3;

    // endOfWork();
    public static final int END_OF_WORK = 4;

    /* Campos das mensagens */

    /**
     * Tipo da mensagem
     */

    private int msgType = -1;

    /**
     * Identificação do cliente
     */

    private int custId = -1;

    /**
     * Identificação do barbeiro
     */

    private int barbId = -1;

    /**
     * Nome do ficheiro de logging
     */

    private String fName = null;

    /**
     * Número de iterações do ciclo de vida dos clientes
     */

    private int nIter = -1;

    /**
     * Instanciação de uma mensagem (forma 1).
     *
     * @param type tipo da mensagem
     */

    public Message(int type) {
        msgType = type;
    }

    /**
     * Instanciação de uma mensagem (forma 2).
     *
     * @param type tipo da mensagem
     * @param id   passenger's ID
     */

    public Message(int type, int id) {
        msgType = type;
        this.id = id;
    }

    
    /**
     * Obtenção do valor do campo tipo da mensagem.
     *
     * @return tipo da mensagem
     */

    public int getType() {
        return (msgType);
    }

    /**
     * Obtenção do valor do campo identificador do cliente.
     *
     * @return identificação do cliente
     */

    public int getCustId() {
        return (custId);
    }

    /**
     * Obtenção do valor do campo identificador do barbeiro.
     *
     * @return identificação do barbeiro
     */

    public int getBarbId() {
        return (barbId);
    }

    /**
     * Obtenção do valor do campo nome do ficheiro de logging.
     *
     * @return nome do ficheiro
     */

    public String getFName() {
        return (fName);
    }

    /**
     * Obtenção do valor do campo número de iterações do ciclo de vida dos clientes.
     *
     * @return número de iterações do ciclo de vida dos clientes
     */

    public int getNIter() {
        return (nIter);
    }

    /**
     * Impressão dos campos internos. Usada para fins de debugging.
     *
     * @return string contendo, em linhas separadas, a concatenação da identificação
     *         de cada campo e valor respectivo
     */

    @Override
    public String toString() {
        return ("Tipo = " + msgType + "\nId Cliente = " + custId + "\nId Barbeiro = " + barbId
                + "\nNome Fic. Logging = " + fName + "\nN. de Iteracoes = " + nIter);
    }
}
