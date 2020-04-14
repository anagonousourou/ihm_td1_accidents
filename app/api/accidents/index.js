const { Router } = require('express')

const { Accident } = require('../../models')
const multer = require('multer')


const router = new Router()

// customize multer storage options , this allow us to add the file extensions which is not allowed by default
// voir la documentation https://expressjs.com/en/resources/middleware/multer.html
var mystorage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, 'uploads/')
  },
  filename: function (req, file, cb) {
    const uniqueSuffix = Date.now() + '' + Math.round(Math.random() * 1E9)
    cb(null, uniqueSuffix + '.' + file.mimetype.split('/')[1])
  }
})

const upload = multer({ dest: 'uploads/', storage: mystorage }) //to handle automatically the file uploaded to the post route

router.get('/', (req, res) => {
  try {
    const accident = { "accidents": [...Accident.get()] }

    res.status(200).json(accident)
  } catch (err) {
    res.status(500).json(err)
  }
})
router.get('/:accidentId', (req, res) => {
  try {
    res.status(200).json(Accident.getById(req.params.accidentId))
  } catch (err) {
    res.status(500).json(err)
  }
})
router.post('/', upload.single('accidentImage'), (req, res, next) => {
  try {
    console.log(req.body)
    const accident = Accident.create({ imageUrl: req.file.filename, ...req.body })
    res.status(201).json(accident)
  } catch (err) {
    if (err.name === 'ValidationError') {
      res.status(400).json(err.extra)
    } else {
      res.status(500).json(err)
    }
  }
})


router.delete('/:AccidentId', (req, res) => {
  try {
    res.status(200).json(Accident.delete(req.params.AccidentId))
  } catch (err) {
    res.status(500).json(err)
  }
})

module.exports = router
