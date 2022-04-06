package com.example.taskmanager.service;

import com.example.taskmanager.Entity.Task;
import com.example.taskmanager.daoRepository.TaskRepository;
import com.example.taskmanager.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImplementation implements CrudService<Task>{

    private TaskRepository taskRepository;

    @Autowired
    public TaskServiceImplementation(TaskRepository theTaskRepository) {
        taskRepository = theTaskRepository;
    }

    @Override
    @Transactional
    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional
    public Task findById(Long theId) {
        Optional<Task> result = taskRepository.findById(theId);

        Task theTask = null;

        if (result.isPresent()) {

            theTask = result.get();
        }

        else {
            // we didn't find the Task
            throw new NotFoundException("Did not find task id - " + theId);
        }

        return theTask;

    }

    @Override
    @Transactional
    public void save(Task theTask) {
     taskRepository.save(theTask);
    }

    @Override
    @Transactional
    public void deleteById(Long theId) {
        taskRepository.deleteById(theId);

    }
}
