package bpm.camunda.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.camunda.bpm.engine.AuthorizationService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.authorization.Authorization;
import org.camunda.bpm.engine.authorization.Permission;
import org.camunda.bpm.engine.authorization.Permissions;
import org.camunda.bpm.engine.authorization.Resource;
import org.camunda.bpm.engine.authorization.Resources;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import bpm.camunda.service.IGroupCamundaService;

@Service
public class GroupCamundaService implements IGroupCamundaService {

    private static final Logger LOG = LoggerFactory.getLogger(GroupCamundaService.class);

    private static final String ERROR_CREATE_GROUP = "Error: couldn't create Group";

    public static final int NUMBER_OF_GROUP_ID = 0;

    public static final int NUMBER_OF_RESOURCE = 1;

    public static final int NUMBER_OF_PERMISSION = 2;

    private IdentityService identityService;

    private TaskService taskService;

    private AuthorizationService authorizationService;

    @Autowired
    public GroupCamundaService(IdentityService identityService, TaskService taskService,
            AuthorizationService authorizationService) {
        super();
        this.identityService = identityService;
        this.taskService = taskService;
        this.authorizationService = authorizationService;
    }

    @Override
    public Group createGroupCamunda(GroupCamundaDto group) {
        LOG.info("Create a new group by calling the 'newGroup' method:");
        String name;
        String type;
        if (StringUtils.isNotBlank(group.getId())) {
            Group groupCamunda = identityService.newGroup(group.getId());
            if (StringUtils.isNotBlank(group.getName())) {
                name = group.getName();
            } else {
                name = StringUtils.EMPTY;
            }
            if (StringUtils.isNotBlank(group.getType())) {
                type = group.getType();
            } else {
                type = StringUtils.EMPTY;
            }
            groupCamunda.setName(name);
            groupCamunda.setType(type);
            identityService.saveGroup(groupCamunda);
            return groupCamunda;
        } else {
            LOG.error(ERROR_CREATE_GROUP);
            return null;
        }

    }

    @Override
    public void deleteGroupCamunda(String groupId) {
        List<Task> tasks = getTasksByGroupCamunda(groupId);
        if (CollectionUtils.isEmpty(tasks)) {
            identityService.deleteGroup(groupId);
        }
    }

    @Override
    public List<Task> getTasksByGroupCamunda(String groupId) {

        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(groupId).list();
        if (!CollectionUtils.isEmpty(tasks)) {
            return tasks;
        } else {
            return Collections.emptyList();
        }

    }

    @Override
    public List<GroupCamundaDto> getAllGroups() {
        List<Group> groups = identityService.createGroupQuery().list();
        if (!CollectionUtils.isEmpty(groups)) {
            List<GroupCamundaDto> groupCamundaDtos = new ArrayList<>();
            groups.forEach((Group group) -> {
                GroupCamundaDto groupCamundaDto = new GroupCamundaDto(group.getId(), group.getName(), group.getType());
                groupCamundaDtos.add(groupCamundaDto);
            });
            return groupCamundaDtos;
        } else {
            return Collections.emptyList();
        }

    }

    @Override
    public Authorization createAuthorization(int authorizationType, String[] args) {
        Authorization authorization = authorizationService.createNewAuthorization(authorizationType);
        String groupId = null;
        if (StringUtils.isNotBlank(args[NUMBER_OF_GROUP_ID])) {
            groupId = args[NUMBER_OF_GROUP_ID];
        } else {
            groupId = StringUtils.EMPTY;
        }
        authorization.setGroupId(groupId);
        if (args[NUMBER_OF_RESOURCE].contains(ConstantsBpm.Resource.RESOURCES_PROCESS)) {
            authorization.setResource(getResourceOfProcess(args[NUMBER_OF_RESOURCE]));
        } else if (args[NUMBER_OF_RESOURCE].contains(ConstantsBpm.Resource.RESOURCES_TENANT)) {
            authorization.setResource(getResourceOfTenant(args[NUMBER_OF_RESOURCE]));
        } else if (args[NUMBER_OF_RESOURCE].contains(ConstantsBpm.Resource.RESOURCES_GROUP)) {
            authorization.setResource(getResourceOfGroup(args[NUMBER_OF_RESOURCE]));
        } else {
            authorization.setResource(getResource(args[NUMBER_OF_RESOURCE]));
        }
        authorization.setResource(getResource(args[NUMBER_OF_RESOURCE]));
        authorization.addPermission(getPermission(args[NUMBER_OF_PERMISSION]));
        return authorizationService.saveAuthorization(authorization);
    }

    private static Permission getPermission(String per) {
        Permission permission;
        switch (per) {
        case ConstantsBpm.Permission.PERMISSIONS_READ:
            permission = Permissions.READ;
            break;
        case ConstantsBpm.Permission.PERMISSIONS_CREATE:
            permission = Permissions.CREATE;
            break;
        case ConstantsBpm.Permission.PERMISSIONS_UPDATE:
            permission = Permissions.UPDATE;
            break;
        case ConstantsBpm.Permission.PERMISSIONS_DELETE:
            permission = Permissions.DELETE;
            break;
        case ConstantsBpm.Permission.PERMISSIONS_ALL:
            permission = Permissions.ALL;
            break;
        case ConstantsBpm.Permission.PERMISSIONS_ACCESS:
            permission = Permissions.ACCESS;
            break;
        default:
            permission = Permissions.NONE;
            break;
        }
        return permission;
    }

    private static Resource getResource(String src) {
        Resource resource;
        switch (src) {
        case ConstantsBpm.Resource.RESOURCES_APPLICATION:
            resource = Resources.APPLICATION;
            break;
        case ConstantsBpm.Resource.RESOURCES_AUTHORIZATION:
            resource = Resources.AUTHORIZATION;
            break;
        case ConstantsBpm.Resource.RESOURCES_BATCH:
            resource = Resources.BATCH;
            break;
        case ConstantsBpm.Resource.RESOURCES_DEPLOYMENT:
            resource = Resources.DEPLOYMENT;
            break;
        case ConstantsBpm.Resource.RESOURCES_FILTER:
            resource = Resources.FILTER;
            break;
        case ConstantsBpm.Resource.RESOURCES_TASK:
            resource = Resources.TASK;
            break;
        case ConstantsBpm.Resource.RESOURCES_USER:
            resource = Resources.USER;
            break;
        case ConstantsBpm.Resource.RESOURCES_DECISION_REQUIREMENTS_DEFINITION:
            resource = Resources.DECISION_REQUIREMENTS_DEFINITION;
            break;
        default:
            resource = null;
            break;
        }
        return resource;

    }

    @Override
    public void deleteAuthorization(String authorizationId) {
        authorizationService.deleteAuthorization(authorizationId);
    }

    private static Resource getResourceOfProcess(String src) {
        switch (src) {
        case ConstantsBpm.Resource.RESOURCES_PROCESS_INSTANCE:
            return Resources.PROCESS_INSTANCE;
        case ConstantsBpm.Resource.RESOURCES_PROCESS_DEFINITION:
            return Resources.PROCESS_DEFINITION;
        default:
            return null;
        }
    }

    private static Resource getResourceOfTenant(String src) {
        Resource resource;
        switch (src) {
        case ConstantsBpm.Resource.RESOURCES_TENANT:
            resource = Resources.TENANT;
            break;
        case ConstantsBpm.Resource.RESOURCES_TENANT_MEMBERSHIP:
            resource = Resources.TENANT_MEMBERSHIP;
            break;
        default:
            resource = null;
        }
        return resource;
    }

    private static Resource getResourceOfGroup(String src) {
        Resource resource;
        switch (src) {
        case ConstantsBpm.Resource.RESOURCES_GROUP:
            resource = Resources.GROUP;
            break;
        case ConstantsBpm.Resource.RESOURCES_GROUP_MEMBERSHIP:
            resource = Resources.GROUP_MEMBERSHIP;
            break;
        default:
            resource = null;
        }
        return resource;
    }
}
