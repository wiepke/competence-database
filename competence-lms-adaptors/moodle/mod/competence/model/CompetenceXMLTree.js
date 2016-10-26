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
    define(['ApiClient', 'model/CompetenceXMLTree'], factory);
  } else if (typeof module === 'object' && module.exports) {
    // CommonJS-like environments that support module.exports, like Node.
    module.exports = factory(require('../ApiClient'), require('./CompetenceXMLTree'));
  } else {
    // Browser globals (root is window)
    if (!root.SwaggerJsClient) {
      root.SwaggerJsClient = {};
    }
    root.SwaggerJsClient.CompetenceXMLTree = factory(root.SwaggerJsClient.ApiClient, root.SwaggerJsClient.CompetenceXMLTree);
  }
}(this, function(ApiClient, CompetenceXMLTree) {
  'use strict';




  /**
   * The CompetenceXMLTree model module.
   * @module model/CompetenceXMLTree
   * @version 1.0.0
   */

  /**
   * Constructs a new <code>CompetenceXMLTree</code>.
   * @alias module:model/CompetenceXMLTree
   * @class
   */
  var exports = function() {
    var _this = this;






  };

  /**
   * Constructs a <code>CompetenceXMLTree</code> from a plain JavaScript object, optionally creating a new instance.
   * Copies all relevant properties from <code>data</code> to <code>obj</code> if supplied or a new instance if not.
   * @param {Object} data The plain JavaScript object bearing properties of interest.
   * @param {module:model/CompetenceXMLTree} obj Optional instance to populate.
   * @return {module:model/CompetenceXMLTree} The populated <code>CompetenceXMLTree</code> instance.
   */
  exports.constructFromObject = function(data, obj) {
    if (data) {
      obj = obj || new exports();

      if (data.hasOwnProperty('name')) {
        obj['name'] = ApiClient.convertToType(data['name'], 'String');
      }
      if (data.hasOwnProperty('qtip')) {
        obj['qtip'] = ApiClient.convertToType(data['qtip'], 'String');
      }
      if (data.hasOwnProperty('icon')) {
        obj['icon'] = ApiClient.convertToType(data['icon'], 'String');
      }
      if (data.hasOwnProperty('children')) {
        obj['children'] = ApiClient.convertToType(data['children'], [CompetenceXMLTree]);
      }
      if (data.hasOwnProperty('isCompulsory')) {
        obj['isCompulsory'] = ApiClient.convertToType(data['isCompulsory'], 'Boolean');
      }
    }
    return obj;
  }

  /**
   * @member {String} name
   */
  exports.prototype['name'] = undefined;
  /**
   * a description
   * @member {String} qtip
   */
  exports.prototype['qtip'] = undefined;
  /**
   * the path to an icon for this tree entry
   * @member {String} icon
   */
  exports.prototype['icon'] = undefined;
  /**
   * @member {Array.<module:model/CompetenceXMLTree>} children
   */
  exports.prototype['children'] = undefined;
  /**
   * @member {Boolean} isCompulsory
   * @default false
   */
  exports.prototype['isCompulsory'] = false;



  return exports;
}));


