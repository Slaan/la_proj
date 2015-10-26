package persistence;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by beckf on 26.10.2015.
 */
public class SharedBuffer<E> {

    private List<E> buffer;

    public SharedBuffer(){
        this.buffer = new ArrayList<>();
    }

    /**
     * Adds one Entity of specified Typ to the buffer
     * @param entity
     */
    public synchronized void addEntity(E entity){
        notify();
        buffer.add(entity);
    }

    /**
     * Retrieves one Entity of the specified Typ
     * waits if Buffer is empty
     * @return
     * @throws InterruptedException
     */
    public synchronized E getEntity() throws InterruptedException{
        while(buffer.size()<1){
            wait();
        }
        E entity = buffer.get(0);
        buffer.remove(0);
        return entity;
    }

    /**
     * Retrieves all Entitys in the Buffer if a specified quantity is reached
     * @param numberOfEntitys specified qantity
     * @return List
     * @throws InterruptedException
     */
    public synchronized List<E> getEntityWhen(int numberOfEntitys) throws InterruptedException {
        while(buffer.size()<numberOfEntitys){
            wait();
        }
        List<E> entitys = new ArrayList<>(buffer);
        buffer.clear();
        return entitys;
    }
}
