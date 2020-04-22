const { Accident } = require('../../models')

const buildListNear = (latitude,longitude,distance) => {
    const accidents = Accident.get();
    return accidents.filter((accident)=>
        isNear(latitude,longitude,distance,accident.id))
}

const isNear = (latitude,longitude,distance,accidentId) => {
    const accident= Accident.getById(accidentId);
    const distanceToAcc = 6371 * Math.acos((Math.sin(latitude)*Math.sin(accident.latitude))+(Math.cos(latitude)*Math.cos(accident.latitude)*Math.cos(longitude-accident.longitude)))
    //console.log("distance to acc "+distanceToAcc)
   // console.log("distance "+distance)
    //console.log(distanceToAcc<=distance)
    return distanceToAcc<=distance;
}

module.exports = {
    buildListNear,
  }