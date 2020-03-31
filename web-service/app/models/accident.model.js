const Joi = require('joi')
const BaseModel = require('../utils/base-model.js')

module.exports = new BaseModel('Accident', {
  adresse: Joi.string().required(),
  type: Joi.string().required(),
  commentaire: Joi.string(),
  photoB64: Joi.string(),
})
