/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jts;



/**
 * <p>Operations on boolean primitives and Boolean objects.</p>
 *
 * <p>This class tries to handle {@code null} input gracefully.
 * An exception will not be thrown for a {@code null} input.
 * Each method documents its behaviour in more detail.</p>
 *
 * <p>#ThreadSafe#</p>
 * @since 2.0
 */
public class Angle {

	  /**
	   * Returns whether an angle must turn clockwise or counterclockwise
	   * to overlap another angle.
	   *
	   * @param ang1 an angle (in radians)
	   * @param ang2 an angle (in radians)
	   * @return whether a1 must turn CLOCKWISE, COUNTERCLOCKWISE or NONE to
	   * overlap a2.
	   */
	  public static int getTurn(double ang1, double ang2) {
	      double crossproduct = Math.sin(ang2 - ang1);
	      int result;
	      if (crossproduct > 0) {
	          result = 1;
	      } else if (crossproduct < 0) {
	          result = -1;
	      } else {
	    	  result = 0;
	      }
	      
	      return result;
	  } 
}
