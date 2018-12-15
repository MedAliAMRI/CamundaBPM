package bpm.camunda.service;

public interface IProcessCamundaService {

    String startProcess(String processId, String key, String value, Long userId);

    void completeTask(String key, String value, Long idOwner, Long idAssigned);
}
