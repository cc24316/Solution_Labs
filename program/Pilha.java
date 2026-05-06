public class Pilha <X> implements Cloneable
{
    private Object[]    elemento; // private X[] elemento;
    private final int   tamanhoInicial;
    private int         ultimo=-1; // vazio
    private Clonador<X> clonador;

    public Pilha (int tamanho) throws Exception
    {
        if (tamanho<=0)
            throw new Exception ("Tamanho invalido");

        this.elemento       = new Object [tamanho]; //this.elemento=new X [tamanho];
        this.tamanhoInicial = tamanho;

        this.clonador = new Clonador<X> ();
    }

    public void guardeUmItem (X x) throws Exception // LIFO
    {
        if (x==null)
            throw new Exception ("Falta o que guardar");


        this.ultimo++;

        if (x instanceof Cloneable)
            this.elemento[this.ultimo]=this.clonador.clone(x);
        else
            this.elemento[this.ultimo]=x;
    }

    public X recupereUmItem () throws Exception // LIFO
    {
        if (this.ultimo==-1) // vazia
            throw new Exception ("Nada a recuperar");

        X ret=null;
        if (this.elemento[this.ultimo] instanceof Cloneable)
            ret = this.clonador.clone((X)this.elemento[this.ultimo]);
        else
            ret = (X)this.elemento[this.ultimo];

        return ret;
    }

    public X removaUmItem() throws Exception // LIFO
    {
        if (this.ultimo == -1)
            throw new Exception("Nada a remover");

        X item = (X) this.elemento[this.ultimo];  // guarda o item
        this.elemento[this.ultimo] = null;         // remove da pilha
        this.ultimo--;                             // decrementa o topo

        return item;                               // retorna o item removido
    }


    public boolean isCheia ()
    {
        return this.ultimo+1==this.elemento.length;
       /*
        if(this.ultimo+1==this.elemento.length)
            return true;

        return false;
        */
    }

    public boolean isVazia ()
    {
        return this.ultimo==-1;
       /*
        if (this.ultimo==-1)
            return true;

        return false;
        */
    }

    @Override
    public String toString ()
    {
        String ret = (this.ultimo+1) + " elemento(s)";

        if (this.ultimo!=-1)
            ret += ", sendo o ultimo "+this.elemento[this.ultimo];

        return ret;
    }

    @Override
    public boolean equals (Object obj)
    {
        if(obj==this)
            return true;

        if(obj==null)
            return false;

        if(obj.getClass()!=this.getClass())
            return false;

        Pilha<X> pil = (Pilha<X>) obj;

        if(this.ultimo!=pil.ultimo)
            return false;
        /*
        if(this.tamanhoInicial!=pil.tamanhoInicial)
            return false;
        */
        for(int i=0 ; i<=this.ultimo;i++)
            if(!this.elemento[i].equals(pil.elemento[i]))
                return false;

        return true;
    }

    @Override
    public int hashCode ()
    {
        int ret=666/*qualquer positivo*/;

        ret = ret*7/*primo*/ + ((Integer)(this.ultimo        )).hashCode();
        //ret = ret*7/*primo*/ + ((Integer)(this.tamanhoInicial)).hashCode();

        for (int i=0; i<=this.ultimo; i++)
            ret = ret*7/*primo*/ + this.elemento[i].hashCode();

        if (ret<0)
            ret=-ret;

        return ret;
    }

    // construtor de copia
    public Pilha (Pilha<X> modelo) throws Exception
    {
        if(modelo == null)
            throw new Exception("Modelo ausente");

        this.tamanhoInicial = modelo.tamanhoInicial;
        this.ultimo         = modelo.ultimo;
        this.clonador       = modelo.clonador;

        // para fazer a copia dum vetor
        // precisa criar um vetor novo, com new
        // nao pode fazer this.elemento=modelo.elemento
        // pois se assim fizermos estaremos com dois
        // objetos, o this e o modelo, compartilhando
        // o mesmo vetor
        this.elemento = new Object[modelo.elemento.length]; // this.elemento = new X [modelo.elemento.length];

        for(int i=0;i<=modelo.ultimo;i++)
            this.elemento[i] = modelo.elemento[i];
    }

}
