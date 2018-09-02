package com.kodnito.restapi.service;

import com.kodnito.restapi.entity.Todo;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author hayricicek
 */
@Stateless
public class TodoService {

    @PersistenceContext(unitName = "restapi_PU")
    EntityManager em;

    public List<Todo> getAll() {
        return em.createNamedQuery("Todo.findAll", Todo.class).getResultList();
    }

    public Todo findById(Long id) {
        return em.find(Todo.class, id);
    }

    public void update(Todo todo) {
        em.merge(todo);
    }

    public Long create(Todo todo) {
        Todo newTodo = new Todo();
        newTodo.setDescription(todo.getDescription());
        newTodo.setTask(todo.getTask());

        em.persist(newTodo);

        return newTodo.getId();
    }

    public void delete(Todo todo) {
        if (!em.contains(todo)) {
            todo = em.merge(todo);
        }

        em.remove(todo);
    }
}