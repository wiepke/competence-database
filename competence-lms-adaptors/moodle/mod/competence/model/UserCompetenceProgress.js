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
    define(['ApiClient', 'model/AbstractAssessment', 'model/CompetenceLinksView', 'model/ReflectiveQuestionAnswerHolder'], factory);
  } else if (typeof module === 'object' && module.exports) {
    // CommonJS-like environments that support module.exports, like Node.
    module.exports = factory(require('../ApiClient'), require('./AbstractAssessment'), require('./CompetenceLinksView'), require('./ReflectiveQuestionAnswerHolder'));
  } else {
    // Browser globals (root is window)
    if (!root.SwaggerJsClient) {
      root.SwaggerJsClient = {};
    }
    root.SwaggerJsClient.UserCompetenceProgress = factory(root.SwaggerJsClient.ApiClient, root.SwaggerJsClient.AbstractAssessment, root.SwaggerJsClient.CompetenceLinksView, root.SwaggerJsClient.ReflectiveQuestionAnswerHolder);
  }
}(this, function(ApiClient, AbstractAssessment, CompetenceLinksView, ReflectiveQuestionAnswerHolder) {
  'use strict';




  /**
   * The UserCompetenceProgress model module.
   * @module model/UserCompetenceProgress
   * @version 1.0.0
   */

  /**
   * Constructs a new <code>UserCompetenceProgress</code>.
   * @alias module:model/UserCompetenceProgress
   * @class
   */
  var exports = function() {
    var _this = this;





  };

  /**
   * Constructs a <code>UserCompetenceProgress</code> from a plain JavaScript object, optionally creating a new instance.
   * Copies all relevant properties from <code>data</code> to <code>obj</code> if supplied or a new instance if not.
   * @param {Object} data The plain JavaScript object bearing properties of interest.
   * @param {module:model/UserCompetenceProgress} obj Optional instance to populate.
   * @return {module:model/UserCompetenceProgress} The populated <code>UserCompetenceProgress</code> instance.
   */
  exports.constructFromObject = function(data, obj) {
    if (data) {
      obj = obj || new exports();

      if (data.hasOwnProperty('competence')) {
        obj['competence'] = ApiClient.convertToType(data['competence'], 'String');
      }
      if (data.hasOwnProperty('competenceLinksView')) {
        obj['competenceLinksView'] = ApiClient.convertToType(data['competenceLinksView'], [CompetenceLinksView]);
      }
      if (data.hasOwnProperty('abstractAssessment')) {
        obj['abstractAssessment'] = ApiClient.convertToType(data['abstractAssessment'], [AbstractAssessment]);
      }
      if (data.hasOwnProperty('reflectiveQuestionAnswerHolder')) {
        obj['reflectiveQuestionAnswerHolder'] = ReflectiveQuestionAnswerHolder.constructFromObject(data['reflectiveQuestionAnswerHolder']);
      }
    }
    return obj;
  }

  /**
   * @member {String} competence
   */
  exports.prototype['competence'] = undefined;
  /**
   * @member {Array.<module:model/CompetenceLinksView>} competenceLinksView
   */
  exports.prototype['competenceLinksView'] = undefined;
  /**
   * @member {Array.<module:model/AbstractAssessment>} abstractAssessment
   */
  exports.prototype['abstractAssessment'] = undefined;
  /**
   * @member {module:model/ReflectiveQuestionAnswerHolder} reflectiveQuestionAnswerHolder
   */
  exports.prototype['reflectiveQuestionAnswerHolder'] = undefined;



  return exports;
}));


