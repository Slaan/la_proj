package SarsaLambda;

/**
 * Created by beckf on 17.10.2015.
 */
public class SarsaMemory<E> {
    private E current;
    private boolean newCurrent;

    public SarsaMemory(){}

    public synchronized void put(E current) throws InterruptedException{
        while(newCurrent){
            wait();
        }
        this.current = current;
        notify();
    }

    public synchronized E get() throws InterruptedException{
        while(!newCurrent){
            wait();
        }
        notify();
        return current;
    }
}
