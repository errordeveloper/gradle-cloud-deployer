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
package de.qaware.cloud.deployer.marathon.update;

import de.qaware.cloud.deployer.commons.update.BaseSoftUpdateStrategy;
import de.qaware.cloud.deployer.marathon.resource.base.MarathonResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the soft update strategy. Meaning that all resources not included in the resources list stay untouched.
 */
class MarathonSoftUpdateStrategy extends BaseSoftUpdateStrategy<MarathonResource> implements MarathonUpdateStrategy {

    /**
     * The logger of this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MarathonSoftUpdateStrategy.class);

    public MarathonSoftUpdateStrategy() {
        super(LOGGER);
    }
}