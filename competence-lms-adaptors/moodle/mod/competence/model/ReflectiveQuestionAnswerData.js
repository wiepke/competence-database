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
    root.SwaggerJsClient.ReflectiveQuestionAnswerData = factory(root.SwaggerJsClient.ApiClient);
  }
}(this, function(ApiClient) {
  'use strict';




  /**
   * The ReflectiveQuestionAnswerData model module.
   * @module model/ReflectiveQuestionAnswerData
   * @version 1.0.0
   */

  /**
   * Constructs a new <code>ReflectiveQuestionAnswerData</code>.
   * @alias module:model/ReflectiveQuestionAnswerData
   * @class
   */
  var exports = function() {
    var _this = this;





  };

  /**
   * Constructs a <code>ReflectiveQuestionAnswerData</code> from a plain JavaScript object, optionally creating a new instance.
   * Copies all relevant properties from <code>data</code> to <code>obj</code> if supplied or a new instance if not.
   * @param {Object} data The plain JavaScript object bearing properties of interest.
   * @param {module:model/ReflectiveQuestionAnswerData} obj Optional instance to populate.
   * @return {module:model/ReflectiveQuestionAnswerData} The populated <code>ReflectiveQuestionAnswerData</code> instance.
   */
  exports.constructFromObject = function(data, obj) {
    if (data) {
      obj = obj || new exports();

      if (data.hasOwnProperty('text')) {
        obj['text'] = ApiClient.convertToType(data['text'], 'String');
      }
      if (data.hasOwnProperty('userId')) {
        obj['userId'] = ApiClient.convertToType(data['userId'], 'String');
      }
      if (data.hasOwnProperty('questionId')) {
        obj['questionId'] = ApiClient.convertToType(data['questionId'], 'String');
      }
      if (data.hasOwnProperty('datecreated')) {
        obj['datecreated'] = ApiClient.convertToType(data['datecreated'], 'Integer');
      }
    }
    return obj;
  }

  /**
   * @member {String} text
   */
  exports.prototype['text'] = undefined;
  /**
   * @member {String} userId
   */
  exports.prototype['userId'] = undefined;
  /**
   * @member {String} questionId
   */
  exports.prototype['questionId'] = undefined;
  /**
   * @member {Integer} datecreated
   */
  exports.prototype['datecreated'] = undefined;



  return exports;
}));


