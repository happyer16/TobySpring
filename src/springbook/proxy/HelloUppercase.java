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

package springbook.proxy;

public class HelloUppercase implements Hello {

  Hello hello;  // Target Object

  public HelloUppercase(Hello hello) {
    this.hello=hello;
  }

  @Override
  public String sayHello(String name) {
    return hello.sayHello(name).toUpperCase();
  }

  @Override
  public String sayHi(String name) {
    return hello.sayHi(name).toUpperCase();
  }

  @Override
  public String sayThankyou(String name) {
    return hello.sayThankyou(name).toUpperCase();
  }
}