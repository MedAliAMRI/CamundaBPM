
package bpm.camunda.service.impl;

public final class ConstantsBpm {

    public static final class Permission {

        public static final String PERMISSIONS_READ = "READ";

        public static final String PERMISSIONS_CREATE = "CREATE";

        public static final String PERMISSIONS_UPDATE = "UPDATE";

        public static final String PERMISSIONS_DELETE = "DELETE";

        public static final String PERMISSIONS_ALL = "ALL";

        public static final String PERMISSIONS_ACCESS = "ACCESS";

        private Permission() {
        }

    }

    public static final class Resource {

        public static final String RESOURCES_PROCESS_INSTANCE = "PROCESS_INSTANCE";

        public static final String RESOURCES_PROCESS_DEFINITION = "PROCESS_DEFINITION";

        public static final String RESOURCES_APPLICATION = "APPLICATION";

        public static final String RESOURCES_AUTHORIZATION = "AUTHORIZATION";

        public static final String RESOURCES_BATCH = "BATCH";

        public static final String RESOURCES_DEPLOYMENT = "DEPLOYMENT";

        public static final String RESOURCES_FILTER = "FILTER";

        public static final String RESOURCES_GROUP = "GROUP";

        public static final String RESOURCES_GROUP_MEMBERSHIP = "GROUP_MEMBERSHIP";

        public static final String RESOURCES_USER = "USER";

        public static final String RESOURCES_TASK = "TASK";

        public static final String RESOURCES_TENANT = "TENANT";

        public static final String RESOURCES_TENANT_MEMBERSHIP = "RESOURCES_TENANT_MEMBERSHIP";

        public static final String RESOURCES_DECISION_REQUIREMENTS_DEFINITION = "DECISION_REQUIREMENTS_DEFINITION";

        public static final String RESOURCES_PROCESS = "PROCESS";

        private Resource() {

        }

    }

    private ConstantsBpm() {
    }

}
