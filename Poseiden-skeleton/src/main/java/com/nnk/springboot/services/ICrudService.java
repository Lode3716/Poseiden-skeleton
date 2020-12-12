package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

public interface ICrudService<T> {

    public T save(T objet);

    public List<T> readAll();

    public Optional<T> update(T objet);

    public  boolean delete(T objet);

    public boolean exist(T objet);


}
