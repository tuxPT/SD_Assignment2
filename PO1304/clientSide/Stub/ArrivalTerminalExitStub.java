package clientSide.Stub;

import shared_regions_JavaInterfaces.IArrivalTerminalExitPassenger;

/**
 * Este tipo de dados define o stub à barbearia numa solução do Problema dos
 * Barbeiros Sonolentos que implementa o modelo cliente-servidor de tipo 2
 * (replicação do servidor) com lançamento estático dos threads barbeiro.
 */

public class ArrivalTerminalExitStub implements IArrivalTerminalExitPassenger
{

  /**
   *  Nome do sistema computacional onde está localizado o servidor
   *    @serialField serverHostName
   */

   private String serverHostName = null;

  /**
   *  Número do port de escuta do servidor
   *    @serialField serverPortNumb
   */

   private int serverPortNumb;

  /**
   *  Instanciação do stub à barbearia.
   *
   *    @param hostName nome do sistema computacional onde está localizado o servidor
   *    @param port número do port de escuta do servidor
   */

   public ArrivalTerminalExitStub(String hostName, int port)
   {
      serverHostName = hostName;
      serverPortNumb = port;
   }

  @Override
  public boolean addPassenger(Integer id, Integer curr) {
      // TODO Auto-generated method stub
      return false;
  }

  @Override
  public Integer getCURRENT_NUMBER_OF_PASSENGERS() {
      // TODO Auto-generated method stub
      return null;
  }

  @Override
  public void lastPassenger() {
      // TODO Auto-generated method stub

  }

 
   
}
