package bpm.camunda.service;

import java.util.List;

import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.task.Task;

import fr.sparkit.sparkys.dto.UserCamundaDto;

public interface IUserCamundaService {

    User createUserCamunda(UserCamundaDto user);

    void deleteUserCamunda(String idUser);

    List<UserCamundaDto> getAllUsersCamundaByGroup(String groupId);

    List<Task> getTasksByUserCamunda(String userId);

}
