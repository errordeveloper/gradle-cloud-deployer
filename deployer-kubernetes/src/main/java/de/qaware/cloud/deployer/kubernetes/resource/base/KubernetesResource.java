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
package de.qaware.cloud.deployer.kubernetes.resource.base;

import de.qaware.cloud.deployer.commons.error.ResourceException;
import de.qaware.cloud.deployer.commons.resource.Resource;
import de.qaware.cloud.deployer.kubernetes.config.resource.ResourceConfig;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public abstract class KubernetesResource implements Resource {

    // Maximum call-duration in seconds
    private static final int TIMEOUT = 300;
    private static final double INTERVAL = 0.5;

    private final String namespace;
    private final ResourceConfig resourceConfig;
    private final ClientFactory clientFactory;

    public KubernetesResource(String namespace, ResourceConfig resourceConfig, ClientFactory clientFactory) {
        this.namespace = namespace;
        this.resourceConfig = resourceConfig;
        this.clientFactory = clientFactory;
    }

    public String getId() {
        return resourceConfig.getResourceId();
    }

    public String getNamespace() {
        return namespace;
    }

    public ResourceConfig getResourceConfig() {
        return resourceConfig;
    }

    public RequestBody createRequestBody() {
        return RequestBody.create(createMediaType(), resourceConfig.getContent());
    }

    public MediaType createMediaType() {
        switch (resourceConfig.getContentType()) {
            case JSON:
                return MediaType.parse("application/json");
            case YAML:
                return MediaType.parse("application/yaml");
            default:
                throw new IllegalArgumentException("Unknown type " + resourceConfig.getContentType());
        }
    }

    public <T> T createClient(Class<T> serviceClass) {
        return clientFactory.create(serviceClass);
    }

    public void executeCall(Call<ResponseBody> call) throws ResourceException {
        try {
            Response<ResponseBody> response = call.execute();
            if (!isSuccessResponse(response)) {
                throw new ResourceException("Received a unhandled http status code: " + response.code());
            }
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }

    public boolean executeExistsCall(Call<ResponseBody> existsCall) throws ResourceException {
        try {
            Response<ResponseBody> response = existsCall.execute();
            if (isSuccessResponse(response)) {
                return true;
            } else if (isNotFoundResponse(response)) {
                return false;
            } else {
                throw new ResourceException("Received a unhandled http status code: " + response.code());
            }
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }

    public void executeCreateCallAndBlock(Call<ResponseBody> createCall) throws ResourceException {
        try {
            Response<ResponseBody> response = createCall.execute();
            if (isSuccessResponse(response)) {
                Blocker blocker = new Blocker(TIMEOUT, INTERVAL, "Resource wasn't created within specified time (" + createCall.request().url() + ")");
                while (!this.exists()) {
                    blocker.block();
                }
            } else {
                throw new ResourceException("Received a unhandled http status code: " + response.code());
            }
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }

    public void executeDeleteCallAndBlock(Call<ResponseBody> deleteCall) throws ResourceException {
        try {
            Response<ResponseBody> response = deleteCall.execute();
            if (isSuccessResponse(response)) {
                Blocker blocker = new Blocker(TIMEOUT, INTERVAL, "Resource wasn't deleted within specified time (" + deleteCall.request().url() + ")");
                while (this.exists()) {
                    blocker.block();
                }
            } else {
                throw new ResourceException("Received a unhandled http status code: " + response.code());
            }
        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }

    @Override
    public abstract String toString();

    private boolean isNotFoundResponse(Response<ResponseBody> response) {
        return response.code() == 404;
    }

    private boolean isSuccessResponse(Response<ResponseBody> response) {
        return response.code() == 200 || response.code() == 201;
    }

    private class Blocker {

        private final int timeout;
        private final int interval;
        private final String errorMessage;
        private int timeoutCounter = 0;

        Blocker(int timeout, double interval, String errorMessage) {
            this.timeout = timeout * 1000;
            this.interval = (int) (interval * 1000);
            this.errorMessage = errorMessage;
        }

        void block() throws ResourceException {
            try {
                timeoutCounter += interval;
                if (timeoutCounter > timeout) {
                    throw new ResourceException(errorMessage);
                }
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                throw new ResourceException(e);
            }
        }
    }
}