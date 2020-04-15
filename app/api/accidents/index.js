const { Router } = require('express')

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
    
    if (req.body.dateCreation === undefined) {
      req.body.dateCreation = Date.now()
    }
    console.log(req.body)
    if (req.file === undefined) {
      const accident = Accident.create({ imageUrl: 'https://cdn3.iconfinder.com/data/icons/basic-ui-6/40/Asset_12-512.png', ...req.body })
      res.status(201).json(accident)
    } else {
      const accident = Accident.create({ imageUrl: req.file.filename, ...req.body })
      res.status(201).json(accident)
    }
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
