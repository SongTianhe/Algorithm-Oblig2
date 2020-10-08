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

    public Liste<T> subliste(int fra, int til){
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean inneholder(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T hent(int indeks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indeksTil(T verdi) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T oppdater(int indeks, T nyverdi) {
        throw new UnsupportedOperationException();
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

        //hjelpmetoden
        private Node<T> finnNode(int indeks){
            Node node = new Node(null,null,null);
            return node;
        }

    } // class DobbeltLenketListeIterator

    public static <T> void sorter(Liste<T> liste, Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }

    public static void main(String[] args) {

        String[] s1 = {"A","B","C"};
        Liste<String> liste = new DobbeltLenketListe<>();

        System.out.println(liste.toString());

        for(String i : s1){
            liste.leggInn(i);
            System.out.println(liste.toString()+liste.antall());
        }

        liste.leggInn("");
        System.out.println(liste.toString()+liste.antall());
    }
} // class DobbeltLenketListe


