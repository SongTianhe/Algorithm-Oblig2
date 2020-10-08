package no.oslomet.cs.algdat;


////////////////// class DobbeltLenketListe //////////////////////////////


import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.StringJoiner;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class DobbeltLenketListe<T> implements Liste<T> {

    /**
     * Node class
     * @param <T>
     */
    private static final class Node<T> {
        private T verdi;                   // nodens verdi
        private Node<T> forrige, neste;    // pekere

        private Node(T verdi, Node<T> forrige, Node<T> neste) {
            this.verdi = verdi;
            this.forrige = forrige;
            this.neste = neste;
        }

        private Node(T verdi) {
            this(verdi, null, null);
        }
    }

    // instansvariabler
    private Node<T> hode;          // peker til den første i listen
    private Node<T> hale;          // peker til den siste i listen
    private int antall;            // antall noder i listen
    private int endringer;         // antall endringer i listen

    public DobbeltLenketListe() {
        hode = null;
        hale = null;
        antall = 0;
        endringer = 0;
    }

    public DobbeltLenketListe(T[] a) {
        //kaste NullPointerException hvis a er null
        if(a == null){
            throw new NullPointerException("Tabellen a er null!");
        }
        //lage en dobbeltlenket liste med verdiene fra tabellen a
        //tas ikke med null-verdier i a

        //hvis a er tom, ikke opprettes noen noder, hode og hale fortsatt er null
        if(a.length == 0){
            hode = null;
            hale = null;
            endringer = 0 ;
            antall = 0;
        }else{
            //finn den først verdi i a som ikke er null
            int firstNotNull = 0;
            while(firstNotNull<a.length && a[firstNotNull] == null){
                    firstNotNull ++;
            }

            if(firstNotNull < a.length){
                Node currentNode = new Node(a[firstNotNull],null,null);
                //hode pekker til første node
                hode = currentNode;
                antall ++;

                for(int i=firstNotNull+1; i<a.length;i++){
                    if(a[i] != null){
                        //nyeste node, og forrige peker currentNode
                        Node nyNode = new Node(a[i],currentNode,null);
                        //neste peker for currentNode pekker nyNode, og nå pekene mellom currentNode og nyNode er ferdig
                        currentNode.neste = nyNode;
                        currentNode = nyNode;//pekker currentNode til nyNode
                        antall++;
                    }
                }
                hale = currentNode;
            }
        }
    }

    //hjelp-metoden som sjekk fa og til er lovlig
    private void fratilKontroller(int fra, int til,int antall){
        if(fra<0){
            throw new IndexOutOfBoundsException("fra : "+fra+" er ikke lovlig");
        }
        if(fra>til){
            throw new IllegalArgumentException("fra kan ikke større enn til");
        }
        if(til>antall){
            throw new IndexOutOfBoundsException("til : "+til+" er ikke lovlig");
        }
    }

    public Liste<T> subliste(int fra, int til){
        //sjekk hvis fra og til er lovlig
        fratilKontroller(fra,til,antall);

        Liste list = new DobbeltLenketListe();

        for(int i=fra; i<til; i++){
            list.leggInn(hent(i));
        }

        return list;
    }

    @Override
    public int antall() {
        return antall;
    }

    @Override
    public boolean tom() {
        return antall == 0;
    }

    @Override
    public boolean leggInn(T verdi) {
        //Stoppes null-verdier
        Objects.requireNonNull(verdi,"Verdi kan ikke være null");

        Node node = new Node(verdi,null,null);
        //sjekk hvis listen er tom
        if(hode == null){
            hode = node;
            hale = node;
        }else{
            hale.neste = node;
            node.forrige = hale;
            hale = node;
        }
        endringer ++;
        antall ++;

        return true;
    }

    @Override
    public void leggInn(int indeks, T verdi) {
        //stoppes null-verdier
        Objects.requireNonNull(verdi,"Verdi kan ikke være null");
        //sjekk indeks er lovelig
        if(indeks != antall){ indeksKontroll(indeks,false);}

        //hvis verdi skal legges bakerst
        if(indeks == antall){
            leggInn(verdi);
        }else if(indeks == 0){ //hvis verdi skal legges først
            Node nyNode = new Node(verdi,null,hode);
            hode.forrige = nyNode;
            hode = nyNode;

            endringer ++;
            antall ++;
        }else{//hvis verdi skal ligges i midten
            Node foran = finnNode(indeks-1);
            Node next = finnNode(indeks);
            Node nyNode = new Node(verdi,foran,next);
            foran.neste = nyNode;
            next.forrige = nyNode;

            endringer ++;
            antall ++;
        }


    }

    @Override
    public boolean inneholder(T verdi) {
        return indeksTil(verdi) != -1;
    }

    //hjelpmetoden
    private Node<T> finnNode(int indeks){
        Node temp;

        if(indeks <= (antall/2)){
            temp = hode;
            for(int i=0; i<indeks; i++){
                temp = temp.neste;
            }
        }else{
            temp = hale;
            for(int i=antall-1; i>indeks; i--){
                temp = temp.forrige;
            }
        }
        return temp;
    }

    @Override
    public T hent(int indeks) {
        indeksKontroll(indeks,false);
        return finnNode(indeks).verdi;
    }

    @Override
    public int indeksTil(T verdi) {
        Node temp = hode;

        //Hvis hode er null, betyr list er tom
        if(temp != null){
            for(int i=0; i<antall; i++){
                if(temp.verdi.equals(verdi)){
                    return i;
                }
                temp = temp.neste;
            }
        }
        return -1;
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        //sjekk om indeks og nyverdi er null
        Objects.requireNonNull(indeks,"Indeks kan ikke være null");
        Objects.requireNonNull(nyverdi,"Ny verdi kan ikke være null");

        //save verdi til indeks før endring
        T verdiFør = hent(indeks);
        Node endreNode = finnNode(indeks);
        endreNode.verdi = nyverdi;
        endringer ++;

        return verdiFør;
    }

    @Override
    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T fjern(int indeks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void nullstill() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {

        StringJoiner result = new StringJoiner(", ","[","]");
        Node temp = hode;

        while(true){
            //Stoppe loop når det finnes ikke mer node(list er ferdig)
            if(temp == null){
                break;
            }
            //add verdien til result
            result.add(String.valueOf(temp.verdi));
            temp = temp.neste;
        }
        return result.toString();
    }

    public String omvendtString() {
        StringJoiner result = new StringJoiner(", ","[","]");
        Node temp = hale;

        while(true){
            //Stoppe loop når det finnes ikke mer node(list er ferdig)
            if(temp == null){
                break;
            }
            //add verdien til result
            result.add(String.valueOf(temp.verdi));
            temp = temp.forrige;
        }
        return result.toString();
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException();
    }

    public Iterator<T> iterator(int indeks) {
        throw new UnsupportedOperationException();
    }

    private class DobbeltLenketListeIterator implements Iterator<T>
    {
        private Node<T> denne;
        private boolean fjernOK;
        private int iteratorendringer;

        private DobbeltLenketListeIterator(){
            denne = hode;     // p starter på den første i listen
            fjernOK = false;  // blir sann når next() kalles
            iteratorendringer = endringer;  // teller endringer
        }

        private DobbeltLenketListeIterator(int indeks){
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext(){
            return denne != null;
        }

        @Override
        public T next(){
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove(){
            throw new UnsupportedOperationException();
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {

        String[] s1 = {"A","B","C","D"};
        //Liste<String> liste = new DobbeltLenketListe<>(s1);
        DobbeltLenketListe<String> liste = new DobbeltLenketListe<>();
        liste.leggInn(0, "4");  // ny verdi i tom liste
        liste.leggInn(0, "2");  // ny verdi legges forrest
        liste.leggInn(2, "6");  // ny verdi legges bakerst
        liste.leggInn(1, "3");  // ny verdi nest forrest
        liste.leggInn(3, "5");  // ny verdi nest bakerst
        liste.leggInn(0, "1");  // ny verdi forrest
        liste.leggInn(6, "7");  // ny verdi legges bakerst
        System.out.println(liste.antall);
        System.out.println(liste.toString());
    }
} // class DobbeltLenketListe


