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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberShipService implements IMembershipCamundaService {

    private IdentityService identityService;

    private TaskService taskService;

    @Autowired
    public MemberShipService(IdentityService identityService, TaskService taskService) {
        super();
        this.identityService = identityService;
        this.taskService = taskService;
    }

    @Override
    public void addAUserBpmToGroupBpm(String userId, String groupId) {
        identityService.createMembership(userId, groupId);
    }

    @Override
    public void deleteMemberShip(String userId, String groupId) {
        if (!CollectionUtils.isEmpty(getTasksByUserCamundaAndGroupCamunda(userId, groupId))) {
            identityService.deleteMembership(userId, groupId);
        }

    }

    @Override
    public List<Task> getTasksByUserCamundaAndGroupCamunda(String userId, String groupId) {
        List<User> users = identityService.createUserQuery().memberOfGroup(groupId).list();
        if (!CollectionUtils.isEmpty(users)) {
            List<Task> tasks = new ArrayList<>();
            users.forEach((User user) -> {
                if (StringUtils.isNotBlank(userId) && userId.equals(user.getId())) {
                    tasks.addAll(taskService.createTaskQuery().taskCandidateUser(userId).list());
                }
            });
            if (!CollectionUtils.isEmpty(tasks)) {
                return tasks;
            } else {
                return Collections.emptyList();
            }

        } else {
            return Collections.emptyList();
        }

    }

}
