package clientSide.BusDriver;

import entities.TBusDriver;
import genclass.GenericIO;
import shared_regions_JavaInterfaces.IArrivalTerminalTransferQuayBusDriver;
import shared_regions_JavaInterfaces.IDepartureTerminalTransferQuayBusDriver;

/**
 *   Este tipo de dados simula uma solução do lado do cliente do Problema dos Barbeiros Sonolentos que implementa o
 *   modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento estático dos threads barbeiro.
 *   A comunicação baseia-se em passagem de mensagens sobre sockets usando o protocolo TCP.
 */

public class mainBusDriver
{
  /**
   *   Programa principal.
   */

   public static void main (String [] args)
   {
      int nCustomer = 5;                                   // número de clientes
      int nBarber = 2;                                     // número máximo de barbeiros
      Customer [] customer = new Customer [nCustomer];     // array de threads cliente
      Barber [] barber = new Barber [nBarber];             // array de threads barbeiro
      BarberShopStub bShopStub;                            // stub à barbearia
      int nIter;                                           // número de iterações do ciclo de vida dos clientes
      String fName;                                        // nome do ficheiro de logging
      String serverHostName;                               // nome do sistema computacional onde está o servidor
      int serverPortNumb;                                  // número do port de escuta do servidor

     /* Obtenção dos parâmetros do problema */

      GenericIO.writelnString ("\n" + "      Problema dos Barbeiros Sonolentos\n");
      GenericIO.writeString ("Numero de iterações? ");
      nIter = GenericIO.readlnInt ();
      GenericIO.writeString ("Nome do ficheiro de logging? ");
      fName = GenericIO.readlnString ();
      GenericIO.writeString ("Nome do sistema computacional onde está o servidor? ");
      serverHostName = GenericIO.readlnString ();
      GenericIO.writeString ("Número do port de escuta do servidor? ");
      serverPortNumb = GenericIO.readlnInt ();
      bShopStub = new BarberShopStub (serverHostName, serverPortNumb);

     /* Criação dos threads barbeiro e cliente */

      for (int i = 0; i < MAX_BUSDRIVER; i++) {
        TBusDriver[i] = new TBusDriver(i, (IArrivalTerminalTransferQuayBusDriver) MArrivalTerminalTransferQuay,
              (IDepartureTerminalTransferQuayBusDriver) MDepartureTerminalTransferQuay);
      }
      
     /* Comunicação ao servidor dos parâmetros do problema */

      bShopStub.probPar(fName, nIter);

     /* Arranque da simulação */

     for (int i = 0; i < MAX_BUSDRIVER; i++) {
      TBusDriver[i].start();

     /* Aguardar o fim da simulação */
     for (int i = 0; i < TBusDriver.length; i++) {
      try {
          //MArrivalTerminalTransferQuay.endOfWork();
          TBusDriver[i].join();
      } catch (InterruptedException e) {
      }
  } 
      bShopStub.shutdown ();
    }
}
