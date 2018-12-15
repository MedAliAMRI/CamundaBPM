package bpm.camunda.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCamundaService implements IUserCamundaService {

    private static final Logger LOG = LoggerFactory.getLogger(UserCamundaService.class);

    private static final String ERROR_CREATE_USER = "Error: couldn't create User";

    private IdentityService identityService;

    private TaskService taskService;

    @Autowired
    public UserCamundaService(IdentityService identityService, TaskService taskService) {
        super();
        this.identityService = identityService;
        this.taskService = taskService;
    }

    @Override
    public User createUserCamunda(UserCamundaDto user) {
        LOG.info("Create a new user by calling the 'newUser' method:");
        if (StringUtils.isNotBlank(user.getId())) {
            String firstName;
            String lastName;
            String password;
            String email;
            User userBpm = identityService.newUser(user.getId());
            if (StringUtils.isNotBlank(user.getFirstName())) {
                firstName = user.getFirstName();
            } else {
                firstName = StringUtils.EMPTY;
            }
            if (StringUtils.isNotBlank(user.getLastName())) {
                lastName = user.getLastName();
            } else {
                lastName = StringUtils.EMPTY;
            }
            if (StringUtils.isNotBlank(user.getPassword())) {
                password = user.getPassword();
            } else {
                password = StringUtils.EMPTY;
            }
            if (StringUtils.isNotBlank(user.getEmail())) {
                email = user.getEmail();
            } else {
                email = StringUtils.EMPTY;
            }
            userBpm.setFirstName(firstName);
            userBpm.setLastName(lastName);
            userBpm.setPassword(password);
            userBpm.setEmail(email);
            identityService.saveUser(userBpm);
            return userBpm;
        } else {
            LOG.error(ERROR_CREATE_USER);
            return null;
        }

    }

    @Override
    public void deleteUserCamunda(String userId) {
        List<Task> tasks = getTasksByUserCamunda(userId);
        if (CollectionUtils.isEmpty(tasks)) {
            identityService.deleteUser(userId);
        }

    }

    @Override
    public List<UserCamundaDto> getAllUsersCamundaByGroup(String groupId) {
        List<User> users = identityService.createUserQuery().memberOfGroup(groupId).list();
        if (!CollectionUtils.isEmpty(users)) {
            List<UserCamundaDto> userCamundaDtos = new ArrayList<>();
            users.forEach((User user) -> {
                UserCamundaDto userCamundaDto = new UserCamundaDto(user.getId(), user.getFirstName(),
                        user.getLastName(), user.getPassword(), user.getEmail());
                userCamundaDtos.add(userCamundaDto);
            });
            return userCamundaDtos;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Task> getTasksByUserCamunda(String userId) {
        List<Task> tasks = taskService.createTaskQuery().taskCandidateUser(userId).list();
        if (!CollectionUtils.isEmpty(tasks)) {
            return tasks;
        } else {
            return Collections.emptyList();
        }

    }

}
