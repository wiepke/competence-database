/**
 * 
 * No descripton provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function(root, factory) {
  if (typeof define === 'function' && define.amd) {
    // AMD. Register as an anonymous module.
    define(['ApiClient'], factory);
  } else if (typeof module === 'object' && module.exports) {
    // CommonJS-like environments that support module.exports, like Node.
    module.exports = factory(require('../ApiClient'));
  } else {
    // Browser globals (root is window)
    if (!root.SwaggerJsClient) {
      root.SwaggerJsClient = {};
    }
    root.SwaggerJsClient.ReflectiveAssessment = factory(root.SwaggerJsClient.ApiClient);
  }
}(this, function(ApiClient) {
  'use strict';




  /**
   * The ReflectiveAssessment model module.
   * @module model/ReflectiveAssessment
   * @version 1.0.0
   */

  /**
   * Constructs a new <code>ReflectiveAssessment</code>.
   * @alias module:model/ReflectiveAssessment
   * @class
   */
  var exports = function() {
    var _this = this;




  };

  /**
   * Constructs a <code>ReflectiveAssessment</code> from a plain JavaScript object, optionally creating a new instance.
   * Copies all relevant properties from <code>data</code> to <code>obj</code> if supplied or a new instance if not.
   * @param {Object} data The plain JavaScript object bearing properties of interest.
   * @param {module:model/ReflectiveAssessment} obj Optional instance to populate.
   * @return {module:model/ReflectiveAssessment} The populated <code>ReflectiveAssessment</code> instance.
   */
  exports.constructFromObject = function(data, obj) {
    if (data) {
      obj = obj || new exports();

      if (data.hasOwnProperty('isLearningGoal')) {
        obj['isLearningGoal'] = ApiClient.convertToType(data['isLearningGoal'], 'Boolean');
      }
      if (data.hasOwnProperty('competenceDescription')) {
        obj['competenceDescription'] = ApiClient.convertToType(data['competenceDescription'], 'String');
      }
      if (data.hasOwnProperty('assessment')) {
        obj['assessment'] = ApiClient.convertToType(data['assessment'], 'String');
      }
    }
    return obj;
  }

  /**
   * @member {Boolean} isLearningGoal
   * @default false
   */
  exports.prototype['isLearningGoal'] = false;
  /**
   * @member {String} competenceDescription
   */
  exports.prototype['competenceDescription'] = undefined;
  /**
   * @member {String} assessment
   */
  exports.prototype['assessment'] = undefined;



  return exports;
}));


