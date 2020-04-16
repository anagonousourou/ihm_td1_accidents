const opencage = require('opencage-api-client')

function prepareAccident(body, file) {

  return new Promise(function (resolve, reject) {
    //si la date n'est pas mise avec 
    if (body.dateCreation === undefined) {
      body.dateCreation = Date.now()
    }
    //si une image n'est pas uploadée
    if (file === undefined) {
      body.imageUrl = 'https://cdn3.iconfinder.com/data/icons/basic-ui-6/40/Asset_12-512.png';

    }
    else {
      body.imageUrl = file.filename;
    }

    

    //préparer la longitude et la longitude
    opencage.geocode({ q: body.adresse }).then(data => {

      if (data.status.code == 200) {
        if (data.results.length > 0) {
          var place = data.results[0];
          console.log(place.formatted);
          console.log(place.geometry);
          body.longitude = `${place.geometry.lng}`
          body.latitude = `${place.geometry.lat}`;

          console.log(place.annotations.timezone.name);
          resolve(body)
        }
      } else if (data.status.code == 402) {
        console.log('hit free-trial daily limit');
        console.log('become a customer: https://opencagedata.com/pricing');
        resolve(body)
      } else {
        resolve(body)
        // other possible response codes:
        // https://opencagedata.com/api#codes
        console.log('error', data.status.message);
      }
    }).catch(error => {
      resolve(body)
      console.log('error', error.message);
    });

  });

}

module.exports = { prepareAccident }
