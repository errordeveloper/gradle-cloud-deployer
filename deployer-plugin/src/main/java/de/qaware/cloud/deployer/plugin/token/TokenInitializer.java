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
package de.qaware.cloud.deployer.plugin.token;

import de.qaware.cloud.deployer.commons.config.cloud.EnvironmentConfig;
import de.qaware.cloud.deployer.commons.error.EnvironmentConfigException;

/**
 * Used to initialize a token.
 */
public interface TokenInitializer {

    /**
     * Initialize the token using the specified environment config.
     *
     * @param environmentConfig The environment config.
     * @return The initialized token.
     * @throws EnvironmentConfigException If a error during token initialization occurs.
     */
    String initialize(EnvironmentConfig environmentConfig) throws EnvironmentConfigException;
}