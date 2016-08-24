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
package de.qaware.cloud.deployer.kubernetes.resource.deployment;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface DeploymentClient {

    @GET("/apis/extensions/v1beta1/namespaces/{namespace}/deployments/{name}")
    Call<ResponseBody> get(@Path("name") String name, @Path("namespace") String namespace);

    @POST("/apis/extensions/v1beta1/namespaces/{namespace}/deployments")
    Call<ResponseBody> create(@Path("namespace") String namespace, @Body RequestBody deploymentDescription);

    @DELETE("/apis/extensions/v1beta1/namespaces/{namespace}/deployments/{name}")
    Call<ResponseBody> delete(@Path("name") String name, @Path("namespace") String namespace);
}
