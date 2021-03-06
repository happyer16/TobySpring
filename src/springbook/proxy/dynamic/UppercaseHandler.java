/*
 *     Copyright 2018 Overnodes. All rights reserved.
 *
 *     Licensed under the Apache License, Version 2.0 (the “License”);
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an “AS IS” BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *  ================================================================================
 *
 *      Developer : Ted Kim
 *      Date :      06/11/2018
 *      Contact :   tedkim@overnodes.com
 *
 *  ================================================================================
 *
 */

package springbook.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UppercaseHandler implements InvocationHandler {

  Object target;

  private UppercaseHandler(Object target){
    this.target=target;
  }
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Object ret = method.invoke(target,args);

    // 리턴 타입과 메소드 이름이 일치하는 경우에만 부가기능을 적용한다.
    if(ret instanceof String && method.getName().startsWith("say"))
      return ret.toString().toUpperCase();
    else
      return ret;
  }
}