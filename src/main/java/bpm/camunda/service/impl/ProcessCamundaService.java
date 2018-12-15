package bpm.camunda.service.impl;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bpm.camunda.service.IProcessCamundaService;

@Service
public class ProcessCamundaService implements IProcessCamundaService {

    private ProcessEngine camunda;

    private TaskService taskService;

    @Autowired
    public ProcessCamundaService(ProcessEngine camunda, TaskService taskService) {
        super();
        this.camunda = camunda;
        this.taskService = taskService;
    }

    @Override
    public String startProcess(String processId, String key, String value, Long userId) {
        ProcessInstance processInstance = camunda.getRuntimeService().startProcessInstanceByKey(processId,
                Variables.putValue(key, value));
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getProcessInstanceId())
                .singleResult();
        taskService.setAssignee(task.getId(), String.valueOf(userId));
        taskService.setOwner(task.getId(), String.valueOf(userId));
        taskService.complete(task.getId());
        return processInstance.getProcessInstanceId();
    }

    @Override
    public void completeTask(String key, String value, Long idOwner, Long idAssigned) {
        ProcessInstanceQuery process = camunda.getRuntimeService().createProcessInstanceQuery().variableValueEquals(key,
                String.valueOf(value));
        String processId = process.singleResult().getProcessInstanceId();
        Task task = taskService.createTaskQuery().processInstanceId(processId).singleResult();
        taskService.setAssignee(task.getId(), String.valueOf(idAssigned));
        taskService.setOwner(task.getId(), String.valueOf(idOwner));
        taskService.complete(task.getId());
    }

}
