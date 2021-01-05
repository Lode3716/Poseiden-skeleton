package com.nnk.springboot.services;

import java.util.List;

public interface ICrudService<T> {

    public T save(T objet);

    public List<T> readAll();

    public T update(T objet);

    public void delete(T objet);


}
