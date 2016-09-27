/*
 * Copyright 2016 QAware GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.qaware.cloud.deployer.plugin.task;

import de.qaware.cloud.deployer.commons.Deployer;
import de.qaware.cloud.deployer.commons.error.EnvironmentConfigException;
import de.qaware.cloud.deployer.commons.error.ResourceConfigException;
import de.qaware.cloud.deployer.commons.error.ResourceException;
import de.qaware.cloud.deployer.plugin.environment.Environment;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.List;

/**
 * A task which deletes all environments in the config.
 */
public class DeleteAllTask extends BaseAllEnvironmentsTask {

    /**
     * Deletes all environments in the configuration.
     *
     * @throws ResourceException          If a error during resource interaction with the backend occurs.
     * @throws ResourceConfigException    If a error during config creation/parsing occurs.
     * @throws EnvironmentConfigException If an error during environment parsing/creation occurs.
     */
    @TaskAction
    public void deleteAll() throws ResourceException, ResourceConfigException, EnvironmentConfigException {
        // Setup environments
        setupEnvironments();

        // Retrieve necessary data
        List<Environment> environments = getEnvironments();

        // Deploy every environment
        for (Environment environment : environments) {
            Deployer deployer = environment.getDeployer();
            List<File> files = environment.getFiles();
            deployer.delete(files);
        }
    }
}