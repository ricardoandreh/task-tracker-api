package br.com.rictodolist.todolist.controllers;

import br.com.rictodolist.todolist.dtos.task.TaskPaginationDTO;
import br.com.rictodolist.todolist.dtos.task.TaskRequestDTO;
import br.com.rictodolist.todolist.dtos.task.TaskResponseDTO;
import br.com.rictodolist.todolist.dtos.task.TaskUpdateDTO;
import br.com.rictodolist.todolist.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public TaskResponseDTO create(@RequestBody @Valid TaskRequestDTO taskRequestDto) {

        return this.taskService.create(taskRequestDto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TaskResponseDTO listOne(@PathVariable(value = "id") UUID id) {

        return this.taskService.getOne(id);
    }

    @GetMapping
    public TaskPaginationDTO listAll(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     @RequestParam(defaultValue = "id") String sortBy,
                                     @RequestParam(defaultValue = "true") boolean ascending) {

        return this.taskService.getAll(page, size, sortBy, ascending);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TaskResponseDTO update(@RequestBody @Valid TaskUpdateDTO taskUpdateDto, @PathVariable(value = "id") UUID id) {

        return this.taskService.update(taskUpdateDto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TaskResponseDTO delete(@PathVariable(value = "id") UUID id) {

        return this.taskService.delete(id);
    }
}
