package bpm.camunda.service;

import java.util.List;

import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.task.Task;

import bpm.camunda.dto.GroupCamundaDto;

public interface IGroupCamundaService {

    Group createGroupCamunda(GroupCamundaDto group);

    void deleteGroupCamunda(String groupId);

    List<GroupCamundaDto> getAllGroups();

    List<Task> getTasksByGroupCamunda(String groupId);

    Authorization createAuthorization(int authorizationType, String[] args);

    void deleteAuthorization(String authorizationId);

}
