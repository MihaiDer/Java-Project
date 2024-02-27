package Repository;

import Domain.Entity;
import Exceptions.RepoException;

import java.util.List;

public interface IRepository<T extends Entity> {
    void addEntity(T elem) throws RepoException;
    List<T> getAll();
    void deleteEntity(int id) throws RepoException;
    void modifyEntity(int id, T elem) throws RepoException;
    int getPosition(int id);
    int getSize();
    T getAt(int pos);
    boolean findById(int id);
    T getById(int id);
}
