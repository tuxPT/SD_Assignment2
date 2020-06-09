package comInf.BaggageReclaimOffice;

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
    /* Passenger */
    /**
     * addbag(Integer id, int i)
     */
    public static final int ADD_BAG = 1;

    /**
     * goHome(Integer id)
     */ 
    public static final int GO_HOME = 2;

    /* Campos das mensagens */

    /**
     * Tipo da mensagem
     */

    private int msgType = -1;

    /**
     * Identificador do passageiro
     */
    private int passengerId = -1;

     /**
     * Número de malas perdidas
     */

    private int numberOfLostBags = -1;

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
     * @param id id do passageiro
     * @param number_of_bags número de malas perdidas
     */

    public Message(int type, Integer id, int number_of_bags) {
        msgType = type;
        passengerId = id;
        numberOfLostBags = number_of_bags;
    }

    /**
     * Obtenção do valor do campo tipo da mensagem.
     *
     * @return tipo da mensagem
     */

    public int getType() {
        return msgType;
    }

    /**
     * Obtenção do valor do campo identificador do passageiro.
     *
     * @return identificação do passageiro
     */

    public int getPassengerId() {
        return passengerId;
    }

    /**
     * Obtenção do valor do campo do número de malas
     *
     * @return número de malas
     */

    public int getNumberOfBags() {
        return numberOfLostBags;
    }

    /**
     * Impressão dos campos internos. Usada para fins de debugging.
     *
     * @return string contendo, em linhas separadas, a concatenação da identificação
     *         de cada campo e valor respectivo
     */
    @Override
    public String toString() {
        return ("Tipo = " + msgType + "\nId Passageiro = " + passengerId + "\nNúmero de Malas = " + numberOfLostBags);
    }
}
