const { Router } = require('express')


const accidentTypes = [
    {
        label: 'Voiture & obstacle',
        iconUrl: 'car_accident_default.png'
    },
    {
        label: 'Voiture & autres véhicules',
        iconUrl: 'car_accident_default.png'
    },
    {
        label: 'Camion(s)',
        iconUrl: 'truck_accident.jpeg'
    },
    {
        label: 'Vélo',
        iconUrl: 'bicycle_with_rider.png'
    },
    {
        label: 'Train, métro ou TGV',
        iconUrl: 'train_wreck.jpeg'
    },
    {
        label: 'Moto',
        iconUrl: 'motorcycle.png'
    },
    {
        label: 'Personne seule',
        iconUrl: 'silhouette.png'
    }
]

const router = new Router()
router.get('/', (req, res) => {
    try {
        res.status(200).json(accidentTypes)
    } catch (err) {
        res.status(500).json(err)
    }
})

module.exports = router

