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
package de.qaware.cloud.deployer.dcos.token;

import de.qaware.cloud.deployer.commons.config.environment.EnvironmentConfig;
import de.qaware.cloud.deployer.commons.error.EnvironmentConfigException;
import de.qaware.cloud.deployer.commons.error.ResourceException;
import de.qaware.cloud.deployer.commons.strategy.Strategy;
import de.qaware.cloud.deployer.dcos.test.DcosTestEnvironmentUtil;
import junit.framework.TestCase;
import org.junit.Before;

import static de.qaware.cloud.deployer.dcos.logging.DcosMessageBundle.DCOS_MESSAGE_BUNDLE;

public class TokenResourceIntegrationTest extends TestCase {

    private EnvironmentConfig environmentConfig;

    private String authToken;

    @Before
    public void setUp() throws Exception {
        this.environmentConfig = DcosTestEnvironmentUtil.createEnvironmentConfig(Strategy.REPLACE);
        this.authToken = DcosTestEnvironmentUtil.getToken();
    }

    public void testRetrieveAuthenticationTokenWithEmptyToken() throws ResourceException {
        assertException(environmentConfig, "", DCOS_MESSAGE_BUNDLE.getMessage("DEPLOYER_DCOS_ERROR_EMPTY_TOKEN"));
    }

    public void testRetrieveAuthenticationTokenWithInvalidToken() throws ResourceException {
        String invalidToken = authToken.substring(0, authToken.length() - 2);
        assertException(environmentConfig, invalidToken, DCOS_MESSAGE_BUNDLE.getMessage("DEPLOYER_DCOS_ERROR_COULD_NOT_RETRIEVE_TOKEN"));
    }

    public void testRetrieveAuthenticationTokenInvalidAddress() throws ResourceException {
        EnvironmentConfig newEnvironmentConfig = new EnvironmentConfig("test", "http://bla-blub-foobar-bla-12341.xy/", environmentConfig.getStrategy());
        assertException(newEnvironmentConfig, authToken, DCOS_MESSAGE_BUNDLE.getMessage("DEPLOYER_DCOS_ERROR_COULD_NOT_RETRIEVE_TOKEN"));

        newEnvironmentConfig = new EnvironmentConfig("test", "http://google.de/mich/gibts/nicht/1234/bla/", environmentConfig.getStrategy());
        assertException(newEnvironmentConfig, authToken, DCOS_MESSAGE_BUNDLE.getMessage("DEPLOYER_DCOS_ERROR_COULD_NOT_RETRIEVE_TOKEN"));
    }

    public void testRetrieveAuthenticationToken() throws ResourceException, EnvironmentConfigException {
        TokenResource tokenResource = new TokenResource(environmentConfig);
        String token = tokenResource.retrieveAuthenticationToken(authToken);
        assertFalse(token.isEmpty());
    }

    private void assertException(EnvironmentConfig environmentConfig, String token, String exceptionMessage) throws ResourceException {
        boolean exceptionThrown = false;
        TokenResource tokenResource = new TokenResource(environmentConfig);
        try {
            tokenResource.retrieveAuthenticationToken(token);
        } catch (EnvironmentConfigException e) {
            exceptionThrown = true;
            assertEquals(exceptionMessage, e.getMessage());
        }
        assertTrue(exceptionThrown);
    }
}

