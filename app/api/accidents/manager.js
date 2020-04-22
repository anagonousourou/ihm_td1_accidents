const { Accident } = require('../../models')

const buildListNear = (latitude,longitude,distance) => {
    const accidents = Accident.get();
    return accidents.filter((accident)=>{
        console.log(accident);
        return isNear(latitude,longitude,distance,accident.id);
})
}

const isNear = (latitude,longitude,distance,accidentId) => {
    const accident= Accident.getById(accidentId);

    const diffLat= toRad(accident.latitude-latitude);
    const diffLong= toRad(accident.longitude-longitude);
    
    const latAcc = toRad(accident.latitude);
    const latUser = toRad(latitude);

    const a = (Math.sin(diffLat/2) * Math.sin(diffLat/2))+
    (Math.sin(diffLong/2) * Math.sin(diffLong/2) * 
    Math.cos(latAcc) * Math.cos(latUser));
    
    const b = 2* Math.atan(Math.sqrt(a)/Math.sqrt(1-a));

    const distanceToAcc = 6371 * b
    //console.log("latitude"+accident.latitude)
    //console.log("longitude"+accident.longitude)
    console.log("distance to acc "+distanceToAcc)
    //console.log("distance "+distance)
    //console.log(distanceToAcc<=distance)
    return distanceToAcc<=distance;
}

function toRad(Value) 
{
    return Value * Math.PI / 180;
}
module.exports = {
    buildListNear,
  }