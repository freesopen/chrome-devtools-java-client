package com.github.kklisura.cdt.services.utils;

/*-
 * #%L
 * cdt-java-client
 * %%
 * Copyright (C) 2018 - 2021 Kenan Klisura
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import javassist.Modifier;
import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Proxy utils.
 *
 * @author Kenan Klisura
 */
public final class ProxyUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProxyUtils.class);

  /** Empty ctor. */
  private ProxyUtils() {
    // Empty ctor.
  }

  /**
   * Creates a proxy class to a given interface clazz supplied with invocation handler.
   *
   * @param clazz Proxy to class.
   * @param invocationHandler Invocation handler.
   * @param <T> Class type.
   * @return Proxy instance.
   */
  @SuppressWarnings("unchecked")
  public static <T> T createProxy(Class<T> clazz, InvocationHandler invocationHandler) {
    LOGGER.debug("代理方法 createProxy,{},参数{}",clazz.getName(),invocationHandler.getClass());

    return (T)
        Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz}, invocationHandler);
  }

  /**
   * Creates a proxy class to a given abstract clazz supplied with invocation handler for
   * un-implemented/abstrat methods.
   *
   * @param clazz Proxy to class.
   * @param paramTypes Ctor param types.
   * @param args Ctor args.
   * @param invocationHandler Invocation handler.
   * @param <T> Class type.
   * @return Proxy instance.
   */
  @SuppressWarnings("unchecked")
  public static <T> T createProxyFromAbstract(
      Class<T> clazz, Class[] paramTypes, Object[] args, InvocationHandler invocationHandler) {
    LOGGER.debug("代理方法 createProxyFromAbstract,{},参数{}",clazz.getName(),args);
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.setSuperclass(clazz);
    proxyFactory.setFilter(method -> Modifier.isAbstract(method.getModifiers()));
    try {
      return (T)
          proxyFactory.create(
                  paramTypes,
                  args,
                  new MethodHandler() {
                    @Override
                    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
                      LOGGER.debug("代理方法 createProxyFromAbstract.MethodHandler,{},方法{}，处理方法{} ，参数{}",
                              self.toString(),thisMethod,proceed,args);

                    return   invocationHandler.invoke(self, thisMethod, args);
                    }
                  }

          );
    } catch (Exception e) {
      LOGGER.error("Failed creating proxy from abstract class", e);
      throw new RuntimeException("Failed creating proxy from abstract class", e);
    }
  }
}
