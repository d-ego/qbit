/*
 * Copyright (c) 2015. Rick Hightower, Geoff Chandler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * QBit - The Microservice lib for Java : JSON, WebSocket, REST. Be The Web!
 */

package io.advantageous.qbit.client;

import io.advantageous.qbit.service.Startable;
import io.advantageous.qbit.service.Stoppable;

/**
 * This is the main interface for accessing the server from a client perspective.
 * With this interface you can createWithWorkers a client proxy.
 * A client proxy is an interface that will marshall calls to a remote server.
 *
 * @author rhightower
 */
public interface Client extends Stoppable, Startable {


    /**
     * Creates a new client proxy given a client interface.
     *
     * @param serviceInterface client interface
     * @param serviceName      client name
     * @param <T>              class type of interface
     * @return new client proxy.. calling methods on this proxy marshals method calls to httpServer.
     */
    <T> T createProxy(final Class<T> serviceInterface,
                      final String serviceName);


    void flush();

    default Client startClient() {
        start();
        return this;
    }

    boolean connected();

}
