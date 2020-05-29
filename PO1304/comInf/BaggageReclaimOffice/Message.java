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
    // Passenger
    // addBag(Integer id, int i);
    public static final int ADD_BAG = 1;

    // goHome(Integer id);
    public static final int GO_HOME = 2;

    
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
     * @param id   identificação do cliente/barbeiro
     */

    public Message(int type, int id) {
        msgType = type;
        if ((msgType == REQCUTH) || (msgType == CUSTID))
            custId = id;
        else
            barbId = id;
    }

    /**
     * Instanciação de uma mensagem (forma 3).
     *
     * @param type   tipo da mensagem
     * @param barbId identificação do barbeiro
     * @param custId identificação do cliente
     */

    public Message(int type, int barbId, int custId) {
        msgType = type;
        this.barbId = barbId;
        this.custId = custId;
    }

    /**
     * Instanciação de uma mensagem (forma 4).
     *
     * @param type  tipo da mensagem
     * @param name  nome do ficheiro de logging
     * @param nIter número de iterações do ciclo de vida dos clientes
     */

    public Message(int type, String name, int nIter) {
        msgType = type;
        fName = name;
        this.nIter = nIter;
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
