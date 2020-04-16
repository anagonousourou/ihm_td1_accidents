const { Router } = require('express')
const { prepareAccident } = require('./helper')
const multer = require('multer')
const { Accident } = require('../../models')


const router = new Router()

// customize multer storage options , this allow us to add the file extensions which is not allowed by default
// voir la documentation https://expressjs.com/en/resources/middleware/multer.html
const mystorage = multer.diskStorage({
  destination(req, file, cb) {
    cb(null, 'uploads/')
  },
  filename(req, file, cb) {
    const uniqueSuffix = `${Date.now()}${Math.round(Math.random() * 1E9)}`
    cb(null, `${uniqueSuffix}.${file.mimetype.split('/')[1]}`)
  },
})

const upload = multer({ dest: 'uploads/', storage: mystorage }) // to handle automatically the file uploaded to the post route

router.get('/', (req, res) => {
  try {
    const accident = { accidents: [...Accident.get()] }

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
    prepareAccident(req.body, req.file).then(result => {
      console.log(result);
      const accident = Accident.create(result)
      res.status(201).json(accident)
    }
    ).catch(error => {
      console.log(error)
      if (error.name === 'ValidationError') {
        res.status(400).json(error.extra)
      } else {
        res.status(500).json(error)
      }
    })



  } catch (err) {
    res.status(500).json(err)
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
