package Repository;

import Domain.*;
import Exceptions.DuplicateObjectError;
import Exceptions.RepoException;

import java.util.ArrayList;
import java.util.List;

public class MemoryRepo <T extends Entity> implements IRepository<T> {
    protected List<T> elems = new ArrayList<>();

    @Override
    public int getSize() {
        return this.elems.size();
    }

    @Override
    public T getAt(int pos) {
        return this.elems.get(pos);
    }

    @Override
    public boolean findById(int id) {
        for (T elem : elems){
            if(elem.getId() == id)
                return true;
        }
        return false;
    }

    @Override
    public T getById(int id){
        for(T elem : elems)
            if(elem.getId() == id)
                return elem;
        return null;
    }
    @Override
    public void addEntity(T elem) throws RepoException {
        if(!findById(elem.getId()))
            elems.add(elem);
        else throw new DuplicateObjectError("Exista deja o entitate cu id-ul dat!");
    }

    @Override
    public List<T> getAll() {
        return this.elems;
    }

    @Override
    public int getPosition(int id) {
        for (int i = 0; i < elems.size(); i++)
            if (elems.get(i).getId() == id)
                return i;
        return -1;
    }

    @Override
    public void modifyEntity(int id, T elem) throws RepoException {
        int entityPosition = getPosition(id);
        if(!findById(id))
            throw new RepoException("Nu exista nici o entitate cu id-ul dat!");
        else
            elems.set(entityPosition, elem);
    }

    @Override
    public void deleteEntity(int id) throws RepoException {
        if(findById(id))
            elems.remove(getById(id));
        else
            throw new RepoException("Nu exista nici o entitate cu id-ul dat!");
    }
}
