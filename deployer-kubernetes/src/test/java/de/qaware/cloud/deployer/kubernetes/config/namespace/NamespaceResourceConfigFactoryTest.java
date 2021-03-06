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
package de.qaware.cloud.deployer.kubernetes.config.namespace;

import de.qaware.cloud.deployer.commons.config.resource.ContentType;
import de.qaware.cloud.deployer.commons.config.util.FileUtil;
import de.qaware.cloud.deployer.commons.error.ResourceConfigException;
import de.qaware.cloud.deployer.kubernetes.config.resource.KubernetesResourceConfig;
import junit.framework.TestCase;

import static de.qaware.cloud.deployer.kubernetes.logging.KubernetesMessageBundle.KUBERNETES_MESSAGE_BUNDLE;

public class NamespaceResourceConfigFactoryTest extends TestCase {

    public void testValidNamespace() throws ResourceConfigException {
        // Create config
        KubernetesResourceConfig namespaceResource = NamespaceResourceConfigFactory.create("test");

        // Retrieve expected json from file
        String namespaceJson = FileUtil.readFileContent("/de/qaware/cloud/deployer/kubernetes/config/namespace/namespace.json");

        // Check config
        assertEquals("test", namespaceResource.getResourceId());
        assertEquals("Namespace", namespaceResource.getResourceType());
        assertEquals("v1", namespaceResource.getResourceVersion());
        assertEquals(ContentType.JSON, namespaceResource.getContentType());
        assertEquals(namespaceJson, namespaceResource.getContent());
    }

    public void testEmptyNamespace() {
        boolean exceptionThrown = false;
        try {
            NamespaceResourceConfigFactory.create("");
        } catch (ResourceConfigException e) {
            exceptionThrown = true;
            assertEquals(KUBERNETES_MESSAGE_BUNDLE.getMessage("DEPLOYER_KUBERNETES_ERROR_INVALID_NAMESPACE"), e.getMessage());
        }
        assertTrue(exceptionThrown);
    }

    public void testNullNamespace() {
        boolean exceptionThrown = false;
        try {
            NamespaceResourceConfigFactory.create(null);
        } catch (ResourceConfigException e) {
            exceptionThrown = true;
            assertEquals(KUBERNETES_MESSAGE_BUNDLE.getMessage("DEPLOYER_KUBERNETES_ERROR_INVALID_NAMESPACE"), e.getMessage());
        }
        assertTrue(exceptionThrown);
    }
}
