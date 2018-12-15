package bpm.camunda.service;

import java.util.List;

import org.camunda.bpm.engine.task.Task;

public interface IMembershipCamundaService {

    void addAUserBpmToGroupBpm(String userId, String groupId);

    void deleteMemberShip(String userId, String groupId);

    List<Task> getTasksByUserCamundaAndGroupCamunda(String userId, String groupId);

}
