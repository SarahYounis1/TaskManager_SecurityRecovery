package com.example.taskmanager.restController;

import com.example.taskmanager.Entity.Task;
import com.example.taskmanager.Entity.User;
import com.example.taskmanager.exception.NotFoundException;
import com.example.taskmanager.service.TaskServiceImplementation;
import com.example.taskmanager.service.UserServiceImplementation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")

public class TaskRestController {

    private TaskServiceImplementation taskServiceImplementation;
    private UserServiceImplementation userServiceImplementation;
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRestController.class);

    @Autowired
    public TaskRestController(TaskServiceImplementation taskServiceImplementation,
                              UserServiceImplementation userServiceImplementation) {
        this.taskServiceImplementation =taskServiceImplementation;
        this.userServiceImplementation =userServiceImplementation;
    }


    // expose "/tasks" and return list of tasks
    @GetMapping("/tasks")
    public List<Task> findAll() {

        return taskServiceImplementation.findAll();
    }

    // add mapping for GET /tasks/{tasksId} depending on Id and make sure it exists before

    @GetMapping("/tasks/{taskId}")
    public Task getTask(@PathVariable Long taskId) {

        Task theTask = (Task) taskServiceImplementation.findById(taskId);

        return theTask;
    }

    // add mapping for POST /tasks - add new task

    @PostMapping("/tasks/{userId}")
    public Task addTask(@RequestBody Task theTask ,@PathVariable Long userId) {

        //  just in case they pass an id in JSON ... set id to 0
        // this is to force a save of new item ... instead of update

        LOGGER.info("new Task is added");
        theTask.setId(0L);

       User theUser=(User) userServiceImplementation.findById(userId);
       theTask.setUser(theUser);

        taskServiceImplementation.save(theTask);

        return theTask;
    }

    // add mapping for PUT /tasks - update existing task -- Ask about posting all related information

    @PutMapping("/tasks/{userId}")
    public Task updateTask(@RequestBody Task theTask,@PathVariable Long userId) {

        User theUser=(User) userServiceImplementation.findById(userId);
        theTask.setUser(theUser);

        taskServiceImplementation.save(theTask);

        return theTask;
    }

    // add mapping for DELETE /tasks/{taskId} - delete task

    @DeleteMapping("/tasks/{taskId}")
    public String deleteTask(@PathVariable Long taskId) {

        LOGGER.info("one Task is deleted");
        Task tempTask = (Task) taskServiceImplementation.findById(taskId);

        // throw Not Found exception if null

        if (tempTask == null) {
            throw new NotFoundException("Task id not found - " + taskId);
        }

        taskServiceImplementation.deleteById(taskId);

        return "Deleted Task id - " + taskId;
    }





}
