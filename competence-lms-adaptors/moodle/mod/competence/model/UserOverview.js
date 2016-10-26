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
    define(['ApiClient', 'model/CourseData', 'model/LearningTemplateData'], factory);
  } else if (typeof module === 'object' && module.exports) {
    // CommonJS-like environments that support module.exports, like Node.
    module.exports = factory(require('../ApiClient'), require('./CourseData'), require('./LearningTemplateData'));
  } else {
    // Browser globals (root is window)
    if (!root.SwaggerJsClient) {
      root.SwaggerJsClient = {};
    }
    root.SwaggerJsClient.UserOverview = factory(root.SwaggerJsClient.ApiClient, root.SwaggerJsClient.CourseData, root.SwaggerJsClient.LearningTemplateData);
  }
}(this, function(ApiClient, CourseData, LearningTemplateData) {
  'use strict';




  /**
   * The UserOverview model module.
   * @module model/UserOverview
   * @version 1.0.0
   */

  /**
   * Constructs a new <code>UserOverview</code>.
   * @alias module:model/UserOverview
   * @class
   * @param courses {Array.<module:model/CourseData>} the courses the user is enrolled in
   * @param learningTemplates {Array.<module:model/LearningTemplateData>} the learning templates the user has selected to take interest in
   */
  var exports = function(courses, learningTemplates) {
    var _this = this;

    _this['courses'] = courses;
    _this['learningTemplates'] = learningTemplates;
  };

  /**
   * Constructs a <code>UserOverview</code> from a plain JavaScript object, optionally creating a new instance.
   * Copies all relevant properties from <code>data</code> to <code>obj</code> if supplied or a new instance if not.
   * @param {Object} data The plain JavaScript object bearing properties of interest.
   * @param {module:model/UserOverview} obj Optional instance to populate.
   * @return {module:model/UserOverview} The populated <code>UserOverview</code> instance.
   */
  exports.constructFromObject = function(data, obj) {
    if (data) {
      obj = obj || new exports();

      if (data.hasOwnProperty('courses')) {
        obj['courses'] = ApiClient.convertToType(data['courses'], [CourseData]);
      }
      if (data.hasOwnProperty('learningTemplates')) {
        obj['learningTemplates'] = ApiClient.convertToType(data['learningTemplates'], [LearningTemplateData]);
      }
    }
    return obj;
  }

  /**
   * the courses the user is enrolled in
   * @member {Array.<module:model/CourseData>} courses
   */
  exports.prototype['courses'] = undefined;
  /**
   * the learning templates the user has selected to take interest in
   * @member {Array.<module:model/LearningTemplateData>} learningTemplates
   */
  exports.prototype['learningTemplates'] = undefined;



  return exports;
}));


