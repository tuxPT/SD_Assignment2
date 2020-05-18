package comInf;

/**
 *    Este tipo de dados define uma memória de tipo FIFO derivada a partir de uma memória genérica de tipo estático.
 */

public class MemFIFO extends MemObject
{
  /**
   *  Ponto de inserção
   *
   *    @serialField inPnt
   */

   private int inPnt = 0;

  /**
   *  Ponto de retirada
   *
   *    @serialField outPnt
   */

   private int outPnt = 0;

  /**
   *  Sinalização de FIFO vazio
   *
   *    @serialField empty
   */

   private boolean empty = true;

  /**
   *  Instanciação do FIFO.
   *
   *    @param nElem tamanho do FIFO (n. de elementos do array de armazenamento)
   */

   public MemFIFO (int nElem)
   {
     super (nElem);
   }

  /**
   *  Escrita de um valor.
   *
   *    @param val valor a armazenar
   */

   public void write (Object val)
   {
     if ((inPnt != outPnt) || empty)
        { mem[inPnt] = val;
          inPnt += 1;
          inPnt %= nMax;
          empty = false;
        }
   }

  /**
   *  Leitura de um valor.
   *
   *    @return valor armazenado
   */

   public Object read ()
   {
     Object val = null;

     if ((outPnt != inPnt) || !empty)
        { val = mem[outPnt];
          outPnt += 1;
          outPnt %= nMax;
          empty = (inPnt == outPnt);
        }
     return (val);
   }

  /**
   *  Detecção de FIFO vazio.
   *
   *    @return <li> true, se o FIFO estiver vazio
   *            <li> false, em caso contrário
   */

   public boolean empty ()
   {
     return (this.empty);
   }

  /**
   *  Detecção de FIFO cheio.
   *
   *    @return <li> true, se o FIFO estiver cheio
   *            <li> false, em caso contrário
   */

   public boolean full ()
   {
     return (!this.empty && (outPnt == inPnt));
   }
}
