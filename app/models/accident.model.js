const Joi = require('joi')
const BaseModel = require('../utils/base-model.js')

module.exports = new BaseModel('Accident', {
  adresse: Joi.string().required(),
  type: Joi.string().required(),
  deviceId: Joi.string().required(),
  commentaire: Joi.string(),
  dateCreation: Joi.date(),
  imageUrl: Joi.string(),
  latitude: Joi.string().required(),
  longitude: Joi.string().required(),
})
